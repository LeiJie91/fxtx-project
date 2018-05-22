package com.fxtx.cloud.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fxtx.framework.util.LogUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nldjyhl on 2014/12/8.
 */
public class FxtxJSONUtils {
    /**
     * 字符串转JAVA对象
     * @param strJson
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> E strToObj(String strJson,Class<E> clazz){
        try {
           return JSONObject.toJavaObject(JSON.parseObject(strJson), clazz);
        }catch (Exception e){
            return null ;
        }
    }

    /**
     * 字符串对象数组转List对象数组
     * @param strArrJson
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> List<E> strToListObj(String strArrJson,Class<E> clazz){
        strArrJson = strArrJson.replaceAll("^ *\\[","").replaceAll("\\] *$","") ;
        String[] jsonStrArr = strArrJson.split(",") ;
        if(null!=jsonStrArr && strArrJson.length()>0){
            List<E> outList = new ArrayList<E>() ;
            for(String jsonStr:jsonStrArr){
                E parseOut = strToObj(jsonStr,clazz) ;
                if(null!=parseOut){
                    outList.add(parseOut) ;
                }
            }
            return outList ;
        }else {
            return null ;
        }
    }

    /**
     * 把数据写入Resopnse对象
     *
     * @param data
     * @param response
     */
    public static void addDataToResponse(Object data, HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(data);
        } catch (IOException e) {
            LogUtils.logError("", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }

    }

    /**
     * 把数据写入Resopnse对象
     *
     * @param data
     * @param response
     */
    public static void writeDataToResponse(Object data, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(data == null ? "" : data.toString());
        } catch (IOException e) {
            LogUtils.logError("",e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
