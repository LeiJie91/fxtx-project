package com.fxtx.cloud.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/21.
 */
@Component
public class WeiBoLogin  extends ThirdPartyLoginBasic implements ThirdPartyLogin{
    final static public Map<String , Byte> GENDER_MAP = new HashMap<String, Byte>() ;
    {
        GENDER_MAP.put("m" ,ThirdPartyUserInfo.Gender.MALE.code ) ;
        GENDER_MAP.put("f" , ThirdPartyUserInfo.Gender.FEMALE.code) ;
    }
    //@Value("${WeiBoLogin.CLIENT_ID}")
    public String CLIENT_ID ;
    //@Value("${WeiBoLogin.REDIRECT_URI}")
    public  String REDIRECT_URI  ;
    public static final String AUTHORIZATION_URL="https://api.weibo.com/oauth2/authorize" ;
    public static final String GET_ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token" ;
    //@Value("${WeiBoLogin.CLIENT_SECRET}")
    public String CLIENT_SECRET  ;
    public static final String GET_WEIBO_USER_INFO_URL = "https://api.weibo.com/2/users/show.json" ;
    public String getLoginUrl(){
        return AUTHORIZATION_URL+"?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI ;
    }
    @Autowired
    private Httpclient httpclient ;
    public AccessToken getAccessToken(String code){
        Map<String , String> map = new HashMap<String, String>();
        map.put("client_id",CLIENT_ID) ;
        map.put("client_secret",CLIENT_SECRET);
        map.put("code",code);
        map.put("redirect_uri",REDIRECT_URI);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json =httpclient.doPost(GET_ACCESS_TOKEN_URL, map) ;
            return objectMapper.readValue(json, WeiBoLogin.AccessToken.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }
    public ThirdPartyUserInfo getThirdPartyUserInfo(String code) throws IOException {
        AccessToken accessToken =this.getAccessToken(code) ;
        ThirdPartyLogin.AccessToken accessToken1 = new ThirdPartyLogin.AccessToken() ;
        accessToken1.setAccess_token(accessToken.getAccess_token());
        accessToken1.setUid(accessToken.getUid());
        return getThirdPartyUserInfo(accessToken1) ;
    }
    public ThirdPartyUserInfo getThirdPartyUserInfo(ThirdPartyLogin.AccessToken accessToken) throws IOException {
        Map weiboUserInfo = this.getWeiboUserInfo(accessToken) ;
        ThirdPartyUserInfo thirdPartyUserInfo = new ThirdPartyUserInfo();
        thirdPartyUserInfo.setToken(accessToken.getAccess_token());
        thirdPartyUserInfo.setAvatar((String) weiboUserInfo.get("avatar_large"));
        thirdPartyUserInfo.setGender(GENDER_MAP.get(weiboUserInfo.get("gender")));
        thirdPartyUserInfo.setNickName((String)weiboUserInfo.get("screen_name"));
        thirdPartyUserInfo.setOpenId(accessToken.getUid());
        thirdPartyUserInfo.setType(TYPE.WEIBO.name());
        return thirdPartyUserInfo ;
    }
    public Map getWeiboUserInfo(ThirdPartyLogin.AccessToken accessToken) throws IOException {
        Map<String , String> map = new HashMap<String, String>();
        map.put("source",CLIENT_ID) ;
        map.put("access_token",accessToken.access_token);
        map.put("uid",accessToken.uid);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = httpclient.doGet(GET_WEIBO_USER_INFO_URL, map) ;
        return objectMapper.readValue(jsonStr, Map.class);
    }
}