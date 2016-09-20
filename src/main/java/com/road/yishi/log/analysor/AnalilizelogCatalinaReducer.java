package com.road.yishi.log.analysor;

import java.util.List;
import com.road.yishi.log.cluster.ClusterMgr;
import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.handler.Reducer;
import com.road.yishi.log.mgr.TopicHandlerMappingMgr;
import com.road.yishi.log.mina.KryoMessage;
import com.road.yishi.log.mina.KryoUtil;
import com.road.yishi.log.mina.cmd.ServerProtocol;
import com.road.yishi.log.mina.cmd.message.TopicMessage;
import com.road.yishi.log.util.ZLibUtils;

public class AnalilizelogCatalinaReducer implements Reducer<LogMetaInfo, List<LogDetailInfo>> {

	@Override
	public void reduce(LogMetaInfo k, List<LogDetailInfo> v) {
		String topicName = TopicHandlerMappingMgr.getReducerTopicName(AnalilizelogCatalinaReducer.class.getName());
		TopicMessage<LogMetaInfo, List<LogDetailInfo>> topicMessage = new TopicMessage<LogMetaInfo, List<LogDetailInfo>>();
		topicMessage.setK(k);
		topicMessage.setV(v);
		topicMessage.setTopic(topicName);
		String msg = KryoUtil.serialization(topicMessage, TopicMessage.class);
		KryoMessage kryoMessage = new KryoMessage();
		kryoMessage.setProtocol(ServerProtocol.CATALINA_TOPIC_MSG);
		kryoMessage.setBody(ZLibUtils.compress(msg.getBytes()));
		ClusterMgr.getMasterIoSession().write(kryoMessage);
//		try {
//			MessagePersiterMgr.persister(topicName, k.getKey(), v,(short) 0);
//			Map<LogKeyInfo,Integer> keyCountMap = ConsumerLogFactory.countMap.get(topicName);
//			if(keyCountMap == null){
//				keyCountMap = new HashMap<LogKeyInfo,Integer>();
//				ConsumerLogFactory.countMap.put(topicName, keyCountMap);
//			}
//			Integer count = keyCountMap.get(new LogKeyInfo(k.getKey()));
//			if(count == null){
//				count = 0 ;
//			}
//			keyCountMap.put(new LogKeyInfo(v.get(v.size()-1).getOccurDate(), k.getKey(), count+v.size()), count+v.size());
//		} catch (Exception e) {
//			Log.error("", e);
//		}
	}
}
