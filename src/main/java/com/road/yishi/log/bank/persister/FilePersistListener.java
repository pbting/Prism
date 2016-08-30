package com.road.yishi.log.bank.persister;

import com.google.gson.reflect.TypeToken;

import audaque.com.pbting.cache.exception.FilePersistException;

public interface FilePersistListener {

	/**
	 * 
	 */
	public void clear() throws FilePersistException;
	
	/**
	 * 
	 */
	public FilePersistListener config(String topic,String path);
	
	/**
	 */
	public boolean isStored(String key);
	
	/**
	 * 
	 */
	
	public boolean remove(String key) throws FilePersistException;
	
	/**
	 * 
	 */
	public void store(String topic,Object key,Object value,short type) throws FilePersistException; 
	
	public Object retrieve(String topic,Object key,short type,TypeToken<?> typeToken) throws FilePersistException ;
}
