package thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 测试ScheduleExecutorService的scheduleAtFixedRate()方法
 * 如果上次任务的执行时间超过了间隔时间，那么并不会把上次任务截断，而是会接着继续直接执行下次任务。
 * 第1次任务耗时2秒，但是依旧等他执行完成，再执行下次任务。
 * 第2次任务耗时1ms，那么就继续等到1秒之后，再执行下次任务。
 *
 */
public class ScheduleDemo {
    private static int j=0;
    public static void main(String[] args){
        ScheduledExecutorService mScheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    System.out.println(i);
                    if(j==0){
                        try{
                            Thread.sleep(200);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(i==9){
                            j++;
                        }
                    }
                }
            }
        },0,1, TimeUnit.SECONDS);

    }
}
