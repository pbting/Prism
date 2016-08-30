package com.road.yishi.log.handler.topic;

import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;
import com.road.yishi.log.handler.AnalylizeLogInter;

public abstract class TopicHandler extends Action{
	protected String topic;
	protected String fileName ;
	protected AnalylizeLogInter<LogMetaInfo, LogDetailInfo> analysor ;
	protected boolean isRepeat ;
	public TopicHandler(AnalylizeLogInter<LogMetaInfo, LogDetailInfo> analysor,String topic,String fileName,boolean isRepeat){
		this.topic = topic;
		this.fileName = fileName;
		this.analysor = analysor;
		this.isRepeat = isRepeat;
	}

	@Override
	public long execute() throws TaskExecuteException {
		return this.execute(topic, fileName);
	}
	
	public abstract long execute(String topic,String fileName);
}
