package exetuor.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ServerHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		ObjectMessage objectMessage = (ObjectMessage) message;
		Person person = KryoSerializeUtil.derializable(objectMessage.getBody(), Person.class);
		System.out.println("message recive:"+person.toString());
		person.setVersion("1.0.1");
		ObjectMessage response = new ObjectMessage();
		response.setBody(KryoSerializeUtil.serializable(person));
		session.write(response);
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}
	
}
