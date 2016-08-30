package com.road.yishi.log.servlet;

import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.road.yishi.log.handler.servlet.AbstractServlet;
import com.road.yishi.log.mgr.MessagePersiterMgr;

/**
 * Servlet implementation class ShowTopics
 */

@WebServlet(name="showTopics",asyncSupported=true,urlPatterns={"/showTopics"})
public class ShowTopicsServlet extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}
	
	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		Set<String> topics = MessagePersiterMgr.getAllTopics();
		sendMessage(response, new Gson().toJson(topics));
	}
}
