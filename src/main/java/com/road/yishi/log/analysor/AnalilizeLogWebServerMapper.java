package com.road.yishi.log.analysor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaFactory;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.handler.TopicMap;

public class AnalilizeLogWebServerMapper extends TopicMap<LogMetaInfo, LogDetailInfo> {

	private StringBuffer logContent = new StringBuffer();
	private String errorLine ="";
	private String secondLine = "";
	@Override
	public void map(String line, Map<LogMetaInfo, List<LogDetailInfo>> logContext, boolean hasNextToken) {
		if(line!=null&&line.trim().length()>0){
			line = line.trim();
			if(line.indexOf("ERROR") >=0){//表示出现 logMetaInfo 的地方
				if(logContent.length()>0){
					//处理之前的日志信息
					handler(logContext,errorLine,secondLine,logContent);
					logContent.setLength(0);
				}
				errorLine = line;
			}else if(errorLine.length()>0&&line.startsWith("com")){
				secondLine = line;
			}else{
				logContent.append(line.trim()+"<br />");
			}
			
			if(!hasNextToken){//如果没有了下一行，就说明已经解析的当前文件尾
				handler(logContext,errorLine,secondLine,logContent);
			}
		}else{
			if(!hasNextToken){//如果没有了下一行，就说明已经解析的当前文件尾
				handler(logContext,errorLine,secondLine,logContent);
			}
		}
	}
	private void handler(Map<LogMetaInfo, List<LogDetailInfo>> logContext,String errorLine,String secondLine,StringBuffer logContent) {
		if(errorLine.length()<=0 || secondLine.length()<=0||logContent.length() <=0)
			return ;
		String printDate = LogMetaFactory.getString(errorLine, false, ":", ",");
		String callMethod = LogMetaFactory.getString(secondLine, "", "(");
		String callClasss = LogMetaFactory.getString(secondLine, "(",")");
		LogMetaInfo logMetaInfo = new LogMetaInfo(printDate, callMethod, callClasss);
		LogDetailInfo logDetailInfo = new LogDetailInfo(printDate, logContent.toString());
		List<LogDetailInfo> logs = logContext.get(logMetaInfo);
		
		if(logs == null){
			logs = new ArrayList<LogDetailInfo>();
			logContext.put(logMetaInfo, logs);
		}
		
		//去重
		boolean isRepeat = false ;
		for(LogDetailInfo ld:logs){
			if(ld.getOccurDate().equalsIgnoreCase(logDetailInfo.getOccurDate())&&ld.getLogInfo().equalsIgnoreCase(logDetailInfo.getLogInfo())){
				isRepeat = true ;
				break;
			}
		}
		
		if(!isRepeat){
			logContext.get(logMetaInfo).add(logDetailInfo);
		}
	}
}
