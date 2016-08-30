package com.road.yishi.log.handler.topic;

import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.core.MappedBufferReadFile;
import com.road.yishi.log.handler.AnalylizeLogInter;
import com.road.yishi.log.handler.FileStatusMgr;

/**
 * <pre>
 * 	这个只对error 文件进行监测
 * </pre>
 */
public class ErrorTopicModifyHandler extends TopicHandler {
	public ErrorTopicModifyHandler(AnalylizeLogInter<LogMetaInfo, LogDetailInfo> analysor, String topic, String fileName, boolean isRepeat) {
		super(analysor, topic, fileName, isRepeat);
	}

	@Override
	public long execute(String topic, String fileName) {
		try {
			int lastReadIndex = FileStatusMgr.getMapPosition(topic, fileName);// 获取上一次文件解析的位置
			int nextReadIndex = MappedBufferReadFile.readAnalysis(analysor, fileName, topic, lastReadIndex, isRepeat);
			FileStatusMgr.setMapPosition(topic, fileName, nextReadIndex);// 更新当前解析的位置
			return nextReadIndex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}