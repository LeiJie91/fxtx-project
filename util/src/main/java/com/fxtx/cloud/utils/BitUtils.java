package com.fxtx.cloud.utils;

import com.fxtx.framework.util.LogUtils;

/**
 * Created by Administrator on 2017/9/26.
 */
public class BitUtils {

    private BitUtils(){

    }

    /**
     * 判断一个二进制字符串是否合法
     *
     * @param bitStr
     * @return
     */
    public static boolean valid(String bitStr){
        try{
            Integer.parseInt(bitStr,2);
            return true;
        }catch (NumberFormatException e){
            LogUtils.logError("",e);
            return false;
        }
    }
    /**
     * 获取十进制值
     *
     * @param bitStr
     * @return
     */
    public static int parseInt(String bitStr){
        try{
            return Integer.parseInt(bitStr,2);
        }catch (NumberFormatException e){
            LogUtils.logError("",e);
        }
        return 0;
    }
}
