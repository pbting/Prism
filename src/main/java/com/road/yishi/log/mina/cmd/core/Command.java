package com.road.yishi.log.mina.cmd.core;

import org.apache.mina.core.session.IoSession;

import com.road.yishi.log.mina.KryoMessage;

public interface Command {

	public abstract void execute(IoSession session, KryoMessage packet) throws Exception;

}
