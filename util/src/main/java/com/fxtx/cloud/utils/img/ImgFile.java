package com.fxtx.cloud.utils.img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

/**
 * Created by nldjy on 2015/10/21.
 */
public class ImgFile {
    //水印位置
    public static enum ImgType{
        裁剪(0) , 压缩(1) ;
        public int code ;
        ImgType(int code){
            this.code = code ;
        }
    }
    private File inFile ;

    private Integer fileWidth ;
    private Integer fileHeight ;
    private Integer outWidth ;
    private Integer outHeight ;
    private Integer cutBorderWidth ;
    private ImgType imgType ;

    private Image fileImage ;
    private Integer multipleWidth ;
    private Integer multipleHeight ;
    private Integer pointX = 0 ;
    private Integer pointY = 0 ;

    private boolean ok = false ;
    public ImgFile(File inFile, ImgType imgType, Integer outWidth, Integer outHeight, Integer cutBorderWidth){
        if(null==cutBorderWidth){
            cutBorderWidth = 0 ;
        }
        this.inFile = inFile ;
        this.imgType = imgType ;
        this.outWidth = outWidth ;
        this.outHeight = outHeight ;
        this.cutBorderWidth = cutBorderWidth ;
        try {
            fileImage = ImageIO.read(inFile);
            // 获取图片的实际大小 宽度
            Integer inWidth = fileImage.getWidth(null)-cutBorderWidth*2;
            this.fileWidth = fileImage.getWidth(null) ;
            // 获取图片的实际大小 高度
            Integer inHeight = fileImage.getHeight(null)-cutBorderWidth*2;
            this.fileHeight = fileImage.getHeight(null) ;
            double multiple = 1d ;
            if(this.imgType.code== ImgType.压缩.code){
                if(inWidth.doubleValue()/outWidth.doubleValue()<inHeight.doubleValue()/outHeight.doubleValue()){
                    multiple = outWidth.doubleValue()/inWidth.doubleValue() ;
                    this.outHeight = Double.valueOf(inHeight*multiple).intValue();
                }else {
                    multiple = outHeight.doubleValue() / inHeight.doubleValue();
                    this.outWidth = Double.valueOf(inWidth*multiple).intValue();
                }
            }else if(this.imgType.code== ImgType.裁剪.code){
                if(inWidth.doubleValue()/outWidth.doubleValue()<inHeight.doubleValue()/outHeight.doubleValue()){
                    multiple = outWidth.doubleValue()/inWidth.doubleValue() ;
                }else {
                    multiple = outHeight.doubleValue()/inHeight.doubleValue() ;
                }
            }
            if(multiple>1){
                this.outHeight =  Double.valueOf(this.outHeight/multiple).intValue() ;
                this.outWidth = Double.valueOf(this.outWidth/multiple).intValue() ;
                multiple = 1d;
            }
            this.multipleWidth = ((Double)(this.fileWidth*multiple)).intValue();
            this.multipleHeight = ((Double)(this.fileHeight*multiple)).intValue();
            pointX = (this.outWidth-this.multipleWidth)/2 ;
            pointY = (this.outHeight-this.multipleHeight)/2 ;
            ok = true ;
        }catch (Exception e){
            ok = false ;
        }
    }
    //是否可以使用
    public boolean isOk(){
        return this.ok ;
    }
    //输出图片宽
    public Integer getOutWidth(){
        return this.outWidth ;
    }
    //输出图片高
    public Integer getOutHeight(){
        return this.outHeight ;
    }
    //输出压缩后原图宽
    public Integer getMultipleWidth() {
        return multipleWidth;
    }
    //输出压缩后原图高
    public Integer getMultipleHeight() {
        return multipleHeight;
    }
    //获取原图文件
    public File getInFile() {
        return inFile;
    }
    //获取横向位移
    public Integer getPointX() {
        return pointX;
    }
    //获取竖向位移
    public Integer getPointY() {
        return pointY;
    }
    //获取裁剪边界宽度
    public Integer getCutBorderWidth(){
        return this.cutBorderWidth ;
    }
    //获取图片处理方式
    public ImgType getImgType(){
        return imgType ;
    }
    //获取图片文件对象
    public Image getFileImage(){
        return fileImage ;
    }
}
