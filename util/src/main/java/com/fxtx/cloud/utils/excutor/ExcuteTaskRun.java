package com.fxtx.cloud.utils.excutor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wuchunjie
 * @date 2017/8/30
 */
public class ExcuteTaskRun implements Runnable {

    private String taskName;
    private Map<String,Object> taskMap;

    public ExcuteTaskRun(String taskName) {
        this.taskName = taskName;
        this.taskMap = taskMap;
    }

    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 1000));// 1000毫秒以内的随机数，模拟业务逻辑处理
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------这里执行业务逻辑，Runnable TaskName = " + taskName + "-------------");
    }
}
