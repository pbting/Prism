package com.road.yishi.log.analysor.test;

import java.util.List;

import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;

public class PutMessage<V> extends Action{
	
	String topic;
	String key;
	List<V> msg;
	public PutMessage(String topic, String key, List<V> msg){
		this.topic = topic;
		this.key = key;
		this.msg = msg;
	}
	
	@Override
	public long execute() throws TaskExecuteException {
		TopicHouseMgr.put(topic, key, msg);
		return 0;
	}
}
