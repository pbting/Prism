package com.road.yishi.log.monitor;

import java.util.TimerTask;

public class TimerTopicListenerTask extends	TimerTask {

	private TopicListener listener ;
	public TimerTopicListenerTask(TopicListener listener) {
		this.listener = listener;
	}
	@Override
	public void run() {
		listener.onEvent();
	}
}
