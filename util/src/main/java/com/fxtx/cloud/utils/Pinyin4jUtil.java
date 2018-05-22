package com.fxtx.cloud.utils;

import cn.jpush.api.utils.StringUtils;
import com.fxtx.framework.util.LogUtils;
import com.google.common.collect.Lists;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/25.
 * Project  :cloud-zspfsc
 * Copyright: 2012-2030 bjfxtx Inc. All rights reserved.
 */
public class Pinyin4jUtil {
    /**
     * 将汉字转换为全拼
     *
     * @param src
     * @return String
     */
    public static String getPinYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        // System.out.println(t1.length);
        String[] t2 = new String[t1.length];
        // System.out.println(t2.length);
        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断能否为汉字字符
                // System.out.println(t1[i]);
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                        t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            LogUtils.logError("",e);
        }
        return t4;
    }

    /**
     * 提取每个汉字的首字母
     *
     * @param str
     * @return String
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }
    /**
     * 提取每个汉字的首字母
     *
     * @param str
     * @return String
     */
    public static String getPinYinFirstHeadChar(String str) {
        String chars = getPinYinHeadChar(str);
        if(StringUtils.isNotEmpty(chars)){
            return chars.substring(0,1);
        }
        return null;
    }

    /**
     * 将字符串转换成ASCII码
     *
     * @param cnStr
     * @return String
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        // 将字符串转换成字节序列
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // System.out.println(Integer.toHexString(bGBK[i] & 0xff));
            // 将每个字符转换成ASCII码
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    /**
     *
     * @param str
     * @return
     */
    public static String firstLetter(String str){
        //首先判断字符串是否为汉子或者字母，如果是则获取首字母，如果不是证明是其他的艺术字或者数字或者其他的，则不进行解析
        if(FirstCharTypeUtils.isLetter(str.charAt(0)) || FirstCharTypeUtils.isChinese(str.toCharArray()[0])){
            String letter = Pinyin4jUtil.getPinYinFirstHeadChar(str).toUpperCase();
            return letter;
        }else{
            return "#";
        }
    }

    /**
     * 转化List<Map>格式
     * @param data
     * @return
     */
    public static List<Map<String, Object>> convert(List<Map<String, Object>> data){
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for(Map<String, Object> one: data){
            String key = (String) one.get("firstLetter");
            if(result.containsKey(key)){
                List<Object> list = (List<Object>) result.get(key);
                list.add(one);
            }else{
                List<Object> list = Lists.newArrayList();
                list.add(one);
                result.put(key, list);
            }
        }

        if(result.containsKey("#")){
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
