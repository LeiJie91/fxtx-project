package com.fxtx.cloud.utils.security;

import com.fxtx.cloud.utils.Encrypt;
import com.fxtx.framework.util.security.Md5Utils;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Created by Administrator on 2015/8/13.
 * Project  :zed
 * Copyright: 2016 shinsoft Inc. All rights reserved.
 */
public class PasswordUtils {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String generateSalt() {
        return (int)((Math.random()*9+1)*1000)+"";
//        return new String(Digests.generateSalt(SALT_SIZE));
    }
    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String generateSmsCode() {
        return (int)((Math.random()*9+1)*100000)+"";
//        return new String(Digests.generateSalt(SALT_SIZE));
    }
    /**
     * 生成盐值
     */
    public static String entryptPassword(String plainPassword,String saltStr) {
        String passwordCipherText= new Md5Hash(plainPassword,saltStr,1).toHex();
        return passwordCipherText;
    }

    /**
     * 验证密码
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword,String saltStr, String password) {
        String newPassword = entryptPassword(plainPassword,saltStr);
        return password.equals(newPassword);
    }

    public static void main(String args[]){
//        Encrypt encrypt = new Encrypt();
//        String password = encrypt.md5("9323"+"admin");
//        String password_cipherText= new Md5Hash("admin","9323",1).toHex();

//        String salt = "3136";
//        System.out.println(salt);
//        String enpassword = entryptPassword("admin",salt);
//        System.out.println(enpassword);
//        boolean flag = validatePassword("admin",salt,enpassword);
//        System.out.println(password);
//        System.out.println(password_cipherText);
        System.out.println(PasswordUtils.generateSalt());
        System.out.println(PasswordUtils.generateSmsCode());
    }
}
