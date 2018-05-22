package com.fxtx.cloud.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

/**
 * 基础值的操作类，可单独使用
 * 
 * @author lindujia 2012-06-21
 */
public class ValueUtil {

    /**
     * 忽略大小写截取最后一个匹配字符串位置之前的字符串（不包括匹配部分）
     * 
     * @param str
     * @param lastCheckStr
     * @return 字符串
     */
    public static String strBefore(String str, String lastCheckStr) {
        String border = "JIANGEFUHAONLDJ";
        return null == str || null == lastCheckStr ? "" : replaceLast(str, "(?i)" + lastCheckStr.replaceAll(" +", " +"), border).replaceFirst(border + ".*?$", "");
    }

    /**
     * 忽略大小写截取首个匹配字符串位置之后的字符串（不包括匹配部分）
     * 
     * @param str
     * @param firstCheckStr
     * @return 字符串
     */
    public static String strAfter(String str, String firstCheckStr) {
        return null == str || null == firstCheckStr ? "" : str.replaceFirst("^.*?(?i)" + firstCheckStr.replaceAll(" +", " +"), "");
    }

    /**
     * 获取后缀名
     * 
     * @param str
     * @param lastCheckStr
     * @return
     */
    public static String strLast(String str, String lastCheckStr) {
        // TODO 有问题，需要修改
        return str.replaceAll(strBefore(str, lastCheckStr) + lastCheckStr, "");
    }

