
package com.road.yishi.log.analysor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaFactory;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.handler.TopicMapper;

public class AnalilizelogCatalinaMapper extends TopicMapper<LogMetaInfo,LogDetailInfo> {

	private final static List<String> keyList = Arrays.asList("WARN","INFO","SEVERE");
	private StringBuffer logContent = new StringBuffer();
	private String errorLine ="";
	private String secondLine = "";
	private String tmpString = "";
	static int lineNum = 0 ;
	@Override
	public void map(String line, Map<LogMetaInfo, List<LogDetailInfo>> logContext, boolean hasNextToken) {
		if(line!=null&&line.trim().length()>0){
			if(line.startsWith("ERROR")){
				handler(logContext, errorLine, secondLine, logContent);
				logContent.setLength(0);
				secondLine="";
				tmpString="";
				errorLine = line;
				return ;
			}
			
			if(secondLine.length()==0&&errorLine!=null&&errorLine.length()>0&&line.startsWith("com")){
				secondLine = line;
				return ;
			}
			//检测当前是否包含以下信息
			for(String str:keyList){
				if(line.startsWith(str)){
					tmpString = line;
					return ;
				}
			}
			
			if(tmpString!=null&&tmpString.length()>0){
				return ;
			}
			
			if(errorLine!=null&&errorLine.length()>0){
				logContent.append(line.trim()+"</ br>");
			}
		}
	}
	/**
	 * 
	 * <pre>
	 * 	自己处理combiner 的过程
	 * </pre>
	 *
	 * @param logContext
	 * @param errorLine
	 * @param secondLine
	 * @param logContent
	 */
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
