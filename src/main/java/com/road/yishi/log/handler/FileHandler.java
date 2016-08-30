package com.road.yishi.log.handler;

import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;
import com.road.yishi.log.core.quence.ActionQueue;

public abstract class FileHandler extends Action {

	protected String fileName ;
	protected String dir ;
	public FileHandler(String dir ,String filePath){
		this.dir = dir;
		this.fileName = filePath;
	}
	
	public FileHandler(ActionQueue queue) {
		super(queue);
	}

	@Override
	public long execute() throws TaskExecuteException {
		execute(dir,fileName);
		return 0;
	}
	public abstract void execute(String dir,String fileName) throws TaskExecuteException ;
}
