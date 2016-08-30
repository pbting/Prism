package com.road.yishi.log.handler.servlet;

import java.io.Serializable;

public class Message implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private String topic;
	private String message;
	public Message() {
	}
	
	public Message(String topic, String message) {
		super();
		this.topic = topic;
		this.message = message;
	}

	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
