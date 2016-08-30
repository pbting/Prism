package exetuor;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("pbting-1", "value-1");
		map.put("pbting-2", "value-2");
		map.put("pbting-3", "value-3");
		System.out.println(map.entrySet());
	}
}
