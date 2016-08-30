package com.road.yishi.log.mgr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.road.yishi.log.Log;
import com.road.yishi.log.util.StringUtil;

import audaque.com.pbting.cache.base.info.HighCache;
import audaque.com.pbting.cache.exception.NeedsRefreshException;
import audaque.com.pbting.cache.factory.GeneralCacheFactory;

/**
 * 
 * <pre>
 * 	消息仓库管理器
 * </pre>
 */
public class MessageHouseMgr {
	
	private final static String className = MessageHouseMgr.class.getSimpleName();
	
	private MessageHouseMgr(){}
	
	private final static Map<String,HighCache> dataMap = new ConcurrentHashMap<String,HighCache>();
	private static Properties houseConfig = null ;
	public static void init(String configPath){
		try {
			houseConfig = new Properties();
			houseConfig.load(new BufferedInputStream(new FileInputStream(new File(configPath))));
		
			GeneralCacheFactory.initByPropertoes(houseConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HighCache getHighCache(String key){
		
		return dataMap.get(key);
	}
	
	public static Object getMessage(String cacheKey,String messagekey){
		
		try {
			return dataMap.get(cacheKey).get(messagekey);
		} catch (NeedsRefreshException e) {
			Log.error(className+":"+e);
		}
		return null ;
	}
	
	public static void putMessage(String cacheKey,String messageKey,Object message){
		dataMap.get(cacheKey).put(messageKey, message);
	}
	
	public static HighCache getMessageStore(String cacheKey){
		
		return dataMap.get(cacheKey);
	}
	
	public static void putMessageStore(String cacheKey,HighCache msgHouse){
		dataMap.put(cacheKey, msgHouse);
	}
	
	/**
	 * 
	 * <pre>
	 * 	得到一个消息仓库存储
	 * </pre>
	 *
	 * @return
	 */
	public static HighCache getNewMessageHouse(String topic){
		if(houseConfig.isEmpty()){
			Log.error(className+":请先初始化创建仓库的必要参数。");
			return null ;
		}
		return GeneralCacheFactory.getInstance().getNewCache(topic);
	}
	
	/**
	 * 
	 * <pre>
	 * 	得到一个消息仓库存储
	 * </pre>
	 *
	 * @return
	 */
	public static HighCache getMessageHouse(String topic){
		if(!StringUtil.isEmpty(topic)){
			return GeneralCacheFactory.getInstance().getHighCache(topic);//();
		}
		return null ;
	}
}
