package com.fxtx.xuelu;

import java.util.regex.Pattern;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: wugong.jie
 * \* Date: 2018/3/29 14:04
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class UrlMatch {

    public static void main(String[] args) {
        String url = "https:/klsfnklnklwnl.csfwfwn.cn?1231=sjkfjkf&sfwfw=";
//        String url = "http://192.168.1.202:8000/group1/M00/00/04/wKgBylpmynaAI2AaAABVhf98iLQ160.jpg";
        if (isUrl(url)) {
            System.out.println("是正确的网址");
        } else {
            System.out.println("非法网址");
        }
    }

    public static boolean isUrl(String url){
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }
//    /**
//     * 验证网址Url
//     *
//     * @param 待验证的字符串
//     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
//     */
//    public static boolean IsUrl(String str) {
//        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
//        return match(regex, str);
//    }
}