package com.road.yishi.log.handler;

import com.road.yishi.log.core.TaskExecuteException;

public class FileDeleteHandler extends FileHandler{

	public FileDeleteHandler(String topic,String filePath){
		super(topic,filePath);
	}
	
	@Override
	public void execute(String dir, String fileName) throws TaskExecuteException {
	}
}
