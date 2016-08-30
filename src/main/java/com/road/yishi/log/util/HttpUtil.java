package com.road.yishi.log.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import com.road.yishi.log.Log;

public class HttpUtil {

	private static final String REQUEST_METHOD_POST = "POST";
	
	private static final String REQUEST_METHOD_GET = "GET";

	/**
	 * 连接超时
	 */
	private static int CONNECT_TIME_OUT = 2000;

	/**
	 * 读取数据超时
	 */
	private static int READ_TIME_OUT = 2000;

	/**
	 * 请求编码
	 */
	public static String REQUEST_ENCODING = "UTF-8";
	
	/**
	 * 接收编码
	 */
	public static String RESPONSE_ENCODING = "UTF-8";
	
	public static final short OK = 200 ;
	
	public static final short Bad_Request = 400  ;
	
	public static final short Internal_Server_Error = 500 ; 
	
	public static final short PARAM_ERROR_NO_ANALYSESOR = 1000 ; 
	/**
	 * <pre>
	 * 发送带参数的GET的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl, Map<String, String> paramMap, String recvEncoding) {
		return doRequest(reqUrl, paramMap, REQUEST_METHOD_GET, recvEncoding);
	}

	/**
	 * <pre>
	 * 发送不带参数的GET的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl HTTP请求URL
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl, String recvEncoding) {
		Map<String, String> paramMap = null;
		String queryUrl = reqUrl;
		int paramIndex = reqUrl.indexOf("?");
		if (paramIndex > 0) {
			paramMap = new HashMap<String, String>();
			queryUrl = reqUrl.substring(0, paramIndex);
			String parameters = reqUrl.substring(paramIndex + 1, reqUrl.length());
			String[] paramArray = parameters.split("&");
			for (int i = 0; i < paramArray.length; i++) {
				String string = paramArray[i];
				int index = string.indexOf("=");
				if (index > 0) {
					paramMap.put(string.substring(0, index), string.substring(index + 1, string.length()));
				}
			}
		} else {
			return null;
		}

		return doRequest(queryUrl, paramMap, REQUEST_METHOD_GET, recvEncoding);
	}

	/**
	 * <pre>
	 * 发送带参数的POST的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doPost(String reqUrl, Map<String, String> paramMap, String recvEncoding) {
		return doRequest(reqUrl, paramMap, REQUEST_METHOD_POST, recvEncoding);
	}

	private static String doRequest(String reqUrl, Map<String, String> paramMap, String reqMethod, String recvEncoding) {
		
		return doExecute(reqUrl,paramMap,reqMethod,recvEncoding);
	}

	private static String doExecute(String reqUrl, Map<String, String> paramMap, String reqMethod, String recvEncoding) {
		HttpURLConnection urlCon = null;
		String responseContent = null;
		try {
			StringBuilder params = new StringBuilder();
			if (paramMap != null) {
				for (Entry<String, String> element : paramMap.entrySet()) {
					params.append(element.getKey());
					params.append("=");
					params.append(URLEncoder.encode(element.getValue(), REQUEST_ENCODING));
					params.append("&");
				}

				if (params.length() > 0) {
					params = params.deleteCharAt(params.length() - 1);
				}
			}
			URL url = new URL(reqUrl);
			urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod(reqMethod);
			urlCon.setConnectTimeout(CONNECT_TIME_OUT);
			urlCon.setReadTimeout(READ_TIME_OUT);
			urlCon.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			Log.info(HttpUtil.class.getName()+";request url:"+reqUrl+";send paramters:"+params);
			urlCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			urlCon.setRequestProperty("Content-Length", String.valueOf(b.length));
			urlCon.getOutputStream().write(b, 0, b.length);
			urlCon.getOutputStream().flush();
			urlCon.getOutputStream().close();

			InputStream in = urlCon.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();

			urlCon.getResponseMessage();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlCon != null) {
				urlCon.disconnect();
			}
		}
		return responseContent;
	}
	public static boolean upload(String localFile,String url){
        File file = new File(localFile);
        PostMethod filePost = new PostMethod(url);
        HttpClient client = new HttpClient();
        
        try {
            Part[] parts = { new FilePart(file.getName(), file) };
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
            int status = client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                Log.info("localFile:"+localFile+"上传成功"+";master url:"+url);
                return true ;
            } else {
            	 Log.info("localFile:"+localFile+"上传失败"+";master url:"+url);
            	 return false ;
            }
        } catch (Exception ex) {
            Log.error("localFile:"+localFile+"上传失败"+";master url:"+url,ex);
            return false ;
        } finally {
            filePost.releaseConnection();
        }
    }
	public static void main(String[] args) {
		String fileName = "D:/demo_1.txt";
		String url = "http://127.0.0.1:81/Prism/fileUpload.do";
		upload(fileName, url);
//		try {
//			File file = new File("D:/demo_1.txt");
//			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//			int count = 500000;
//			while(count-->0){
//				writer.write("我是一个的说法都是发的发的是官方的广泛地功夫功夫功夫功夫刚刚发给方法刚刚复古风格方法刚刚发给发给 ");
//				writer.newLine();
//			}
//			writer.flush();
//			writer.close();
//		} catch (IOException e) {
//			Log.error("", e);
//		}
	}

	private static void demo() {
		String url = "http://127.0.0.1:81/Prism/reciverMessage.do";
		Map<String,String> paramter = new HashMap<String,String>();
		paramter.put("topic", "test");
		paramter.put("message", ":message-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wifemessage-pbting\r\nwyl-my wife");
		long start = System.currentTimeMillis();
		String response = HttpUtil.doPost(url, paramter,HttpUtil.REQUEST_ENCODING);
		System.out.println(System.currentTimeMillis()-start);
		System.out.println(response);
	}
}
