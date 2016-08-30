package exetuor;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.road.yishi.log.analysor.test.Person;

public class GsonFile {

	public static void main(String[] args) {
		List<Person> message = new ArrayList<Person>();
		message	.add(new Person(12, "12121", "pbting-1", 34));
		message.add(new Person(12, "12121", "pbting-2", 34));
		message.add(new Person(12, "12121", "pbting-3", 34));
		message.add(new Person(12, "12121", "pbting-4", 34));
		message.add(new Person(12, "12121", "pbting-5", 34));
		String json = new Gson().toJson(message);
		File file = new File("D:/gson.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
//			FileOutputStream fos = new FileOutputStream(file, true);
//			DataOutputStream writer = new DataOutputStream(fos);
			// 先加密后压缩存储
			// String inputString = DESUtil.encrypt(new Gson().toJson(message));
			StringBuffer inputString = new StringBuffer(new Gson().toJson(message));
			int tempLenth = inputString.toString().getBytes("UTF-8").length;
			ByteBuffer buffer = ByteBuffer.allocate(tempLenth);
			buffer.put(inputString.toString().getBytes("UTF-8"));
			System.out.println(file.length()+"---vs--->"+tempLenth);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
