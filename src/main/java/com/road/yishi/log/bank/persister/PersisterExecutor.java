package com.road.yishi.log.bank.persister;

import com.road.yishi.log.core.quence.Executor;

public class PersisterExecutor {

	private PersisterExecutor(){}

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
