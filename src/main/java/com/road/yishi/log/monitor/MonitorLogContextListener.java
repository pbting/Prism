package com.road.yishi.log.monitor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

import com.road.yishi.log.bank.ShutDownHook;
import com.road.yishi.log.cluster.ClusterMgr;
import com.road.yishi.log.mgr.ConfigMgr;
import com.road.yishi.log.mgr.TopicHandlerMappingMgr;
import com.road.yishi.log.mgr.TopicListenerMgr;
import com.road.yishi.log.util.StringUtil;

public class MonitorLogContextListener implements ServletContextListener {
	
	private static final String logAnalyseConfig = "logAnalyseConfig";
	
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent context) {
		String dataPath = context.getServletContext().getRealPath("/");
		//1、
		ConfigMgr.init(context.getServletContext().getInitParameter(logAnalyseConfig),dataPath);
		
		//2、
		if(StringUtil.isEmpty(ConfigMgr.getMonitorDir())){
			throw new IllegalArgumentException("监听目录不能为空");
		}
		
		TopicListenerMgr.init();
		
		//4、
		PropertyConfigurator.configure(ConfigMgr.getlog4jPath());
		//5、集群管理
		ClusterMgr.init();
		//6、
		TopicHandlerMappingMgr.init(ConfigMgr.getTopicHandler());
		
		//7、
		Runtime.getRuntime().addShutdownHook(new ShutDownHook());
		
	}
}
