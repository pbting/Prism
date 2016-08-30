package com.road.yishi.log.core;

import java.io.Serializable;

public class LogDetailInfo extends Status implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	private String occurDate ;//这个日志发生的时间
	
	private String logInfo = null ;

	public LogDetailInfo() {
		super(0);
	}

	public LogDetailInfo(String occurDate, String logInfo) {
		super(0);
		this.occurDate = occurDate;
		this.logInfo = logInfo;
		this.status = 0 ;
	}

	public String getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	
	@Override
	public String toString() {
		return "LogDetailInfo [occurDate=" + occurDate + ", logInfo=" + logInfo + "]";
	}
}
