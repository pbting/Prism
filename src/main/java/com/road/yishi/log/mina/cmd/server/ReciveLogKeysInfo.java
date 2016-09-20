package com.road.yishi.log.mina.cmd.server;

import java.util.List;

import org.apache.mina.core.session.IoSession;

import com.road.yishi.log.mina.KryoMessage;
import com.road.yishi.log.mina.KryoUtil;
import com.road.yishi.log.mina.cmd.ServerProtocol;
import com.road.yishi.log.mina.cmd.core.Cmd;
import com.road.yishi.log.mina.cmd.core.Command;
import com.road.yishi.log.util.ZLibUtils;
import com.road.yishi.log.vo.LogKeyInfo;

@Cmd(code=ServerProtocol.SEND_LOGKEYS,desc="接收客户端发送过来的日志消息")
public class ReciveLogKeysInfo implements Command{

	@Override
	public void execute(IoSession session, KryoMessage packet) throws Exception {
		if(packet.getBody()==null||packet.getBody().length<=0){
			return ;
		}
		List<LogKeyInfo> logkeys = KryoUtil.deserializationList(new String(ZLibUtils.decompress(packet.getBody())),LogKeyInfo.class);
		for(LogKeyInfo log:logkeys){
			System.out.println("key:"+log.getKey()+",print date:"+log.getPrintDate());
		}
		System.out.println("recive time is:"+System.currentTimeMillis());
	}
}
