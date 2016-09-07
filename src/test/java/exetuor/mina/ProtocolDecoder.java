package exetuor.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 
 * <pre>
 * 	处理粘包的问题。在解码的时候，因为tcp 规定每个包只能发送指定大小。因此如果有的数据包一次性发送的数据大小超过了这个范围(65536)
 *  就会进行拆包。那这个时候应用层接收到这个数据后，就要处理这个问题 mina 没接收到一个数据包，都会触发 ProtocolDecoder 的 decode 方法，
 *  在这个方法里面处理数据有没有完全被接受。mina 将接收到的网络数据 都放在IoBuffer里面，因此每次decode的时候都会从 ioBuffer中 拿到需要
 *  解析数据包的长度。
 *  
 * </pre>
 */
public class ProtocolDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		//至少需要有8个字节的包头
		if(in.remaining()<ObjectMessage.HEAD_LENGTH){
			return false;
		}
		
		//1、解析出包头表示
		int headFlag = in.getInt();
		int length = in.getInt();
		
		//回溯buffer 的possition
		in.position(in.position()-8);
		
		if(ObjectMessage.HEAD_FLAG!=headFlag){
			System.err.println("数据包头不匹配："+Integer.toHexString(headFlag));
			return false;
		}
		
		if(length < ObjectMessage.HEAD_LENGTH){
			System.err.println("数据包长度不匹配："+length);
			return false;
		}
		//处理tcp 拆包
		if (in.remaining() < length) { // 数据长度不足，等待下次接收
			return false;
		}
		
		byte[] data = new byte[length];//接收到的源数据
		
		in.get(data, 0, length);//处理有粘包的情况
		
		ObjectMessage message = new ObjectMessage();
		//读取head info
		IoBuffer headerBuf = IoBuffer.wrap(data, 0, ObjectMessage.HEAD_LENGTH);
		message.readHeader(headerBuf);
		int bodyLength = length-ObjectMessage.HEAD_LENGTH;
		byte[] bytes = new byte[bodyLength];
		System.arraycopy(data, ObjectMessage.HEAD_LENGTH, bytes, 0, bodyLength);
		message.setBody(bytes);
		out.write(message);
		return true;
	}
}
