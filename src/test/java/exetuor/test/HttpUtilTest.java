package exetuor.test;

import java.util.HashMap;
import java.util.Map;

import com.road.yishi.log.util.HttpUtil;

public class HttpUtilTest {

	public static void main(String[] args) {
		String url = "http://127.0.0.1:81/Prism/testServlet.do" ;
		Map<String,String> param = new HashMap<String,String>();
		param.put("topic", "test");
		param.put("message", "message in chaina in jiangxi\r\n");
		String response = HttpUtil.doPost(url, param, HttpUtil.RESPONSE_ENCODING);
		System.out.println(response);
	}
}
