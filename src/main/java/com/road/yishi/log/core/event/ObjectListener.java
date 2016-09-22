package com.road.yishi.log.core.event;

import java.util.EventListener;
/**
 * 
 * <pre>
 * 	事件处理器
 * </pre>
 */
public interface ObjectListener<V> extends EventListener {
	
	public void onEvent(ObjectEvent<?> event);

}
