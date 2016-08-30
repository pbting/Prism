package com.road.yishi.log;

import java.util.Comparator;

import com.road.yishi.log.vo.LogKeyInfo;

public class LogDateComparator implements Comparator<LogKeyInfo>{

	@Override
	public int compare(LogKeyInfo o1, LogKeyInfo o2) {
		
		int key = o2.getPrintDate().compareTo(o1.getPrintDate());
		
		return key==0 ? o2.getKey().compareTo(o1.getKey()):key;
	}
}
