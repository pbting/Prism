package com.road.yishi.log.analysor.filter;

import com.road.yishi.log.handler.filter.FileHandlerFilter;

public class ErrorFileFilter implements FileHandlerFilter {

	@Override
	public boolean accept(String fileName) {
		return fileName.endsWith("error.log");
	}
}
