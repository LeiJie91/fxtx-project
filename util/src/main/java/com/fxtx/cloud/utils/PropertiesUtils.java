/*
 * @(#) PropertiesUtil.java    2014-8-22
 * Project  :上海鑫磊信息技术有限公司
 * Copyright: 2016 shinsoft Inc. All rights reserved.
 */
package com.fxtx.cloud.utils;

import com.fxtx.framework.util.LogUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * TODO class purpose description
 * 
 * @author weidong
 * @version 1.0 Revise History:
 * 
 */
public class PropertiesUtils {

    public static Map<String,String> getProperties(String filePath){
        Map<String,String> maps= null;
        InputStream inputStream = PropertiesUtils.class.getResourceAsStream(filePath);
        Properties prop = null;
        if(inputStream != null){
            try {
                prop = new Properties();
                prop.load(inputStream);
                maps = new HashMap<String,String>();
                Iterator it=prop.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry entry=(Map.Entry)it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if(key!=null&&value!=null){
                        maps.put(key.toString(),value.toString());
                    }
                }
            } catch (IOException e) {
                LogUtils.logError("资源文件读取失败", e);
            }
        }
        return maps;

    }

    /**
     * 读属性文件
     * 
     * @param file
     * @return
     */
    public static Properties loadProperties(File file) {
        Properties prop = null;
        if (file.exists()) {
            prop = new Properties();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                prop.load(fis);
            } catch (IOException e) {
                LogUtils.logError("资源加载失败", e);
            }
            if(fis!=null){
                
            }
        }
        return prop;
    }

    /**
     * 获取属性
     * 
     * @param properties
     * @param key
     * @return
     */
    public static String getProperties(Properties properties, String key) {
        if (properties != null && properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return null;
    }

    /**
     * 设置属性
     * 
     * @param filePath
     * @param key
     * @param value
     * @return
     */
    public static boolean setProperties(String filePath, String key, String value) {
        File file = new File(filePath);
        if (file.exists()) {
            Properties prop = loadProperties(file);
            // 文件输出流
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                // 将Properties集合保存到流中
                prop.store(fos, "Copyright (c) Boxcode Studio");
                fos.close();// 关闭流
            } catch (Exception e) {
                LogUtils.logError("资源加载失败", e);
            }

            return true;
        }
        return false;
    }
    
    /**
     * 设置属性
     * 
     * @param key
     * @param value
     * @return
     */
    public static boolean setProperties(File file, String key, String value) {
        if (file.exists()) {
            Properties prop = loadProperties(file);
            // 文件输出流
            FileOutputStream fos;
            try {
                prop.setProperty(key, value);
                fos = new FileOutputStream(file);
                // 将Properties集合保存到流中
                prop.store(fos, "Copyright (c) shinsoft");
                fos.close();// 关闭流
            } catch (Exception e) {
                LogUtils.logError("资源加载失败", e);
            }
            
            return true;
        }
        return false;
    }
}
