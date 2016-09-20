package com.road.yishi.log.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.road.yishi.log.Log;
import com.road.yishi.log.mgr.ConfigMgr;
import com.road.yishi.log.mina.cmd.ServerProtocol;
import com.road.yishi.log.mina.cmd.client.SlaveRecivePackage;
import com.road.yishi.log.mina.cmd.core.CmdMgr;

public class MinaSlaveServer {
	
	private static NioSocketConnector connector ;
	private static IoSession serverSession ;//跟服务端通信的 session
	private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);
	public static void init(){
		String masterHost = ConfigMgr.getMasterHost();
		if(StringUtils.isNotEmpty(masterHost)){
			Log.debug("slave connector to master host is:"+masterHost);
			CmdMgr.init(SlaveRecivePackage.class);
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(1000*60);
			connector.setHandler(new ClientHandler());
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectCodecFactory()));
			connector.getFilterChain().addLast("executor", new ExecutorFilter(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2+1)));
			connector.getSessionConfig().setBothIdleTime(10);
			ConnectFuture connectFuture = connector.connect(new InetSocketAddress(masterHost, 4404));
			connectFuture.awaitUninterruptibly(1000*60);
			serverSession = connectFuture.getSession();//与服务端通信的session
		}else{
			Log.error("master is not config in config.properties file.");
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 	
	 * </pre>
	 *
	 * @return
	 */
	public static IoSession getMasterIoSession(){
		final IoSession session = serverSession ;//不希望上层拿到这个session 做修改，因此需要final 一次
		return session;
	}
	
	public static void main(String[] args) {
		
		EXECUTOR_SERVICE.scheduleAtFixedRate(new SendMessage(serverSession),0,1,TimeUnit.MINUTES);
	}
}

class SendMessage implements Runnable{

	private IoSession serverSession;
	private KryoMessage request;
	public SendMessage(IoSession serverSession) {
		this.serverSession = serverSession;
		request = KryoMessage.buildKryoMessage(ServerProtocol.REQUEST_RESOURCE,"get resource",String.class,true);
	}
	@Override
	public void run() {
		serverSession.write(request);
	}
}
