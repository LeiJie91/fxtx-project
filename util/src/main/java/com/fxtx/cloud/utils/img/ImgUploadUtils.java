/*
 * @(#) FileUploadUtils.java    2014年11月10日
 * Project  :上海鑫磊信息技术有限公司
 * Copyright: 2016 shinsoft Inc. All rights reserved.
 */
package com.fxtx.cloud.utils.img;

import com.fxtx.framework.util.web.WebPathUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传
 *
 * @author zhangweidong
 * @version 1.0
 *          Revise History:
 */
public class ImgUploadUtils {

    //默认上传的地址
    private static final String UPLOAD_DIR = "uploads"+ File.separator+"img"+ File.separator;
    private static final String UPLOAD_DIR_ORIGINAL = UPLOAD_DIR+"original"+ File.separator ;
    private static final String UPLOAD_DIR_THUMB = UPLOAD_DIR+"thumb" ;

    public static final String FILE_NAME_ENCODE = "UTF-8";


    //默认文件扩展名称分隔符
    private static final String DEFAULT_EXTENSION_SEPARATOR = ",";

    public static final String IMAGE_EXTENSION = "bmp,gif,jpg,jpeg,png";

//    private static int counter = 0;

    /**
     *
     * @param request
     * @param file MultipartFile OR File
     * @param thumbs
     * @return
     */
    public static ImgUploadResult upload(HttpServletRequest request, Object file, Integer[]... thumbs) {
        ImgUploadResult result = new ImgUploadResult();
        if(null==file || (!(file instanceof MultipartFile) && !(file instanceof File)) || (file instanceof MultipartFile && ((MultipartFile) file).isEmpty()) || (file instanceof File && (!((File)file).isFile() || ImgUtil.getImgSizes(((File)file)) <=0))){
            result.setFlag(false);
            result.setErrCode("notImg");
            result.setMessage("图片不存在");
            return result ;
        }
        try {
            String originalFilename = "" ;
            if(file instanceof MultipartFile){
                originalFilename = ((MultipartFile) file).getOriginalFilename();
            }else if(file instanceof File){
                originalFilename = ((File)file).getName();
            }
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1) ;
            if(!isInExtName(extName)){
                result.setFlag(false);
                result.setErrCode("errType");
                StringBuffer sb = new StringBuffer();
                int prevNum = -1 ;
                for(String key: ImgUtil.typeSet.keySet()){
                    if(ImgUtil.typeSet.get(key)!=prevNum){
                        sb. append(",");
                        sb. append(key);
                        prevNum = ImgUtil.typeSet.get(key) ;
                    }
                }
                sb = sb.deleteCharAt(0) ;
                result.setMessage("只能上传"+sb.toString()+"格式的图片");
            }else{
                String outFileName = UUID.randomUUID()+"."+extName ;
                String datePath = DateFormatUtils.format(new Date(), "yyyy" + File.separator + "MM" + File.separator + "dd");
                String filePath = WebPathUtils.getContextRealPath(request) ;
                //原图上传
                String originalUploadDir = UPLOAD_DIR_ORIGINAL+datePath ;
                originalUploadDir = FilenameUtils.normalizeNoEndSeparator(originalUploadDir);
                File originalUploadFile = new File(filePath+originalUploadDir + File.separator + outFileName);

                if(file instanceof MultipartFile){
                    if(!originalUploadFile.getParentFile().exists()){
                        originalUploadFile.getParentFile().mkdirs() ;
                    }
                    if(!originalUploadFile.exists()){
                        originalUploadFile.createNewFile();
                    }
                    ((MultipartFile) file).transferTo(originalUploadFile);
                }else if(file instanceof File){
                    ImgUtil.copy((File)file,originalUploadFile);
                }
                if(!ImgUtil.isRGB(originalUploadFile)){
                    result.setFlag(false);
                    result.setErrCode("errRGB");
                    result.setMessage("请用PS查看图片是否为RGB模式，CMYK模式图片不可用");
                }else{
                    String sizeStr = ImgUtil.getImageSizeByBufferedImage(originalUploadFile);
                    String pathStr = (originalUploadDir + File.separator + outFileName).replaceAll("\\\\", "/").trim();
                    if(StringUtils.isNotEmpty(sizeStr)){
                        pathStr+="@"+sizeStr;
                    }
                    result.setOriginal(pathStr);
                    if(null!=thumbs && thumbs.length>0){
                        for (int i=0;i<thumbs.length;i++){
                            Integer[] thumbSize = thumbs[i] ;
                            String thumbUploadDir = UPLOAD_DIR_THUMB+i+ File.separator + datePath;
                            thumbUploadDir = FilenameUtils.normalizeNoEndSeparator(thumbUploadDir);
//                            File thumbUploadFile = new File(filePath + thumbUploadDir + File.separator + thumbSize[0]+"X"+thumbSize[1]+"_"+outFileName);
                            File thumbUploadFile = new File(filePath + thumbUploadDir + File.separator + outFileName);
                            ImgUtil.imgReduce(originalUploadFile, thumbUploadFile, thumbSize[0],thumbSize[1]);
                            sizeStr = ImgUtil.getImageSizeByBufferedImage(thumbUploadFile);
                            String thumbStr = (thumbUploadDir + File.separator + outFileName).replaceAll("\\\\", "/").trim();
                            if(StringUtils.isNotEmpty(sizeStr)){
                                thumbStr+="@"+sizeStr;
                            }
                            result.addThumb(thumbStr);
                        }
                    }
                    result.setFlag(true);
                }
            }
        } catch (Exception e) {
            result.setFlag(false);
            result.setErrCode("sysErr");
            result.setMessage("图片上传错误");
        }
        return result;
    }
    //判断是否允许的格式范围内
    public static boolean isInExtName(String targetValue) {
        return ImgUtil.typeSet.get(targetValue)!=null ;
//        for (String s : arr) {
//            if (s.equalsIgnoreCase(targetValue)) {
//                return true;
//            }
//        }
//        return false;
    }

    /**
     * 去掉结尾的@符号以后的扩展部分
     *
     * @param imgPath
     * @return
     */
    public static String trimImgAtExt(String imgPath){
        if(StringUtils.isNotEmpty(imgPath) && imgPath.indexOf('@')>0){
             return imgPath.substring(0,imgPath.lastIndexOf('@'));
        }
        return imgPath;
    }

}

