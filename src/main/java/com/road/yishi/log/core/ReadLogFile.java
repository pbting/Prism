package com.road.yishi.log.core;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.handler.AnalylizeLogInter;

@Deprecated
public class ReadLogFile implements ReadFile{
	/**
	 * <pre>
	 * </pre>
	 *
	 * @param fileChannel
	 * @param fileSize
	 * @param lastReadIndex
	 * @return 返回当前读取的文件位置
	 * @throws IOException
	 */
	public static int readAnalysis(AnalylizeLogInter analylize,FileChannel fileChannel, long fileSize, int lastReadIndex) throws IOException {
		synchronized (fileChannel) {
			long size = fileSize > BUFFER_SIZE ? BUFFER_SIZE : fileSize;// 设置缓冲区的大小
			boolean isQuit = true;
			while (isQuit) {
				// 1、计算剩下读取的字节,表示已经读取到文件的结尾处了
				if (fileSize - lastReadIndex <= size) {
					size = fileSize - lastReadIndex;
					isQuit = false;
				}
				
				if(size <= 0)
					break;
				
				MappedByteBuffer inputBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, lastReadIndex, size);

				// 2、初始化需要读取的字节数
				byte[] dst = new byte[(int) size];

				// 3、读取实际的字节数
				for (int i = 0; i < size; i++) {
					dst[i] = inputBuffer.get(i);
				}
				// 在这里使用多线程消费读取后的数据
				Map<LogMetaInfo, List<LogDetailInfo>> result = analylize.analylize(new String(dst));
				for (Map.Entry<LogMetaInfo, List<LogDetailInfo>> entry : result.entrySet()) {
					String key = entry.getKey().getKey();
//					int logCount = ConsumerLogFactory.add(key, entry.getValue());
//					ConsumerLogFactory.updateKeyCount(new LogKeyInfo(key, entry.getKey().getPrintDate()), logCount);
				}
				// 设置好下一次读取的位置
				lastReadIndex += size;
			}
			return lastReadIndex;
		}

	}
}