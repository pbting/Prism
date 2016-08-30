package com.road.yishi.log.servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.handler.servlet.AbstractServlet;
import com.road.yishi.log.mgr.MessagePersiterMgr;
import com.road.yishi.log.servlet.service.ConsumerLogFactory;
import com.road.yishi.log.vo.LogKeyInfo;

@WebServlet(name="ShowLogInfoServlet",asyncSupported=true,urlPatterns={"/showLogInfoServlet"})
public class ShowLogInfoServlet extends AbstractServlet{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		String qKey = request.getParameter("key");
		String topic = request.getParameter("topic");
		if(qKey !=null && qKey.trim().length()>0){
			if(ConsumerLogFactory.countMap.get(topic)== null)
				return ;
			ConsumerLogFactory.countMap.get(topic).remove(new LogKeyInfo(new SimpleDateFormat("yyyy-MM-dd HH:dd:ss").format(new Date()),qKey,0));
			
			List<List<LogDetailInfo>> logDetailInfos = (List<List<LogDetailInfo>>) MessagePersiterMgr.retrive(topic, qKey,(short) 0, new TypeToken<List<LogDetailInfo>>(){});
			if(logDetailInfos!=null){
				ArrayList<LogDetailInfo> result = new ArrayList<LogDetailInfo>();
				for(List<LogDetailInfo> list:logDetailInfos){
					result.addAll(list);
				}
	
				request.setAttribute("logList", result);
				request.setAttribute("key", qKey);
				try {
					request.getRequestDispatcher("/showLogList.jsp").forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}
}
