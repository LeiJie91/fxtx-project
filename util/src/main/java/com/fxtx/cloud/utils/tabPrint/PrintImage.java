package com.fxtx.cloud.utils.tabPrint;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
/**
 * ŽòÓ¡ÍŒÆ¬µÄÀà
 * @author tianmaochun
 *
 */
public class PrintImage {
    public void drawImage(String fileName, int count){
        try {
            DocFlavor dof = null;

            if(fileName.endsWith(".gif")){
                dof = DocFlavor.INPUT_STREAM.GIF;
            }else if(fileName.endsWith(".jpg")){
                dof = DocFlavor.INPUT_STREAM.JPEG;
            }else if(fileName.endsWith(".png")){
                dof = DocFlavor.INPUT_STREAM.PNG;
            }

            PrintService ps = PrintServiceLookup.lookupDefaultPrintService();

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

            PrintService pss[] = PrintServiceLookup.lookupPrintServices(dof, pras);
            if (pss.length == 0) {
                //没有获取打印机，终止程序
                return;
            }

            pras.add(OrientationRequested.PORTRAIT);

            pras.add(new Copies(count));
            pras.add(PrintQuality.HIGH);

            pras.add(MediaSizeName.ISO_A6);
            BufferedImage bufferedImage = ImageIO.read(new File(fileName));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            DocAttributeSet das = new HashDocAttributeSet();
            das.add(new MediaPrintableArea(0, 0, width/2, height/2, MediaPrintableArea.MM));
            FileInputStream fin = new FileInputStream(fileName);

            Doc doc = new SimpleDoc(fin ,dof, das);

            DocPrintJob job = ps.createPrintJob();

            job.print(doc, pras);
            fin.close();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        catch (PrintException pe) {
            pe.printStackTrace();
        }
    }

}