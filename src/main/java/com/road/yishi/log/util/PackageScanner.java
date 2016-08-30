package com.road.yishi.log.util;

import java.io.IOException;
import java.util.List;

public interface PackageScanner {

	public List<String> getClassNameList() throws IOException;
}
