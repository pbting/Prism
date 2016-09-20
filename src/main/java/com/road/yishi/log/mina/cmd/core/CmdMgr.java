package com.road.yishi.log.mina.cmd.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.road.yishi.log.Log;
import com.road.yishi.log.mina.cmd.client.SlaveRecivePackage;
import com.road.yishi.log.mina.cmd.server.MasterRecivePackage;
import com.road.yishi.log.util.ClasspathPackageScanner;

public final class CmdMgr {

	private CmdMgr() {
	}

	private static final String SERVER_PACKAGE = "MasterRecivePackage" ;
	
	private static final String CLIENT_PACKAGE = "SlaveRecivePackage" ;
	
	public static final ConcurrentHashMap<Integer, Command> COMMAND_MAP = new ConcurrentHashMap<Integer, Command>();
	
	
	public static void init(Class clazz){
		if(clazz == null){
			return ;
		}
		
		String classSimpleName = clazz.getSimpleName();
		if(SERVER_PACKAGE.equals(classSimpleName)){
			MasterRecivePackage target = new MasterRecivePackage();
			Field[] fileds = MasterRecivePackage.class.getDeclaredFields();
			String[] packages = new String[fileds.length];
			try {
				for(int i=0;i<fileds.length;i++){
					packages[i] = fileds[i].get(target).toString();
					System.out.println("[MasterRecivePackage] package name:"+packages[i]);
				}
			} catch (IllegalArgumentException e) {
				Log.error("", e);
			} catch (IllegalAccessException e) {
				Log.error("", e);
			}
			scan(packages);
		}else if(CLIENT_PACKAGE.equals(classSimpleName)){
			SlaveRecivePackage target = new SlaveRecivePackage();
			Field[] fileds = SlaveRecivePackage.class.getDeclaredFields();
			String[] packages = new String[fileds.length];
			try {
				for(int i=0;i<fileds.length;i++){
					packages[i] = fileds[i].get(target).toString();
					System.out.println("[SlaveRecivePackage] package name:"+packages[i]);
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
		init(MasterRecivePackage.class);
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
