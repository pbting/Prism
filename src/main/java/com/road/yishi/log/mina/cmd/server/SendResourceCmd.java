package com.road.yishi.log.mina.cmd.server;

import org.apache.mina.core.session.IoSession;
import com.road.yishi.log.mina.KryoMessage;
import com.road.yishi.log.mina.KryoUtil;
import com.road.yishi.log.mina.cmd.ClientProtocol;
import com.road.yishi.log.mina.cmd.ServerProtocol;
import com.road.yishi.log.mina.cmd.core.Cmd;
import com.road.yishi.log.mina.cmd.core.Command;
import com.road.yishi.log.mina.cmd.message.Resource;

@Cmd(code=ClientProtocol.REQUEST_RESOURCE,desc="客户端请求服务端本地资源情况")
public class SendResourceCmd implements Command{

	@Override
	public void execute(IoSession session, KryoMessage packet) throws Exception {
		String message = KryoUtil.deserialization(new String(packet.getBody()), String.class);
		if("get resource".equals(message)){
			int cpuCount = Runtime.getRuntime().availableProcessors();
			String platform = System.getProperty("os.name");
			Resource resource = new Resource();
			resource.setCpuCount(cpuCount);
			resource.setPlatform(platform);
			KryoMessage kryoMessage = KryoMessage.buildKryoMessage(ServerProtocol.RESOURCE_PRO,resource,Resource.class);
			session.write(kryoMessage);
		}
	}
}
