package com.road.yishi.log.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.road.yishi.log.Log;
import com.road.yishi.log.handler.servlet.AbstractServlet;
import com.road.yishi.log.mgr.ConfigMgr;
import com.road.yishi.log.util.FileUtil;

@WebServlet(name="testServlet",asyncSupported=true,urlPatterns={"/testServlet.do"})
public class TestServlet extends AbstractServlet{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		String topic = request.getParameter("topic");
		String message = request.getParameter("message");
		String data = ConfigMgr.getDataPath();
		String dataDirs = data + File.separator + topic;
		String fileName = "/user/local/error.log";
		String fName = fileName.substring(fileName.lastIndexOf("/")+1);
		File file = new File(dataDirs, fName);
		try {
			if(topic!=null&&message!=null){
				FileUtil.checkFile(file);
				FileUtil.writeWithTransferTo(file,message.getBytes(Charset.forName("utf-8")));
				sendMessage(response, "200");
			}
		} catch (IOException e) {
			Log.error("["+TestServlet.class.getName()+"] 日志数据写 写出现异常", e);
			sendMessage(response, "500");
		}
	}

	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}
}
