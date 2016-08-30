package com.road.yishi.log.monitor;

import java.io.IOException;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Timer;

public class TopicListener implements Runnable {

	private WatchService service;
	private String rootPath;
	private long lastUpdate = 0;
	private WatchKey watchKey;
	private static Timer executorService = new Timer("timer-topic", false);
	
	public static final int ONE_MIN = 1000 * 60;

	public static final int ONE_SECONDS = 1000;
	
	public TopicListener(WatchService service, String rootPath) {
		this.service = service;
		this.rootPath = rootPath;
		lastUpdate = System.currentTimeMillis();
	}

	public void run() {
		try {
			System.out.println("开始监听：" + rootPath);
			executorService.scheduleAtFixedRate(new TimerTopicListenerTask(this), ONE_SECONDS, ONE_SECONDS);
			while (true) {
				watchKey = service.take();// save the last watch key when timer is update then handler the last change
				onEvent();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("fdsfsdf");
			try {
				service.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void onEvent() {
		long currentTime = System.currentTimeMillis();
		// 多久时间采集一次：默认一分钟
		synchronized (rootPath) {
			if (watchKey != null && currentTime - lastUpdate > ONE_SECONDS) {
				synchronized (watchKey) {
					if (watchKey != null) {
						WatchKeyExecutor.getExecutor().enDefaultQueue(new WatchKeyHander(watchKey, rootPath));
						lastUpdate = currentTime;
					}
				}
				watchKey = null;
			}
		}
	}
}