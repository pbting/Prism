package com.road.yishi.log.analysor.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.analysor.anotation.TopicHandler;
import com.road.yishi.log.handler.TopicMapper;

//@TopicHandler(fileName="E:/prism/topic_2",topic="topicTwo")
public class TopicTwoMapper extends TopicMapper<String,Person> {

	@Override
	public void map(String line, Map<String, List<Person>> logContext, boolean hasNextToken) {
		String[] values = line.split("=");
		if(values.length == 4){
			String luckyNum = values[0];
			System.out.println("index -->"+values[3]);
			if(true){
//				if(luckyNum.indexOf("6")>=0||luckyNum.indexOf("8")>=0){
				Person person = new Person(Integer.valueOf(values[0]), values[1], values[2], Integer.valueOf(values[3]));
				if(logContext.containsKey(luckyNum)){
					logContext.get(luckyNum).add(person);
				}else{
					List<Person> pers = new ArrayList<Person>();
					pers.add(person);
					logContext.put(luckyNum, pers);
				}
			}
		}
	}
}
