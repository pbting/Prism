package com.road.yishi.log.handler.servlet;

import java.io.Serializable;

public class RequestMessage implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	private String topic ;
	
	private String fileName ;
	
	private String eventId ;
	public RequestMessage() {
	}
	
	public RequestMessage(String topic, String fileName, String eventId) {
		super();
		this.topic = topic;
		this.fileName = fileName;
		this.eventId = eventId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
}
