package com.road.yishi.log.mgr;

import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.road.yishi.log.Log;
import com.road.yishi.log.analysor.anotation.TopicHandler;
import com.road.yishi.log.analysor.anotation.TopicReduce;
import com.road.yishi.log.handler.AnalylizeLogInter;
import com.road.yishi.log.handler.Reducer;
import com.road.yishi.log.util.ClasspathPackageScanner;
import com.road.yishi.log.util.StringUtil;

import audaque.com.pbting.cache.house.MD5Security;

public class TopicHandlerMappingMgr {

	private static final Map<String, AnalylizeLogInter> topicHandlerMapping = new ConcurrentHashMap<String, AnalylizeLogInter>();
	private static final Map<String, Reducer> topicReduceMapping = new ConcurrentHashMap<String, Reducer>();
	private static final Map<String,String> classTopicMapping = new HashMap<String,String>();
	private static final Map<String,String> topicDirMapping = new ConcurrentHashMap<String, String>();
	private static String[] monitorPath = null ;
	public static void init(String path){
		if(!StringUtil.isEmpty(path)){
			if(path.endsWith(".xml")){
				try {
					Document document = DocumentHelper.parseText(IOUtils.toString(new FileReader(path)));
					Element rootElement = document.getRootElement();
					//1、
					handerMapping(rootElement);
					//2、
					reduceMapping(rootElement);
					//3
					initTopicDir(topicDirMapping.values());
				} catch (Exception e) {
					Log.error("", e);
				}
			}
		}
	}

	public static void setMonitorPaths(String[] monitorPath){
		if(monitorPath!=null&&monitorPath.length>0){
			TopicHandlerMappingMgr.monitorPath = monitorPath;
		}
	}
	private static void reduceMapping(Element rootElement) throws IOException {
		long start = System.currentTimeMillis();
		System.err.println(TopicHandlerMappingMgr.class.getName()+"开始解析 reduceMapping");
		List<Element> topicReduce = rootElement.elements("reducer");
		List<HandlerInfo> reducerHandler = new ArrayList<HandlerInfo>();
		for(Element reduce:topicReduce){
			reducerHandler.add(new HandlerInfo(reduce.attributeValue("class"),reduce.attributeValue("topic")));
		}
		
		System.err.println(TopicHandlerMappingMgr.class.getName()+"解析完，一共配置 reduceMapping"+reducerHandler.size()+",共耗时："+(System.currentTimeMillis()-start));
		
		mappingTopicReduce(reducerHandler);
	}

	private static void handerMapping(Element rootElement) throws IOException {
		List<Element> topicHandlers = rootElement.elements("mapper");
		List<HandlerInfo> handlerInfos = new ArrayList<HandlerInfo>();
		for(Element topicHandler : topicHandlers){
			handlerInfos.add(new HandlerInfo(topicHandler.attributeValue("class"), topicHandler.attributeValue("fileName"),topicHandler.attributeValue("topic")));
		}
		mappingTopicHandler(handlerInfos);
	}
	private static void mappingTopicHandler(List<HandlerInfo> handlerInfos){
		for (HandlerInfo handlerInfo : handlerInfos) {
			Class<?> clazz;
			try {
				clazz = Class.forName(handlerInfo.getClassName());
				topicHandlerMapping.put(MD5Security.compute(getFilePath(handlerInfo.getFileName())), (AnalylizeLogInter) clazz.newInstance());
				topicDirMapping.put(MD5Security.compute(getFilePath(handlerInfo.getFileName())), handlerInfo.getTopic());
			} catch (Exception e) {
				Log.error(ClasspathPackageScanner.class.getName(), e);
			}
		}
	}
	
	private static void handerMapping(Element rootElement, Map<String, List<String>> packageClassName) throws IOException {
		List<Element> topicHandlers = rootElement.elements("handlerscan");
		List<String> packageNames = new ArrayList<String>();
		for(Element topicHandler : topicHandlers){
			packageNames.add(topicHandler.attributeValue("base-package"));
		}
		long start = System.currentTimeMillis();
		
		Set<String> allTopicHandlerClass = new HashSet<String>();
		for(String packageN:packageNames){
			System.err.println("开始扫描："+packageN);
			List<String> packges = new ClasspathPackageScanner(packageN).getClassNameList();
			packageClassName.put(packageN,packges);
			allTopicHandlerClass.addAll(packges);
		}
		System.out.println("共耗时："+(System.currentTimeMillis()-start)+";一共配置了："+allTopicHandlerClass.size()+" 处理类。");
		mappingTopicHandler(allTopicHandlerClass);
	}
	
