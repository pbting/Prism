package com.road.yishi.log.analysor.test.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.analysor.anotation.TopicHandler;
import com.road.yishi.log.handler.TopicMap;

//@TopicHandler(fileName="E:/prism/count_1/count_1.txt",topic="countOne")
public class StatisticCountOneMapper extends TopicMap<Integer,Integer> {

	@Override
	public void map(String line, Map<Integer, List<Integer>> logContext, boolean hasNextToken) {
		if(line!=null&&line.trim().length()>0){
			Integer key = Integer.valueOf(line.trim());
			if(logContext.get(key)==null){
				List<Integer>  count = new ArrayList<Integer>();
				count.add(0);
				logContext.put(key,count);
			}
			
			logContext.get(key).set(0,logContext.get(key).get(0)+1);
		}
	}
}
