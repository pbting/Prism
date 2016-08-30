package com.road.yishi.log.mgr;

public class HandlerInfo {

	private String className;
	private String fileName ;
	private String topic;
	public HandlerInfo() {
	}
	
	public HandlerInfo(String className, String topic) {
		super();
		this.className = className;
		this.topic = topic;
	}

	public HandlerInfo(String className, String fileName,String topic) {
		super();
		this.className = className;
		this.fileName = fileName;
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
