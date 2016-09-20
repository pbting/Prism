package com.road.yishi.log.core;

public interface ReadFile{
	public final static int BLOCKING_SIZE = 1024 * 1024 * 62;// 缓冲区大小为64M,每个mapper 处理数据的大小
}
