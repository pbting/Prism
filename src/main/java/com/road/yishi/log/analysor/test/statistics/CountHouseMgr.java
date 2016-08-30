package com.road.yishi.log.analysor.test.statistics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.mgr.MessagePersiterMgr;

public class CountHouseMgr {

	private static final Map<Integer,Integer> COUNT_MAP = new ConcurrentHashMap<Integer,Integer>();
	private static final Map<Integer,Integer> PERSISTER_COUNT_MAP = new ConcurrentHashMap<Integer,Integer>();
	public static int add(String topic,int key,int value){
		if(COUNT_MAP.get(key) == null){
			COUNT_MAP.put(key, 0);
		}
		
		if(PERSISTER_COUNT_MAP.get(key) == null){
			PERSISTER_COUNT_MAP.put(key, 0);
		}
		
		COUNT_MAP.put(key, COUNT_MAP.get(key)+value);
		
		MessagePersiterMgr.persister(topic, key, value, (short)0);
		List<Integer> values = (List<Integer>) MessagePersiterMgr.retrive(topic, key, (short)0, new TypeToken<Integer>(){});
		if(values!=null){
//			System.out.println("add count:"+value+"<-----> retrive count:"+values.size());
			int totalC = 0 ;
			for(int c:values){
				totalC+=c;
			}
			PERSISTER_COUNT_MAP.put(key, PERSISTER_COUNT_MAP.get(key)+totalC);
		}
		return COUNT_MAP.get(key);
	}
	
	public static int getCount(Integer key){
		
		return COUNT_MAP.get(key)==null?0:COUNT_MAP.get(key);
	}
	
	public static Map<Integer,Integer> getCountMap(){
		return COUNT_MAP;
	}
	public static Map<Integer,Integer> getPersisterCountMap(){
		return PERSISTER_COUNT_MAP;
	}
}
