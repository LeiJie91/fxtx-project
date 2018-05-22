package com.fxtx.cloud.utils;

import cn.jpush.api.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/21.
 */
public class EmojiUtils {
    private static Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;

    public static String filterEmoji(String source) {
        if(StringUtils.isNotEmpty(source)){
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()){
                source = emojiMatcher.replaceAll("*");
                return source ;
            }
            return source;
        }
        return source;
    }
}
