package com.road.yishi.log.analysor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaFactory;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.handler.TopicMapper;

//@TopicHandler(fileName="",topic="")
public class AnalylizeLogTest extends TopicMapper<LogMetaInfo,LogDetailInfo>{

	private StringBuffer content = new StringBuffer();
	private LogMetaInfo LogMetaInfo = null ;
	@Override
	public void map(String line, Map<LogMetaInfo, List<LogDetailInfo>> logContext,boolean hasNextToken) {
		if(line.indexOf("[ERROR]")>=0){
			if(content.length()>0){
				LogDetailInfo logDetailInfo = new LogDetailInfo(LogMetaInfo.getPrintDate(), content.toString());
				List<LogDetailInfo> logList = logContext.get(LogMetaInfo);
				if(logList == null){
					logList = new ArrayList<LogDetailInfo>();
					logContext.put(LogMetaInfo, logList);
				}
				logList.add(logDetailInfo);
				content.setLength(0);
			}
			
			LogMetaInfo = new LogMetaInfo(line,LogMetaFactory.getString(line, true,"[ERROR]", "$"),LogMetaFactory.getString(line,true, "(", ")"),UUID.randomUUID().toString());
		}else{
			content.append(line+"<br />");
		}
	}
}