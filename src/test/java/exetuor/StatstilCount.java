package exetuor;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.road.yishi.log.Log;

public class StatstilCount implements Runnable{

	private String path;
	private String spe;
	private String NEWLINE = "\r\n";
	private CyclicBarrier cyclicBarrier ;
	public StatstilCount(String path,String spe,CyclicBarrier cyclicBarrier){
		this.path = path;
		this.spe = spe;
		this.cyclicBarrier = cyclicBarrier;
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
		int count = 100;
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
				int index = random.nextInt(10)+1;
				String con = String.valueOf(index);
				synchronized (path) {
					writer.write(con.getBytes("utf-8"));//w
					writer.write(NEWLINE.getBytes("utf-8"));
					writer.flush();
				}
				Thread.sleep(1000);
				System.out.println(index+"----<"+count+"---->byte:"+new String(con.getBytes("utf-8")));
				count--;
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
		try {
			cyclicBarrier.await();
		} catch (InterruptedException e) {
			Log.error("", e);
		} catch (BrokenBarrierException e) {
			Log.error("", e);
		}
	}
}
