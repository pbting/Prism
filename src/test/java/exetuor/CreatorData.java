package exetuor;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.road.yishi.log.Log;
import com.road.yishi.log.analysor.test.Person;

public class CreatorData implements Runnable{

	private static final List<String> tels = 
			Arrays.asList("+8618026993009","+8615279132865","+8613435546929",
					"+8615679622762","+8613117866095","+8613197898715");
	private static final List<String> name = 
			Arrays.asList("wuyinli","pbting","baba-1","baba-2","mum","sister");
	private static final int[] LUCK_NUM = new int[]{6,8,68,668,688,888}; 
	private static final String content = "我是一个最后边那个过热我是一个最后边那个过热我是一个最后边那个过热我是一个最后边那个过热我是一个最后边那个过热我是一个最后边那个过热";
	private String path;
	private String spe;
	private String NEWLINE = "\r\n";
	public CreatorData(String path,String spe){
		this.path = path;
		this.spe = spe;
	}

	@Override
	public void run() {
		File file = new File(path);
		if(!file.exists()){
			File pa = file.getParentFile();
			if(!pa.exists())
				pa.mkdirs();
			
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		int count = 2000;
		Random random = new Random();
		
		while(count > 0){
			FileLock fileLock = null ;
			FileOutputStream fos = null ;
			FileChannel fileC  =null ;
			DataOutputStream writer = null ;
			try {
				fos = new FileOutputStream(file,true);
				fileC = fos.getChannel();
				writer = new DataOutputStream(fos);
				int index = random.nextInt(tels.size());
				Person per = new Person(random.nextInt(100),tels.get(index), name.get(index)+">"+content, count);
				String con = per.toString(spe);
				writer.write(con.getBytes("utf-8"));//w
				writer.write(NEWLINE.getBytes("utf-8"));
				writer.flush();
				Thread.sleep(1000);
				count--;
				System.err.println(per.toString(spe));
			} catch (Exception e) {
				Log.error("", e);
				e.printStackTrace();
			}finally{
				if(fileLock!=null){
					try {
						fileC.close();
						fos.close();
						writer.close();
					} catch (IOException e) {
						Log.error("", e);
						e.printStackTrace();
					}
				}
			}
		}
	}
}
