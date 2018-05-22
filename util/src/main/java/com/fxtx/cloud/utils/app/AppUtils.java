package com.fxtx.cloud.utils.app;

import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class AppUtils {

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 验证消息是否是支付宝发出的合法消息
     *
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params) {

	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    boolean isSign = getSignVeryfy(params, sign);
        return isSign;
    }

    /**
     * 验证请求参数的合法性
     *
     * @param request 请求连接
     *
     * @return 验证结果
     */
    public static boolean verify(HttpServletRequest request) {
        Map<String,String> params = getRequestParams(request);
        return verify(params);
    }

    /**
     * 获取所有请求参数
     *
     * @param request
     * @return
     */
    public static Map<String,String> getRequestParams(HttpServletRequest request){
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String> params, String sign) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = paraFilter(params);
        //获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
        if(AppConfig.sign_type.equals("MD5") ) {
        	isSign = AppMD5.verify(preSignStr, sign, AppConfig.key, AppConfig.input_charset);
        }
        return isSign;
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @return 生成的签名结果
     */
    public static String getSign(Map<String, String> params) {
        //过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(params);
        //获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        //获得签名验证结果
        return preSignStr;
    }
    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @return 生成的签名结果
     */
    public static String getRealSign(Map<String, String> params) {
        String preSignStr = getSign(params);
        if(AppConfig.sign_type.equals("MD5") ) {
            return AppMD5.sign(preSignStr , AppConfig.key, AppConfig.input_charset);
        }
        return "";
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param request Http请求
     * @return 生成的签名结果
     */
    public static String getSign(HttpServletRequest request) {
        //过滤空值、sign与sign_type参数
        Map<String,String> params = getRequestParams(request);
        Map<String, String> sParaNew = paraFilter(params);
        //获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        //获得签名验证结果
        if(AppConfig.sign_type.equals("MD5") ) {
            return AppMD5.sign(preSignStr , AppConfig.key, AppConfig.input_charset);
        }
        return "";
    }


}
