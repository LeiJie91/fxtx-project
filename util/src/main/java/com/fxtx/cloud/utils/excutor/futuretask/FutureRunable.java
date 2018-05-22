package com.fxtx.cloud.utils.excutor.futuretask;

/**
 * @author wuchunjie
 * @date 2017/9/6
 */
public class FutureRunable implements Runnable{

    private Integer runCount;

    public void run() {
        System.out.println("当前执行时间戳:"+System.currentTimeMillis());
    }
}
