package com.road.yishi.log.mgr;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.road.yishi.log.Log;
import com.road.yishi.log.util.StringUtil;

public class ConfigMgr {
	private static Properties properties = null ;
	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param path 配置文件的路径
	 * @param dataPath 日志数据存储的路径
	 */
	public static void init(String path,String dataPath){
		if(!StringUtil.isEmpty(path)){
			try {
				InputStream is = new BufferedInputStream(new FileInputStream(path));
				properties = new Properties();
				properties.load(is);
				properties.put("dataPath", dataPath);
			} catch (IOException e) {
				Log.error(ConfigMgr.class.getName(), e);
			}
		}
	}
	
	public static String getMsgHouseConfig(){
		return properties.getProperty("msghourse");
	}
	
	public static String getlog4jPath(){
		return properties.getProperty("log4j.path");
	}
	
	public static String getTopicHandler(){
		return properties.getProperty("topic_handler");
	}
	
	public static String getMonitorDir(){
		
		return properties.getProperty("monitor.path");
	}
	
	public static String getTopics(){
		return properties.getProperty("topics");
	}
	
	public static String getMsgPath(){
		return properties.getProperty("msg.path");
	}
	
	public static String getMaster(){
		return properties.getProperty("master");
	}
	public static String[] getSlaves(){
		return properties.getProperty("slaves").split("[;]");
	}
	
	public static String getRole(){
		String role = properties.getProperty("role");
		if(StringUtil.isEmpty(role)){
			role = "salve";
		}
		return role;
	}
	
	public static String getTransMode(){
		String transMode = properties.getProperty("trans.mode");
		if(StringUtil.isEmpty(transMode)){
			transMode = "download";//default transfer mode is download
		}
		return transMode;
	}
	
	public static String getDataPath(){
		
		return properties.getProperty("dataPath");
	}
	
	public static int getReUploadFail(){
		String count = properties.getProperty("reupload.fail");
		if(!StringUtil.isEmpty(count)&&StringUtil.isNumeric(count)){
			
			return Integer.parseInt(count);
		}
		
		return 3 ;
	}
}
