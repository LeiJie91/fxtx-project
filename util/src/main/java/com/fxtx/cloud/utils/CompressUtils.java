package com.fxtx.cloud.utils;

import com.fxtx.framework.util.LogUtils;
import com.fxtx.framework.util.upload.FileUploadUtils;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.exception.RarException.RarExceptionType;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2016/12/21.
 */
public class CompressUtils {
    private static Logger logger = LogUtils.getAccessLog();

    /**
     * 解压文件
     *
     * @param inputFilePath
     * @param outputPath
     * @return
     */
    public static boolean unCompress(String inputFilePath, String outputPath){
        String ext = FileUploadUtils.getExtension(inputFilePath);
        boolean flag = false;
        if(StringUtils.isNotEmpty(ext)){
            ext = ext.toLowerCase();
            if("zip".equals(ext)){
                flag = unzip(inputFilePath,outputPath);
            }else if("rar".equals(ext)){
                flag = unrar(inputFilePath,outputPath);
            }
        }
        return flag;
    }
    /**
     * 压缩文件
     *
     * @param zipOutFile
     * @param zipInputFile
     * @return
     * @throws IOException
     */
    public static boolean zipCompressing(File zipOutFile, File zipInputFile)throws IOException {
        String source = zipInputFile.getPath();
        String dirst = zipOutFile.getPath();
        logger.info("开始压缩文件，文件输入文件目录:"+source+"，输出文件目录:"+dirst);
        boolean flag = true;
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipOutFile));
        BufferedOutputStream bos = new BufferedOutputStream(zos);
        flag = zipCompressingStream(zos, zipInputFile, zipOutFile.getAbsolutePath(),bos);
        bos.close();
        zos.close();
        logger.info("文件压缩完毕....");
        return flag;
    }

    /**
     * @param zipFileName  The name of the zip file will be generated
     * @param zipInputFile To compress the file or folder
     * @throws IOException
     */
    public static boolean zipCompressing(String zipFileName, String zipInputFile)throws IOException {
        return zipCompressing(new File(zipFileName), new File(zipInputFile));
    }

    /**
     * @param out  The implementation of file compression flow
     * @param f    To compress the file or folder
     * @param base Root node of the compressed file
     * @param bos  The compression stream buffer
     * @throws IOException
     */
    private static boolean zipCompressingStream(ZipOutputStream out, File f,String base, BufferedOutputStream bos) throws IOException {
        boolean flag = true;
        if (f.isDirectory()) {
            File[] f1 = f.listFiles();
            if (f1.length == 0) {
                out.putNextEntry(new ZipEntry(base + "/"));
            } else {
                for (int i = 0; i < f1.length; i++) {
                    File file = f1[i];
                    flag = flag && zipCompressingStream(out, file, file.getName(), bos);
                }
            }

        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);
            int b;

            while ((b = bis.read()) != -1) {
                bos.write(b);
            }
            bos.flush();
            bis.close();
            in.close();
            flag = true;
        }
        return flag;
    }

    /**
     * 解压zip文件
     *
     * @param zipFileName
     * @param outputPath
     * @throws IOException
     */
    public static boolean unzip(String zipFileName, String outputPath){
        logger.info("开始解压文件...");
        boolean flag = false;
        try {
            File zipFile = new File(zipFileName);
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            BufferedInputStream bis = new BufferedInputStream(zis);
            File fOut = null;
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                if(entry.isDirectory()){
                    continue;
                }
                fOut = new File(outputPath, entry.getName());
                if (!fOut.exists()) {
                    (new File(fOut.getParent())).mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(fOut);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int b;
                while ((b = bis.read()) != -1) {
                    bos.write(b);
                }
                bos.close();
                fos.close();
            }
            bis.close();
            zis.close();
            flag = true;
            logger.info("文件解压完成");
        }catch(Exception e){
            logger.info("文件解压失败");
            LogUtils.logError("CompressUtils",e);
        }
        return flag;

    }


    /**
     * 解压rar文件
     *
     * @param rarFileName
     * @param outFilePath
     * @return
     */
    public static boolean unrar(String rarFileName, String outFilePath){
        boolean flag = false;
        try {
            Archive archive = new Archive(new File(rarFileName));
            if (archive == null) {
                throw new FileNotFoundException(rarFileName + " NOT FOUND!");
            }
            if (archive.isEncrypted()) {
                throw new Exception(rarFileName + " IS ENCRYPTED!");
            }
            List<FileHeader> files = archive.getFileHeaders();
            for (FileHeader fh : files) {
                if (fh.isEncrypted()) {
                    throw new Exception(rarFileName + " IS ENCRYPTED!");
                }
                String fileName = fh.getFileNameW();
                if (fileName != null && fileName.trim().length() > 0) {
                    String saveFileName = outFilePath + "\\" + fileName;
                    File saveFile = new File(saveFileName);
                    File parent = saveFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    if (!saveFile.exists()) {
                        saveFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(saveFile);
                    try {
                        archive.extractFile(fh, fos);
                        fos.flush();
                        fos.close();
                    } catch (RarException e) {
                        if (e.getType().equals(RarExceptionType.notImplementedYet)) {

                        }
                    } finally {

                    }
                }
            }
            flag = true;
        } catch (Exception e) {
            LogUtils.logError("CompressUtils",e);
        }
        return flag;
    }
}
