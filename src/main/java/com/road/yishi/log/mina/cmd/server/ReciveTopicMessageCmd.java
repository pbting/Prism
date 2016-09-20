package com.road.yishi.log.mina.cmd.server;

import java.util.List;
import org.apache.mina.core.session.IoSession;
import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.mgr.MessagePersiterMgr;
import com.road.yishi.log.mina.KryoMessage;
import com.road.yishi.log.mina.KryoUtil;
import com.road.yishi.log.mina.cmd.ServerProtocol;
import com.road.yishi.log.mina.cmd.core.Cmd;
import com.road.yishi.log.mina.cmd.core.Command;
import com.road.yishi.log.mina.cmd.message.TopicMessage;
import com.road.yishi.log.util.ZLibUtils;

@Cmd(code=ServerProtocol.CATALINA_TOPIC_MSG,desc="接收slave 发送过来的消息集")
public class ReciveTopicMessageCmd implements Command{

	@Override
	public void execute(IoSession session, KryoMessage packet) throws Exception {
		if(packet.getBody()==null ||packet.getBody().length <=0){
			return ;
		}
		
		@SuppressWarnings("unchecked")
		TopicMessage<LogMetaInfo,List<LogDetailInfo>> topicMessage = 
				KryoUtil.deserialization(new String(ZLibUtils.decompress(packet.getBody())), TopicMessage.class);
		
		MessagePersiterMgr.persister(topicMessage.getTopic(), topicMessage.getK(), topicMessage.getV(), (short)0);
	}
}
