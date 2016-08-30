package com.road.yishi.log.servlet.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.road.yishi.log.vo.LogKeyInfo;

public class ConsumerLogFactory {

	private ConsumerLogFactory() {
	}

	private static final ConsumerLogFactory CONSUMER_LOG_FACTORY = new ConsumerLogFactory();
	
	public static final Map<String,Map<LogKeyInfo,Integer>> countMap = new ConcurrentHashMap<String,Map<LogKeyInfo,Integer>>();
}
