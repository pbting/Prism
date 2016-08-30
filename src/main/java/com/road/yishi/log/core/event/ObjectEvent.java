package com.road.yishi.log.core.event;

import java.util.EventObject;

public class ObjectEvent<V> extends EventObject {

	private static final long serialVersionUID = -855454486771839444L;
	private V value;
	private int eventType;

	/**
	 * @param source 系统默认参数
	 * @param objData 自定义参数
	 * @param eventType 事件健值
	 */
	public ObjectEvent(Object source, int eventType) {
		super(source);
		this.eventType = eventType;
	}

	public ObjectEvent(Object source, V value, int eventType) {
		super(source);
		this.value = value;
		this.eventType = eventType;
	}

	public int getEventType() {
		return eventType;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
	
}
