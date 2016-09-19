/**
 * All rights reserved. This material is confidential and proprietary to 7ROAD SQ team.
 */
package com.road.yishi.log.mina.cmd.core;

import org.apache.mina.core.session.IoSession;

import com.road.yishi.log.Log;
import com.road.yishi.log.mina.KryoMessage;

/**
 * <pre>
 * 执行cmd
 * </pre>
 */
public class CmdTask implements Runnable {

	private CmdTaskQueue queue;
	private IoSession session;
	private KryoMessage packet;
	private Command cmd;
	protected Long createTime;

	public CmdTask(Command cmd, IoSession session, KryoMessage packet, CmdTaskQueue queue) {
		this.session = session;
		this.packet = packet;
		this.cmd = cmd;
		this.queue = queue;
		createTime = System.currentTimeMillis();
	}

	@Override
	public void run() {
		if (queue != null) {
			long start = System.currentTimeMillis();
			try {
				cmd.execute(session, packet);
				long end = System.currentTimeMillis();
				long interval = end - start;
				long leftTime = end - createTime;
				if (interval >= 1000) {
					Log.warn("execute action : " + this.toString() + ", interval : " + interval + ", leftTime : " + leftTime + ", size : " + queue.getQueue().size());
				}
			} catch (Exception e) {
				Log.error("执行 command 异常, command : " + cmd.toString() + ", packet : " + packet.toString(), e);
			} finally {
				queue.dequeue(this);
			}
		}
	}

	@Override
	public String toString() {
		return cmd.toString() + ", packet : " + packet.getProtocol();
	}
}
