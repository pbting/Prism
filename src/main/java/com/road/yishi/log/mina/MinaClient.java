package com.road.yishi.log.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import com.road.yishi.log.mina.cmd.ClientProtocol;
import com.road.yishi.log.mina.cmd.client.ClientRecivePackage;
import com.road.yishi.log.mina.cmd.core.CmdMgr;

public class MinaClient {
	
	public static NioSocketConnector connector ;
	public static IoSession serverSession ;//跟服务端通信的 session
	private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);
	public static void main(String[] args) {
		CmdMgr.init(ClientRecivePackage.class);
		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(1000*60);
		connector.setHandler(new ClientHandler());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectCodecFactory()));
		connector.getFilterChain().addLast("executor", new ExecutorFilter(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2+1)));
		connector.getSessionConfig().setBothIdleTime(10);
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress("127.0.0.1", 4404));
		connectFuture.awaitUninterruptibly(1000*60);
		serverSession = connectFuture.getSession();//与服务端通信的session
		EXECUTOR_SERVICE.scheduleAtFixedRate(new SendMessage(serverSession),30,30,TimeUnit.SECONDS);
	}
}

class SendMessage implements Runnable{

	private IoSession serverSession;
	private KryoMessage request;
	public SendMessage(IoSession serverSession) {
		this.serverSession = serverSession;
		request = KryoMessage.buildKryoMessage(ClientProtocol.REQUEST_RESOURCE,"get resource",String.class);
	}
	@Override
	public void run() {
		serverSession.write(request);
	}
}
