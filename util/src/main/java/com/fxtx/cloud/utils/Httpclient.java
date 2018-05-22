package com.fxtx.cloud.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by Administrator on 2014/10/22.
 */
@Component
public class Httpclient {
    public String doPost(String urlStr , Map<String, String> parameter)   {
        Set<String> parameterList = new HashSet<String>() ;
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            parameterList.add(entry.getKey()+"="+entry.getValue());
        }
        String parameterStr = StringUtils.join(parameterList , "&") ;
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder sTotalString = null;
        BufferedReader l_reader = null;
        if (url==null){
            return null ;
        }
        try {
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
            out.write(parameterStr); // 向页面传递数据。post的关键所在！
            out.flush();
            out.close();
            String sCurrentLine;
            sTotalString = new StringBuilder();
            l_reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            while ((sCurrentLine = l_reader.readLine()) != null) {
                sTotalString.append(sCurrentLine);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (l_reader!=null){
                try {
                    l_reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sTotalString==null){
            return null ;
        }

        return sTotalString.toString() ;
    }
    public String doGet(String urlStr , Map<String, String> parameter)   {
        if (parameter!=null) {
            Set<String> parameterList = new HashSet<String>();
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                parameterList.add(entry.getKey() + "=" + entry.getValue());
            }
            String parameterStr = StringUtils.join(parameterList, "&");
            urlStr = urlStr + "?" + parameterStr;
        }
        URL url = null;
        BufferedReader l_reader = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection connection = null;
        String sCurrentLine;
        StringBuilder sTotalString = null;
        if (url==null){
            return null ;
        }
        try {
            connection = url.openConnection();
            sTotalString = new StringBuilder();
            l_reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));


            while ((sCurrentLine = l_reader.readLine()) != null) {
                sTotalString.append(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (l_reader!=null){
                try {
                    l_reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sTotalString==null){
            return null ;
        }
        return sTotalString.toString();
    }


}
