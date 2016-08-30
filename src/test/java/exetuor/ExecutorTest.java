package exetuor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorTest {

	private final static ExecutorService service = Executors.newCachedThreadPool();//(2);
	public static void main(String[] args) {
		int count = 20 ;
		CyclicBarrier cyclicBarrier = new CyclicBarrier(count,new Runnable() {
			
			@Override
			public void run() {
				System.out.println("20个线程处理完，接下来做统计结果");
				String path = "E:/prism/count_1/count_1.txt";
				HashMap<String, Integer> count = new HashMap<String,Integer>();
				try(BufferedReader reader = new BufferedReader(new FileReader(new File(path)))){
					String line = "";
					while((line=reader.readLine()) != null){
						if(count.get(line.trim()) == null){
							count.put(line, 0);
						}
						count.put(line,count.get(line)+1);
					}
				}catch(IOException e){
					e.printStackTrace();
				}
				System.out.println("self statistic:"+count.entrySet());
			}
		});

		for(int i=0;i<count;i++){
			new Thread(new StatstilCount("E:/prism/count_1/count_1.txt","|",cyclicBarrier)).start();
		}
//		for(int i=0;i<count;i++){
//			new Thread(new StatstilCount("E:/prism/count_2/count_2.txt","=")).start();
//		}
//		
//		for(int i=0;i<count;i++){
//			new Thread(new StatstilCount("E:/prism/count_3/count_3.txt","-")).start();
//		}
	}
}

class Task implements Runnable{

	@Override
	public void run() {
		System.out.println("------yunxing le");
	}
	
}