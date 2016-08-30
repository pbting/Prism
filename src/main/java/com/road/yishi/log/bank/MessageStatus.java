package com.road.yishi.log.bank;

public class MessageStatus {

	private long type ;//1表示已经消费的，0表示未消费
	private long startIndex ;
	private long size ;
	public MessageStatus() {}
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	public long getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	public MessageStatus(long type, long startIndex, long size) {
		super();
		this.type = type;
		this.startIndex = startIndex;
		this.size = size;
	}
	@Override
	public String toString() {
		return "MessageStatus [type=" + type + ", startIndex=" + startIndex + ", size=" + size + "]";
	}
}
