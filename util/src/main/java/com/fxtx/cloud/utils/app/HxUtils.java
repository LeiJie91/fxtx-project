package com.fxtx.cloud.utils.app;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fxtx.framework.util.LogUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class HxUtils {
    public static final String API_SERVER_HOST = "a1.easemob.com";
    public static final String APPKEY = "bjfxtx-403#fxxy";
    public static final String relativeUrl = "bjfxtx-403/zspfsc";
    public static final String APP_CLIENT_ID = "YXA6mGh8kJjCEeW9uG8AX2BKnA";
    public static final String APP_CLIENT_SECRET = "YXA6mJMZilo9eICN3g1CTbKPMdVmXG8";
    private static final JsonNodeFactory factory = new JsonNodeFactory(false);
    public static ObjectNode registerHx(String username, String password) throws IOException, URISyntaxException {
        HttpResponse response = null;
        HttpClient httpClient=null;
        ObjectNode resObjectNode = factory.objectNode();
        try {
            ObjectNode datanode = JsonNodeFactory.instance.objectNode();
            datanode.put("username",username);
            datanode.put("password", password);
            httpClient = getClient(true);
            URL url=new URL("https://a1.easemob.com/"+relativeUrl+"/users");
            HttpPost httpPost = new HttpPost(url.toURI());
            Authorization(httpPost,httpClient);
            httpPost.setEntity(new StringEntity(datanode.toString(), "UTF-8"));
            response=httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                resObjectNode=stringToNode(responseContent);
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
                return resObjectNode;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        registerHx("test123","123456");
    }
    public static void Authorization(HttpEntityEnclosingRequestBase httpMethodEntity, HttpClient client) throws IOException, URISyntaxException {
        URL tokenUrl=new URL("https://a1.easemob.com/"+relativeUrl+"/token");
        ObjectNode objectNode = factory.objectNode();
        objectNode.put("grant_type", "client_credentials");
        objectNode.put("client_id", APP_CLIENT_ID);
        objectNode.put("client_secret", APP_CLIENT_SECRET);
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(tokenUrl.toURI());
        for (NameValuePair nameValuePair : headers) {
            httpPost.addHeader(nameValuePair.getName(), nameValuePair.getValue());
        }
        httpPost.setEntity(new StringEntity(objectNode.toString(), "UTF-8"));
        HttpResponse tokenResponse = client.execute(httpPost);
        HttpEntity entity = tokenResponse.getEntity();
        String results = EntityUtils.toString(entity, "UTF-8");
        if (tokenResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            JsonNode json = stringToNode(results);
            String accessToken = json.get("access_token").asText();
            httpMethodEntity.addHeader("Authorization", "Bearer " + accessToken);
        }
    }
    public static ObjectNode stringToNode(String jsonStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser(jsonStr);
        return  mapper.readTree(jp);
    }
    public static HttpClient getClient(boolean isSSL) {

        HttpClient httpClient = new DefaultHttpClient();
        if (isSSL) {
            X509TrustManager xtm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            try {
                SSLContext ctx = SSLContext.getInstance("TLS");

                ctx.init(null, new TrustManager[] { xtm }, null);

                SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

                httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        return httpClient;
    }
    /**
     * 输入用户名和hx 密码 返回环信ID 就是环信用户名和环信密码
     *
     * @param userName
     * @param hxPwd
     * @return
     */
    public static String createHX(String userName, String hxPwd) {
        try {
            ObjectNode objectNode = HxUtils.registerHx(userName, hxPwd);
            if (objectNode.has("entities")) {
                JsonNode arrayNode = objectNode.get("entities");
                return arrayNode.get(0).get("username").asText();  //环信Id
            } else if (objectNode.has("error")) {
                LogUtils.logError(objectNode.get("error").asText(), null);
            }
        } catch (Exception e) {
            LogUtils.logError("创建环信账户失败", e);
        }
        return null;
    }
}

