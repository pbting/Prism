package com.road.yishi.log.core;

import java.io.Serializable;

public class Status implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	protected int status ;//0 indicate that no read,1 indicate have read the log
	public Status() {
	}
	public Status(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
