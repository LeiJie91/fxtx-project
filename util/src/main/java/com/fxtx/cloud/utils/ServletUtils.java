package com.fxtx.cloud.utils;

import com.fxtx.framework.util.LogUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/28.
 * Project  :风行学院
 * Copyright: 2012-2030 shinsoft Inc. All rights reserved.
 */
public class ServletUtils {

    /**
     * 获取HttpRequest对象
     *
     * @return
     */
    public static HttpServletRequest getServletRequest(){
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if(servletRequestAttributes!=null){
            HttpServletRequest request =servletRequestAttributes.getRequest();
            return request;
        }
        return null;
    }

    /**
     * 获取HttpRequest对象
     *
     * @return
     */
    public static HttpServletResponse getServletResponse(){
        ServletWebRequest servletWebRequest = ((ServletWebRequest)RequestContextHolder.getRequestAttributes());
        if(servletWebRequest!=null){
            HttpServletResponse response = servletWebRequest.getResponse();
            return response;
        }
        return null;
    }

    /**
     * 获取session对象
     *
     * @return
     */
    public static HttpSession getSession(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession();
    }

    /**
     * 获取请求中的参数
     *
     * @param request
     * @return
     */
    public static Map<String,String> getRequestParams(HttpServletRequest request){
        return getRequestParams(request,true);
    }
    /**
     * 获取请求中的参数
     *
     * @param request
     * @return
     */
    public static Map<String,String> getRequestParams(HttpServletRequest request,boolean transEncoding){
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";
            }
            if(transEncoding){
                try{
                    valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
                }catch(Exception e){
                    LogUtils.logError("ServletUtils",e);
                }
            }

            params.put(name, valueStr);
        }
        return params;
    }
}
