package com.fxtx.cloud.operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingtianlun on 2014/12/1.
 * 此类封装一些常用的操作
 */
public class Operation {

    /**
     * 字符串IDs转List<Long>
     * @param str
     * @param pattern
     * @return
     */
    public static List<Long> strIdsToList(String str, String pattern){
        List<Long> ids = new ArrayList<Long>();
        try{
            String[] strs = str.split(pattern);
            for(String string: strs){
                ids.add(Long.valueOf(string));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ids;
    }

}
