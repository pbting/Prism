package exetuor.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {

	public static void main(String[] args) {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(1000*60);
		connector.setHandler(new ClientHandler());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectCodecFactory()));
		connector.getFilterChain().addLast("executor", new ExecutorFilter(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2+1)));
		connector.getSessionConfig().setBothIdleTime(10);
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress("127.0.0.1", 4404));
		connectFuture.awaitUninterruptibly(1000*60);
		IoSession serverSession = connectFuture.getSession();//与服务端通信的session
		System.out.println("连接成功");
		//向服务端发送一个消息
		ObjectMessage objectMessage = new ObjectMessage();
		Person person = new Person();
		person.setAddress("江西省吉安市");
		person.setAge(23);
		person.setName("pbting-1");
		String[] array = new String[30];
		for(int i=0;i<30;i++){
			array[i] = "ARRAY_"+i;
		}
		person.setDemoArry(array);
		long start = System.currentTimeMillis();
		byte[] body = KryoSerializeUtil.serializable(person);
		System.out.println("序列化时间："+(System.currentTimeMillis()-start));
		objectMessage.setBody(body);
		objectMessage.setLength((short)body.length);
		serverSession.write(objectMessage);
	}
}
