package com.fxtx.cloud.utils;

import com.fxtx.framework.util.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/8/12.
 * Project  :zed
 * Copyright: 2016 shinsoft Inc. All rights reserved.
 */
public class WebUtils {
    private static final Pattern urlPattern = Pattern.compile("(http|ftp|https):\\/\\/([\\w.]+\\/?)\\S*");
    private WebUtils(){

    }

    public static boolean isAjax(HttpServletRequest request){
        String requestType = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestType)){
            return true;
        }
        return false;
    }

    public static void sendJson(HttpServletResponse response,String content){

    }

    /**
     * 获取完整链接
     *
     * @param prefix
     * @param path
     * @return
     */
    public static String getFullUrl(String prefix,String path){
        if(!isHttp(path)){
            if(!prefix.endsWith("/")){
                prefix = prefix+"/";
            }
            if(path.startsWith("/")){
                path = path.substring(1);
            }
            return prefix+path;
        }
        return path;
    }

    /**
     * 判断是否是完整的url请求
     *
     * @param url
     * @return
     */
    public static boolean  isHttp(String url){
        if(StringUtils.isNotEmpty(url)){
            Matcher matcher2 = urlPattern.matcher(url);
            return matcher2.matches();
        }
        return false;
    }
    public static String getIp(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return IpUtils.getIpAddr(request);
    }

}
