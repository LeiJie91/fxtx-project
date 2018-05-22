package com.fxtx.cloud.utils.excutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author wuchunjie
 * @date 2017/8/31
 */
public class FutureTask1 extends FutureTask<Map>{
    public FutureTask1(Callable<Map> callable) {
        super(callable);
    }

    public FutureTask1(Runnable runnable, Map result) {
        super(runnable, result);
    }
}
