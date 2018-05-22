package com.fxtx.cloud.utils;

/**
 * Created by Administrator on 2015/9/21.
 * Project  :风行学院
 * Copyright: 2012-2030 shinsoft Inc. All rights reserved.
 */
public class CnUpperCaserUtils {
    public static String convert(int d) {
        String[] str = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String ss[] = new String[] { "", "十", "百", "千", "万", "十", "百", "千", "亿" };
        String s = String.valueOf(d);
        System.out.println(s);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        String sss = String.valueOf(sb);
        int i = 0;
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, ss[i++]);
        }
        return sb.toString();
    }
}