    /**
     * 上传的文件重命名 sub：是文件名前面识别文字 filename： 文件的名称
     */
    public static String strLastPoint(String fileName) {
        return null == fileName ? null : fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 去除字符串指定前缀
     * 
     * @param Str
     *            需要处理的字符串
     * @param beginRegex
     *            要去除的前缀(可以是正则表达式)
     * @return 字符串
     */
    public static String removeBeginStr(String str, String beginRegex) {
        return (null == str || null == beginRegex) ? "" : str.replaceFirst("^(" + beginRegex + ")+", "");
    }

    /**
     * 替换最后一个匹配的字符串，支持正则
     * 
     * @param text
     *            需要处理的字符串
     * @param regex
     *            要替换的字符串（可以是正则表达式）
     * @param replacement
     *            替换成的字符串
     * @return
     */
    public static String replaceLast(String str, String lastRegex, String replacement) {
        return str.replaceFirst("(?s)" + lastRegex + "(?!.*?" + lastRegex + ")", replacement);
    }

    /**
     * 根据传入的字符数组产生随机字符串
     * 
     * @param length
     *            生成的随机字符串长度
     * @param numbersAndLetters
     *            传入的字符串
     * @return 字符串
     */
    public static String randomString(int length, String numbersAndLetters) {
        if (!isEmpty(length) && !isEmpty(numbersAndLetters)) {
            char[] charArr = numbersAndLetters.toCharArray();
            Random randGen = new Random();
            char[] randBuffer = new char[length];
            for (int i = 0; i < randBuffer.length; i++) {
                randBuffer[i] = charArr[randGen.nextInt(charArr.length - 1)];
            }
            return new String(randBuffer);
        }
        return null;
    }

    /**
     * 根据长度产生由大小写字母以及数字组成的字符串
     * 
     * @param length
     *            生成的随机字符串长度
     * @return 字符串
     */
    public static String randomString(int length) {
        return randomString(length, "0123456789abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * 在字符串后面加上系统时间
     * 
     * @param str
     * @return
     */
    public static String strWithSysTime(String str) {
        return str + System.currentTimeMillis();
    }

    /**
     * 返回UUID字串
     * 
     * @return :32位字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 计算百分比
     * 
     * @param dividend
     *            被除数
     * @param divisor
     *            除数
     * @param bit
     *            保留小数点后面位数
     * @return
     */
    public static String percent(double dividend, double divisor, int bit) {
        double result = dividend / divisor;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(bit);
        return nf.format(result);
    }

    /**
     * 计算百分比保留小数点后2位
     * 
     * @param dividend
     *            被除数
     * @param divisor
     *            除数
     * @return
     */
    public static String percent(double dividend, double divisor) {
        return percent(dividend, divisor, 2);
    }

    /**
     * 根据指定编码转换字符串
     * 
     * @param str
     * @param EncodFormat
     * @return
     */
    public static String decode(String str, String encodFormat) {
        if (null == str || null == encodFormat) {
            return "";
        }
        try {
            return URLDecoder.decode(str, encodFormat);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    /**
     * 根据指定编码解读字符串
     * 
     * @param str
     * @param encodFormat
     * @return
     */
    public static String readByEncod(String str, String encodFormat) {
        if (null == str || null == encodFormat) {
            return "";
        }
        byte[] strArry = null;
        try {
            strArry = str.getBytes(encodFormat);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return str;
        }
        return new String(strArry);
    }

    /**
     * 上传的文件重命名 sub：是文件名前面识别文字 filename： 文件的名称
     */
    public static String renameFileName(String firstName, String fileName) {
        return null == fileName ? firstName + "" : fileName.lastIndexOf(".") == -1 ? firstName + uuid() : firstName + uuid() + fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 检测字符串是否包含另一个字符串
     * 
     * @param str
     *            检测的字符串
     * @param containRegex
     *            包含的字符串（支持正则）
     * @return 返回检查结果
     */
    public static boolean contentContain(String str, String containRegex) {
        return null == str || null == containRegex ? false : str.replaceAll(containRegex, "").length() < str.length();
    }

    /**
     * 忽略大小写检测字符串是否包含另一个字符串
     * 
     * @param str
     * @param containRegex
     * @return
     */
    public static boolean contentContainIgnoreCase(String str, String containRegex) {
        return contentContain(str, "(?i)" + containRegex);
    }

    /**
     * 拆分中文格式的日期，然后重新组合成阿拉伯数值格式的日期
     * 
     * @param cDate
     * @return
     */
    public static String cDateNumToADateNum(String cDate) {
        String[] cDateArray = cDate.split("[^○一二三四五六七八九十]+");
        String[] otherArray = cDate.split("[○一二三四五六七八九十]+");
        String outADateNum = "";
        for (int i = 0; i < cDateArray.length; i++) {
            outADateNum += dateDNumToANum(cDateArray[i]) + (i + 1 < otherArray.length ? otherArray[i + 1] : "");
        }
        return outADateNum;
    }

    /**
     * 中文数值日期转换成 阿拉伯数值日期
     * 
     * @param cNum
     * @return
     */
    public static String dateDNumToANum(String cNum) {
        if (cNum.length() < 4) {
            return cNumToANum(cNum).split("\\.")[0];
        }
        String[] chinaNum = { "○", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] arabNum = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        for (int i = 0; i < chinaNum.length; i++) {
            cNum = cNum.replaceAll(chinaNum[i], arabNum[i]);
        }
        return cNum;
    }

    /**
     * 中文数值转换成阿拉伯数值
     * 
     * @param cNum
     * @return
     */
    public static String cNumToANum(String cNum) {
        String[] unit = { "毛", "厘", "分", "个", "十", "百", "千", "万", "亿" };
        String[] unitValue = { "0.001", "0.01", "0.1", "1", "10", "100", "1000", "10000", "100000000" };
        String[] chinaNum = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] arabNum = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        // 〇
        return cNumToANumByFormat(cNum, unit, unitValue, chinaNum, arabNum);
    }

    /**
     * 常用中文金额数值转阿拉伯数值
     * 
     * @param cNum
     * @return
     */
    public static String cMoneyToAMoney(String cNum) {
        String[] unit = { "厘", "分", "角", "元", "拾", "佰", "仟", "萬", "億" };
        String[] unitValue = { "0.001", "0.01", "0.1", "1", "10", "100", "1000", "10000", "100000000" };
        String[] chinaNum = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String[] arabNum = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        return cNumToANumByFormat(cNum, unit, unitValue, chinaNum, arabNum);
    }

    /**
     * 传入中文数值单位格式和数字格式，获取阿拉伯数值
     * 
     * @param cNum
     *            中文数值
     * @param unitFormat
     *            单位格式（跨度是千分到亿，既0.001~100000000）
     * @param numFormat
     *            数值格式（0~9中文表述法）
     * @return
     */
    public static String cNumToANumByFormat(String cNum, String[] unit, String[] unitValue, String[] chinaNum, String[] arabNum) {
        String[] unitFormulaValue = arrToArrAddFirstStr(unitValue, "*");
        String[] numValue = arrToArrAddFirstStr(arabNum, "+");
        StringBuffer unitStr = new StringBuffer();
        for (String string : unit) {
            unitStr.append(string);
        }
        for (String string : chinaNum) {
            unitStr.append(string);
        }
        cNum = cNum.replaceAll("[^" + unitStr + "]", "");
        for (int i = 0; i < unit.length; i++) {
            cNum = cNum.replaceAll(unit[i], unitFormulaValue[i]);
        }
        for (int i = 0; i < chinaNum.length; i++) {
            cNum = cNum.replaceAll(chinaNum[i], numValue[i]);
        }
        return calculateFormula(cNum.replaceFirst("^[*+]", ""));
    }

    /**
     * 把一个字符串数值加上前缀返回一个新的数组
     * 
     * @param arr
     *            数组
     * @param firstStr
     *            前缀字符串
     * @return 数组
     */
    public static String[] arrToArrAddFirstStr(String[] arr, String firstStr) {
        String[] temp = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            temp[i] = firstStr + arr[i];
        }
        return temp;
    }

    /**
     * 计数字符串公式结果
     * 
     * @param formula
     * @return
     */
    public static String calculateFormula(String formula) {
        // ScriptEngine jse = new
        // ScriptEngineManager().getEngineByName("JavaScript");
        try {
            // return formatDouble((Double)jse.eval(formula), "0.000");
            return "0.000";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 格式化Double显示模式
     * 
     * @param dobl
     * @param pn
     * @return
     */
    public static String formatDouble(Double dobl, String pn) {
        DecimalFormat df = new DecimalFormat(pn);
        return df.format(dobl);
    }

    /**
     * 字符串首字母大写
     * 
     * @param str
     * @return
     */
    public static String toUpperFirst(String str) {
        return isEmpty(str) ? str : str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
    }

    /**
     * 传入正则表达式，验证字符串是否符合正则表达式的规则
     * 
     * @param str
     * @param regex
     * @return
     */
    public static boolean checkWithDeelx(String str, String regex) {
        return isEmpty(str) || isEmpty(regex) ? false : str.replaceFirst(regex, "").length() == 0;
    }

    /**
     * 处理sql里的where后面的语句问题
     * 
     * @param sql
     * @return
     */
    public static String sqlCheck(String sql) {
        return null == sql ? sql : sql.replaceAll("[wW][hH][eE][rR][eE] +([aA][nN][dD]|[oO][rR]) +", "where ")
                .replaceAll("[wW][hH][eE][rR][eE] +[oO][rR][dD][eE][rR] +", " order ")
                .replaceAll("[wW][hH][eE][rR][eE] +[gG][rR][oO][uU][pP] +", " group ")
                .replaceAll("[wW][hH][eE][rR][eE] *$", "");
    }
    /**
     * 获取计算条数的sql
     * @param HQL
     * @return
     */
    public static String countSQL(String sql){
        StringBuffer outHQL = new StringBuffer("select COUNT(") ;
        outHQL.append(sql.replaceFirst(" *(?i)from.*$", "").replaceFirst("^ *(?i)select *", "")) ;
        outHQL.append(") from ");
        outHQL.append(ValueUtil.strBefore(ValueUtil.strAfter(sql, "from"), "order by")) ;
        return ValueUtil.sqlCheck(outHQL.toString());
    }
    /*********************************** 以下为内部调用方法 **********************************************/
    /**
     * 检查字符串
     * 
     * @param str
     * @return
     */
    private static boolean isEmpty(String str) {
        return null == str ? true : "".equals(str.replaceAll(" +", ""));
    }

    /**
     * 检测Integer
     * 
     * @param value
     * @return
     */
    private static boolean isEmpty(Integer value) {
        return null == value ? true : "".equals(value.toString().replaceAll("[ 0]+", ""));
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static Integer[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        Integer[] result = new Integer[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if( result[j] != null && num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }
}
