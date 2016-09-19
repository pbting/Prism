package com.road.yishi.log.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.road.yishi.log.Log;
import com.road.yishi.log.mina.cmd.core.CmdMgr;
import com.road.yishi.log.mina.cmd.server.ServerRecivePackage;

public class MinaServer {

	public static void main(String[] args) {
		try {
			CmdMgr.init(ServerRecivePackage.class);
			NioSocketAcceptor socketAcceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()*2+1);
			SocketSessionConfig config = socketAcceptor.getSessionConfig();
			config.setBothIdleTime(10);
			config.setSendBufferSize(4096);
			config.setReadBufferSize(4096);
			config.setSoLinger(5);// 5秒
			config.setTcpNoDelay(true);
			socketAcceptor.setHandler(new ServerHandler());
			socketAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectCodecFactory()));
			socketAcceptor.getFilterChain().addLast("executor", new ExecutorFilter(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1)));
			socketAcceptor.bind(new InetSocketAddress(4404));
			System.out.println("开始监听："+4404);
		} catch (IOException e) {
			Log.error("", e);
		}
	}
}
