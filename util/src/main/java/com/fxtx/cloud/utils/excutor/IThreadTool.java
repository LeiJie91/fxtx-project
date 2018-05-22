package com.fxtx.cloud.utils.excutor;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author wuchunjie
 * @date 2017/8/31
 */
public interface IThreadTool {

    /**
     * 本List必须要添加一个同步的锁功能
     * @Author wugong
     * @Date 2017/8/31 11:04
     * @Modify if true,please enter your name or update time
     * @param
     */
    ConcurrentLinkedDeque<Map<String, Object>> concurrentLinkedDeques = new ConcurrentLinkedDeque<Map<String,Object>>();

}
