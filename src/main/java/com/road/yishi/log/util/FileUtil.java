package com.road.yishi.log.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.road.yishi.log.Log;
import com.road.yishi.log.bank.DataBankMgr;

public final class FileUtil {

	public final static boolean downLoad(String fileUrl, String saveLocalPath, String fileName) {
		try {
			String urlFileName = fileUrl + "_" + fileName;
			int byteSum = 0;
			int byteRead = 0;
			URL url = new URL(urlFileName);
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();// 得到一个网络流，这个网络流就是指向成功连接该站点的一个通道
			FileOutputStream out = new FileOutputStream(new File(saveLocalPath + fileName));
			byte[] bytes = new byte[1024];
			while ((byteRead = in.read(bytes)) != -1) {
				byteSum += byteRead;
				out.write(bytes, 0, byteRead);
			}
			out.flush();
			out.close();
			in.close();
		} catch (MalformedURLException e) {
			Log.error("下载文件异常 FileName " + fileName + ";" + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.error("下载文件异常 FileName " + fileName + ";" + e.getMessage());
			return false;
		}
		return true;
	}

	public final static void checkFile(File file) {
		try {
			if (!file.exists()) {
				File parentFile = new File(file.getParent());

				if (!parentFile.exists()) {
					parentFile.mkdirs();//
				}

				if (file.isDirectory()) {
					file.mkdir();
				} else {
					file.createNewFile();
				}
				Log.info("mkdirs by store method ,the absoulute path is:" + parentFile.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(DataBankMgr.class.getName() + "创建文件错误" + e);
		}
	}

	// data chunk be written per time 128M
	private static final int DATA_CHUNK = 128 * 1024 * 1024;

	// total data size is 2G
	private static final long LEN = 2L * 1024 * 1024 * 1024L;

	public static void writeWithFileChannel(File file) throws IOException {

		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = raf.getChannel();

		byte[] data = null;
		long len = LEN;
		ByteBuffer buf = ByteBuffer.allocate(DATA_CHUNK);
		int dataChunk = DATA_CHUNK / (1024 * 1024);
		while (len >= DATA_CHUNK) {
			System.out.println("write a data chunk: " + dataChunk + "MB");

			buf.clear(); // clear for re-write
			data = new byte[DATA_CHUNK];
			for (int i = 0; i < DATA_CHUNK; i++) {
				buf.put(data[i]);
			}

			data = null;

			buf.flip(); // switches a Buffer from writing mode to reading mode
			fileChannel.write(buf);
			fileChannel.force(true);

			len -= DATA_CHUNK;
		}

		if (len > 0) {
			System.out.println("write rest data chunk: " + len + "B");
			buf = ByteBuffer.allocateDirect((int) len);
			data = new byte[(int) len];
			for (int i = 0; i < len; i++) {
				buf.put(data[i]);
			}

			buf.flip(); // switches a Buffer from writing mode to reading mode, position to 0, limit not changed
			fileChannel.write(buf);
			fileChannel.force(true);
			data = null;
		}

		fileChannel.close();
		raf.close();
	}

	/**
	 * write big file with MappedByteBuffer
	 * 
	 * @throws IOException
	 */
	public static void writeWithMappedByteBuffer(File file,byte[] data) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = raf.getChannel();
		MappedByteBuffer mbb = null;
		mbb = fileChannel.map(MapMode.READ_WRITE, file.length(),data.length);
		mbb.put(data);
		fileChannel.close();
		unmap(mbb); // release MappedByteBuffer
	}

	public static void writeWithTransferTo(File file ,byte[] srcData) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel toFileChannel = raf.getChannel();
		FileLock lock = toFileChannel.tryLock();
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(srcData);
			ReadableByteChannel fromByteChannel = null;
			fromByteChannel = Channels.newChannel(bais);
			toFileChannel.transferFrom(fromByteChannel, file.length(),srcData.length);
			toFileChannel.close();
			fromByteChannel.close();
		} catch (Exception e) {
			Log.error("", e);
		}finally{
			try {
				lock.release();
			} catch (Exception e) {
				Log.error("", e);
			}
		}
	}

	/**
	 * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
	 * 
	 * @param mappedByteBuffer
	 */
	public static void unmap(final MappedByteBuffer mappedByteBuffer) {
		try {
			if (mappedByteBuffer == null) {
				return;
			}

			mappedByteBuffer.force();
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					try {
						Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
						getCleanerMethod.setAccessible(true);
						sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer, new Object[0]);
						cleaner.clean();

					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static void copy(File src,File to){
		if(src!=null&&src.exists()){
			FileChannel srcFileChanel = null ;
			FileChannel toFileChannel = null ;
			try {
				srcFileChanel = new FileInputStream(src).getChannel();
				toFileChannel = new FileOutputStream(to).getChannel();
				srcFileChanel.transferTo(0, src.length(), toFileChannel);
			} catch (IOException e) {
				Log.error("", e);
			}finally{
				try {
					if(srcFileChanel!=null){
						srcFileChanel.close();
					}
					
					if(toFileChannel!=null){
						toFileChannel.close();
					}
				} catch (IOException e) {
					Log.error("", e);
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File("D:/demo_1.txt");
		File toFile = new File("D:/copy_txt.txt");
		copy(file, toFile);
//		RandomAccessFile raf = new RandomAccessFile(file, "rw");
//		FileChannel fileChannel = raf.getChannel();
//
//		ByteBuffer buf = ByteBuffer.wrap("demo pbting".getBytes());
//		buf.flip(); // switches a Buffer from writing mode to reading mode
//		fileChannel.write(buf);
//		fileChannel.force(true);
//		fileChannel.close();
//		raf.close();
//		writeWithTransferTo(file,"我是一个中国人".getBytes(Charset.forName("utf-8")));
//		writeWithMappedByteBuffer(file,"我是一个中国人_1".getBytes(Charset.forName("utf-8")));
		
	}
}
