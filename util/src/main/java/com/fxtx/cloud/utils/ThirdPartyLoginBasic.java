package com.fxtx.cloud.utils;

import java.io.IOException;

/**
 * Created by chenlong on 2015/5/5.
 */
abstract public class ThirdPartyLoginBasic {
    public ThirdPartyLogin.ThirdPartyUserInfo getTPUserInfo(String code) throws IOException {
        ThirdPartyLogin.ThirdPartyUserInfo  thirdPartyUserInfo=  this.getTPUserInfoByCache(code) ;
        if (thirdPartyUserInfo==null){
            thirdPartyUserInfo = getThirdPartyUserInfo(code) ;
            setCache(code, thirdPartyUserInfo);
        }
        return thirdPartyUserInfo ;
    }
    public abstract ThirdPartyLogin.ThirdPartyUserInfo getThirdPartyUserInfo (String token) throws IOException;
    public ThirdPartyLogin.ThirdPartyUserInfo  getTPUserInfoByCache(String code){
        return null  ;
    }
    public void   setCache(String code ,ThirdPartyLogin.ThirdPartyUserInfo thirdPartyUserInfo){
    }
}
