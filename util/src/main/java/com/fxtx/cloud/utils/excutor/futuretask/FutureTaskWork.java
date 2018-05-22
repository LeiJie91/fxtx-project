package com.fxtx.cloud.utils.excutor.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author wuchunjie
 * @date 2017/8/31
 */
public class FutureTaskWork extends FutureTask<ExcuteResultTask>{

    public FutureTaskWork(Callable<ExcuteResultTask> callable) {
        super(callable);
    }

    public FutureTaskWork(Runnable runnable, ExcuteResultTask excuteResultTask) {
        super(runnable, excuteResultTask);
    }

    public Integer excuteTask(ExcuteResultTask excuteResultTask){
        return excuteResultTask.getExcuteCount();
    }

}
