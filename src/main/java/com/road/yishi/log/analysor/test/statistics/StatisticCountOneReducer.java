package com.road.yishi.log.analysor.test.statistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.road.yishi.log.Log;
import com.road.yishi.log.analysor.anotation.TopicReduce;
import com.road.yishi.log.handler.Reducer;

//@TopicReduce(topic="countOne")
public class StatisticCountOneReducer implements Reducer<Integer,List<Integer>> {
	@Override
	public void reduce(Integer k, List<Integer> v) {
//		System.out.println(k+"--内存出现次数->"+CountHouseMgr.add("countOne",k, v.get(0)));
//		System.out.println(k+"--磁盘出现次数->"+);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter("D:/out_count.txt",true));){
			writer.write(k+"----reducer----"+v.get(0));
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			Log.error("", e);
		}
		
		int memoryCount = CountHouseMgr.add("countOne",k, v.get(0));
		System.out.println(k+"-->memory count:"+memoryCount);
		int diskCount = CountHouseMgr.getPersisterCountMap().get(k);
		System.out.println(k+"-->disk count:"+diskCount);
		System.out.println("<------------------------->");
	}
}
