package com.road.yishi.log.core;

import java.io.Serializable;

/**
 * 
 * <pre>
 * 	解析日志元数据信息
 * </pre>
 */
public class LogMetaInfo implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	protected String metaLogInfo ;
	protected String printDate;//该条日志的打印时间
	protected int lineNum ;//调用Log.error 的行号，如果是70 则调用的事普通日志打印，如果调用是76，则调用的是需要传throwable 这个类型
	protected String callClassName ;//发生的额是哪个类
	protected String callMethodName ;//发生的是哪个方法
	public LogMetaInfo() {
	}
	
	public LogMetaInfo(String metaLogInfo, String printDate, String callClassName, String callMethodName) {
		super();
		this.metaLogInfo = metaLogInfo;
		this.printDate = printDate;
		this.callClassName = callClassName;
		this.callMethodName = callMethodName;
	}

	public LogMetaInfo(String printDate, String callClassName, String callMethodName) {
		super();
		this.printDate = printDate;
		this.callClassName = callClassName;
		this.callMethodName = callMethodName;
	}

	public String getMetaLogInfo() {
		return metaLogInfo;
	}


	public void setMetaLogInfo(String metaLogInfo) {
		this.metaLogInfo = metaLogInfo;
	}


	public String getPrintDate() {
		return printDate;
	}


	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}


	public int getLineNum() {
		return lineNum;
	}


	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public String getCallClassName() {
		return callClassName;
	}


	public void setCallClassName(String callClassName) {
		this.callClassName = callClassName;
	}


	public String getCallMethodName() {
		return callMethodName;
	}

	public void setCallMethodName(String callMethodName) {
		this.callMethodName = callMethodName;
	}

	public String getKey(){
		return this.callMethodName+"->"+this.callClassName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callMethodName == null) ? 0 : callMethodName.hashCode());
		result = prime * result + ((callClassName == null) ? 0 : callClassName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogMetaInfo other = (LogMetaInfo) obj;
		//1、Log 中的调用方法
		if (callMethodName == null) {
			if (other.callMethodName != null)
				return false;
		} else if (!callMethodName.equals(other.callMethodName))
			return false;
		//2、
		if (callClassName == null) {
			if (other.callClassName != null)
				return false;
		} else if (!callClassName.equals(other.callClassName))
			return false;
		//3、
		if (lineNum != other.lineNum)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return "LogMetaInfo [printDate=" + printDate + ", lineNum=" + lineNum + ", classMethodKey=" + callClassName + ", callMethodName=" + callMethodName + "]";
	}
	
}
