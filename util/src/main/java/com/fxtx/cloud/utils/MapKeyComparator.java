package com.fxtx.cloud.utils;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/12/25.
 * Project  :cloud-zspfsc
 * Copyright: 2012-2030 bjfxtx Inc. All rights reserved.
 */
public class MapKeyComparator implements Comparator<String> {

    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
