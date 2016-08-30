package exetuor;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Writertest {

	public static void main(String[] args) {
		String path = "D:/yishi/slg/server/jiuzhou/branches/Workspace/Lib/log/castle/error.log";
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(path,true));){
			for(int i=0;i<100;i++){
				writer.write("TEST DATA.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
