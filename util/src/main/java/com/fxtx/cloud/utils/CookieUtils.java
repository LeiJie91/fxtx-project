package com.fxtx.cloud.utils;

import com.fxtx.framework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CookieUtils {
	
	public static void setCookieValue(HttpServletResponse response,String cookieName,String cookieValue){
		setCookieValue(response,cookieName,cookieValue,7*60*60*24);
	}

	public static void setCookieValue(HttpServletResponse response,String cookieName,String cookieValue,int maxAge){
        try {
            if(cookieValue != null){
                cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Cookie c = new Cookie(cookieName,cookieValue);
		c.setPath("/");
 		c.setMaxAge(maxAge);//7*24小时过期
		response.addCookie(c);
	}

	
	public static String getCookieValue(HttpServletRequest request,String cookieName){
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return "";
		
		for(Cookie c : cookies){
			if(c.getName().equalsIgnoreCase(cookieName)){
                String cookieValue = c.getValue();
                try {
                    cookieValue = URLEncoder.encode(cookieValue,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return cookieValue;
			}
		}
		return "";
	}
	public static Long getCookieLongValue(HttpServletRequest request,String cookieName){
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;

		for(Cookie c : cookies){
			if(c.getName().equalsIgnoreCase(cookieName)){
                String cookieValue = c.getValue();
                if(StringUtils.isNotEmpty(cookieValue) && StringUtils.isNumeric(cookieValue)){
                    return new Long(cookieValue);
                }
			}
		}
		return null;
	}
	public static void clearCookies(HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
            for(Cookie c : cookies){
                c.setMaxAge(0);
                c.setValue(null);
                response.addCookie(c);
            }
        }
	}

}
