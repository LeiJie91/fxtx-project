package com.fxtx.cloud.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by chenlong on 2015/5/18.
 */
public class Upload {
    public static final String UPLOAD_BASIC_PATH = "/uploads/img/" ;
    public static enum  FileType {
        IMG(new String[]{"jpg", "png","jpeg"}) ;
        public String[] suffixs ;
        FileType(String[] suffixs){
            this.suffixs = suffixs ;
        }
    }
    private String fullPath ;
    public String getFullPath(){
        return fullPath ;
    }
    private String relativePath ;
    public String getRelativePath(){
        return relativePath ;
    }
    public Upload(MultipartFile multipartFile, String path, FileType fileType) throws Exception {
        FileOutputStream fileOutputStream = null;
        String originalFilename = multipartFile.getOriginalFilename() ;
        String suffix  =originalFilename.substring(originalFilename.lastIndexOf(".", originalFilename.length())+1) ;
        if (!Arrays.asList(fileType.suffixs).contains(suffix.toLowerCase())){
            throw new Exception("文件类型不正确") ;
        }
        this.relativePath = path+"."+suffix ;
        this.fullPath = UPLOAD_BASIC_PATH+relativePath ;
        File file = new File(fullPath) ;
        File parentFile = file.getParentFile() ;
        if (!parentFile.isDirectory()){
            if (!parentFile.mkdirs()){
                throw new Exception("非法请求");
            }
        }
        fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
    }
}
