package com.road.yishi.log.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.road.yishi.log.Log;
import com.road.yishi.log.cluster.ClusterMgr;
import com.road.yishi.log.handler.AnalylizeLogInter;
import com.road.yishi.log.mgr.ConfigMgr;
import com.road.yishi.log.util.FileUtil;
import com.road.yishi.log.util.HttpUtil;

public class MappedBufferReadFile implements ReadFile {

	private static String className = MappedBufferReadFile.class.getSimpleName();

	private final static Charset charset = Charset.forName("UTF-8");
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("resource")
	public static<K,V> int readAnalysis(AnalylizeLogInter<K, V> analysor, String fileName,String topic,int lastReadIndex, boolean isRepeat) {
		StringBuilder messageBuilder = new StringBuilder();
		int copyLastReadIndex = lastReadIndex;
		File file = new File(fileName);
		long fileSize = file.length();

		if (lastReadIndex > fileSize) {// 说明创建了新的文件名文件：记日志文件被切割
			lastReadIndex = 0;
		}
		if(lastReadIndex < fileSize){//文件大小一定要比上一次读的位置大，防止重复读的情况
			FileChannel fileChannel = null ;
			try {
				fileChannel = new RandomAccessFile(file, "rw").getChannel();
				long size = fileSize > BUFFER_SIZE ? BUFFER_SIZE : fileSize;// 设置缓冲区的大小
				boolean isQuit = true;
				while (isQuit) {
					if (fileSize - lastReadIndex <= size) {
						size = fileSize - lastReadIndex;
						isQuit = false;
					}
	
					if (size <= 0)
						break;
	
					MappedByteBuffer inputBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, lastReadIndex, size);
					Log.info("time:"+sdf.format(new Date())+"["+MappedBufferReadFile.class.getClass().getName()+"] fileName:"+fileName+";read position:"+lastReadIndex+";size:"+size);
					byte[] dst = new byte[(int) size];
	
					for (int i = 0; i < size; i++) {
						dst[i] = inputBuffer.get(i);
					}
					
					messageBuilder.append(new String(dst,charset));
					lastReadIndex += size;
				}
				handler(analysor, topic,fileName,messageBuilder);
				return lastReadIndex;
			} catch (Exception e) {
				Log.error(className + ":" + e);
				copyLastReadIndex = 0 ;
			} finally {
				try {
					fileChannel.close();
				} catch (IOException e) {
					Log.error(className + ":" + e);
					copyLastReadIndex = 0 ;
				}
			}
		}
		return copyLastReadIndex;
	}
	private static <K, V> void handler(AnalylizeLogInter<K, V> analysor, String topic,String fileName,StringBuilder messageBuilder) {
		String role = ClusterMgr.getClusterContiner().getRole();
		if("master".equals(role)){
			ConsumerExecutor.consumer(topic,analysor.analylize(messageBuilder.toString()));
		}else if("slave".equals(role)){
			String transMode = ConfigMgr.getTransMode();
			switch (transMode) {
			case Paramter.TRANS_MODE_UPLOAD://slave 向 master 定点上传 日志数据
				{
					byte[] messageBytes = messageBuilder.toString().getBytes(Charset.forName("utf-8"));
					if(messageBytes.length<=0){
						return ;
					}
					String data = ConfigMgr.getDataPath();
					String dataDirs = data + File.separator + topic;
					String fName = fileName.substring(fileName.lastIndexOf("/")+1);
					File file = new File(dataDirs, fName);
					try {
						FileUtil.writeWithTransferTo(file,messageBytes);
					} catch (IOException e) {
						Log.error("["+MappedBufferReadFile.className+"] 日志数据写 写出现异常", e);
					}
					//写完后将该文件上传给master
					String masterUploadUrl = ClusterMgr.getClusterContiner().getMasterUrl()+"/" +Paramter.URL_fileUpload;
					int count = ConfigMgr.getReUploadFail();
					while(!HttpUtil.upload(file.getAbsolutePath(),masterUploadUrl)&&(count--)>0);
					if(count>0){
						try {
							file.deleteOnExit();
						} catch (Exception e) {
							Log.error("topic:"+topic+";file name:"+file.getName()+"上传master success,删除本地失败", e);
						}
					}else{
						Log.error("topic:"+topic+";file name:"+file.getName()+"上传master 失败,");
					}
				}
				break;
			case Paramter.TRANS_MODE_POST:
				{
					ConsumerExecutor.forward(topic,fileName,messageBuilder);
				}
				break;
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 	当主机的角色为slave时，只记住需要读取的最后文件位置
	 * </pre>
	 *
	 */
	public static void recordLastPosition(String fileName,String topic,int lastReadIndex){
		
	}
}
