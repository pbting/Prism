package com.road.yishi.log.handler;

public interface Reducer<K,V> {

	public void reduce(K k,V v);
}
