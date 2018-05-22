package com.fxtx.cloud.utils.file;


import com.fxtx.framework.util.StringUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */
public class FxFileNameFilter implements FilenameFilter{
    private List<String> typeList;
    public FxFileNameFilter(String type){
        if(StringUtils.isNotEmpty(type)){
            String[] typeArray = StringUtils.split(type,",");
            typeList = Lists.newArrayList();
            for(String tmp : typeArray){
                if(com.fxtx.framework.util.StringUtils.isNotEmpty(tmp)){
                    typeList.add(tmp);
                }
            }
        }

    }

    public boolean accept(File dir, String name){
        if(CollectionUtils.isNotEmpty(typeList)){
            for(String type : typeList){
                if(name.endsWith(type)){
                    return true;
                }
            }
            return false;
        }else{
            return true;
        }

    }
}
