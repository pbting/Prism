package com.road.yishi.log.core;

public class LogMetaFactory {

	private LogMetaFactory() {
	}

	// 开始解析日志行的标记
	public static String KEY_START ;//= "TKC";// 这一行的关键词(1)，如果改关键出现，就初步认为是接下来的日志信息是需要解析的

	public static String KEY_END ;//= "Log.java";// 这一行的关键词(2),如果改行出现(1)和(2)则认为肯定有日志信息输出，

	public static String KEY_TIME_START;// = KEY_START;
	
	public static String KEY_TIME_END ; //= "[ERROR]";
	
//	public static final String LOG_KEY = "[ERROR] [TKC]";// 开始打印日志 标识行

	// 1、日志输出的调用方法一
	public final static int NUMBER_70 = 70;
	private final static String LOG_METHOD_NAME_70 = "Log.error(Object msg)";
	// 2、日志输出的调用方法二
	public final static int NUMBER_76 = 76;
	private final static String LOG_METHOD_NAME_76 = "Log.error(Object msg, Throwable t)";

	/**
	 * <pre>
	 * [ERROR] [BYLIUYINGBO] 2016-01-26 20:39:03: 290953 [ERROR] [main] ( Log.java,70 ) - com.road.yishi.CastleServer.start(CastleServer.java:724)
	 * </pre>
	 *
	 * @param lineInfo
	 * @return
	 */
	public static LogMetaInfo getLogMetaInfo(String lineInfo) {
		LogMetaInfo logMetaInfo = new LogMetaInfo();

		// 1、解析出发生的时间
		
		// System.out.println("--->date str:"+dateStr);
		logMetaInfo.setPrintDate(getDate(lineInfo, KEY_TIME_START, KEY_TIME_END));

		String classNameKey = lineInfo.substring(lineInfo.lastIndexOf("-") + 1).trim();
		// System.out.println("class name key:"+classNameKey);
		logMetaInfo.setCallClassName(classNameKey);

		// 解析出行号
		int lineNumStartIndex = lineInfo.indexOf("Log.java,") + "Log.java,".length();
		int lineNum = 0;
		try {
			lineNum = Integer.valueOf(lineInfo.substring(lineNumStartIndex, lineNumStartIndex + 2));
		} catch (Exception e) {
			lineNum = 70;// 给一个默认的调用
		}
		logMetaInfo.setLineNum(lineNum);
		if (lineNum == NUMBER_70) {
			logMetaInfo.setCallMethodName(LOG_METHOD_NAME_70);
		} else if (lineNum == NUMBER_76) {
			logMetaInfo.setCallMethodName(LOG_METHOD_NAME_76);
		}
		logMetaInfo.setMetaLogInfo(lineInfo);

		return logMetaInfo;
	}
	
	public static String getDate(String lineInfo,String startKey,String endKey){
		
		return getString(lineInfo, startKey, endKey);
	}
	
	public static String getClassName(String lineInfo,String startKey,String endKey){
		
		return getString(lineInfo, startKey, endKey);
	}
	
	public static String getString(String lineInfo,String startKey,String endKey){
		int dateStartIndex = lineInfo.indexOf(startKey) + startKey.length();
		int dateEndIndex = lineInfo.lastIndexOf(endKey);
		String dateStr = lineInfo.substring(dateStartIndex, dateEndIndex).trim();
		return dateStr;
	}
	
	public static String getString(String lineInfo,boolean isLastScan,String startKey,String endKey){
		int dateStartIndex = 0 ;
		int dateEndIndex = 0 ;
		if(isLastScan){
			dateStartIndex = lineInfo.lastIndexOf(startKey) + startKey.length();
			dateEndIndex = lineInfo.lastIndexOf(endKey);
		}else{
			dateStartIndex = lineInfo.indexOf(startKey) + startKey.length();
			dateEndIndex = lineInfo.indexOf(endKey);
		}
		try {
			if(dateStartIndex >0&&dateStartIndex<lineInfo.length()&&dateEndIndex>0&&dateEndIndex<lineInfo.length()){
				return lineInfo.substring(dateStartIndex, dateEndIndex).trim();
			}
		} catch (Exception e) {
//			Log.error("", e);
			return "";
		}
		return "";
	}
	
	public static void main(String[] args) {
		String line = "[ERROR] [TKC] 2016-11-26 00:00:01: 92395070 [ERROR] [ScanTimer] ( Log.java,70 ) - com.road.yishi.goal.GoalManager.switchToNextGoal(GoalManager.java:352)";
		String callMethod = LogMetaFactory.getString(line,true,"-","(");
		String callClass = LogMetaFactory.getString(line, true,"(", ")");
		String printDate = LogMetaFactory.getString(line.substring(0,line.lastIndexOf("[ERROR]")), true, "[TKC]", ":");
		System.out.println(callMethod);
		System.out.println(callClass);
		System.out.println(printDate);
	}
}
