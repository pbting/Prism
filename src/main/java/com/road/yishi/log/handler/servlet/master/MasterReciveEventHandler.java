package com.road.yishi.log.handler.servlet.master;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.handler.servlet.AbstractServlet;
import com.road.yishi.log.handler.servlet.EventNameId;
import com.road.yishi.log.handler.servlet.RequestMessage;
import com.road.yishi.log.util.StringUtil;

@WebServlet(name = "masterEventHandler", asyncSupported = true, urlPatterns = { "/masterEventHandler.do" })
public class MasterReciveEventHandler extends AbstractServlet {

	/**
	 * <pre>
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		String param = request.getParameter("param");
		if (!StringUtil.isEmpty(param)) {
			TypeToken<RequestMessage> typeToken = new TypeToken<RequestMessage>(){};
			RequestMessage requestMessage = new Gson().fromJson(param,typeToken.getType());
			if (checkParam(requestMessage.getEventId(), requestMessage.getTopic(), requestMessage.getFileName())) {
				int eventIdInt = Integer.parseInt(requestMessage.getEventId());
				switch (eventIdInt) {
				case EventNameId.FILE_MODIFY:// master 接收到slave 发送过来的日志发生更改的消息
				{

				}
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}

	private boolean checkParam(String eventId, String topic, String fileName) {
		boolean flag = true;
		flag |= StringUtil.isEmpty(eventId);
		flag |= StringUtil.isEmpty(topic);
		flag |= StringUtil.isEmpty(fileName);

		return flag;
	}
}
