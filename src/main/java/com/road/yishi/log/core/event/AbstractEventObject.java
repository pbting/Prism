package com.road.yishi.log.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.road.yishi.log.Log;

/**
 * 
 * <pre>
 * 	事件提供者：发布自己的事件，让对该事件感兴趣的监听者 处理
 * </pre>
 */
public abstract class AbstractEventObject {
	private ConcurrentHashMap<Integer, Collection<ObjectListener<?>>> listeners;
	private static boolean isDebug = false;
	private Object lock = new Object();
	//所有的事件都异步处理
	private final static ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
	public void addListener(ObjectListener<?> objectListener, int eventType) {
		synchronized (lock) {
			if (listeners == null) {
				listeners = new ConcurrentHashMap<Integer, Collection<ObjectListener<?>>>();
			}
		}

		if (listeners.get(eventType) == null) {
			Collection<ObjectListener<?>> tempInfo = new HashSet<ObjectListener<?>>();
			tempInfo.add(objectListener);
			listeners.put(eventType, tempInfo);
		} else {
			listeners.get(eventType).add(objectListener);
		}
		debugEventMsg("注册一个事件,类型为" + eventType);
	}

	public void removeListener(ObjectListener<?> objectListener, int eventType) {
		if (listeners == null)
			return;
		Collection<ObjectListener<?>> tempInfo = listeners.get(eventType);
		if (tempInfo != null) {
			tempInfo.remove(objectListener);
		}
		debugEventMsg("移除一个事件,类型为" + eventType);
	}

	public void notifyListeners(ObjectEvent<?> event) {
		List<ObjectListener<?>> tempList = null;
		if (listeners == null){
			return;
		}
		int eventType = event.getEventType();
		Collection<ObjectListener<?>> tempInfo = listeners.get(eventType);
		if (tempInfo != null) {
			tempList = new ArrayList<ObjectListener<?>>();
			tempList.addAll(tempInfo);
			// 触发
			if (tempList != null) {
				for (ObjectListener<?> listener : tempList) {
					listener.onEvent(event);
				}
			}
		}else{
			Log.error(event.getEventType()+" 没有相关的事件监听器!");
		}
	}

	public void clearListener() {
		synchronized (lock) {
			if (listeners != null) {
				listeners = null;
			}
		}
	}

	public void debugEventMsg(String msg) {
		if (isDebug) {
			Log.info(msg);
		}
	}
}