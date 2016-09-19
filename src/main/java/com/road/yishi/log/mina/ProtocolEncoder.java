package com.road.yishi.log.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ProtocolEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		KryoMessage objectMessage = (KryoMessage) message;
		if(objectMessage==null||objectMessage.getBody()==null||objectMessage.getBody().length<=0){
			return ;
		}
		//1、默认有一个head的长度，20字节
		int size = KryoMessage.HEAD_LENGTH + objectMessage.getBody().length;//这是整个数据包的长度
		IoBuffer buffer = IoBuffer.allocate(size);
		buffer.putInt(KryoMessage.HEAD_FLAG);//第一个int 字节处放标识
		buffer.putInt(size);//第二个int 放数据包长度
		buffer.putInt(objectMessage.getProtocol());//第三个int 该消息所携带的协议号
		
		buffer.put(objectMessage.getBody());
		buffer.flip();
		out.write(buffer);
	}
}
