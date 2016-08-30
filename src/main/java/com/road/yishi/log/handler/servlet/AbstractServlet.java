package com.road.yishi.log.handler.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public abstract class AbstractServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected static final String IP_LIMIT = "来源IP不合法";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long t1 = System.currentTimeMillis();
		executor(request, response);
		long t2 = System.currentTimeMillis();
		String str = request.getRequestURI();

		System.out.println(str+";http request time :" + (t2 - t1));
	}

	protected abstract void executor(HttpServletRequest request, HttpServletResponse response) ;
	
	protected abstract boolean checkIp(String ip, HttpServletResponse response);
	
	public void sendMessage(HttpServletResponse response, String msg) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(msg);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageByJson(HttpServletResponse response, Message msg) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write( new Gson().toJson(msg));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 返回JSON数据
	 */
	public void sendMessage(HttpServletResponse response, Gson json) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(HttpServletResponse response, byte[] msg) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getOutputStream().write(msg);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
