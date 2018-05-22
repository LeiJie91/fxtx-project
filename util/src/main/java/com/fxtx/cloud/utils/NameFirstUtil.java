package com.fxtx.cloud.utils;

import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by z on 2017/5/25.
 */
public class NameFirstUtil {

    public static List<Map<String, Object>> convert(List<Map<String, Object>> data) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for (Map<String, Object> one : data) {
            String key = (String) one.get("firstLetter");
            if (result.containsKey(key)) {
                List<Object> list = (List<Object>) result.get(key);
                list.add(one);
            } else {
                List<Object> list = Lists.newArrayList();
                list.add(one);
                result.put(key, list);
            }
        }
        if (result.containsKey("#")) {
            Object object = result.get("#");
            result.remove("#");
            result.put("#", object);
        }
        List<Map<String, Object>> list = Lists.newArrayList();

        for (Map.Entry<String, Object> entry : result.entrySet()) {
            Map<String, Object> child = new LinkedHashMap<String, Object>();
            child.put("key", entry.getKey());
            child.put("list", entry.getValue());
            list.add(child);
        }
        return list;
    }
}
