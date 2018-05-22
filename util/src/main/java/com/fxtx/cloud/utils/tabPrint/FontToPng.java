package com.fxtx.cloud.utils.tabPrint;




import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by nldjyhl_admin on 2015/7/21.
 */
public class FontToPng {
    public static void main(String[] args) throws Exception {
//        createImage("中华人民共和国国歌", new Font("宋体", Font.BOLD, 18), new File("e:/a.png"));
//        createImage("中华人民", new Font("黑体", Font.BOLD, 30), new File("e:/a1.png"));
        String printName = "e:/a2.png" ;
        createImage(new String[]{"七街",
                "石桥大酱",
                "￥20.00"
        }, new Font[]{new Font("黑体", Font.PLAIN, 38),
                new Font("黑体", Font.BOLD, 20),
                new Font("黑体", Font.BOLD, 18)
        }, new File(printName));
        PrintImage one = new PrintImage() ;
        one.drawImage(printName,1);
    }

    //根据str,font的样式以及输出文件目录
    public static void createImage(String[] outStrArr, Font[] outStrFontArr, File outFile) throws Exception {

        int width = 200;//图片宽
        int height = 400;//图片高
        int borderWidth = 10 ;//边距
        int fontSpacing = 3 ;//文字行距
        int panelSpacing = 20 ;//文字块行距

        int beginY = borderWidth ;

        //创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);

        g.fillRect(0, 0, width, height);//先用白色填充整张图片,也就是背景
        g.setColor(Color.black);//在换成黑色

        for (int n=0;n<outStrArr.length;n++){
            if(n>0){
                beginY+=panelSpacing ;
            }
            String str = outStrArr[n] ;
            Font font = outStrFontArr[n] ;
            int oneRowStrLength = (width-borderWidth*2)/font.getSize() ;//一行能放的字数
            Double strLength = Math.ceil(new Double(str.length()) / new Double(oneRowStrLength)) ;
            //获取font的样式应用在str上的整个矩形
            String strArr[] = new String[strLength.intValue()] ;
            for (int i=0;i<strLength.intValue();i++){
                int endIndex = (i*oneRowStrLength)+oneRowStrLength ;
                if(endIndex>str.length()){
                    endIndex = str.length() ;
                }
                strArr[i] = str.substring(i*oneRowStrLength,endIndex) ;
            }
            g.setFont(font);//设置画笔字体
            for (int i=0;i<strLength.intValue();i++){
                beginY+=font.getSize()+fontSpacing;
                g.drawString(strArr[i], borderWidth, beginY);//画出字符串
            }
        }
        g.dispose();
        ImageIO.write(image, "png", outFile);//输出png图片
    }
}
