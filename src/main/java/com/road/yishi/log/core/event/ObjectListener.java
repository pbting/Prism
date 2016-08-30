package com.road.yishi.log.core.event;

import java.util.EventListener;

public interface ObjectListener<V> extends EventListener {
	
	public void onEvent(ObjectEvent<?> event);

}
