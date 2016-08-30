package com.road.yishi.log.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileStatusMgr {

	// 他应该维护一组修改的文件[具有相同的模式]。记录这组文件上一次被解析的位置
	private static final Map<String,ConcurrentHashMap<String,Integer>> topicFileStatus = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();
	private FileStatusMgr() {
	}
	/**
	 * 
	 * <pre>
	 * 	返回前后两次文件改变的大小
	 * </pre>
	 *
	 * @param filePath
	 * @param newPosition
	 * @return
	 */
	public static int setMapPosition(String topic,String filePath,int newPosition){
		if(topicFileStatus.containsKey(topic)){
			
			ConcurrentHashMap<String, Integer> fileStatus = topicFileStatus.get(topic);
			
			Integer position = fileStatus.get(filePath);
			if(position == null){
				position = 0 ;
			}
			fileStatus.put(filePath, newPosition);
			topicFileStatus.put(topic, fileStatus);
			return newPosition-position;
		}else{
			ConcurrentHashMap<String, Integer> fileStatus = new ConcurrentHashMap<String, Integer>();
			fileStatus.put(filePath, newPosition);
			topicFileStatus.put(topic, fileStatus);
			return newPosition ;
		}
	}
	/**
	 * 
	 * <pre>
	 * 	返回上一次文件解析的位置
	 * </pre>
	 *
	 * @param filePath
	 * @return
	 */
	public static int getMapPosition(String topic,String filePath){
		if(topicFileStatus.containsKey(topic)){
			Integer position = topicFileStatus.get(topic).get(filePath);
			return position == null ? 0 :position.intValue();
		}
		return 0 ;
	}
	
	public static ConcurrentHashMap<String,Integer> getTopicPosition(String topic){
		return topicFileStatus.get(topic);
	}
	
	public static void setTopicPosition(String topic,ConcurrentHashMap<String,Integer> topicStatus){
		topicFileStatus.put(topic, topicStatus);
	}
}
