package exetuor.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;
import com.road.yishi.log.handler.FileHanderExecutor;

import io.netty.util.concurrent.DefaultThreadFactory;

public class ThreadPoolExecutorTest {

	public static void main(String[] args) {
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
