package com.fxtx.cloud.utils.img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

/**
 * Created by nldjy on 2015/10/21.
 */
public class IconFile {
    //水印位置
    public static enum IconPoint{
        左上(0) , 右上(1),右下(2),左下(3),居中(4) ;
        public int code ;
        IconPoint(int code){
            this.code = code ;
        }
    }
    private File inFile ;
    private Integer fileWidth ;
    private Integer fileHeight ;
    private Integer outWidth ;
    private Integer outHeight ;
    private Integer pointBorderWidth ;
    private Float alpha ;
    private IconPoint iconPoint ;

    private Image iconImage ;
    private Integer multipleWidth ;
    private Integer multipleHeight ;

    private boolean ok = false ;

    public IconFile(File inFile, IconPoint iconPoint, Integer outWidth, Integer outHeight, Integer pointBorderWidth, Float alpha){
        if(null==pointBorderWidth){
            pointBorderWidth = 0 ;
        }
        if(null==alpha){
            alpha = 1f ;
        }
        this.inFile = inFile ;
        this.iconPoint = iconPoint ;
        this.outWidth = outWidth ;
        this.outHeight = outHeight ;
        this.pointBorderWidth = pointBorderWidth ;
        this.alpha = alpha ;

        try {
            iconImage = ImageIO.read(inFile);
            // 获取图片的实际大小 宽度
            this.fileWidth = iconImage.getWidth(null) ;
            // 获取图片的实际大小 高度
            this.fileHeight = iconImage.getHeight(null) ;
            double multiple = 1d ;
            if(fileWidth.doubleValue()/outWidth.doubleValue()<fileHeight.doubleValue()/outHeight.doubleValue()){
                multiple = outWidth.doubleValue()/fileWidth.doubleValue() ;
            }else {
                multiple = outHeight.doubleValue() / fileHeight.doubleValue();
            }
            if(multiple>1){
                this.outHeight =  Double.valueOf(this.outHeight/multiple).intValue() ;
                this.outWidth = Double.valueOf(this.outWidth/multiple).intValue() ;
                multiple = 1d;
            }
            this.multipleWidth = ((Double)(this.fileWidth*multiple)).intValue();
            this.multipleHeight = ((Double)(this.fileHeight*multiple)).intValue();
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
    public Integer getPointX(Integer backgroundWidth) {
        if(iconPoint.code== IconPoint.左上.code){
            return pointBorderWidth ;
        }else if(iconPoint.code== IconPoint.右上.code){
            return backgroundWidth-multipleWidth-pointBorderWidth ;
        }else if(iconPoint.code== IconPoint.左下.code){
            return pointBorderWidth ;
        }else if(iconPoint.code== IconPoint.居中.code){
            return (backgroundWidth-multipleWidth)/2 ;
        }else{
            //null==iconPoint || iconPoint.code==IconPoint.右下.code
            return backgroundWidth-multipleWidth-pointBorderWidth ;
        }
    }
    //获取竖向位移
    public Integer getPointY(Integer backgroundHeight) {
        if(iconPoint.code== IconPoint.左上.code){
            return pointBorderWidth ;
        }else if(iconPoint.code== IconPoint.右上.code){
            return pointBorderWidth ;
        }else if(iconPoint.code== IconPoint.左下.code){
            return backgroundHeight-multipleHeight-pointBorderWidth ;
        }else if(iconPoint.code== IconPoint.居中.code){
            return (backgroundHeight-multipleHeight)/2 ;
        }else{
            //null==iconPoint || iconPoint.code==IconPoint.右下.code
            return backgroundHeight-multipleHeight-pointBorderWidth ;
        }
    }
    //获取边界位移宽度
    public Integer getPointBorderWidth() {
        return pointBorderWidth;
    }
    //获取透明度
    public Float getAlpha() {
        return alpha;
    }
    //获取图片处理方式
    public IconPoint getIconPoint() {
        return iconPoint;
    }
    //获取图片文件对象
    public Image getIconImage(){
        return iconImage ;
    }
}
