package com.road.yishi.log.handler;

import java.util.HashMap;
import java.util.Map;

import com.road.yishi.log.Log;
import com.road.yishi.log.cluster.ClusterMgr;
import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;
import com.road.yishi.log.util.HttpUtil;

/**
 * 
 * <pre>
 * 	just only slave to call
 * </pre>
 */
public class ForwardMessage extends Action {
	
	private StringBuilder messageBuilder ;
	private String topic ;
	private String filePath ;
	public ForwardMessage(String topic,String abFilePath,StringBuilder messageBuilder) {
		this.topic = topic ;
		this.messageBuilder = messageBuilder;
		this.filePath = abFilePath;
	}
	
	@Override
	public long execute() throws TaskExecuteException {
		long start = System.currentTimeMillis();
		if(messageBuilder!=null&&messageBuilder.length()>0){
			String master = ClusterMgr.getClusterContiner().getMasterUrl();
			if(!master.endsWith("/")){
				master += "/" ;
			}
			master += "/reciverMessage.do";
			String message = messageBuilder.toString();
			Map<String,String> param = new HashMap<String,String>();
			param.put("message", message);
			param.put("topic", topic);
			param.put("filePath", filePath);
			String response = HttpUtil.doPost(master, param,"utf-8");
			if(null!=response&&HttpUtil.OK == Integer.parseInt(response)){
				Log.info("["+ForwardMessage.class.getName()+"] 消息发送成功!");
			}else if(null!=null&&HttpUtil.PARAM_ERROR_NO_ANALYSESOR == Integer.parseInt(response)){
				Log.info("["+ForwardMessage.class.getName()+"] 发送参数错误。!"+filePath+" 没有响应的日志解析器。");
			}else{
				Log.info("["+ForwardMessage.class.getName()+"] 消息发送失败!");
			}
		}
		
		return System.currentTimeMillis()-start;
	}

}
