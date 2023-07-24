package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Runnable的run()函数不能将结果返回给客户程序。Runnable既可以用在Thread中，也可以用在线程池中。
 * Callable与Runnable的功能大致相似，不同的是Callable有一个泛型接口，该接口中有一个返回值为V(泛型参数）的call函数。
 * Runnable和Callable更像脱缰的野马，无法控制。Future更易于管理，制定了可管理的任务标准，就是cancel,isDone,get,set这个几个方法。
 * Future只是定义了一些规范的接口，而FutureTask是它的实现类。
 * Callable、Future、FutureTask都只能用在线程池中。
 * FutureTask既是Future、Runnable，又是包装了Callable，是两者的结合体。
 */
public class FutureDemo {
    static ExecutorService mExecutor=Executors.newSingleThreadExecutor();

    //效率低下的斐波那契数列，耗时的操作
    private static int fib(int num) {
        if(num==0)
            return 0;
        if(num==1)
            return 1;
        return fib(num-1)+fib(num-2);
    }
    //向线程池中提交Runnable对象
    private static void futureWithRunnable() throws InterruptedException,ExecutionException {
        Future<?> resultFuture=mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                fib(40);
            }
        });
        //Future中的get()函数会获取结果，如果任务未完成，会阻塞等待，直到完成。
        //提交了Runnable，在run()函数中直接计算，该函数无返回值，因此get()得到的值为null
        System.out.println("使用Runnable提交的Future的结果是："+resultFuture.get());
    }

    //向线程池中提交Callable对象，有返回值，Future中就可以获得返回值
    private static void futureWithCallabe() throws InterruptedException,ExecutionException{
        Future<Integer> future2=mExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception{
                return fib(40);
            }
        });
        //get()方法的英文解释：如有必要，就等待计算完成，查询它的结果
        System.out.println("使用Callable提交的Future的结果是："+future2.get());
    }


    //向线程中提交FutureTask对象
    private static void futureTask() throws InterruptedException,ExecutionException{
        FutureTask<Integer> futureTask=new FutureTask<Integer>(
                new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return fib(40);
                    }
                });
        mExecutor.submit(futureTask);
        //通过get()函数得到执行结果，如果线程体没有执行完成，主线程会一直阻塞等待，执行完则直接返回结果。
        System.out.println("使用FutureTask提交的Future的结果是："+futureTask.get());
    }



    public static void main(String[] args) {
        try {
            futureWithRunnable();
            futureWithCallabe();
            futureTask();
        }catch (Exception e) {

        }
    }


}
