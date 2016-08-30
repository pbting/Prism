package com.road.yishi.log.core.quence;

import java.util.LinkedList;
import java.util.Queue;

public class BaseActionQueue implements ActionQueue {

	private Queue<Action> queue;
	private Executor executor;

	public BaseActionQueue(Executor executor) {
		this.executor = executor;
		queue = new LinkedList<Action>();
	}

	public BaseActionQueue(Executor executor, Queue<Action> queue) {
		this.executor = executor;
		this.queue = queue;
	}

	public ActionQueue getActionQueue() {
		return this;
	}

	public Queue<Action> getQueue() {
		return queue;
	}

	public void enqueue(Action action) {
		int queueSize = 0;
		synchronized (queue) {
			queue.add(action);
			queueSize = queue.size();
		}

		if (queueSize == 1) {
			executor.execute(action);
		}
		if (queueSize > 1000) {
			// Log.warn(action.toString() + " queue size : " + queueSize);
		}
	}

	public void dequeue(Action action) {
		Action nextAction = null;
		int queueSize = 0;
		String tmpString = null;
		synchronized (queue) {
			queueSize = queue.size();
			Action temp = queue.remove();
			if (temp != action) {
				tmpString = temp.toString();

			}
			if (queueSize != 0) {
				nextAction = queue.peek();
			}
		}

		if (nextAction != null) {
			executor.execute(nextAction);
		}
		if (queueSize == 0) {
			// Log.error("queue.size() is 0.");
		}
		if (tmpString != null) {
			// Log.error("action queue error. temp " + tmpString + ", action : " + action.toString());
		}
	}

	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}
}
