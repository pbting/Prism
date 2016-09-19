package com.road.yishi.log.mina.cmd;

/**
 * 
 * <pre>
 * 	3001 - 6000 means 客户端向向服务端发送消息
 * </pre>
 */
public interface ClientProtocol extends Protocol{

	public static final int REQUEST_RESOURCE = 0x3001;
}
