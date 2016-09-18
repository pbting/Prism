package exetuor;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.road.yishi.log.Log;

/**
 * 线程池的类型目前有以下几种分别适应以下几种场景：
 * 1、Executoes.newFixedThreadPool(n):
 * 开指定线程处理任务。如果提交过来的任务还未来的处理，则放在一个无界队列里面。如果执行任务的线程突然因为异常
 * 而中断，则会开启一个新的线程来取代继续执行只任务序列;
 * 【note】 这个线程池里面始终都会保持n个活动的线程。使用的是无界队列
 * 
 * 2、Executors.newCachedThreadPool():
 * 线程池中没有始终存活的核心线程。是当有任务过来的时候，线程池中如果有存活空闲的线程就处理该任务，如果没有，则放在异步队列
 * 暂存起来。直到有空闲的存活线程去执行。当所有的任务执行完后，在指定的idle 时间后仍然没有新的任务提交进来，这个时候这些线
 * 程都会被terminate掉
 * [优点]当任务提交的比较频繁时，并发访问量不大的时候，可以很好的利用空闲线程来处理请求。同时也可以很好的节约内存。因为一旦idle
 * 时间到了，这些线程都会 terminate 掉。
 * [缺点]如果大并发访问量的情况下，该线程池会无限创建n个线程来处理任务。这个时候很有可能就会很快就把内存的消耗完。同时，因为线程池里
 * 不会始终持有活动的线程，因此当下次有新的任务提交过来的时候，又会重新创建新建，频繁的从用户态到核心的切换，降低系统性能。
 * 
 * 3、Executors.newSingleThreadExecutor():
 * 线程池里面始终都一个活动线程处理提交过来的任务。该线程保证了同一时刻只能有一个线程可执行，基于公平调度策略。因为使用的是基于链表的无界队列。
 * [问题]那么他和newFixedThreadPool(1)时有啥区别呢？也是保证线程池中始终只有一个活动线程。通过源码可发现。newSignalThreadPool()它返回的
 * 是一个仅仅拥有可调ExecutorService 接口提供的方法[单存的]实例。该实例是对ThreadPoolExecutor进行简单包装后的一个实例。他希望调用者不会使用
 * 到ThreadPoolExecutor 类中的除ExecutorService 其他的方法。装饰设计模式就是给调用着只暴露调用着所关心的核心信息，一些其他的进行屏蔽。这样对
 * 于用户来说是友好的。
 * 有调度功能的线程池：
 * 
 * 4、Executors.newSingleThreadScheduledExecutor()
 * 5、Executors.newScheduledThreadPool(n)
 * 隔多长时间执行[有返回结果的任务+无结果返回的任务]
 * 一指定的频率执行某个任务
 * <pre>
 * 	提交过来的任务执行策略有三种：
 *  1、立即执行
 *  2、在延时
 * </pre>
 */
public class ThreadPoolExecutors {

	public static void main(String[] args) throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executorService;
		ExecutorService signalService = Executors.newSingleThreadExecutor();
//		ThreadPoolExecutor poolExecutor1 = (ThreadPoolExecutor) signalService;
		System.out.println("executorService:"+executorService+";signalService"+signalService);
		
		/**
		 * 注释是这样子说的：调用 service.executor 方法，如果线程池中有空闲的线程，该线程
		 * 来处理提交上来的任务，如果没有的话，则创建一个新的线程处理，所有的空闲线程超过了60s,
		 * 都会被stop掉
		 */
		ExecutorService service = Executors.newCachedThreadPool();
		
		/**
		 * 单个线程来对提交过来的任务进行有序的调度。如果该线程在执行过程中突然因为异常而终止，他会创建一个新的
		 * 线程继续执行子任务序列。该可调度的线程池保证了同一时刻只能有一个runnable 在执行
		 */
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		
		//the section one:immeditiliy to executor a task
		Future<String> future = scheduledExecutorService.submit(new CallableTask());
		System.out.println(future.get());
		//delay to executor task
		ScheduledFuture<String> scheduledFuture = scheduledExecutorService.schedule(new CallableTask(),3,TimeUnit.SECONDS);
		String result = scheduledFuture.get();
		//at fixed rate to executor task
		System.out.println("after 3s the result:"+result);
//		scheduledExecutorService.scheduleAtFixedRate(new RunnableTask(), 0, 3,TimeUnit.SECONDS);
		
		CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<String>();
	}
}

class CallableTask implements Callable<String>{

	@Override
	public String call() throws Exception {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			Log.error("", e);
		}
		return "7roaddba";
	}
}

class RunnableTask implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			Log.error("", e);
		}
		System.out.println("scheduleAtFixedRate:runnable is runing...");
	}
}