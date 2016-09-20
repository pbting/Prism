package com.road.yishi.log.analysor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaFactory;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.handler.TopicMapper;

public class AnalylizLogLocalMapper extends TopicMapper<LogMetaInfo,LogDetailInfo> {

	private StringBuffer logContent = new StringBuffer();
	private LogMetaInfo logMetaInfo = null ;
	@Override
	public void map(String line, Map<LogMetaInfo, List<LogDetailInfo>> logContext, boolean hasNextToken) {
		if(line!=null&&line.trim().length()>0){
			if(line.indexOf("[ERROR]") >=0){//表示出现 logMetaInfo 的地方
				if(logContent.length()>0){
					//处理之前的日志信息
					handler(logContext);
				}
				//1、解析出方法名.[映射错位是去上一次的数据]
				String callMethod = LogMetaFactory.getString(line,true,"-","(").trim();
				callMethod = (callMethod.length()>0?callMethod:logMetaInfo.getCallMethodName());
				
				String callClass = LogMetaFactory.getString(line,true, "(", ")").trim();
				callClass = (callClass.length()>0?callClass:logMetaInfo.getCallClassName());
				
				String printDate = LogMetaFactory.getString(line.substring(0,line.lastIndexOf("[ERROR]")), true, "[TKC]", ":").trim();
				printDate = (printDate.length()>0?printDate:logMetaInfo.getPrintDate());
				
				logMetaInfo = new LogMetaInfo(line,printDate,callClass,callMethod);
				logContent.setLength(0);
			}else{
				logContent.append(line.trim()+"<br />");
			}
			
			if(!hasNextToken){//如果没有了下一行，就说明已经解析的当前文件尾
				handler(logContext);
			}
		}else{
			if(!hasNextToken){//如果没有了下一行，就说明已经解析的当前文件尾
				handler(logContext);
			}
		}
	}
	private void handler(Map<LogMetaInfo, List<LogDetailInfo>> logContext) {
		if(logMetaInfo ==null || logContext==null||logContent.length() <=0)
			return ;
		
		LogDetailInfo logDetailInfo = new LogDetailInfo(logMetaInfo.getPrintDate(), logContent.toString());
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
