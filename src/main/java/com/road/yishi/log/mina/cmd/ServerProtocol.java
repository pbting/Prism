package com.road.yishi.log.mina.cmd;

/**
 * 
 * <pre>
 * 	1-3000 means server receive message to load protocol
 * </pre>
 */
public interface ServerProtocol extends Protocol{

	public static final int REQUEST_RESOURCE = 0x0001;
	
	public static final int SEND_LOGKEYS = 0x0002 ;
}
