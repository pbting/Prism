package com.road.yishi.log.monitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.road.yishi.log.handler.filter.FileHandlerFilter;

public class ResourceListener {

	private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();//(5);  
    private WatchService ws;  
    private String listenerPath;  
    private FileHandlerFilter[] handlerFileters ;
    private ResourceListener(String path) {  
        try {  
            ws = FileSystems.getDefault().newWatchService();  
            this.listenerPath = path;  
            start();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private ResourceListener(String path,FileHandlerFilter[] handlerFileters) {  
        try {  
            ws = FileSystems.getDefault().newWatchService();  
            this.listenerPath = path;  
            this.handlerFileters = handlerFileters;
            start();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    private void start() {  
        fixedThreadPool.execute(new TopicListener(ws,this.listenerPath));  
    }  
  
    public static void addListener(String path) throws IOException {  
        ResourceListener resourceListener = new ResourceListener(path);  
        Path p = Paths.get(path);  
        p.register(resourceListener.ws,StandardWatchEventKinds.ENTRY_MODIFY,  
            StandardWatchEventKinds.ENTRY_DELETE,  
            StandardWatchEventKinds.ENTRY_CREATE);  
    }  
      
    public static void addListener(String path,FileHandlerFilter handlerFilter) throws IOException {  
        ResourceListener resourceListener = new ResourceListener(path);  
        Path p = Paths.get(path);  
        p.register(resourceListener.ws,StandardWatchEventKinds.ENTRY_MODIFY,  
            StandardWatchEventKinds.ENTRY_DELETE,  
            StandardWatchEventKinds.ENTRY_CREATE);  
    }  
    
    
    public static void main(String[] args) throws IOException { 
        ResourceListener.addListener("E:/prism/topic_1");  
        ResourceListener.addListener("E:/prism/topic_2");  
        ResourceListener.addListener("E:/prism/topic_3");  
    }  
}