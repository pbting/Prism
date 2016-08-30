package exetuor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public class ValidateCount {

	public static void main(String[] args) {
		String[] paths = new String[]{"E:/prism/topic_1/topic.txt","E:/prism/topic_2/topic_2.txt","E:/prism/topic_3/topic_3.txt"};
		String[] spe = new String[]{"\\|","=","-"};
		Map<Integer, Integer> read = new HashMap<Integer, Integer>();
		for(int i=0;i<paths.length;i++){
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File(paths[i])));
				String line = "";
				while((line=reader.readLine())!=null){
					if(line.length()<=0)
						continue;
					
					System.out.println(line+"-->"+paths[i]);
					int luckNum = Integer.valueOf(line.split(spe[i])[0]);
//					if(String.valueOf(luckNum).indexOf("6")>=0||String.valueOf(luckNum).indexOf("8")>=0){
//						if(read.get(luckNum) == null){
//							read.put(luckNum, 0);
//						}
//						read.put(luckNum, read.get(luckNum)+1);
//					}
					if(read.get(luckNum) == null){
						read.put(luckNum, 0);
					}
					read.put(luckNum, read.get(luckNum)+1);
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(new Gson().toJson(read));
	}
}
