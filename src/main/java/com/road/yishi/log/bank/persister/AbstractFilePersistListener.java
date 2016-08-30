package com.road.yishi.log.bank.persister;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.bank.DataBankMgr;
import com.road.yishi.log.bank.MD5Security;

import audaque.com.pbting.cache.exception.FilePersistException;
import audaque.com.pbting.cache.util.StringUtils;

public abstract class AbstractFilePersistListener implements FilePersistListener {

	private final static Log log = LogFactory.getLog(AbstractFilePersistListener.class);
	
	// 
	protected final static String FILE_EXTION = ".cache";

	// application
	protected final static String APPLICATION_CACHE_PATH = "prism";

	//
	protected File cachePath = null;

	// 
	private String rootPath = null;

	public void clear() throws FilePersistException {

		this.clear(rootPath);
	}

	/**
	 * @param rootDirName
	 * @throws FilePersistException
	 */
	private void clear(String rootDirName) throws FilePersistException {
		
		if(log.isDebugEnabled()){
			
			log.debug(":[clear="+rootDirName+"]");
		}
		
		File rootFile = new File(rootDirName);

		File[] fileList = rootFile.listFiles();

		// 
		try {
			if (fileList != null) {

				for (int i = 0; i < fileList.length; i++) {

					if (fileList[i].isFile()) {

						fileList[i].delete();
					} else {

						this.clear(fileList[i].toString());//
						fileList[i].delete();//
					}
				}
			}

			//
			rootFile.delete();
		} catch (Exception e) {
			throw new FilePersistException("");
		}
	}

	public FilePersistListener config(String topic,String path) {

		initFileStore(path);
		StringBuffer root = new StringBuffer(getCachePath().getPath());
		root.append("/");
		root.append(StringUtils.isEmpty(topic)?APPLICATION_CACHE_PATH:topic);
		this.rootPath = root.toString();
		return this;
	}

	public File getCachePath() {
		return cachePath;
	}

	public boolean isStored(String key) {
		try {
			File file = this.getCacheFile(key);

			return file.exists();
		} catch (Exception e) {

			return false;
		}
	}

	public boolean remove(String key) throws FilePersistException {
		try {
			File file = this.getCacheFile(key);
			
			this.remove(file);

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public Object retrieve(String topic,Object key,short type,TypeToken<?> typeToken) throws FilePersistException {
		
		File file = this.getCacheFile(MD5Security.compute(key.toString()));
		
		return DataBankMgr.retrieve(topic,key,file.getAbsolutePath(),type,typeToken);
	}

	public void store(String topic,Object key, Object value,short type) throws FilePersistException {
		this.MsgStore(topic,key,this.getCacheFile(MD5Security.compute(key.toString())),value,type);
	}

	// will get the file of cache directly
	protected File getCacheFile(String key) {
		char[] fileName = this.getCacheFileName(key);

		return new File(rootPath, new String(fileName) + FILE_EXTION);
	}

	// will based on group name get the file of the related group file
	protected abstract char[] getCacheFileName(String key);

	/**
	 * init file cacheing by the file path
	 * @param cacheFilePath
	 */
	protected void initFileStore(String cacheFilePath) {
		if (cacheFilePath != null) {
			this.cachePath = new File(cacheFilePath);
			System.err.println("cache file path is :"+this.cachePath.getAbsolutePath());
			try {

				if (!this.cachePath.exists()) {//不存在，则创建
					if (log.isDebugEnabled()) {

						log.info("HighCache:the file directory of cache is:" + cacheFilePath + "");
					}

					//create new dirs
					this.cachePath.mkdirs();

				}

				// judge the path whether is directory or not 
				if (!this.cachePath.isDirectory()) {//如果配置的不是目录，则删除，并置为null
					if (log.isErrorEnabled()) {

						log.error("[" + this.cachePath.getAbsolutePath()+ "]");
					}

					this.cachePath = null;
					return ;
				}

				// 
				if (!this.cachePath.canWrite()) {
					if (log.isErrorEnabled()) {

						log.error("[" + this.cachePath.getAbsolutePath()+ "]");
					}

					this.cachePath = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 */
	private static final long DELETE_THREAD_SLEEP = 500;

	private static final int DELETE_COUNT = 60;

	protected void remove(File file) throws FilePersistException {
		int count = DELETE_COUNT;

		try {
			//
			while (file.exists() && !file.delete() && count != 0) {
				count--;

				try {
					Thread.sleep(DELETE_THREAD_SLEEP);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (file.exists() && count == 0) {

			throw new FilePersistException("HighCache:[" + file.getName() + "]cano't delete it");
		}
	}

	public void MsgStore(String topic,Object key,File file,Object value,short type){
		PersisterExecutor.getExecutor().enDefaultQueue(new Persister(topic, key, value, file.getAbsolutePath(), type));
	}
}
