package com.road.yishi.log.analysor.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.core.quence.Executor;
import com.road.yishi.log.mgr.MessagePersiterMgr;

public class TopicHouseMgr {

	private final static Map<String, Map<String, List<Person>>> messageMap = new ConcurrentHashMap<String, Map<String, List<Person>>>();

	private static Executor executor;
	static {
		int corePoolSize = 8;
		int maxPoolSize = 16;
		int keepAliveTime = 5;
		int cacheSize = 64;
		executor = new Executor(corePoolSize, maxPoolSize, keepAliveTime,cacheSize, "handler-log");
	}
	
	public static Executor getExecutor(){
		return executor;
	}
	
	public static<V> void put(String topic, String key, List<V> msg) {
		// if(messageMap.get(topic) == null){
		// messageMap.put(topic, new HashMap<String,List<Person>>());
		// }
		//
		// if(messageMap.get(topic).get(key) == null){
		// messageMap.get(topic).put(key,new ArrayList<Person>());
		// }
		//
		// messageMap.get(topic).get(key).addAll(msg);
		//
		// if(messageMap.get(topic).get(key).size() > 25){
		// //每一种类型的消息累计生产25个以上，则存数据库一遍
		// MessagePersiterMgr.persister(topic, key, msg, (short)0);
		// messageMap.get(topic).get(key).clear();
		// }
		MessagePersiterMgr.persister(topic, key, msg, (short) 0);
	}

	public static List<Person> getPersons(String topic, String key) {
		// if(messageMap.containsKey(topic)&&
		// messageMap.get(topic).containsKey(key)){
		// return messageMap.get(topic).get(key);
		// }
		ArrayList<Person> messageList = new ArrayList<Person>();
		List<List<Person>> message = (List<List<Person>>) MessagePersiterMgr.retrive(topic, key, (short) 0, new TypeToken<List<Person>>() {
				});
		if (message != null) {
			for (List<Person> p : message) {
				messageList.addAll(p);
			}
		}
		return messageList;
	}
}
