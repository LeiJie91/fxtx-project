package com.fxtx.cloud.utils;


import java.io.IOException;

/**
 * Created by Administrator on 2014/10/23.
 */
public interface ThirdPartyLogin {
    public static enum TYPE{
        QQ,WEIXIN,WEIBO
    }

    public ThirdPartyUserInfo getThirdPartyUserInfo(String code) throws IOException ;
    public String getLoginUrl();

    static public class AccessToken{
        public String access_token ;
        public String uid ;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    static public class ThirdPartyUserInfo{
        public static enum Gender{
            MALE((byte)1),FEMALE((byte)2) ;
            public Byte code ;
            Gender(Byte code){
                this.code = code ;
            }
        }
        private String NickName ;
        private Byte Gender ;
        private String Avatar ;
        private String OpenId ;
        private String Token ;
        private String type ;
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String token) {
            Token = token;
        }

        public String getOpenId() {
            return OpenId;
        }

        public void setOpenId(String openId) {
            OpenId = openId;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public Byte getGender() {
            return Gender;
        }

        public void setGender(Byte gender) {
            Gender = gender;
        }

        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String avatar) {
            Avatar = avatar;
        }
    };
    public ThirdPartyUserInfo getThirdPartyUserInfo(AccessToken accessToken) throws IOException ;
    public ThirdPartyLogin.ThirdPartyUserInfo getTPUserInfo(String code) throws IOException ;
}
