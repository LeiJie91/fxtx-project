package com.fxtx.cloud.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2014/10/15.
 */
@Component
public class Encrypt {
    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public  String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                md.update(str.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位的加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 使用DES加密数据
     * @param data
     * @return
     */
    public  String encrypt(String data) {
        try {
            Key k = toKey();
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            byte[] e = cipher.doFinal(data.getBytes("UTF-8"));

            return new String( Base64.encodeBase64(e),"UTF-8") ;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 使用DES解密数据
     * @param data	base64加密的字符串
     * @return 解密后的字符串
     */
    public  String dencrypt(String data) {
        byte[] d = new byte[0];
        try {
            d = Base64.decodeBase64(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Key k = toKey();
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            return new String(cipher.doFinal(d),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 使用HMAC_SHA1加密数据
     * @param s
     * @param key
     * @return 加密后的Base64格式（URLSafe）
     */
    public  String hmac_sha1(String s, String key, String charset) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(charset), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(s.getBytes(charset));

            return encodeURLSafeBase64(rawHmac, charset);
        } catch (Exception e) {
            System.out.println("[HMAC_SHA1]" + e.getMessage());
        }
        return null;
    }
    /**
     * 将字节数字使用URLSafe标准进行base64编码（按照七牛的标准）
     * @param bytes
     * @return
     */
    public  String encodeURLSafeBase64(byte [] bytes, String charset) {
        byte[] enBytes = Base64.encodeBase64(bytes);
        for (int i=0; i<enBytes.length; i++) {
            if (enBytes[i] == 47)
                enBytes[i] = 95;
            else if (enBytes[i] == 43) {
                enBytes[i] = 45;
            }
        }
        try {
            return new String(enBytes, charset);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private  Key toKey() throws Exception {
        byte[] key = "EVhJ7r`E3G".getBytes("UTF-8");
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(des);
        return secretKey;
    }
}
