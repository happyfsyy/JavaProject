package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 当我们需要频繁地创建多个线程进行耗时操作时，每次都通过new Thread实现并不是一种好的方式，
 * 每次new Thread新建和销毁对象的性能较差，线程缺乏统一管理，可能无限制地创建线程，相互之间竞争，
 * 可能占用过多系统资源导致死锁，并且缺乏定时执行、定期执行、线程中断等功能。
 * 线程池可以有效地管理、调度线程，避免过多的资源消耗。可以重用存在的线程，减少对象创建、销毁的开销；
 * 可以有效地控制最大并发线程数，提高系统资源的使用率，同时避免过多的资源竞争，避免堵塞；提供定时执行、定期执行、单线程、并发数控制等功能。
 */
public class ExecutorDemo {
    /**
     * 线程池都实现了ExecutorService接口，该接口定义了线程池需要实现的接口，如submit，execute，shutdown等。
     * ExecutorService的生命周期包括3种状态：运行、关闭、终止。创建后便进入运行状态，调用shutdown()之后，便渐入关闭状态，
     * 此时意味着ExecutorServie不再接受新的任务，但它还在执行已经提交了的任务。当所有已经提交了的任务都执行完后，就变成终止状态。
     */

    private static int fib(int num) {
        if(num==0)
            return 0;
        if(num==1)
            return 1;
        return fib(num-1)+fib(num-2);
    }

    //启动固定数量的线程池，执行结果是3个线程交替执行。
    private static void fixedThreadPoll(int size) throws CancellationException,ExecutionException,InterruptedException{
        ExecutorService executorService=Executors.newFixedThreadPool(3);
        for(int i=0;i<10;i++) {
            Future<Integer> task=executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception{
                    System.out.println("执行线程："+Thread.currentThread().getName());
                    return fib(40);
                }
            });
            System.out.println("第"+i+"次计算，结果是："+task.get());

        }
    }


    //如果新来一个任务，并且没有空闲线程可用，因此必须马上创建一个线程来立即执行任务。
    //可以通过Executors.newCachedThreadPoll函数来实现。
    //执行结果是为每一任务都创建了一个线程。
    private static void newCachedThreadPoll() throws CancellationException,ExecutionException,InterruptedException{
        ExecutorService executorService=Executors.newCachedThreadPool();
        for(int i=0;i<10;i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("执行线程："+Thread.currentThread().getName()+",结果为："+fib(40));
                }
            });
        }
    }

    //创建定时执行的线程池，第二个参数为第一次延迟的时间，第三个参数是执行周期
    //如果是三个线程，单个任务，就是每隔一段时间，从3个线程中抽取一个来执行任务，大多数情况都是第一个线程。
    //如果是三个线程，两个任务，每隔一段时间，从3个线程中抽取2个来执行任务，这就看谁空闲了。有可能是12，也有可能是13，也有可能是23.
    //如果是单个线程，那就是单个线程周期性地执行任务。
    private static void scheduledThreadPoll() throws CancellationException,ExecutionException,InterruptedException{
//		ScheduledExecutorService executorService=Executors.newScheduledThreadPool(3);
        ScheduledExecutorService executorService=Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("第一个任务 Thread :"+Thread.currentThread().getName()+", 开始计算：");
                System.out.println("结果是："+fib(30));
            }
        }, 1, 2, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("第二个任务 Thread :"+Thread.currentThread().getName()+", 开始计算：");
                System.out.println("结果是："+fib(40));
            }
        }, 1, 2, TimeUnit.SECONDS);
    }


    public static void main(String[] args) {
        try {
//			fixedThreadPoll(3);
//			newCachedThreadPoll();
            scheduledThreadPoll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
