package com.road.yishi.log.handler.servlet.slave;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.road.yishi.log.handler.servlet.AbstractServlet;
import com.road.yishi.log.handler.servlet.EventNameId;
import com.road.yishi.log.util.FileUtil;
import com.road.yishi.log.util.StringUtil;

/**
 * master 接收slave 发送过来的事件id.根据具体的事件id做不同的操作
 */
@WebServlet(name = "eventHandler", asyncSupported = true, urlPatterns = { "/slaveEventHandler.do" })
public class SlaveReciveEventHandler extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SlaveReciveEventHandler() {
		super();
	}

	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		String eventId = request.getParameter("eventId");
		if (StringUtil.isEmpty(eventId) || !StringUtil.isNumeric(eventId)) {
			return;
		}
		int enentIdInt = Integer.parseInt(eventId);
		switch (enentIdInt) {
			case EventNameId.DOWNLOAD_DATA: 
			{
				// 1、会将去哪里下载数据的目录发送到master
				String url = request.getParameter("download.url");
				if (StringUtil.isEmpty(url)) {
					return;
				}
				String realPath = request.getServletContext().getRealPath("");
				FileUtil.downLoad(url, realPath, "fileName");
			}
			break;
			case EventNameId.PRE_DOWNLOAD_DATA:
			{
				String topic = request.getParameter("topic");
				String fileName = request.getParameter("fileName");
			}
			break;
		}
	}

	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}
}
