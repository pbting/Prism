package exetuor.mina;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter {
	private final static AtomicInteger count = new AtomicInteger(1);
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		int c = count.getAndIncrement();
		if(c>300){
			System.out.println(c+"---end-----");
			session.closeOnFlush();//(true);
			session.getService().dispose();
			MinaClient.connector.dispose(false);
			return ;
		}
		ObjectMessage objectMessage = (ObjectMessage) message;
		Person person = KryoSerializeUtil.derializable(objectMessage.getBody(), Person.class);
		System.out.println("接收服务端返回的消息："+person.toString());
		//
		person.setAge(person.getAge()+c);
		ObjectMessage send = new ObjectMessage();
		send.setBody(KryoSerializeUtil.serializable(person));
		session.write(send);
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
	}
}
