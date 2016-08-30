package com.road.yishi.log.bank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.handler.FileStatusMgr;
import com.road.yishi.log.util.FileUtil;

public class DataBankMgr {

	private final static Log log = LogFactory.getLog(DataBankMgr.class);

	/**
	 * 说明:所有的索引都是按主题分的.没存储一个消息,都可能会有重复的key,每一个消息都有自己的具体位置。 当要找某一个类型的消息时，我们可以得到这些消息的没具体存储位置。通过这些具体位置，我们就可以获取这些消息
	 */
	private static Map<String, HashMap<String, List<MessageStatus>>> TOPIC_MESSAGE_INDEX = new ConcurrentHashMap<String, HashMap<String, List<MessageStatus>>>();

	private static final Map<String, String> TOPIC_PATH = new ConcurrentHashMap<String, String>();

	private volatile static AtomicInteger changeCount = new AtomicInteger();
	private volatile static AtomicInteger readChangeCount = new AtomicInteger();

	private final static Charset charset = Charset.forName("UTF-8");

	private static final String FILE_EXTENION = "meta_index.index";

	private static final String READ_STATUS_EXTENION = "read_status.index";
	
	private static final String READ_POSITION_EXTENION = "position.index";

	private static java.util.Timer timer = new Timer("flush meta index", true);

	private static String SPERATOR = "\r\n";

	public static enum STORE_TYPE {HAS_CONSUMER, NO_CONSUMER};

	// 保存最后一次读取的类型
	private static final Map<String, ConcurrentHashMap<String, MessageStatus>> LAST_READ_STATUS = new ConcurrentHashMap<String, ConcurrentHashMap<String, MessageStatus>>();

	/**
	 * <pre>
	 * 初始化数据存储目录
	 * </pre>
	 *
	 * @param path
	 * @return
	 */
	public static boolean init(String topic, String path) {
		com.road.yishi.log.Log.debug("数据银行开始初始化：" + topic + "--->" + path);
		File file = new File(path);
		FileUtil.checkFile(file);
		TOPIC_PATH.put(topic, path);
		loadMetaIndex(topic, path);
		loadReadStatus(topic, path);
		loadReadPosition(topic,path);
		return true;
	}

	private static void loadMetaIndex(String topic, String path) {
		File file = new File(path + "/" + topic, FILE_EXTENION);
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
				List<String> lines = new LinkedList<String>();
				String line = "";
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
				if (!lines.isEmpty()) {
					HashMap<String, List<MessageStatus>> metaIndexMap = new HashMap<String, List<MessageStatus>>();
					for (String l : lines) {
						String[] valus = l.split("=");
						List<MessageStatus> statusList = new Gson().fromJson(valus[1], new TypeToken<List<MessageStatus>>() {
						}.getType());
						if (metaIndexMap.get(valus[0]) == null) {
							metaIndexMap.put(valus[0], new ArrayList<MessageStatus>());
						}
						metaIndexMap.get(valus[0]).addAll(statusList);
					}
					TOPIC_MESSAGE_INDEX.put(topic, metaIndexMap);
				}
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
	}
	
	private static void loadReadStatus(String topic, String path) {
		File file = new File(path + File.separator+topic, READ_STATUS_EXTENION);
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
				StringBuffer sb = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				if (!StringUtils.isEmpty(sb.toString())) {
					ConcurrentHashMap<String, MessageStatus> tmpMetaIndex = new Gson().fromJson(sb.toString(), new TypeToken<ConcurrentHashMap<String, MessageStatus>>() {
					}.getType());
					LAST_READ_STATUS.put(topic, tmpMetaIndex);
				}
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
	}

	private static void loadReadPosition(String topic, String path) {
		File file = new File(path +File.separator+topic, READ_POSITION_EXTENION);
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
				StringBuffer sb = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				if (!StringUtils.isEmpty(sb.toString())) {
					ConcurrentHashMap<String,Integer> tmpMetaPosition = new Gson().fromJson(sb.toString(), new TypeToken<ConcurrentHashMap<String,Integer>>() {}.getType());
					FileStatusMgr.setTopicPosition(topic, tmpMetaPosition);
				}
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
	}
	public static void registerTask() {
		long ONE_MIN = 1000 * 60 * 1;
		timer.schedule(new FlushDataMetaIndexTask(), ONE_MIN * 2, ONE_MIN * 2);
	}

