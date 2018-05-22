package com.fxtx.cloud.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/16.
 */
public class CharUtils {
    private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    private CharUtils(){

    }
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {

            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
