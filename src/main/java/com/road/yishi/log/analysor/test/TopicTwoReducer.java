package com.road.yishi.log.analysor.test;

import java.util.List;

import com.road.yishi.log.analysor.anotation.TopicReduce;
import com.road.yishi.log.handler.Reducer;

//@TopicReduce(topic = "topicTwo")
public class TopicTwoReducer implements Reducer<String, List<Person>> {

	@Override
	public void reduce(String k, List<Person> v) {
		TopicHouseMgr.getExecutor().enDefaultQueue(new PutMessage<Person>("topicTwo", k, v));
		TopicHouseMgr.getExecutor().enDefaultQueue(new TakeMessage("topicTwo", k));
	}
}
