package exetuor.io.reactor;

import java.io.BufferedInputStream;
import java.net.URL;

public class ResourceRead {

	public static void main(String[] args) {
		BufferedInputStream is = (BufferedInputStream) ResourceRead.class.getResourceAsStream("demo.txt");
		System.out.println(is);
		URL url = ResourceRead.class.getResource("demo.txt");
		System.out.println(url.getFile());
	}
}
