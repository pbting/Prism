package com.road.yishi.log.handler.servlet;

public interface EventNameId {

	public final int DOWNLOAD_DATA = 1 ;//下载数据事件触发
	
	public final int PRE_DOWNLOAD_DATA = 2 ;//下载数据事件触发
	
	public final int FILE_MODIFY = 3 ;//文件发生变化时的处理
}
