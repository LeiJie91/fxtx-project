package com.fxtx.cloud.utils.img;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import com.fxtx.framework.util.LogUtils;
import com.sun.imageio.plugins.bmp.BMPImageWriter;
import com.sun.imageio.plugins.gif.GIFImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;


/**
 * 图像压缩工具
 *
 * @author lhj
 */
@SuppressWarnings("restriction")
public class ImgUtil {
    public static LinkedHashMap<String, Integer> typeSet = new LinkedHashMap<String, Integer>();
    public static final String BASE_TITLE = "data:image/jpeg;base64,";

    static {
        typeSet.put("png", 0);
        typeSet.put("PNG", 0);

        typeSet.put("jpg", 1);
        typeSet.put("JPG", 1);

        typeSet.put("jpeg", 2);
        typeSet.put("JPEG", 2);

        typeSet.put("bmp", 3);
        typeSet.put("MBP", 3);

        typeSet.put("gif", 4);
        typeSet.put("GIF", 4);
    }

    /**
     * 重新绘制图片
     *
     * @param outFile
     * @param imgFile
     * @param iconFile
     * @throws Exception
     */
    public static void imgReloadDraw(File outFile, ImgFile imgFile, IconFile iconFile) throws Exception {
        if (null == imgFile || !imgFile.isOk()) {
            return;
        }
        FileOutputStream out = null;
        try {
            // 文件不存在时
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            if (!outFile.isFile()) {
                outFile.createNewFile();
            }
            out = new FileOutputStream(outFile);
            //新图片对象
            BufferedImage tag = new BufferedImage(imgFile.getOutWidth(), imgFile.getOutHeight(), BufferedImage.TYPE_INT_RGB);

            ImageWriter imageWriter = null;
            String suffix = outFile.getName().substring(outFile.getName().lastIndexOf(".") + 1);
            Integer suffixPoint = typeSet.get(suffix);
            switch (suffixPoint) {
                case 0:
                    imageWriter = (PNGImageWriter) ImageIO
                            .getImageWritersBySuffix(suffix).next();
                    //            设置图片透明背景
                    Graphics2D g2d = tag.createGraphics();
                    tag = g2d.getDeviceConfiguration().createCompatibleImage(imgFile.getOutWidth(), imgFile.getOutHeight(), Transparency.TRANSLUCENT);
                    g2d.dispose();
                    break;
                case 1:
                    imageWriter = (JPEGImageWriter) ImageIO
                            .getImageWritersBySuffix(suffix).next();
                    break;
                case 2:
                    imageWriter = (BMPImageWriter) ImageIO
                            .getImageWritersBySuffix(suffix).next();
                    break;
                case 3:
                    imageWriter = (GIFImageWriter) ImageIO
                            .getImageWritersBySuffix(suffix).next();
                    break;
            }


            tag.getGraphics().drawImage(imgFile.getFileImage().getScaledInstance(imgFile.getMultipleWidth(), imgFile.getMultipleHeight(), Image.SCALE_SMOOTH), imgFile.getPointX(), imgFile.getPointY(), null);


            if (null != iconFile && iconFile.isOk()) {
                Graphics2D iconG = tag.createGraphics();
                iconG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, iconFile.getAlpha()));
                // 表示水印图片的位置
                iconG.drawImage(iconFile.getIconImage().getScaledInstance(iconFile.getMultipleWidth(), iconFile.getMultipleHeight(), Image.SCALE_SMOOTH), iconFile.getPointX(imgFile.getOutWidth()), iconFile.getPointY(imgFile.getOutHeight()), null);
                iconG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                iconG.dispose();
            }
            //1.6以前包括1.6的裁剪
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
////                JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
////                jep.setQuality(0.1f, true);
//            encoder.encode(tag);


            //1.7以后包括1.7的裁剪

//            ImageWriter imageWriter = null ;
//            String suffix = outFile.getName().substring(outFile.getName().lastIndexOf(".")+1) ;
//            Integer suffixPoint = typeSet.get(suffix) ;
//            switch (suffixPoint){
//                case 0:
//                    imageWriter = (PNGImageWriter) ImageIO
//                            .getImageWritersBySuffix(suffix).next();
//                    break;
//                case 1:
//                    imageWriter = (JPEGImageWriter) ImageIO
//                            .getImageWritersBySuffix(suffix).next();
//                    break;
//                case 2:
//                    imageWriter = (BMPImageWriter) ImageIO
//                            .getImageWritersBySuffix(suffix).next();
//                    break;
//                case 3:
//                    imageWriter = (GIFImageWriter) ImageIO
//                            .getImageWritersBySuffix(suffix).next();
//                    break;
//            }

//            JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO
//                    .getImageWritersBySuffix(suffix).next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            imageWriter.setOutput(ios);
            IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
                    new ImageTypeSpecifier(tag), null);

            //压缩质量，暂时用不上
