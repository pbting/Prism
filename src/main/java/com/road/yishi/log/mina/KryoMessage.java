package com.road.yishi.log.mina;

import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

import com.road.yishi.log.util.ZLibUtils;
/**
 * 
 * <pre>
 * 	12个字节长度的包头：3 * 4
 *  第一个字节：消息头标识
 *  第二个字节：该消息的长度
 *  第三个字节：该消息携带的协议号
 * </pre>
 */
public class KryoMessage implements Serializable{
	public final static int HEAD_FLAG = 0x7784;
	
	public final static int HEAD_LENGTH = 12 ;//分配包头有20个字节的长度,再分一个int 4个字节的协议号
	public KryoMessage() {
	}
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private int length ;
	private int protocol;//协议号
	private byte[] body ;
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
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
		this.protocol = ioBuffer.getInt();
	}
	
	public static <T extends Serializable> KryoMessage buildKryoMessage(int protocol,T t,Class<T> clazz){
		KryoMessage kryoMessage = new KryoMessage();
		byte[] body = KryoUtil.serialization(t, clazz).getBytes();
		kryoMessage.setProtocol(protocol);
		kryoMessage.setLength(body.length);
		kryoMessage.setBody(body);
		return kryoMessage;
	}
	public static <T extends Serializable> KryoMessage buildKryoMessage(int protocol,T t,Class<T> clazz,boolean isZlib){
		if(!isZlib){
			return buildKryoMessage(protocol, t, clazz);
		}else{
			KryoMessage kryoMessage = new KryoMessage();
			byte[] body = KryoUtil.serialization(t, clazz).getBytes();
			body = ZLibUtils.compress(body);
			kryoMessage.setProtocol(protocol);
			kryoMessage.setLength(body.length);
			kryoMessage.setBody(body);
			return kryoMessage;
		}
	}
}
