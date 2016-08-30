package com.road.yishi.log.mgr;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.Log;
import com.road.yishi.log.bank.DataBankMgr;
import com.road.yishi.log.bank.persister.FilePersistListener;
import com.road.yishi.log.bank.persister.GeneralDiskPersistListener;

import audaque.com.pbting.cache.exception.FilePersistException;

public class MessagePersiterMgr {

	private MessagePersiterMgr(){}
	
	private static final Map<String,FilePersistListener> topicFilePMap = new ConcurrentHashMap<String,FilePersistListener>();
	
	public static void init(TopicPath...topicPaths){
		for(TopicPath tp : topicPaths){
			DataBankMgr.init(tp.getTopic(),tp.getPath());
			FilePersistListener fp = new GeneralDiskPersistListener();
			fp.config(tp.getTopic(), tp.getPath());
			topicFilePMap.put(tp.getTopic(),fp);
		}
		DataBankMgr.registerTask();
	}
	
	public static void persister(String topic,Object key,Object value,short type){
		if(topicFilePMap.containsKey(topic)){
			try {
				topicFilePMap.get(topic).store(topic, key, value, type);
			} catch (FilePersistException e) {
				Log.error("", e);
			}
		}
	}
	
	public static Set<String> getAllTopics(){
		return topicFilePMap.keySet();
	}
	
	
	public static<T> Object retrive(String topic,Object key,short type,TypeToken<T> typeToken){
		if(topicFilePMap.containsKey(topic)){
			try {
				return topicFilePMap.get(topic).retrieve(topic, key,type,typeToken);
			} catch (FilePersistException e) {
				Log.error("", e);
			}
		}
		return null ;
	}
	
	
	public static void main(String[] args) {
		init(new TopicPath("countOne", "D:/prism/count"));
	}
}
