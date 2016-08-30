package com.road.yishi.log.mgr;

public class TopicPath {

	private String topic;
	private String path ;
	
	public TopicPath() {
	}

	public TopicPath(String topic, String path) {
		super();
		this.topic = topic;
		this.path = path;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
