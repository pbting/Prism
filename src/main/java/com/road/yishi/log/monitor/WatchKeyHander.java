package com.road.yishi.log.monitor;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.List;

import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;
import com.road.yishi.log.handler.FileCreateHandler;
import com.road.yishi.log.handler.FileDeleteHandler;
import com.road.yishi.log.handler.FileHanderExecutor;
import com.road.yishi.log.handler.FileModifyHandler;

public class WatchKeyHander extends Action {

	private WatchKey watchKey;
	private String listenerPath;

	public WatchKeyHander(WatchKey watchKey, String listenerPath) {
		this.watchKey = watchKey;
		this.listenerPath = listenerPath;
	}

	@Override
	public long execute() throws TaskExecuteException {
		List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
		for (WatchEvent<?> event : watchEvents) {
			if(event.context()!=null){
				String fileName = event.context().toString();
				if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {
					// 文件更改
					FileHanderExecutor.getExecutor().enDefaultQueue(new FileModifyHandler(listenerPath, fileName));
				} else if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_DELETE.name())) {
					// 文件删除
					FileHanderExecutor.getExecutor().enDefaultQueue(new FileDeleteHandler(listenerPath, fileName));
				} else if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_CREATE.name())) {
					// 文件创建
					FileHanderExecutor.getExecutor().enDefaultQueue(new FileCreateHandler(listenerPath, fileName));
				}
			}
		}
		watchKey.reset();
		return 1;
	}
}
