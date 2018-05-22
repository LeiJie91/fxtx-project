package com.fxtx.cloud.utils;

import com.fxtx.framework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * TODO class purpose description
 *
 * @author zhangweidong
 * @version 1.0 Revise History:
 */
public class NumberUtils {

    /**
     * 把String数据转换成Long类型数组
     *
     * @param array
     * @return
     */
    public static Long[] convertStringArrayToLong(String[] array) {
        List<Long> list = new ArrayList<Long>();
        for (String num : array) {
            if (num != null && !num.trim().equals("")) {
                list.add(Long.parseLong(num));
            }

        }
        return list.toArray(new Long[list.size()]);
    }

    public static Double getDouble(Double number){
        if(number == null){
            return 0d;
        }
        return number;
    }
    public static Integer getInteger(Integer number){
        if(number == null){
            return 0;
        }
        return number;
    }
    /**
     * 把字符串转换成Long类型数组
     *
     * @param str       字符串
     * @param splitChar 被分割字符
     * @return
     */
    public static List<Long> convertStringsToList(String str, char splitChar) {
        str = StringUtils.trimTrailingCharacter(str, splitChar);
        String[] array = str.split(splitChar + "");
        return convertStringArrayToList(array);
    }

    /**
     * 把String数据转换成List<Long>
     *
     * @param array
     * @return
     */
    public static List<Long> convertStringArrayToList(String[] array) {
        List<Long> list = new ArrayList<Long>();
        for (String num : array) {
            if (num != null && !num.trim().equals("")) {
                list.add(Long.parseLong(num));
            }

        }
        return list;
    }

    /**
     * 把字符串转换成List<Long>
     *
     * @param str       字符串
     * @param splitChar 被分割字符
     * @return
     */
    public static Long[] convertStringsToLongArray(String str, char splitChar) {
        str = StringUtils.trimTrailingCharacter(str, splitChar);
        String[] array = str.split(splitChar + "");
        return convertStringArrayToLong(array);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal result =  b1.multiply(b2);
        result = result.add(new BigDecimal(0.00001));
        return result.doubleValue();
    }
    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mulD(Double v1, Double v2) {
        return mul(getDouble(v1),getDouble(v2));
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(int v1, double v2) {
        BigDecimal b1 = new BigDecimal(Integer.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static int sub(int v1, int v2){
        return v1 - v2;
    }
    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double subD(Double v1, Double v2){
        return sub(getDouble(v1),getDouble(v2));
    }
    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static int subI(Integer v1, Integer v2){
        return sub(getInteger(v1),getInteger(v2));
    }
    /**
     * 提供精确的加法运算。
     *
     * @param v1 加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * 提供精确的加法运算。
     *
     * @param v1 加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double addD(Double v1, Double v2){
        return add(getDouble(v1),getDouble(v2));
    }

    /**
     * 提供精确的除法运算。
     *
     * @param v1 除
     * @param v2 被除数
     * @return 两个参数的积
     */
    public static double divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的除法运算。
     *
     * @param v1 除
     * @param v2 被除数
     * @return 两个参数的积
     */
    public static double divide(double v1, double v2,Integer scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale==null?2:scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的除法运算。
     *
     * @param v1 除
     * @param v2 被除数
     * @return 两个参数的积
     */
    public static double divideD(Double v1, Double v2){
        return divide(getDouble(v1),getDouble(v2));
    }
    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static int mul(BigDecimal v1, int v2) {
        BigDecimal b1 = new BigDecimal(v2);
        return v1.multiply(b1).intValue();
    }

    public static String numberFormat(double num, int degree) {
        StringBuffer format = new StringBuffer("#");
//        if(degree>0){
//            format.append(".");
//            for(int i=0;i<degree;i++);
//            format.append("#");
//        }
//        DecimalFormat df = new DecimalFormat(format.toString());
//        return df.format(num);
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        if(degree<=0){
            degree = 0;
        }
        df.setMaximumFractionDigits(degree);
        df.setGroupingUsed(false);
        return df.format(num);
    }

    public static BigDecimal comvertStringToBigDecimal(String str) {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        return new BigDecimal(str);
    }

    public static String random(int length) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(random.nextInt(10));
        }
        return buffer.toString();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 把数字转换成文件大小字符串
     *
     * @param size
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /**
     * 验证输入的字符串为纯数字，可验证小数
     * @param inputNumber：输入要判断的字符串
     * @return true false
     */
    public static boolean validateNumberLegal(String inputNumber){
        boolean bool = false;
        if(StringUtils.isEmpty(inputNumber)){
            return false;
        }
        inputNumber = inputNumber.trim();
        // 如果为负数，则将-去除
        if(inputNumber.indexOf("-") == 0&&inputNumber.length() >= 2){
            inputNumber = inputNumber.substring(1,inputNumber.length());
            // 如果包含+号，则+去除
        }else if(inputNumber.indexOf("+") == 0&&inputNumber.length() >= 2){
            inputNumber = inputNumber.substring(1,inputNumber.length());
        }
        // 判断输入的字符串是否合法数字
        if(inputNumber.length() > 0){
            String[] inputNumbers = inputNumber.split("\\.");
            // 没有小数点 就应该只有一个数组
            if(inputNumbers.length == 1){
                bool = validateNumber(inputNumbers[0],0);
            // 正常的输入字符串应该是只有一个小数点
            }else if(inputNumbers.length == 2){
                // 两次验证
                for(String number : inputNumbers){
                    bool = validateNumber(number,0);
                }
                // 如果数组的个数过多，则标识非法数字
            }else{
                bool = false;
            }
            // 除了符号，什么都没有，则也算是不合法的数字
        }else{
            bool = false;
        }
        return bool;
    }

    /**
     * 验证输入的数字是否合法
     * @param inputNumber
     * @param precision
     * @return
     */
    private static boolean validateNumber(String inputNumber,int precision){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(inputNumber).matches();
    }

    public static void main(String args[]){
        System.out.println(NumberUtils.mulD(1.501d,3.8501d));
    }
    /**
     * 拼接List集合
     * @Author wugong
     * @Date 2018/2/8 16:02
     * @Modify if true,please enter your name or update time
     * @param longList：被合并的List
     * @param sepeartor：合并的标识,默认为,
     * @return
     */
    public static String numberListToString(List<Long> longList,String sepeartor){
        if(CollectionUtils.isEmpty(longList)){
            return "";
        }
        StringBuffer longStr = new StringBuffer();
        sepeartor = StringUtils.isBlank(sepeartor)?",":sepeartor;
        for (int i=0;i < longList.size();i++) {
            Long str = longList.get(i);
            longStr.append(str);
            if(i < (longList.size()-1)){
                longStr.append(sepeartor);
            }
        }
        return longStr.toString();
    }
}
