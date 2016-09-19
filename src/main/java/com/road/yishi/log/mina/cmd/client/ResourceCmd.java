package com.road.yishi.log.mina.cmd.client;

import org.apache.mina.core.session.IoSession;

import com.road.yishi.log.mina.KryoMessage;
import com.road.yishi.log.mina.KryoUtil;
import com.road.yishi.log.mina.cmd.ServerProtocol;
import com.road.yishi.log.mina.cmd.core.Cmd;
import com.road.yishi.log.mina.cmd.core.Command;
import com.road.yishi.log.mina.cmd.message.Resource;

@Cmd(code=ServerProtocol.RESOURCE_PRO,desc="接收服务端发送过来的资源消息")
public class ResourceCmd implements Command{

	@Override
	public void execute(IoSession session, KryoMessage packet) throws Exception {
		Resource resource = KryoUtil.deserialization(new String(packet.getBody()), Resource.class);
		System.out.println(resource.toString());
	}
}
