package com.fxtx.cloud.utils;

import com.fxtx.framework.util.IpUtils;
import com.fxtx.framework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Util {
    public static String spliceLink(String url,String domainUrl){
        return StringUtils.isBlank(url)?"":domainUrl + url;
    }

    public static String spliceLinkUrl(String url,String domainUrl){
        if (StringUtils.isNotBlank(url)){
            if(url.startsWith(domainUrl)){
                return url;
            }else{
                return domainUrl + url;
            }
        }
        return StringUtils.isBlank(url)?"":domainUrl + url;
    }

    public static String replaceLetter(String value){
        String first = value.substring(0,1);
        String second = value.substring(value.length()-1);
        return first+"****"+second;
    }

    public static String getIp(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return IpUtils.getIpAddr(request);
    }
}
