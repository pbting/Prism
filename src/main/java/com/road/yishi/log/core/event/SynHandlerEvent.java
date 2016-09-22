package com.road.yishi.log.core.event;

import java.util.List;

public class SynHandlerEvent implements Runnable {

	private List<ObjectListener<?>> listeners ;
	private ObjectEvent<?> event;
	public SynHandlerEvent(List<ObjectListener<?>> listeners,ObjectEvent<?> event) {
		this.listeners = listeners;
		this.event = event;
	}
	
	@Override
	public void run() {
		if (listeners != null) {
			for (ObjectListener<?> listener : listeners) {
				listener.onEvent(event);
			}
		}
	}
}
