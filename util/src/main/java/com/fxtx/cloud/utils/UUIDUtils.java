package com.fxtx.cloud.utils;

import com.fxtx.framework.util.StringUtils;
import com.fxtx.framework.util.security.Md5Utils;

import java.util.UUID;

/**
 * Created by Administrator on 2016.7.28.
 * Project  :cloud-exam
 * Copyright:  2016 shinsoft Inc. All rights reserved.
 */
public class UUIDUtils {
    private UUIDUtils(){

    }

    public static String generate(){
        UUID uuid = UUID.randomUUID();
        return Md5Utils.hash(uuid.toString());
    }

    public static void main(String args[]){
        System.out.println(UUIDUtils.generate());
    }
}
