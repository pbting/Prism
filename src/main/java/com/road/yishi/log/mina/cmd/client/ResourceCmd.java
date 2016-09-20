package com.road.yishi.log.mina.cmd.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.session.IoSession;

import com.road.yishi.log.mina.KryoMessage;
import com.road.yishi.log.mina.KryoUtil;
import com.road.yishi.log.mina.cmd.ClientProtocol;
import com.road.yishi.log.mina.cmd.ServerProtocol;
import com.road.yishi.log.mina.cmd.core.Cmd;
import com.road.yishi.log.mina.cmd.core.Command;
import com.road.yishi.log.mina.cmd.message.Resource;
import com.road.yishi.log.util.ZLibUtils;
import com.road.yishi.log.vo.LogKeyInfo;

@Cmd(code=ClientProtocol.RESOURCE_PRO,desc="接收服务端发送过来的资源消息")
public class ResourceCmd implements Command{

	@Override
	public void execute(IoSession session, KryoMessage packet) throws Exception {
		Resource resource = KryoUtil.deserialization(new String(ZLibUtils.decompress(packet.getBody())), Resource.class);
		System.out.println(resource.toString());
		//向服务端发送log key
		List<LogKeyInfo> logKeyInfos = new ArrayList<LogKeyInfo>();
		for(int i=0;i<1000;i++){
			logKeyInfos.add(new LogKeyInfo("接收服务端发送过来的资源消息 log key_"+i,new Date().toString()));
		}
		
		String infos = KryoUtil.serializationList(logKeyInfos, LogKeyInfo.class);
		KryoMessage kryoMessage = new KryoMessage();
		kryoMessage.setProtocol(ServerProtocol.SEND_LOGKEYS);
		kryoMessage.setBody(ZLibUtils.compress(infos.getBytes()));
		System.out.println("send time is:"+System.currentTimeMillis());
		session.write(kryoMessage);
	}
}