	public static void flushMetaIndex() {
		if (changeCount.get() > 0) {
			for (Entry<String, String> entry : TOPIC_PATH.entrySet()) {
				String topic = entry.getKey();
				String path = entry.getValue() + File.separator + topic;
				HashMap<String, List<MessageStatus>> partionIndexs = TOPIC_MESSAGE_INDEX.get(topic);
				if (partionIndexs != null) {
					File file = new File(path, FILE_EXTENION);
					FileUtil.checkFile(file);
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));) {
						for (Entry<String, List<MessageStatus>> entryTmp : partionIndexs.entrySet()) {
							List<MessageStatus> ls = entryTmp.getValue();
							int page = (ls.size() - 1) / 25 + 1;
							for (int i = 0; i < page; i++) {
								int startIndex = i * 25;
								int endIndex = i * 25 + 25;
								endIndex = (endIndex > ls.size() ? ls.size() : endIndex);
								// System.out.println("start index:"+startIndex+";end index:"+endIndex);
								List<MessageStatus> put = ls.subList(startIndex, endIndex);
								writer.write(entryTmp.getKey() + "=" + new Gson().toJson(put));
								writer.newLine();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			changeCount.set(0);
		}
	}

	public static void flushStatus() {

		for (Entry<String, String> entry : TOPIC_PATH.entrySet()) {
			String topic = entry.getKey();
			String path = entry.getValue() + File.separator + topic;
			
			flushReadStatus(topic, path);//1、存储读取消息状态
			
			flushAnaPosition(topic, path);//2、存储分析状态
		}
	}

	private static void flushAnaPosition(String topic, String path) {
		File file  = new File(path,READ_POSITION_EXTENION);
		FileUtil.checkFile(file);
		ConcurrentHashMap<String,Integer> position = FileStatusMgr.getTopicPosition(topic);
		if(position!=null&&!position.isEmpty()){
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));) {
				writer.write(new Gson().toJson(position));
			} catch (IOException e) {
				log.error("flush the index of analilize log file:", e);
			}
		}
	}

	private static void flushReadStatus(String topic, String path) {
		ConcurrentHashMap<String, MessageStatus> partionIndexs = LAST_READ_STATUS.get(topic);
		if (partionIndexs != null) {
			File file = new File(path, READ_STATUS_EXTENION);
			FileUtil.checkFile(file);
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));) {
				writer.write(new Gson().toJson(partionIndexs));
			} catch (IOException e) {
				log.error("flush the status of that read message:", e);
			}
		}
	}
	
	private static final Map<String,Object> lockMap = new ConcurrentHashMap<String,Object>();

	private static final Map<String,AtomicInteger> storeCountMap = new ConcurrentHashMap<String,AtomicInteger>();
	/**
	 * <pre>
	 * </pre>
	 *
	 * @param topic
	 * @param key
	 * @param message
	 * @param filePath
	 * @param type 1 表示存储已经消费的数据，0 表示未消费消息存储
	 * @return
	 */
	public static boolean store(String topic, Object key, Object message, String filePath, short type) {
		if (topic == null || key == null || filePath.isEmpty() || message == null) {
			return false;
		}
				
		File file = new File(filePath);
		FileUtil.checkFile(file);
		String encrtpykey = MD5Security.compute(key.toString());
		String storeKey = topic+"_"+encrtpykey;
		Object lock = lockMap.get(encrtpykey);
		if(lock == null){
			lock = new Object();
			lockMap.put(encrtpykey, lock);
		}
		
		HashMap<String, List<MessageStatus>> topicMessageIndex = TOPIC_MESSAGE_INDEX.get(topic);
		if (topicMessageIndex == null) {
			topicMessageIndex = new HashMap<String, List<MessageStatus>>();
			if(TOPIC_MESSAGE_INDEX.get(topic) == null){
				TOPIC_MESSAGE_INDEX.put(topic, topicMessageIndex);
			}
		}
		synchronized (topicMessageIndex) {
			if (topicMessageIndex.get(encrtpykey) == null) {
				List<MessageStatus> msl = new ArrayList<MessageStatus>();
				topicMessageIndex.put(encrtpykey, msl);
			}
		}
		try {
			MessageStatus ms = new MessageStatus();
			ms.setType(type);
			ms.setStartIndex(file.length());

			FileOutputStream fos = new FileOutputStream(file, true);
			DataOutputStream writer = new DataOutputStream(fos);
			// 先加密后压缩存储
			// String inputString = DESUtil.encrypt(new Gson().toJson(message));
			StringBuffer inputString = new StringBuffer(new Gson().toJson(message));
			int tempLenth = inputString.toString().getBytes("UTF-8").length;
			ms.setSize(tempLenth);
			ByteBuffer buffer = ByteBuffer.allocate(tempLenth);
			buffer.put(inputString.toString().getBytes("UTF-8"));
			buffer.flip();
			synchronized (lock) {
				writer.write(buffer.array());
				writer.write(SPERATOR.getBytes());
				writer.flush();
				writer.close();
			}
			synchronized (topicMessageIndex) {
				topicMessageIndex.get(encrtpykey).add(ms);
			}
			
			changeCount.incrementAndGet();
			
			if(storeCountMap.get(storeKey) == null){
				storeCountMap.put(storeKey, new AtomicInteger(0));
			}
			
			synchronized (storeCountMap.get(storeKey)) {
				storeCountMap.get(storeKey).incrementAndGet();
				storeCountMap.get(storeKey).notifyAll();
			}
			
			return true;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return false;
		} 
	}

	/**
	 * <pre>
	 * 获取一个消息，每次都是从上一次消费的地方开始retrieve .如果发现已经到末尾了。则返回null.
	 * </pre>
	 * @param <X>
	 * 
	 * @param topic
	 * @param fileName
	 * @param key
	 * @param type 1 表示获取已经消费的数据，0表示获取未消费的数据，-1表示获取全部
	 * @return
	 */
	@SuppressWarnings({ "resource", "unchecked" })
	public static <X> List<X> retrieve(String topic, Object key, String fileName, short type, TypeToken<X> typeToken) {
		String encrtpyKey = MD5Security.compute(key.toString());
		String storeKey = topic+"_"+encrtpyKey;
		if(storeCountMap.get(storeKey)==null){
			storeCountMap.put(storeKey, new AtomicInteger(0));
		}
		synchronized (storeCountMap.get(storeKey)) {
			if(storeCountMap.get(storeKey).get()<=0){
				System.out.println(storeKey+"还没有数据：正在等待："+key.toString());
				return null;
			}
			
			if(storeCountMap.get(storeKey).get()>0){
				storeCountMap.get(storeKey).decrementAndGet();
			}
		}
		
		try {
			if (fileName == null || fileName.length() == 0)
				return null;

			if (!new File(fileName).exists())
				return null;

			FileInputStream fileInputStream = new FileInputStream(fileName);
			FileChannel fileChannel = fileInputStream.getChannel();
			if (fileChannel.size() <= 0)
				return null;

			
			HashMap<String, List<MessageStatus>> topicMessageIndex = TOPIC_MESSAGE_INDEX.get(topic);
			List<MessageStatus> msList = null ;
			synchronized (topicMessageIndex) {
				msList = topicMessageIndex.get(encrtpyKey);
			}
			
			if (msList == null || msList.isEmpty()) {
				return null;
			}
			if (!LAST_READ_STATUS.containsKey(topic)) {
				LAST_READ_STATUS.put(topic, new ConcurrentHashMap<String, MessageStatus>());
			}

			List<MessageStatus> readMessage = new ArrayList<MessageStatus>();
			// 每次读取15条数据
			ConcurrentHashMap<String, MessageStatus> topicStatus = LAST_READ_STATUS.get(topic);
			
			MessageStatus lastStatus = topicStatus.get(encrtpyKey);
			if (lastStatus == null) {
				synchronized (topicStatus) {
					if(topicStatus.get(encrtpyKey) == null){
						lastStatus = new MessageStatus();
					}
				}
			}
			synchronized (lastStatus) {
//				System.out.println("开始位置："+lastStatus.getType()+"----->；结束位置："+msList.size());
				for(int i = (int)lastStatus.getType();i<msList.size();i++){
					readMessage.add(msList.get(i));
				}
				lastStatus.setType(msList.size());// 更新读取的位置
			}
			topicStatus.put(encrtpyKey, lastStatus);
			LAST_READ_STATUS.put(topic, topicStatus);

			if (readMessage.isEmpty()) {// 如果已经没有可消费的消息，
				return null;
			}

			List<X> messageList = new ArrayList<X>();
			for (MessageStatus ms : readMessage) {
				if (ms.getType() == type) {
					MappedByteBuffer mappedByteBuffer = fileChannel.map(MapMode.READ_ONLY, ms.getStartIndex(), ms.getSize());
					String str = charset.decode(mappedByteBuffer).toString();
					mappedByteBuffer = null;
					messageList.add((X) new Gson().fromJson(str, typeToken.getType()));
				}
			}
			fileInputStream.close();
			fileChannel.close();

			readChangeCount.getAndIncrement();
			return messageList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];
		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);) {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = null;
		}
		compresser.end();
		return output;
	}

	/**
	 * 解压缩
	 * 
	 * @param data 待压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		decompresser.end();
		return output;
	}
}