//            if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
//
//                // old compression
//                // jpegEncodeParam.setQuality(JPEGcompression,false);
//
//                // new Compression
//                JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter
//                        .getDefaultWriteParam();
//                jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
//                jpegParams.setCompressionQuality(JPEGcompression);
//
//            }
            imageWriter.write(imageMetaData,
                    new IIOImage(tag, null, null), null);
            ios.close();
            imageWriter.dispose();


            if (null != iconFile && iconFile.isOk()) {
                iconFile.getIconImage().flush();
            }
            imgFile.getFileImage().flush();
        } catch (Exception e) {
            String errMsg = e.toString();
            System.out.println(errMsg);
        } finally {
            if (out != null) out.close();
        }
    }

    /**
     * 图片压缩
     *
     * @param inFile
     * @param outFile
     * @param outWidth  压缩后的最小宽度
     * @param outHeight 压缩后的最小高度
     */
    public static void imgReduce(File inFile, File outFile, Integer outWidth, Integer outHeight) throws Exception {
        ImgFile imgFile = new ImgFile(inFile, ImgFile.ImgType.压缩, outWidth, outHeight, null);
        imgReloadDraw(outFile, imgFile, null);
    }

    /**
     * 图片居中裁剪
     *
     * @param inFile
     * @param outFile
     * @param outWidth    裁剪后图片大小
     * @param outHeight
     * @param borderWidth
     */
    public static void imgCut(File inFile, File outFile, Integer outWidth, Integer outHeight, Integer borderWidth) throws Exception {
        ImgFile imgFile = new ImgFile(inFile, ImgFile.ImgType.裁剪, outWidth, outHeight, borderWidth);
        imgReloadDraw(outFile, imgFile, null);
    }

    /**
     * 图片居中裁剪，不去边界
     *
     * @param inFile
     * @param outFile
     * @param outWidth
     * @param outHeight
     */
    public static void imgCut(File inFile, File outFile, Integer outWidth, Integer outHeight) throws Exception {
        imgCut(inFile, outFile, outWidth, outHeight, null);
    }

    /**
     * 获取图片大小
     *
     * @param imgFile
     * @return
     */
    public static long getImgSizes(File imgFile) {//取得文件大小
        long s = 0;
        if (imgFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imgFile);
                s = fis.available();
            } catch (Exception e) {
            }
        }
        return s;
    }

    /**
     * 判断图片是否RGB模式的图片
     *
     * @param imgFile
     * @return
     */
    public static boolean isRGB(File imgFile) {
        try {
            Image fileImage = ImageIO.read(imgFile);
            fileImage.flush();
            return true;
        } catch (Exception e) {
            if (imgFile.isFile()) {
                imgFile.delete();
            }
            return false;
        }
    }

    /**
     * 文件拷贝
     *
     * @param inFile
     * @param outFile
     * @throws Exception
     */
    public static void copy(File inFile, File outFile) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            if (!inFile.isFile() || getImgSizes(inFile) <= 0) {
                return;
            }
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            is = new FileInputStream(inFile);
            os = new FileOutputStream(outFile);
            byte[] data = new byte[2048];
            Integer length = 0;
            while ((length = is.read(data)) != -1) {
                os.write(data, 0, length);
            }
        } finally {
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
    }

    public static String getImageSizeByBufferedImage(String src) {
        File file = new File(src);
        if (file.exists()) {
            return getImageSizeByBufferedImage(file);
        }
        return null;
    }

    public static String getImageSizeByBufferedImage(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(is);
            return sourceImg.getWidth() + "*" + sourceImg.getHeight();
        } catch (IOException e1) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    LogUtils.logError("ImgUtil", e);
                }
            }

        }
        return null;
    }

    public static String getImageSizeByBufferedImage(MultipartFile file) {
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(is);
            return sourceImg.getWidth() + "*" + sourceImg.getHeight();
        } catch (IOException e1) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    LogUtils.logError("ImgUtil", e);
                }
            }

        }
        return null;
    }

    public static void main(String[] args) {
        String data = getImageStrFromPath("d:\\baike_logo.gif");
        String str = "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";
        generateImage(str, new File("d:\\aa.jpeg"));
        System.out.println(data);
    }

    /**
     * 通过url获取图片
     *
     * @param imgURL
     * @return
     */
    public static String getImageStrFromUrl(String imgURL) {
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            data = new byte[inStream.available()];
            inStream.read(data);
            inStream.close();
        } catch (IOException e) {
            LogUtils.logError("ImgUtil", e);
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * 获取文件base64
     *
     * @param imgPath
     * @return
     */
    public static String getImageStrFromPath(String imgPath) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * base64转换成图片
     *
     * @param imgStr
     * @param imgFile
     * @return
     */
    public static boolean generateImage(String imgStr, File imgFile) {

        if (imgStr == null) // 图像数据为空
            return false;
        if (imgStr.startsWith(BASE_TITLE)) {
            imgStr = StringUtils.replace(imgStr, BASE_TITLE, "");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            if (imgFile.getParentFile() != null) {
                imgFile.getParentFile().mkdir();
            }
            OutputStream out = new FileOutputStream(imgFile);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * base64转换成图片
     *
     * @param imgStr
     * @return
     */
    public static byte[] generateImageByte(String imgStr) {

        if (imgStr == null){
            return null;
        }
        if (imgStr.startsWith(BASE_TITLE)) {
            imgStr = StringUtils.replace(imgStr, BASE_TITLE, "");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            return b;
        } catch (Exception e) {
            LogUtils.logError("ImgUtil", e);
        }
        return null;
    }

}