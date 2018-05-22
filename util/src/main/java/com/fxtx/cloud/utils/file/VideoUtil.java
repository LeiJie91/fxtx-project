package com.fxtx.cloud.utils.file;

import java.io.File;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

/**
 * Created by super-man on 2016/11/16.
 */
public class VideoUtil {

    /**
     * 获取文件后缀名
     * @param file
     * @return
     */
    public static String getPrefix(File file) {
        String fileName = file.getName();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    public static String getName(File file) {
        String fileName = file.getName();
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        return name;
    }

    /**
     * 获取视频文件时长
     * @param file
     * @return
     */
    public static Long getTime(File file) {
        Encoder encoder = new Encoder();
        Long time = 0L;
        try {
            MultimediaInfo m = encoder.getInfo(file);
            time = m.getDuration();
//            System.out.println("此视频时长为:" + ls / 60000 + "分" + (ls`000) / 1000 + "秒！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

}
