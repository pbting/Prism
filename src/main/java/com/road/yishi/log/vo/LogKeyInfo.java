package com.road.yishi.log.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.road.yishi.log.core.LogMetaInfo;

public class LogKeyInfo implements Comparable<LogKeyInfo>,Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private String key ;
	private String printDate ;
	private int count ;//the count of log informations to the key
	public LogKeyInfo() {
	}
	public LogKeyInfo(String key){
		this.key = key;
	}
	public LogKeyInfo(String printDate,String key, int count) {
		super();
		this.printDate = printDate;
		this.key = key;
		this.count = count;
	}
	public LogKeyInfo(String key, String printDate) {
		super();
		this.key = key;
		this.printDate = printDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogKeyInfo other = (LogKeyInfo) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	public String getPrintDate() {
		return printDate;
	}

	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int compareTo(LogKeyInfo o) {
		int i = o.count - this.count;
		if(i == 0){
			i = o.key.compareTo(this.key);
		}
		
		return i;
	}
	
	public static void main(String[] args) {
		HashMap<LogKeyInfo, Integer> KEY_COUNT = new HashMap<LogKeyInfo,Integer>();
		LogKeyInfo key1 = new LogKeyInfo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"error->Java",0);
		KEY_COUNT.put(key1, 1);
		LogKeyInfo key2 = new LogKeyInfo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"error->Java",0);
		KEY_COUNT.put(key2, 0);
		System.out.println("KEY_COUNT--->"+KEY_COUNT.get(key2));
		
		HashMap<LogMetaInfo, Integer> hashMap = new HashMap<LogMetaInfo,Integer>();
		LogMetaInfo logm = new LogMetaInfo("error->java->1", "2015-04-15", "error", "error");
		hashMap.put(logm, 1);
		LogMetaInfo logm1 = new LogMetaInfo("error->java->2", "2015-05-15", "error", "error");
		hashMap.put(logm1, 3);
		System.out.println(hashMap.size()+"-0->"+hashMap.get(logm1));
	}
}
