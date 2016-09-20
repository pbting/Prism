package com.road.yishi.log.cluster;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.apache.mina.core.session.IoSession;

import com.road.yishi.log.core.Paramter;
import com.road.yishi.log.mgr.ConfigMgr;
import com.road.yishi.log.mina.MinaMasterServer;
import com.road.yishi.log.mina.MinaSlaveServer;

public class ClusterMgr {

	private ClusterMgr() {
	}

	private static ClusterContiner CLUSTER_CONTINER = null;

	public static void init() {
		CLUSTER_CONTINER = new ClusterContiner();
		String master = ConfigMgr.getMaster();
		CLUSTER_CONTINER.setMasterUrl(master);
		String[] slaves = ConfigMgr.getSlaves();
		Set<String> slavesSet = new TreeSet<String>();
		slavesSet.addAll(Arrays.asList(slaves));
		CLUSTER_CONTINER.setSalvesUrl(slavesSet);
		CLUSTER_CONTINER.setRole(ConfigMgr.getRole());
		initMinaServer();
	}

	private static void initMinaServer(){
		String role = CLUSTER_CONTINER.getRole();
		if(Paramter.ROLE_MASTER.equals(role)){
			MinaMasterServer.init();
		}else if(Paramter.ROLE_SLAVE.equals(role)){
			MinaSlaveServer.init();
		}
	}
	
	public static IoSession getMasterIoSession(){
		
		return MinaSlaveServer.getMasterIoSession();
	}
	
	public static ClusterContiner getClusterContiner() {
		return CLUSTER_CONTINER;
	}
}
