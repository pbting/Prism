package com.road.yishi.log.analysor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.road.yishi.log.Log;
import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.handler.Reducer;
import com.road.yishi.log.mgr.MessagePersiterMgr;
import com.road.yishi.log.mgr.TopicHandlerMappingMgr;
import com.road.yishi.log.servlet.service.ConsumerLogFactory;
import com.road.yishi.log.vo.LogKeyInfo;

public class AnalilizelogCatalinaReducer implements Reducer<LogMetaInfo, List<LogDetailInfo>> {

	@Override
	public void reduce(LogMetaInfo k, List<LogDetailInfo> v) {
		String topicName = TopicHandlerMappingMgr.getReducerTopicName(AnalilizelogCatalinaReducer.class.getName());
		try {
			MessagePersiterMgr.persister(topicName, k.getKey(), v,(short) 0);
			Map<LogKeyInfo,Integer> keyCountMap = ConsumerLogFactory.countMap.get(topicName);
			if(keyCountMap == null){
				keyCountMap = new HashMap<LogKeyInfo,Integer>();
				ConsumerLogFactory.countMap.put(topicName, keyCountMap);
			}
			Integer count = keyCountMap.get(new LogKeyInfo(k.getKey()));
			if(count == null){
				count = 0 ;
			}
			keyCountMap.put(new LogKeyInfo(v.get(v.size()-1).getOccurDate(), k.getKey(), count+v.size()), count+v.size());
		} catch (Exception e) {
			Log.error("", e);
		}
	}
}
