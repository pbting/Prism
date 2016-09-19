package com.road.yishi.log.mina.cmd.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.road.yishi.log.Log;
import com.road.yishi.log.mina.cmd.client.ClientRecivePackage;
import com.road.yishi.log.mina.cmd.server.ServerRecivePackage;
import com.road.yishi.log.util.ClasspathPackageScanner;

public final class CmdMgr {

	private CmdMgr() {
	}

	private static final String SERVER_PACKAGE = "ServerRecivePackage" ;
	
	private static final String CLIENT_PACKAGE = "ClientRecivePackage" ;
	
	public static final ConcurrentHashMap<Integer, Command> COMMAND_MAP = new ConcurrentHashMap<Integer, Command>();
	
	
	public static void init(Class clazz){
		if(clazz == null){
			return ;
		}
		
		String classSimpleName = clazz.getSimpleName();
		if(SERVER_PACKAGE.equals(classSimpleName)){
			ServerRecivePackage target = new ServerRecivePackage();
			Field[] fileds = ServerRecivePackage.class.getDeclaredFields();
			String[] packages = new String[fileds.length];
			try {
				for(int i=0;i<fileds.length;i++){
					packages[i] = fileds[i].get(target).toString();
					System.out.println("[ServerRecivePackage] package name:"+packages[i]);
				}
			} catch (IllegalArgumentException e) {
				Log.error("", e);
			} catch (IllegalAccessException e) {
				Log.error("", e);
			}
			scan(packages);
		}else if(CLIENT_PACKAGE.equals(classSimpleName)){
			ClientRecivePackage target = new ClientRecivePackage();
			Field[] fileds = ClientRecivePackage.class.getDeclaredFields();
			String[] packages = new String[fileds.length];
			try {
				for(int i=0;i<fileds.length;i++){
					packages[i] = fileds[i].get(target).toString();
					System.out.println("[ServerRecivePackage] package name:"+packages[i]);
				}
				scan(packages);
			} catch (IllegalArgumentException e) {
				Log.error("", e);
			} catch (IllegalAccessException e) {
				Log.error("", e);
			}
		}
	}
	public static void main(String[] args) {
		init(ServerRecivePackage.class);
	}
	/**
	 * 
	 */

	public static boolean scan(String... packages) {
		if (packages == null || packages.length <= 0) {
			return true;
		}

		LinkedList<String> classNames = new LinkedList<String>();

		try {
			for (String pack : packages) {
				List<String> cns = new ClasspathPackageScanner(pack).getClassNameList(); 
				classNames.addAll(cns);
				Log.debug("package:"+pack+" has command size:"+cns.size());
			}
		} catch (IOException e) {
			Log.error("", e);
		}

		return initCommand(classNames);
	}
	
	private static boolean initCommand(LinkedList<String> classNames){
		boolean flag = true ;
		try {
			loop:for(String cn:classNames){
				Class clazz = Class.forName(cn);
				Cmd cmd = (Cmd) clazz.getAnnotation(Cmd.class);
				if(cmd == null){
					continue;
				}
				Command command = (Command) clazz.newInstance();
				if(null!=COMMAND_MAP.putIfAbsent(cmd.code(), command)){
					System.err.println("code is repeat:"+cmd.code());
					flag = false;
					break loop;
				}
			}
		} catch (InstantiationException e) {
			Log.error("initCommand:", e);
			flag = false;
		} catch (IllegalAccessException e) {
			Log.error("initCommand:", e);
			flag = false;
		} catch (ClassNotFoundException e) {
			Log.error("initCommand:", e);
			flag = false;
		}
		return flag;
	}
}
