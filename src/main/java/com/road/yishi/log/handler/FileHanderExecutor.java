package com.road.yishi.log.handler;

import com.road.yishi.log.core.quence.Executor;

public class FileHanderExecutor {

	private FileHanderExecutor() {
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
