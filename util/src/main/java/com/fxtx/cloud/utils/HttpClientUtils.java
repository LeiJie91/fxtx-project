package com.fxtx.cloud.utils;

import com.fxtx.cloud.utils.app.AppUtils;
import com.fxtx.framework.util.LogUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 2014/12/18.
 * Project  :风行学院
 * Copyright: 2016 shinsoft Inc. All rights reserved.
 */
public class HttpClientUtils {

    /**
     * 请求编码
     */
    public static String requestEncoding = "UTF-8";

    public static String post(String url, Map<String, String> params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String body = null;
        HttpPost post = postForm(url, params);
        body = invoke(httpclient, post);
        httpclient.getConnectionManager().shutdown();
        return body;
    }

    private static String invoke(DefaultHttpClient httpclient,HttpUriRequest httpost) {
        HttpResponse response = sendRequest(httpclient, httpost);
        String body = paseResponse(response);
        return body;
    }

    private static String paseResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        String charset = EntityUtils.getContentCharSet(entity);
        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    private static HttpResponse sendRequest(DefaultHttpClient httpclient,HttpUriRequest httpost) {
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params) {
        HttpPost httpost = new HttpPost(url);
        try {
            if(params!=null){
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    nvps.add(new BasicNameValuePair(key, params.get(key)));
                }
                httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpost;
    }

    /**
     *
     * @param reqUrl
     * @param parameters
     * @param recvEncoding
     * @param fileIn
     *            文件流
     * @return
     */
    public static String uploadMedia(String reqUrl, Map parameters,String recvEncoding, InputStream fileIn, String fileName) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            // 设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            String params = getMapParamsToStr(parameters,HttpClientUtils.requestEncoding);
            if(reqUrl.indexOf("?")<0){
                reqUrl+="?";
            }
            if(StringUtils.isNotBlank(params.toString())){
                reqUrl = reqUrl + params.toString();
            }
            URL urlObj = new URL(reqUrl);
            // 连接
            url_con = (HttpURLConnection) urlObj.openConnection();
            /**
             * 设置关键值
             */
            url_con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
            url_con.setDoInput(true);
            url_con.setDoOutput(true);
            url_con.setUseCaches(false); // post方式不能使用缓存

            // 设置请求头信息
            url_con.setRequestProperty("Connection", "Keep-Alive");
            url_con.setRequestProperty("Charset",recvEncoding);

            // 设置边界
            url_con.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            // 请求正文信息

            // 第一部分：
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                    + fileName + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");

            byte[] head = sb.toString().getBytes(recvEncoding);

            // 获得输出流
            OutputStream out = new DataOutputStream(url_con.getOutputStream());
            // 输出表头
            out.write(head);
            // 文件正文部分
            // 把文件已流文件的方式 推入到url中
            DataInputStream in = new DataInputStream(fileIn);
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            // 结尾部分
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes(recvEncoding);// 定义最后数据分隔线

            out.write(foot);
            out.flush();
            out.close();

            InputStream iddn = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(iddn,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
        } catch (IOException e) {
            LogUtils.logError("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    /**
     * 将参数转换成string
     * @param paramMap
     * @param requestEncoding
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    private static String getMapParamsToStr(Map paramMap,String requestEncoding) throws IOException {
        StringBuffer params = new StringBuffer();
        // 设置边界
        for (Iterator iter = paramMap.entrySet().iterator(); iter
                .hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            params.append(element.getKey().toString());
            params.append("=");
            params.append(URLEncoder.encode(element.getValue().toString(),
                    requestEncoding));
            params.append("&");
        }

        if (params.length() > 0) {
            params = params.deleteCharAt(params.length() - 1);
        }

        return params.toString();
    }

    public static void main(String args[]) throws Exception{
        Map parameter = Maps.newHashMap();
        parameter.put("Fx-Timestamp",System.currentTimeMillis()+"");
        String sign = AppUtils.getRealSign(parameter);
        parameter.put("sign",sign);
        FileInputStream inputStream = new FileInputStream("d:\\baike_logo.gif");
        String str = HttpClientUtils.uploadMedia("http://localhost:8080/manager/v1/upload/imgFile.json",parameter,HttpClientUtils.requestEncoding,inputStream,"avator.jpg");
        System.out.println(str);
    }
}
