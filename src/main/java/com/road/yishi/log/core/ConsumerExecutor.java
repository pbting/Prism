package com.road.yishi.log.core;

import java.util.Map;

import com.road.yishi.log.core.quence.Executor;
import com.road.yishi.log.handler.ConsumerData;
import com.road.yishi.log.handler.ForwardMessage;

public class ConsumerExecutor {

	private ConsumerExecutor() {
	}

	private static Executor executor;
	static {
		int corePoolSize = Runtime.getRuntime().availableProcessors()*2+1;
		int maxPoolSize = 2 * corePoolSize;
		int keepAliveTime = 5;
		int cacheSize = 64;
		
		executor = new Executor(corePoolSize,maxPoolSize, keepAliveTime,cacheSize, "ConsumerExecutor-log");
	}
	
	public static Executor getExecutor(){
		return executor;
	}
	/**
	 * 
	 * <pre>
	 * 	when role is master then call the method to handler the log message
	 * </pre>
	 *
	 * @param topic
	 * @param logs
	 */
	public static <K, V> void consumer(String topic, Map<K,V> logs) {
		executor.enDefaultQueue(new ConsumerData<K,V>(topic, logs));
	}
	
	public static void forward(String topic,String abFilePath,StringBuilder message){
		executor.enDefaultQueue(new ForwardMessage(topic,abFilePath,message));
	}
}
