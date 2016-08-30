package exetuor;

import com.road.yishi.log.core.LogMetaFactory;

public class WebServerTest {

	public static void main(String[] args) {
		String errorLine = "ERROR:2016-09-02 03:00:02,085--53180097[http-bio-81-exec-5]";
		String secondLine = "com.road.yishi.servlet.gm.system.LoadTemps.doExec(LoadTemps.java:43)";
		String printDate = LogMetaFactory.getString(errorLine, false, ":", ",");
		String callMethod = LogMetaFactory.getString(secondLine, "", "(");
		String callClasss = LogMetaFactory.getString(secondLine, "(",")");
		System.out.println(printDate);
		System.out.println(callMethod);
		System.out.println(callClasss);
	}
}
