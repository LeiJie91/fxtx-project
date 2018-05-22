package com.fxtx.cloud.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/30.
 */
@Component
public class WeiXinLogin extends ThirdPartyLoginBasic implements  ThirdPartyLogin{
    public static final String GRANT_TYPE = "authorization_code" ;
    //@Value("${WeiXinLogin.REDIRECT_URI}")
    public  String REDIRECT_URI;
    //@Value("${WeiXinLogin.CLIENT_ID}")
    public  String CLIENT_ID;
    //@Value("${WeiXinLogin.CLIENT_SECRET}")
    public  String CLIENT_SECRET;
    //@Value("${WeiXinLogin.SCOPE}")
    public String SCOPE;
    public static final String LOGIN_URL = "https://open.weixin.qq.com/connect/qrconnect?appid={APPID}&redirect_uri={REDIRECT_URL}&response_type=code&scope={SCODE}&state=STATE#wechat_redirect";
    public static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token" ;
    public static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo" ;
    @Autowired
    private Httpclient httpclient ;
    public String getLoginUrl(){
        String redirect_url = REDIRECT_URI;
        try {
            redirect_url = URLEncoder.encode(REDIRECT_URI, "utf-8");
        } catch (Exception e) {}

        return LOGIN_URL
                .replace("{APPID}", CLIENT_ID)
                .replace("{REDIRECT_URL}", redirect_url)
                .replace("{SCODE}", SCOPE);
    }

    static public class AccessToken{
        /*{
            "access_token":"ACCESS_TOKEN",
            "expires_in":7200,
            "refresh_token":"REFRESH_TOKEN",
            "openid":"OPENID",
            "scope":"SCOPE"
        }*/
        String access_token;
        String expires_in;
        String refresh_token;
        String openid;
        String scope;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    public AccessToken getAccessToken(String code) throws IOException {
        Map<String , String> map = new HashMap<String, String>();
        map.put("grant_type", GRANT_TYPE);
        map.put("appid", CLIENT_ID);
        map.put("secret", CLIENT_SECRET);
        map.put("code", code );
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json =httpclient.doGet(GET_ACCESS_TOKEN_URL, map) ;
            return objectMapper.readValue(json, AccessToken.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null ;
    }

    public ThirdPartyUserInfo getThirdPartyUserInfo(String code) throws IOException {
        AccessToken token = this.getAccessToken(code);
        if (token == null){
            return null ;
        }
        ThirdPartyLogin.AccessToken accessToken1 = new ThirdPartyLogin.AccessToken() ;
        accessToken1.setAccess_token(token.getAccess_token());
        accessToken1.setUid(token.getOpenid());
        return getThirdPartyUserInfo(accessToken1) ;
    }
    public ThirdPartyUserInfo getThirdPartyUserInfo(ThirdPartyLogin.AccessToken accessToken) throws IOException {
        Map<String , String> map = new HashMap<String, String>() ;
        map.put("access_token" , accessToken.getAccess_token());
        map.put("openid" , accessToken.getUid()) ;
        ObjectMapper objectMapper = new ObjectMapper();
        String json =httpclient.doGet(GET_USER_INFO_URL, map) ;
        Map userInfoMap=objectMapper.readValue(json, Map.class);
        ThirdPartyUserInfo thirdPartyUserInfo = new ThirdPartyUserInfo();
        thirdPartyUserInfo.setOpenId(accessToken.getUid());
        thirdPartyUserInfo.setNickName(String.valueOf(userInfoMap.get("nickname")));
        thirdPartyUserInfo.setAvatar(String.valueOf(userInfoMap.get("headimgurl")));
        Byte gender;
        if ("1".equals(String.valueOf(userInfoMap.get("sex")))){
            gender = ThirdPartyUserInfo.Gender.MALE.code;
        }else{
            gender = ThirdPartyUserInfo.Gender.FEMALE.code;
        }
        thirdPartyUserInfo.setGender(gender);
        thirdPartyUserInfo.setToken(accessToken.getAccess_token());
        thirdPartyUserInfo.setType(TYPE.WEIXIN.name());
        return thirdPartyUserInfo;
    }
}
