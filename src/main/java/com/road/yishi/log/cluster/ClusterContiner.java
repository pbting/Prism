package com.road.yishi.log.cluster;

import java.util.Set;

/**
 * 
 * <pre>
 * 	集群信息的封装器
 * </pre>
 */
public class ClusterContiner {

	private String role ;
	
	private String masterUrl;
	
	private Set<String> salvesUrl ;
	
	public ClusterContiner() {
	}
	
	public ClusterContiner(String masterUrl, Set<String> salvesUrl,String role) {
		super();
		this.masterUrl = masterUrl;
		this.salvesUrl = salvesUrl;
		this.role = role ;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMasterUrl() {
		return masterUrl;
	}

	public void setMasterUrl(String masterUrl) {
		this.masterUrl = masterUrl;
	}

	public Set<String> getSalvesUrl() {
		return salvesUrl;
	}

	public void setSalvesUrl(Set<String> salvesUrl) {
		this.salvesUrl = salvesUrl;
	}
}
