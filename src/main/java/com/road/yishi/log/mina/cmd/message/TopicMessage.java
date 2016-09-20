package com.road.yishi.log.mina.cmd.message;

import java.io.Serializable;

public class TopicMessage<K,V> implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	private String topic ;
	
	private K k ;
	
	private V v ;

	public TopicMessage() {
	}

	public TopicMessage(String topic, K k, V v) {
		super();
		this.topic = topic;
		this.k = k;
		this.v = v;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public K getK() {
		return k;
	}

	public void setK(K k) {
		this.k = k;
	}

	public V getV() {
		return v;
	}

	public void setV(V v) {
		this.v = v;
	}
}
