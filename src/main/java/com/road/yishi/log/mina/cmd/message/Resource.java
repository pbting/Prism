package com.road.yishi.log.mina.cmd.message;

import java.io.Serializable;

public class Resource implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private int cpuCount;
	private String platform;
	public int getCpuCount() {
		return cpuCount;
	}
	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	@Override
	public String toString() {
		return "Resource [cpuCount=" + cpuCount + ", platform=" + platform + "]";
	}
}
