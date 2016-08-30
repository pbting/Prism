package com.road.yishi.log.handler.topic;

import com.road.yishi.log.core.quence.Executor;

public class TopicHandlerExecutor {

	private TopicHandlerExecutor() {
	}

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
}
