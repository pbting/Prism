package com.road.yishi.log.handler;

import java.util.Map;

import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;
import com.road.yishi.log.mgr.TopicHandlerMappingMgr;

public class ConsumerData<K,V> extends Action{
	private Map<K,V> data ;
	private String topic ;
	public ConsumerData(String topic,Map<K,V> data){
		this.data = data ;
		this.topic = topic;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public long execute() throws TaskExecuteException {
		long start = System.currentTimeMillis();
		for (Map.Entry<K,V> entry : data.entrySet()) {
			TopicHandlerMappingMgr.getTopicReduce(topic).reduce(entry.getKey(), entry.getValue());
		}
		return System.currentTimeMillis()-start;
	}
}
