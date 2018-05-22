package com.fxtx.cloud.utils;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/9.
 */
public class SmsV1Utils {
    //=====================文件流上传操作开始====================================
    //文件流上传连接超时
    private static final int UPLOAD_CONNECTION_TIMEOUT = 6000;
    //文件流上传读取超时
    private static final int UPLOAD_READ_TIMEOUT = 6000;
    //文件上传拼接字符串
    private static final String UPLOAD_HYPHENS = "--";
    private static final String UPLOAD_BOUNDARY = "*****";
    private static final String UPLOAD_END = "\r\n";
    //更新上传进度
    public static final int UPLOAD_UPDATE_PROGRESS = 1 ;
    //上传结束
    public static final int UPLOAD_UPDATE_END = 2 ;
    //上传出错
    public static final int UPLOAD_UPDATE_ERR = 2 ;

    //=====================文件流上传操作结束====================================

    //连接超时设定
    public static final int CONNECTION_TIMEOUT = 5000;
    //请求超时
    private static final int SO_TIMEOUT = 6000;

    //调试模式，打正式包之前一定要改成false
    public static final boolean debug = true;
    private static HttpClient httpClient;
    //创建多线程  HttpClient 类
    private static HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 433));
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
        return new DefaultHttpClient(conMgr, params);
    }

    //初始化请求参数
    public static Map<String, Object> initParams() {
        return new HashMap<String, Object>();
    }

    //初始化请求 httpclient
    private static HttpClient initOnly() {
        if (null == httpClient) {
            HttpClient httpClientCreate = createHttpClient();
            // 连接超时
            httpClientCreate.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            // 请求超时
            httpClientCreate.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            httpClient = httpClientCreate ;
        }
        return httpClient ;
    }
    //初始化请求 httpclient
    private static HttpClient init() {
        HttpClient httpClientCreate = new DefaultHttpClient();
        // 连接超时
        httpClientCreate.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        // 请求超时
        httpClientCreate.getParams().setParameter(
                CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
        return httpClientCreate ;
    }

    /**
     * http请求
     * @param url 连接
     * @param params 传参
     * @param keepSession 是否保持会话session
     * @param isPost 是否post请求
     * @return
     */
    public static String httpRequest(String url,Map<String, Object> params,Boolean keepSession,Boolean isPost,String encoded){
        Date begin = new Date() ;
        HttpClient httpClientTemp = keepSession?initOnly():init() ;
        String msg = null;
        HttpResponse httpResponse = null;
        HttpRequestBase baseHttpEntity = null ;
        StringBuffer paramString = new StringBuffer("");
        try{
            if(isPost){
                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
                if (null != params && params.size() > 0) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        if(null!=entry.getValue()) {
                            paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                            if(debug){
                                paramString.append(entry.getKey());
                                paramString.append("=");
                                paramString.append(entry.getValue().toString());
                                paramString.append("&");
                            }
                        }
                    }
                    if(paramString.length()>0){
                        paramString.deleteCharAt(paramString.length() - 1);
                    }
                }
                httpPost.setEntity(new UrlEncodedFormEntity(paramsList, encoded));
                baseHttpEntity = httpPost ;
                httpResponse = httpClientTemp.execute(httpPost);
                msg = result(msg, httpResponse) ;
            }else {
                StringBuffer paramGet = new StringBuffer(url) ;
                if (null != params && params.size() > 0) {
                    for (String key : params.keySet()) {
                        Object value = params.get(key);
                        if (null != value && !value.toString().trim().equals("")) {
                            paramString.append(key);
                            paramString.append("=");
                            paramString.append(value);
                            paramString.append("&");
                        }
                    }
                    if(paramString.length()>0){
                        paramString.deleteCharAt(paramString.length() - 1);
                        paramGet.append("?");
                        paramGet.append(paramString) ;
                    }
                }
                HttpGet httpGet = new HttpGet(paramGet.toString());
                baseHttpEntity = httpGet ;
                httpResponse = httpClientTemp.execute(httpGet);
                msg = result(msg, httpResponse) ;
            }
        }catch (ConnectTimeoutException cte){
            msg = debug ? "异常：" + cte.toString() : "请求超时";
        }catch (Exception e){
            msg = debug ? "异常：" + e.toString() : "网络错误，请检查网络";
        }finally {
            if(null!=baseHttpEntity){
                baseHttpEntity.abort();
            }
        }
        if(debug){
            Date end = new Date() ;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
            String content = "接口地址：" + url+
                    "请求参数：" + paramString.toString()+
                    "请求开始时间：" + format.format(begin)+
                    "请求结束时间：" + format.format(end)+
                    "所耗时间：" + (end.getTime() - begin.getTime()) + "毫秒"+
                    "接口获得内容：" + msg;
            FxtxLogUtils.writeToLog(SmsV1Utils.class,"httpRequest",content);
        }
        return msg;
    }
    /**
     * 保持会话的post请求
     * @param url
     * @param params
     * @return
     */
    public static String POST(Map<String, Object> params,String... url) {
        return httpRequest(url[0],params,false,true,url.length>1?url[1]:"utf-8") ;
    }
    //get方式提交参数
    public static String GET(Map<String, Object> params,String... url) {
        return httpRequest(url[0],params,true,false,url.length>1?url[1]:"utf-8") ;
    }
    /**
     * 下载文件
     * @param url
     * @return
     */
    public static InputStream download(String url) {
        Date begin = new Date() ;
        HttpClient httpClientTemp = init();
        HttpGet httpGet = new HttpGet(url);
        InputStream inputStream = null;
        String downLoadErr = "" ;
        try {
            HttpResponse httpResponse = httpClientTemp.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK ) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    inputStream = httpEntity.getContent();
                    downLoadErr = "下载成功："+httpEntity.getContentLength()+"b" ;
                }else{
                    downLoadErr = "下载失败：null";
                }
            }
        } catch (Exception e) {
            downLoadErr = "下载成功："+e.toString() ;
            httpGet.abort();
        }
        if(debug){
            Date end = new Date() ;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
            String content = "下载地址："+url+
                    "请求开始时间："+format.format(begin)+
                    "请求结束时间："+format.format(end)+
                    "所耗时间："+(end.getTime()-begin.getTime())+"毫秒"+
                    "接口获得内容："+downLoadErr;
            FxtxLogUtils.writeToLog(SmsV1Utils.class,"download",content);

        }
        return  inputStream ;
    }

    /**
     * 处理接口请求的数据
     * @param msg
     * @param httpResponse
     * @return
     */
    private static String result(String msg, HttpResponse httpResponse) {
        if (null == msg) {
            try {
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
                    msg = EntityUtils.toString(httpResponse.getEntity());
                    return msg;
                } else {
                    msg = debug ? "异常：" + String.valueOf(httpResponse.getStatusLine().getStatusCode()) : "服务器返回异常";
                }
            } catch (ConnectTimeoutException cte){
                msg = debug ? "异常：" + cte.toString() : "请求超时";
            } catch (IOException ioe){
                msg = debug ? "异常：" + ioe.toString() : "请求异常";
            }
        }
        return msg ;
    }
    //发送验证码
    public static boolean sendMessage(String phone,String content){
        Map<String ,Object> params = new HashMap<String ,Object>() ;
        params.put("username", "fengxingzed");
        params.put("password", "WDNwQ05rbmU2Z280");
        params.put("isLongSms", "0");
        params.put("extno", "20230");
        params.put("method", "sendSMS");
        params.put("smstype", "2");
        params.put("mobile", phone);
        params.put("content", content);
        String uploadStr = POST(params, "http://218.241.153.225:6100/servlet/UserServiceAPI","GBK") ;
        uploadStr = uploadStr.trim() ;
        String successStr = uploadStr.replaceAll("^(success);[0-9]+$", "$1") ;
        if("success".equals(successStr)){
            return true ;
        }else {
            return false ;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(SmsV1Utils.sendMessage("15956993592","验证码：1234【掌上批发市场】"));
    }
}
