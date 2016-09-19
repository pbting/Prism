/**
 * All rights reserved. This material is confidential and proprietary to 7ROAD SQ team.
 */
package com.road.yishi.log.mina.cmd.core;

import java.util.LinkedList;
import java.util.Queue;

import com.road.yishi.log.Log;

/**
 * <pre>
 * CmdTaskQueue基本功能实现
 * </pre>
 */
public class AbstractCmdTaskQueue implements CmdTaskQueue {

	private Queue<CmdTask> queue;
	private CmdExecutor executor;

	public AbstractCmdTaskQueue(CmdExecutor executor) {
		this.executor = executor;
		queue = new LinkedList<CmdTask>();
	}

	public CmdTaskQueue getCmdTaskQueue() {
		return this;
	}

	public void enqueue(CmdTask cmd) {
		int queueSize = 0;
		synchronized (queue) {
			queue.add(cmd);
			queueSize = queue.size();
		}

		if (queueSize == 1) {
			executor.execute(cmd);
		}
		if (queueSize > 1000) {
			Log.warn(cmd.toString() + " queue size : " + queueSize);
		}
	}

	public void dequeue(CmdTask cmdTask) {
		CmdTask nextCmdTask = null;
		int queueSize = 0;
		//boolean shouldError = false;
		String tmpString = null;
		synchronized (queue) {
			queueSize = queue.size();
			CmdTask temp = queue.remove();
			if (temp != cmdTask) {
				tmpString = temp.toString();

			}
			if (queueSize != 0) {
				nextCmdTask = queue.peek();
			}
		}

		if (nextCmdTask != null) {
			executor.execute(nextCmdTask);
		}
		if (queueSize == 0) {
			Log.error("queue.size() is 0.");
		}
		if (tmpString != null) {
			Log.error("action queue error. temp " + tmpString + ", action : " + cmdTask.toString());
		}
	}

	public Queue<CmdTask> getQueue() {
		return queue;
	}

	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}
}
