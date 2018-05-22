package com.fxtx.cloud.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class QRCodeUtils {

    /**
     * 生成二维码
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param format 生成二维码后缀名
     * @param content 二维码内容
     * @param targetDir 生成图片存放路径
     * @return
     */
    public static Map<String, Object> createQRCode(Integer width, Integer height, String format, String content, String targetDir){
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            Hashtable hints= new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            File outputFile = new File(targetDir);
            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
            result.put("flag", true);
            result.put("msg", "生成成功");
        }catch (Exception e){
            e.printStackTrace();
            result.put("flag", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

}
