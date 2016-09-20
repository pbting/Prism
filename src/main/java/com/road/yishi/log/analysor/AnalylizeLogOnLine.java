package com.road.yishi.log.analysor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.analyze.LogMetaInfoOnLine;
import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaFactory;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.handler.TopicMapper;

/**
 * 
 * <pre>
 * 	分析线上的日志
 * </pre>
 */
//@TopicHandler(fileName="dir",topic="")
public class AnalylizeLogOnLine extends TopicMapper<LogMetaInfo,LogDetailInfo>{

	/**
	 * 当前文件映射到内存中已经解析出一行一行的信息，解析完之后，需要放入日志上下文处理
	 * @param line
	 * @param logContext
	 * @see com.road.yishi.log.analysor.TopicMapper#map(java.lang.Object, java.util.Map)
	 */
	private String dateLine = "";
	private String detailLine = "";
	private int flagIndex = 0 ;
	private StringBuffer sbContent = new StringBuffer();
	@Override
	public void map(String line, Map<LogMetaInfo, List<LogDetailInfo>> logContext,boolean hasNextToken) {
		if(line.indexOf("ERROR") >= 0){
			if(flagIndex >= 2){//表示正常出现日志的地方
				String dateStr = getDate(dateLine);
				String className =getClassName(detailLine);
				String callMethodName = detailLine.substring(0, detailLine.indexOf("("));
				//1、
				LogMetaInfo logMetaInfoKey = new LogMetaInfoOnLine();
				logMetaInfoKey.setCallClassName(className);
				logMetaInfoKey.setCallMethodName(callMethodName);
				logMetaInfoKey.setPrintDate(dateStr);
				//2、
				List<LogDetailInfo> logDetailInfos = logContext.get(logMetaInfoKey); 
				if(logDetailInfos== null || logDetailInfos.isEmpty()){
					logDetailInfos = new ArrayList<LogDetailInfo>(); 
				}
				String tmpLog = sbContent.toString();
				//去重
				boolean flag = false ;
				if(true){//如果不需要重复出现的日志
					for (LogDetailInfo logDetail : logDetailInfos) {
						if(logDetail.getLogInfo().equalsIgnoreCase(tmpLog)){
							flag = true ;
							break ;
						}
					}
				}
				
				if(!flag){
					//3、
					LogDetailInfo detailValue = new LogDetailInfo(dateStr, sbContent.toString());
					logDetailInfos.add(detailValue);
					//4、
					logContext.put(logMetaInfoKey, logDetailInfos);
				}
			}
			init();
			dateLine = line;
			flagIndex++;
		}else if(line.indexOf("com") >= 0&& flagIndex== 1){//紧接着在ERROR 之后出现
			detailLine = line;
			flagIndex++;
		}else{
			sbContent.append(line+"<br />");
		}
	}
	
	private void init(){
		flagIndex = 0 ;
		dateLine = "";
		detailLine = "";
		sbContent.setLength(0);
	}
	
	private String getDate(String logInfos){
		
		return LogMetaFactory.getDate(logInfos, "ERROR:",",");
	}
	
	private String getClassName(String logInfos){
		
		return LogMetaFactory.getDate(logInfos, "(", ")");
	}
	public static void main(String[] args) {
		String detailLine = "com.road.yishi.util.qq.QQOpenApi.buyGoods(QQOpenApi.java:411)";
		
		System.out.println(detailLine.substring(0, detailLine.indexOf("(")));

		System.out.println(LogMetaFactory.getDate(detailLine, "(", ")"));
	}
}
