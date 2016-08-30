package com.road.yishi.log.core;

public class Status {

	protected int status ;//0 indicate that no read,1 indicate have read the log

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
