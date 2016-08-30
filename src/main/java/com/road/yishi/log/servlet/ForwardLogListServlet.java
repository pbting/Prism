package com.road.yishi.log.servlet;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.road.yishi.log.Log;
import com.road.yishi.log.handler.servlet.AbstractServlet;

/**
 * 
 * <pre>
 *  2分钟刷新一次日志
 * </pre>
 */
@WebServlet(name="forwardLogList",asyncSupported=true,urlPatterns={"/forwardLogList"})
public class ForwardLogListServlet extends AbstractServlet{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}
	
	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		
		String topic =request.getParameter("topic");
		request.getSession().setAttribute("topic", topic);
		try {
			response.sendRedirect(request.getContextPath()+"/logMonitor.jsp");
		} catch (IOException e) {
			Log.error("", e);
		}
	}
}
