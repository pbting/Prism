package exetuor.thread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.road.yishi.log.Log;

import io.netty.util.concurrent.DefaultThreadFactory;

public class ThreadPoolExecutorTest {

	public static void main(String[] args) {
//		
		int[] demo = new int[10];
		System.out.println(demo.getClass().getName());
		float[] floatArry = new float[10];
		System.out.println(floatArry.getClass().getName());
	}

	private static void threadMethod() {
		Thread thread = new Thread(new Runnable() {
			private Random random = new Random();
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(2000);
						System.out.println("唯我独尊");
					} catch (InterruptedException e) {
						Log.error("", e);
					}
					if(random.nextInt(100)<50){
						Thread.currentThread().interrupt();
					}
				}
			}
		});
		thread.setDaemon(false);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
		while(thread.isAlive()){
			if(thread.isInterrupted()){
				System.out.println("线程被中断，");
				ThreadGroup threadGroup = thread.getThreadGroup();
				Thread[] list = new Thread[threadGroup.activeCount()];
				threadGroup.enumerate(list);
				System.out.println("thread group name is:"+threadGroup.getName()+";size:"+list.length);
			}
		}
		System.out.println("线程一已经执行完");
	}
	
	public static void linkedBlockingQuence(){

		LinkedBlockingQueue<Runnable> workQuence = new LinkedBlockingQueue<Runnable>();
//		ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);
		//daemon and user thread.
		ThreadFactory threadFactory = new DefaultThreadFactory("test", false);
		//最大并发数为6，正常情况下两个或者两个以下正常访问，只用core thread to handler,when the concurrent visit greater than two then
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 64, 60,TimeUnit.SECONDS,workQuence,threadFactory);
		for(int i=1;i<=10000;i++){
//			FileHanderExecutor.getExecutor().enDefaultQueue(new Action() {
//				@Override
//				public long execute() throws TaskExecuteException {
//					System.out.println("thread name:"+Thread.currentThread().getName()+";task count:"+executor.getTaskCount()+";active count:"+executor.getActiveCount()+";complete task count:"+executor.getCompletedTaskCount());
//					return 0;
//				}
//			});
			executor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("thread name:"+Thread.currentThread().getName()+";task count:"+executor.getTaskCount()+";active count:"+executor.getActiveCount()+";complete task count:"+executor.getCompletedTaskCount());
				}
			});
		}
	
	}
}
