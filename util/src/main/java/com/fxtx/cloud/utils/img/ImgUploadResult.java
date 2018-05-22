package com.fxtx.cloud.utils.img;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/12/5.
 * Project  :fx
 * Copyright: 2016 shinsoft Inc. All rights reserved.
 */
public class ImgUploadResult {

    //执行状态
    private boolean flag;
    //错误类型
    private String errCode;
    //消息描述
    private String message;
    //路径
    //原图
    private String original;
    //压缩后的图片
    private List<String> thumbs = new ArrayList<String>();

    public ImgUploadResult() {
    }


    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public List<String> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<String> thumbs) {
        this.thumbs = thumbs;
    }
    public String getThumb(int point){
        return this.thumbs.get(point) ;
    }
    public void addThumb(String url){
        this.thumbs.add(url) ;
    }
}
