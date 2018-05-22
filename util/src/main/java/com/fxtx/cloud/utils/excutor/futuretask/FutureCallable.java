package com.fxtx.cloud.utils.excutor.futuretask;

import java.util.concurrent.Callable;

/**
 * @author wuchunjie
 * @date 2017/9/6
 */
public class FutureCallable implements Callable<Integer>{
    private Integer excuteCount = 1;

    public Integer call() throws Exception {
        return excuteCount;
    }
}
