package exetuor.mina;

import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

public class ObjectMessage implements Serializable{
	public final static int HEAD_FLAG = 0x7784;
	
	public final static int HEAD_LENGTH = 8 ;//分配包头有20个字节的长度
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private int length ;
	private byte[] body ;
	public int getLength() {
		return length;
	}
	public void setLength(short length) {
		this.length = length;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	
	public void readHeader(IoBuffer ioBuffer){
		ioBuffer.getInt();
		this.length = ioBuffer.getInt();
	}
	
}
