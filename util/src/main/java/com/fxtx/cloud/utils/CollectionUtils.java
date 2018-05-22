package com.fxtx.cloud.utils;

import com.fxtx.cloud.utils.date.DateUtil;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by nldjyhl on 2014/12/8.
 */
public class CollectionUtils {
    public static <O> boolean isEmpty(O obj){
        if(null == obj){
            return true ;
        }
        if(obj instanceof Collection){
            Collection set = (Collection)obj ;
            if(set.size()<=0){
                return true ;
            }
        }
        if(obj instanceof Map){
            Map map = (Map)obj ;
            if(map.size()<=0){
                return true ;
            }
        }
        if(obj instanceof Object[]){
            Object[] arr = (Object[])obj ;
            if(arr.length>0){
                return true ;
            }
        }
        return false ;
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static <E> Map<String, E> sortMapByKey(Map<String, E> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, E> sortMap = new TreeMap<String, E>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}
