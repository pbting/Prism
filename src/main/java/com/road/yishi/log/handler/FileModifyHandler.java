package com.road.yishi.log.handler;

import com.road.yishi.log.Log;
import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.handler.topic.ErrorTopicModifyHandler;
import com.road.yishi.log.handler.topic.TopicHandlerExecutor;
import com.road.yishi.log.mgr.TopicHandlerMappingMgr;

public class FileModifyHandler extends FileHandler{
	public FileModifyHandler(String dir,String filePath){
		super(dir,filePath);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void execute(String dir, String fileName) throws TaskExecuteException {
		if(!dir.endsWith("/")){
			dir += "/";
		}
		String absoluPath = dir+fileName;
		@SuppressWarnings("rawtypes")
		AnalylizeLogInter handler = TopicHandlerMappingMgr.getTopicHandler(absoluPath);
		if(handler != null){
			String topic = TopicHandlerMappingMgr.getTopicName(absoluPath);
			TopicHandlerExecutor.getExecutor().enDefaultQueue(new ErrorTopicModifyHandler(handler,topic,absoluPath,false));
		}else{
			Log.error("[ "+absoluPath+" ] 没有相应的分析器.");
		}
	}
}
