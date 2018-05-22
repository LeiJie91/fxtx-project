package com.fxtx.cloud.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/23.
 */
@Component
public class QQLogin  extends ThirdPartyLoginBasic implements  ThirdPartyLogin{

    public static final String AUTHORIZATION_URL="http://openapi.qzone.qq.com/oauth/show" ;
    public static final String RESPONSE_TYPE = "token";
    //@Value("${QQLogin.REDIRECT_URI}")
    public  String REDIRECT_URI;
    //@Value("${QQLogin.SCOPE}")
    private String SCOPE = "all" ;
    //@Value("${QQLogin.CLIENT_ID}")
    public  String CLIENT_ID  ;
    public static final String GRANT_TYPE = "authorization_code" ;
    //@Value("${QQLogin.CLIENT_SECRET}")
    public  String CLIENT_SECRET;
    public static final String GET_OPENID_URL = "https://graph.qq.com/oauth2.0/me" ;
    public static final String GET_QQ_USER_INFO_URL = "https://graph.qq.com/user/get_user_info" ;
    @Autowired
    private Httpclient httpclient ;
    public String getLoginUrl(){
        return AUTHORIZATION_URL+"?which=ConfirmPage&response_type="+RESPONSE_TYPE+"&client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&scope="+SCOPE ;
    }
    static public class Openid{
        String openid ;
        String client_id ;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }
    }

    public Openid getOpenid(String accessToken) throws IOException {
        if (accessToken==null) {
            return null ;
        }
        try {
            Map<String , String> map = new HashMap<String, String>() ;
            map.put("access_token" , accessToken);
            ObjectMapper objectMapper = new ObjectMapper();
            String json =httpclient.doGet(GET_OPENID_URL, map) ;
            json = json.replace("callback( " , "").replace(" );" ,"") ;

            return objectMapper.readValue(json, Openid.class);
        }
        catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }
    public ThirdPartyUserInfo getThirdPartyUserInfo (ThirdPartyLogin.AccessToken accessToken) throws IOException {
        Map<String , String> map = new HashMap<String, String>() ;
        map.put("access_token", accessToken.getAccess_token());
        map.put("oauth_consumer_key", CLIENT_ID);
        map.put("openid", accessToken.getUid());
        ObjectMapper objectMapper = new ObjectMapper();
        String json =httpclient.doGet(GET_QQ_USER_INFO_URL, map) ;
        Map userInfoMap=objectMapper.readValue(json, Map.class);
        ThirdPartyUserInfo thirdPartyUserInfo = new ThirdPartyUserInfo() ;
        thirdPartyUserInfo.setOpenId(accessToken.getUid());
        thirdPartyUserInfo.setToken(accessToken.getAccess_token());
        thirdPartyUserInfo.setNickName(String.valueOf(userInfoMap.get("nickname")));
        String avatar ;
        if (!userInfoMap.get("figureurl_qq_2").equals("")){
            avatar = (String)userInfoMap.get("figureurl_qq_2");
        } else if (!userInfoMap.get("figureurl_2").equals("")){
            avatar = (String)userInfoMap.get("figureurl_2");
        } else {
            avatar = (String)userInfoMap.get("figureurl_qq_1");
        }
        thirdPartyUserInfo.setAvatar(avatar);
        Byte gender ;
        if (userInfoMap.get("gender").toString().equals("男")){
            gender = ThirdPartyUserInfo.Gender.MALE.code ;
        }
        else{
            gender = ThirdPartyUserInfo.Gender.FEMALE.code ;
        }
        thirdPartyUserInfo.setGender(gender);
        thirdPartyUserInfo.setType(TYPE.QQ.name());
        return thirdPartyUserInfo ;
    }
    public ThirdPartyUserInfo getThirdPartyUserInfo (String token) throws IOException {
        Openid o = this.getOpenid(token) ;
        ThirdPartyLogin.AccessToken accessToken = new AccessToken() ;
        accessToken.setUid(o.getOpenid());
        accessToken.setAccess_token(token);
        return this.getThirdPartyUserInfo(accessToken);
    }
}
