package com.road.yishi.log.handler;

import java.util.List;
import java.util.Map;

public interface AnalylizeLogInter<K,V> {
	
	public Map<K,List<V>> analylize(String logInfo);
}
