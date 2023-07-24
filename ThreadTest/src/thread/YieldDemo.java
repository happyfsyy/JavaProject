package thread;

public class YieldDemo {

    private static Object sLockObject=new Object();
    /**
     * 使用wait和notify测试等待机制的实现
     * 执行了wait之后，就会进入到和该对象相关的等待池中。
     * 当调用notify或者notifyAll之后，可以唤醒当前等待池中的线程。
     * wait，notify，notifyAll必须放在synchronize block中，否则会抛出异常
     *
     */
    static class WaitThread extends Thread{
        @Override
        public void run() {
            try {
                synchronized (sLockObject) {
                    Thread.sleep(3000);
                    sLockObject.notifyAll();
                }
            }catch (Exception e) {

            }
        }
    }
    static void waitAndNotifyAll() {
        System.out.println("主线程运行");
        //创建并启动子线程
        Thread thread=new WaitThread();
        thread.start();
        long startTime=System.currentTimeMillis();
        try {
            System.out.println("try块中");
            synchronized (sLockObject) {
                System.out.println("主线程等待");
                sLockObject.wait();
            }
        }catch (Exception e) {

        }
        long timeMs=(System.currentTimeMillis()-startTime);
        System.out.println("主线程继续->等待耗时："+timeMs+"ms");
    }


    /**
     * 测试join的作用，等待目标线程执行完成之后再继续执行。
     * 阻塞当前调用join函数时所在的线程，直到接收线程执行完毕之后再继续.
     */
    static class Worker extends Thread{
        public Worker(String name) {
            super(name);
        }
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("work in "+getName());
        }
    }
    static void joinDemo() {
        Worker worker1=new Worker("work-1");
        Worker worker2=new Worker("work-2");
        worker1.start();
        System.out.println("启动线程1");
        try {
            //调用worker1的join函数，主线程会阻塞直到worker1执行完成
            worker1.join();
            System.out.println("启动线程2");
            //启动线程2，调用线程2的join函数，主线程会阻塞直到worker2执行完成
            worker2.start();
            worker2.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程继续运行");
    }





    /**
     * 测试yield的作用。线程礼让，让出执行权限，让其他线程得以优先执行，但其他线程能否优先执行是未知的。
     */
    static class YieldThread extends Thread{
        public YieldThread(String name) {
            super(name);
        }
        public synchronized void run() {
            for(int i=0;i<5;i++) {
                System.out.printf("%s,优先级为：%d -->i=%d\n", getName(),getPriority(),i);
                if(i==2) {
                    Thread.yield();
                }
            }
        }
    }
    static void yieldDemo() {
        YieldThread t1=new YieldThread("thread-1");
        YieldThread t2=new YieldThread("thread-2");
        t1.start();
        t2.start();
    }



    public static void main(String[] args) {
//		waitAndNotifyAll();
//		joinDemo();
        yieldDemo();
    }

}
