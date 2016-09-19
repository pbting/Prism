package com.road.yishi.log.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.road.yishi.log.Log;
import com.road.yishi.log.mina.cmd.core.CmdExecutor;
import com.road.yishi.log.mina.cmd.core.CmdMgr;
import com.road.yishi.log.mina.cmd.core.CmdTask;
import com.road.yishi.log.mina.cmd.core.Command;

public class ClientHandler extends IoHandlerAdapter {
	private static CmdExecutor CmdExecutor;

	static {
		int processor = Runtime.getRuntime().availableProcessors();
		CmdExecutor = new CmdExecutor(processor * 2 + 1, processor * 2 + 1 + 10, 10, -1, "prism server handler");
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		KryoMessage objectMessage = (KryoMessage) message;
		Command command = CmdMgr.COMMAND_MAP.get(objectMessage.getProtocol());
		if (command != null) {
			CmdExecutor.enDefaultQueue(new CmdTask(command, session, objectMessage,CmdExecutor.getDefaultQueue()));
			Log.debug(objectMessage.getProtocol() + "protocol is executor;the command class name:" + command.getClass().getName());
		} else {
			Log.error(objectMessage.getProtocol() + "protocol is not related command");
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		session.closeOnFlush();
	}
}
