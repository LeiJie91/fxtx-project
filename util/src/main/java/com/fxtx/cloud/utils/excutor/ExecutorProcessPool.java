package com.fxtx.cloud.utils.excutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * wugong
 * 2017年8月30日09:53:26
 */
public class ExecutorProcessPool{

    private ExecutorService executor;
    private static ExecutorProcessPool pool = new ExecutorProcessPool();
    private final int threadMax = 10;

    private ExecutorProcessPool() {
        executor = ExecutorServiceFactory.getInstance().createFixedThreadPool(threadMax);
    }

    public static ExecutorProcessPool getInstance() {
        return pool;
    }

    /**
     * 关闭线程池，这里要说明的是：调用关闭线程池方法后，线程池会执行完队列中的所有任务才退出
     *
     * @Author wugong
     * @Date 2017/8/30 9:02
     * @Modify if true,please enter your name or update time
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     *
     * @param task
     * @Author wugong
     * @Date 2017/8/30 9:02
     * @Modify if true,please enter your name or update time
     */
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     *
     * @param
     * @Author wugong
     * @Date 2017/8/30 9:03
     * @Modify if true,please enter your name or update time
     */
    public Future<?> submitCallableTask(Callable<?> task) {
        System.out.println("当前:"+Thread.currentThread().getName());
        return executor.submit(task);
    }

    /**
     * 直接提交任务到线程池，无返回值
     *
     * @param
     * @Author wugong
     * @Date 2017/8/30 9:03
     * @Modify if true,please enter your name or update time
     */
    public void executeRunableTask(Runnable task) {
        executor.execute(task);
    }

    public void excuteFutureTask(Callable callable){
        //进行异步任务列表
        List<FutureTask<Integer>> futureTasks = new ArrayList<FutureTask<Integer>>();
        //线程池 初始化十个线程 和JDBC连接池是一个意思 实现重用
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //类似与run方法的实现 Callable是一个接口，在call中手写逻辑代码

        // 如果队列中是空，在而直接返回，不进行线程池
        //创建一个异步任务
        FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);
        futureTasks.add(futureTask);
        //提交异步任务到线程池，让线程池管理任务
        //由于是异步并行任务，所以这里并不会阻塞
        executor.submit(futureTask);

        int count = 0;
        // 任务池任务队列
        for (FutureTask<Integer> nowFutureTask : futureTasks) {
            //futureTask.get() 得到我们想要的结果
            //该方法有一个重载get(long timeout, TimeUnit unit) 第一个参数为最大等待时间，第二个为时间的单位
            try {
                count += nowFutureTask.get(2,TimeUnit.SECONDS);
                // 抓到任何的异常 都需要将这个值赋值
            } catch (InterruptedException e) {
                count += -1;
//                e.printStackTrace();
            } catch (ExecutionException e) {
                count += -1;
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("我不等了,你超时啦:"+Thread.currentThread().getName());
                count += -1;
//                e.printStackTrace();
            }
        }
        //清理线程池:此处不要清理线程池，需要一直在运行中
//        executor.shutdown();
    }

    public void excuteSingleFutureTask(Callable callable){
        //进行异步任务列表
        List<FutureTask<Integer>> futureTasks = new ArrayList<FutureTask<Integer>>();
        //线程池 初始化十个线程 和JDBC连接池是一个意思 实现重用
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //类似与run方法的实现 Callable是一个接口，在call中手写逻辑代码

        // 如果队列中是空，在而直接返回，不进行线程池
        //创建一个异步任务
        FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);
        futureTasks.add(futureTask);
        //提交异步任务到线程池，让线程池管理任务
        //由于是异步并行任务，所以这里并不会阻塞
        executor.submit(futureTask);

        int count = 0;
        // 任务池任务队列
        //futureTask.get() 得到我们想要的结果
        //该方法有一个重载get(long timeout, TimeUnit unit) 第一个参数为最大等待时间，第二个为时间的单位
        try {
            count += futureTask.get(2,TimeUnit.SECONDS);
            // 抓到任何的异常 都需要将这个值赋值
        } catch (InterruptedException e) {
            count += -1;
//                e.printStackTrace();
        } catch (ExecutionException e) {
            count += -1;
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("我不等了,你超时啦:"+Thread.currentThread().getName());
            count += -1;
//                e.printStackTrace();
        }
        //清理线程池:此处不要清理线程池，需要一直在运行中
//        executor.shutdown();
    }

}