	public static void putTopicHandler(String topic, AnalylizeLogInter topicHandler) {
		if (!StringUtil.isEmpty(topic)) {
			topicHandlerMapping.put(topic, topicHandler);
		}
	}

	public static<K,V> AnalylizeLogInter<K,V> getTopicHandler(String fileName) {
		if (!StringUtil.isEmpty(fileName)) {
			return topicHandlerMapping.get(MD5Security.compute(fileName));
		}
		return null;
	}
	
	public static String getTopicName(String fileName){
		if(!StringUtil.isEmpty(fileName)){
			return topicDirMapping.get(MD5Security.compute(fileName));
		}
		return null ;
	}
	public static void mappingTopicHandler(Set<String> className) {
		for (String name : className) {
			Class<?> clazz;
			try {
				clazz = Class.forName(name);
				TopicHandler topicH = clazz.getAnnotation(TopicHandler.class);
				if (topicH != null&&!StringUtil.isEmpty(topicH.fileName())) {
					if (topicHandlerMapping.get(topicH.topic()) != null) {
						Log.error("topic handler repeat at_1:" + TopicHandlerMappingMgr.getTopicHandler(topicH.topic()).getClass().getName());
						Log.error("topic handler repeat at_2:" + clazz.getName());
						return;
					}

					topicHandlerMapping.put(MD5Security.compute(getFilePath(topicH.fileName())), (AnalylizeLogInter) clazz.newInstance());
					topicDirMapping.put(MD5Security.compute(getFilePath(topicH.fileName())), topicH.topic());
				}
			} catch (Exception e) {
				Log.error(ClasspathPackageScanner.class.getName(), e);
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 	传过来的fileName 是一个带通配符的表达式{0}error.log
	 * </pre>
	 *
	 * @param fileName
	 * @return
	 */
	private static String getFilePath(String fileName){
		int start = fileName.indexOf("{");
		int end = fileName.indexOf("}"); 
		if(start>=0&&end>0){
			try {
				int index = Integer.valueOf(fileName.substring(start+1, end));
				String[] paramter = new String[index+1];
				for(int i=0;i<index;i++){
					paramter[i]="";
				}
				paramter[index] = monitorPath[index];
				return MessageFormat.format(fileName,paramter);
			} catch (NumberFormatException e) {
				Log.error("", e);
			}
			
		}
		return fileName;
	}
	
	public static void mappingTopicReduce(List<HandlerInfo> reducerHandler) {
		for (HandlerInfo handlerInfo: reducerHandler) {
			Class<?> clazz;
			try {
				clazz = Class.forName(handlerInfo.getClassName());
				if (!StringUtil.isEmpty(handlerInfo.getTopic())) {
					if (topicReduceMapping.get(handlerInfo.getTopic()) != null) {
						Log.error("topic reduce repeat at_1:" + topicReduceMapping.get(handlerInfo.getTopic()).getClass().getName());
						Log.error("topic reduce repeat at_2:" + clazz.getName());
						return;
					}

					topicReduceMapping.put(handlerInfo.getTopic(), (Reducer) clazz.newInstance());
					classTopicMapping.put(handlerInfo.getClassName(), handlerInfo.getTopic());
				}
			} catch (Exception e) {
				Log.error(ClasspathPackageScanner.class.getName(), e);
			}
		}
	}
	
	public static void mappingTopicReduce(Set<String> className) {
		for (String name : className) {
			Class<?> clazz;
			try {
				clazz = Class.forName(name);
				TopicReduce topicH =clazz.getAnnotation(TopicReduce.class);
				
				if (topicH != null&&!StringUtil.isEmpty(topicH.topic())) {
					if (topicReduceMapping.get(topicH.topic()) != null) {
						Log.error("topic reduce repeat at_1:" + topicReduceMapping.get(topicH.topic()).getClass().getName());
						Log.error("topic reduce repeat at_2:" + clazz.getName());
						return;
					}

					topicReduceMapping.put(topicH.topic(), (Reducer) clazz.newInstance());
				}
			} catch (Exception e) {
				Log.error(ClasspathPackageScanner.class.getName(), e);
			}
		}
	}
	
	private static void initTopicDir(Collection<String> topics){
		for(String topic:topics){
			MessagePersiterMgr.init(new TopicPath(topic, ConfigMgr.getMsgPath()));
		}
	}
	
	public static Reducer getTopicReduce(String topic){
		return topicReduceMapping.get(topic);
	}
	
	public static String getReducerTopicName(String className){
		
		return classTopicMapping.get(className);
	}
}
