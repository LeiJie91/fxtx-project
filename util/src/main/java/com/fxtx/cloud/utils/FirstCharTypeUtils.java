package com.fxtx.cloud.utils;

import cn.jpush.api.utils.StringUtils;

public class FirstCharTypeUtils {

    /**
     * 用ascii码
     *
     * @param chr
     * @return
     */
    public static boolean isNumeric(int chr) {
        if (chr < 48 || chr > 57) {
            return false;
        }
        return true;
    }

    /**
     * 判断一个字符串的首字符是否为字母
     *
     * @param chr
     * @return
     */
    public static boolean isLetter(int chr) {
        if ((chr >= 65 && chr <= 90) || (chr >= 97 && chr <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 获取字符首字母简称
     *
     * @param str
     * @return
     */
    public static String firstLetter(String str) {
        if (StringUtils.isNotEmpty(str) && (isLetter(str.charAt(0)) || isChinese(str.toCharArray()[0]))) {
            String letter = Pinyin4jUtil.getPinYinFirstHeadChar(str).toUpperCase();
            return letter;
        } else {
            return "#";
        }
    }

    /**
     * 获取字符串头部字母
     *
     * @param str
     * @return
     */
    public static String strHeadletter(String str) {
        if (StringUtils.isNotEmpty(str)) {
            String letter = Pinyin4jUtil.getPinYinHeadChar(str);
            return letter;
        } else {
            return "";
        }
    }

}
