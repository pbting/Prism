package com.road.yishi.log.handler.servlet.master;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.road.yishi.log.Log;
import com.road.yishi.log.core.ConsumerExecutor;
import com.road.yishi.log.handler.AnalylizeLogInter;
import com.road.yishi.log.handler.servlet.AbstractServlet;
import com.road.yishi.log.mgr.TopicHandlerMappingMgr;
import com.road.yishi.log.util.HttpUtil;
/**
 * 
 * <pre>
 * 	master 接收 slave 通过http 请求发送过来的日志数据
 * </pre>
 */
@WebServlet(name="reciverMessage",urlPatterns={"/reciverMessage.do"},asyncSupported=true,description="",loadOnStartup=1)
public class ReciverTopicMessageHandler extends AbstractServlet {
	private static final long serialVersionUID = 1L;
    private static final String className = ReciverTopicMessageHandler.class.getName();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReciverTopicMessageHandler() {
        super();
    }

	@Override
	protected boolean checkIp(String ip, HttpServletResponse response) {
		return true;
	}

	@Override
	protected void executor(HttpServletRequest request, HttpServletResponse response) {
		String topic = request.getParameter("topic");
		String message = request.getParameter("message");
		String filePath = request.getParameter("filePath");
		AnalylizeLogInter<?, ?> handler = TopicHandlerMappingMgr.getTopicHandler(filePath);
		if(handler!=null){
			ConsumerExecutor.consumer(topic,handler.analylize(message));//处理发送过来的日志数据
			super.sendMessage(response, "200");
		}else{
			Log.error(className+"["+filePath+"] 没有相关的日志解析器！");
			super.sendMessage(response,String.valueOf(HttpUtil.PARAM_ERROR_NO_ANALYSESOR));
		}
	}
}
