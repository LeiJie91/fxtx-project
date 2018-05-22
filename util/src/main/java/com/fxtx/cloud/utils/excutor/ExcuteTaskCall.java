package com.fxtx.cloud.utils.excutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author wuchunjie
 * @date 2017/8/30
 */
public class ExcuteTaskCall implements Callable<String> {

    private String taskName;
    private Map<String,Object> taskMap = null;

    public ExcuteTaskCall(String taskName, Map<String,Object> taskMap) {
        this.taskName = taskName;
        this.taskMap = taskMap;
    }

    @Override
    public String call() throws Exception {
        try {
//              Java 6/7最佳的休眠方法为TimeUnit.MILLISECONDS.sleep(100);
//              最好不要用 Thread.sleep(100);
            TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 1000));// 1000毫秒以内的随机数，模拟业务逻辑处理
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------这里执行业务逻辑，Callable TaskName = " + taskName + "-------------");
        return ">>>>>>>>>>>>>线程返回值，Callable TaskName = " + taskName + "<<<<<<<<<<<<<<";
    }
}
