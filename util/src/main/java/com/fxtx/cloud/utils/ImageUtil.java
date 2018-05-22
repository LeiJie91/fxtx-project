package com.fxtx.cloud.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenlong on 2015/7/23.
 */
public class ImageUtil {

    private ImageUtil(){

    }
    /**
     * URL检查<br>
     * <br>
     * @param pInput     要检查的字符串<br>
     * @return boolean   返回检查结果<br>
     */
    public static boolean isUrl (String pInput) {
        if(pInput == null){
            return false;
        }
        String regEx = "^(http|https)//://([a-zA-Z0-9//.//-]+(//:[a-zA-"
                + "Z0-9//.&%//$//-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                + "2}|[1-9]{1}[0-9]{1}|[1-9])//.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-4][0-9]|"
                + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-"
                + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                + "-9//-]+//.)*[a-zA-Z0-9//-]+//.[a-zA-Z]{2,4})(//:[0-9]+)?(/"
                + "[^/][a-zA-Z0-9//.//,//?//'///////+&%//$//=~_//-@]*)*$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(pInput);
        return matcher.matches();
    }

    public static class FilePath {
        private String fullPath ;
        private String relativePath ;

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        public String getRelativePath() {
            return relativePath;
        }

        public void setRelativePath(String relativePath) {
            this.relativePath = relativePath;
        }
    }
    static enum FileType{
        JPG("jpg" , "image/jpeg") , PNG("png" , "image/png")  , GIF("gif" , "image/gif") ;
        public String name ;
        public String type ;
        FileType(String name , String type){
            this.name = name ;
            this.type = type ;
        }
        final static Map<String , FileType> FILE_TYPE_MAP = new HashMap<String, FileType>() ;
        static {
            for (FileType fileType: FileType.values()){
                FILE_TYPE_MAP.put(fileType.type , fileType) ;
            }
        }
    }
    public static FilePath generatePath(MultipartFile file , MultipartHttpServletRequest multipartHttpServletRequest , String smallImgBaseDir){
        Date now = new Date();
        String[] ftmp =  file.getOriginalFilename().split("\\.") ;
        String xd = smallImgBaseDir+ "/"+ DateFormatUtils.format(now, "yyyy/MM/dd") ;
        String tmp = multipartHttpServletRequest.getRealPath("/")+xd;
        File mfile = new File(tmp);
        mfile.mkdirs();
        String xdq = xd+"/"+System.currentTimeMillis()+"."+ftmp[ftmp.length-1];
        FilePath filePath = new FilePath() ;
        filePath.setRelativePath(xdq);
        filePath.setFullPath(tmp+"/"+System.currentTimeMillis()+"."+ftmp[ftmp.length-1]);
        return filePath;
    }
    public static String tailorPic (MultipartFile image, int w, int h , String generatePath) throws IOException {
        if (null == image || 0 == w || 0 == h) {
            new Exception ("哎呀，截图出错！！！");
        }
        InputStream inputStream = image.getInputStream();
        // 用ImageIO读取字节流
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        BufferedImage distin = null;
        // 返回源图片的宽度。
        int srcW = bufferedImage.getWidth();
        // 返回源图片的高度。
        int srcH = bufferedImage.getHeight();
        int x , y ;
        Double ht ;
        Double wt ;
        if (w>h){
            ht = Double.valueOf(h)/Double.valueOf(w) *srcW ;
            wt = Double.valueOf(w)/Double.valueOf(h) * ht ;
        }else{
            wt = Double.valueOf(w)/Double.valueOf(h) *srcH ;
            ht = Double.valueOf(h)/Double.valueOf(w) * wt ;
        }
        if (ht>srcH){
            wt = srcH/ht*srcW;
            ht = Double.valueOf(srcH) ;
        }
        if (wt>srcW){
            ht = srcW/wt *srcH ;
            wt = Double.valueOf(srcW) ;
        }
        // 使截图区域居中
        x = srcW / 2 - wt.intValue() / 2;
        y = srcH / 2 - ht.intValue()/ 2;
        // 生成图片
        distin = new BufferedImage(wt.intValue(), ht.intValue(), BufferedImage.TYPE_INT_RGB);
        Graphics g = distin.getGraphics();
        g.drawImage(bufferedImage, 0, 0, wt.intValue(), ht.intValue(), x, y, x + wt.intValue(), y + ht.intValue(), null);
        ImageIO.write(distin, FileType.FILE_TYPE_MAP.get(image.getContentType()).name, new File(generatePath));
        return generatePath ;
    }

    public   static BufferedImage resize(BufferedImage source,  int  targetW,  int  targetH) {
        // targetW，targetH分别表示目标长和宽
        int  type = source.getType();
        BufferedImage target = null ;
        double  sx = ( double ) targetW / source.getWidth();
        double  sy = ( double ) targetH / source.getHeight();
        //这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
        //则将下面的if else语句注释即可
        if (sx>sy)
        {
            sx = sy;
            targetW = (int )(sx * source.getWidth());
        }else {
            sy = sx;
            targetH = (int )(sy * source.getHeight());
        }
        if  (type == BufferedImage.TYPE_CUSTOM) {  //handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean  alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new  BufferedImage(cm, raster, alphaPremultiplied,  null );
        } else
            target = new  BufferedImage(targetW, targetH, type);
        Graphics2D g = target.createGraphics();
        //smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return  target;
    }
    public   static   void  generateThumImg (MultipartFile fromFile,String saveToFileStr, int  width, int  hight)            throws  Exception {
        BufferedImage srcImage = ImageIO.read(fromFile.getInputStream());
        if (width >  0  || hight >  0 )        {
            srcImage = resize(srcImage, width, hight);
        }
        File saveFile=new  File(saveToFileStr);
        ImageIO.write(srcImage, FileType.FILE_TYPE_MAP.get(fromFile.getContentType()).name, saveFile);
    }
}
