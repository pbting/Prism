package com.road.yishi.log.analysor.test;

import java.util.List;

import com.road.yishi.log.analysor.anotation.TopicReduce;
import com.road.yishi.log.handler.Reducer;

//@TopicReduce(topic="topicThree")
public class TopicThreeReducer implements Reducer<String,List<Person>> {

	@Override
	public void reduce(String k, List<Person> v) {
		TopicHouseMgr.getExecutor().enDefaultQueue(new PutMessage<Person>("topicThree", k, v));
		TopicHouseMgr.getExecutor().enDefaultQueue(new TakeMessage("topicThree", k));
	}
}
