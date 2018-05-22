package com.fxtx.cloud.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/25.
 */
public class FxtxLogUtils {
    /**
     */
    private static File logFile ;
    private static String logDirPath  = "/data/server/logs/zspfsc-test";
    private static String logFileName ;
    private static String logDate;
    public static String appName = "all" ;
    private final static SimpleDateFormat format =  new SimpleDateFormat("yyyyMMdd") ;
    private final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     */
    private static void init(){
        logDate = format.format(new Date()) ;
        logFileName = "fxtxLog("+appName+")"+logDate+".txt" ;
        logFile = new File(logDirPath.replaceAll("/$","")+"/"+logFileName) ;
        File logDir = new File(logDirPath) ;
        if(!logDir.exists()){
            logDir.mkdirs();
        }
        if(!logFile.exists()){
            try {
                logFile.createNewFile();
                String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) ;
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(logFile, true));
                fileWriter.write(""+createTime+"\r\n");
                fileWriter.write("=================================================="+"\r\n");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void writeToLog(String className, String method, Map params, String resultStr){
        StringBuffer paramString = new StringBuffer("");
        if(params!=null){
            for (Object key : params.keySet()) {
                Object value = params.get(key);
                if (null!=value && File.class.toString().equals(value.getClass().toString())) {
                    File file = (File) value;
                        /* 当文件不为空，把文件包装并且上传*/
                    if (file != null) {
                        if (null != value && !value.toString().trim().equals("")) {
                            paramString.append(key);
                            paramString.append("=");
                            paramString.append(file.getName());
                            paramString.append("&");
                        }
                    }
                } else if(null!=value && value instanceof MultipartFile){
                    MultipartFile mFile = (MultipartFile) value ;
                    if (mFile != null) {
                        if (null != value && !value.toString().trim().equals("")) {
                            paramString.append(key);
                            paramString.append("=");
                            paramString.append(mFile.getOriginalFilename());
                            paramString.append("&");
                        }
                    }
                }  else if(null!=value) {
                    if (null != value && !value.toString().trim().equals("")) {
                        paramString.append(key);
                        paramString.append("=");
                        paramString.append(value);
                        paramString.append("&");
                    }
                }

            }
        }
        String[] msgStr = new String[]{
                "请求参数：" + paramString.toString(),
                "附加内容：" + resultStr
        };
        checkIfNeedInit();
        try{
            writeMessage(className,method,msgStr);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void writeToLog(Class classes, String method, Map params, String resultStr){
        String className = classes.getName();
        writeToLog(className,method,params,resultStr);
    }

    /**
     * @param msgStr
     * @return
     */
    public static boolean writeToLog(String className, String method, String... msgStr){
        if(null== msgStr || msgStr.length<=0){
            return false ;
        }
        checkIfNeedInit();
        try{
            writeMessage(className,method,msgStr);
            return true ;
        }catch(Exception e){
            e.printStackTrace();
            return false ;
        }
    }
    /**
     * @param msgStr
     * @return
     */
    public static void writeToLog(Class classes, String method, String... msgStr){
        String className = classes.getName();
        writeToLog(className,method,msgStr);
    }
//    static {
//        FileLogUtils.class.getResource("/") ;
//        logDirPath = FileLogUtils.class.getResource("/").getPath() ;
//    }

    private static void checkIfNeedInit(){
        if( null==logFile|| !logFile.exists() || !format.format(new Date()).equals(logDate)|| !("fxtxLog("+appName+")"+logDate+".txt").equals(logFileName)){
            init();
        }
    }

    private synchronized static void writeMessage(String className, String method, String[] contents){
        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(logFile, true));
            fileWriter.write("["+dateTimeFormat.format(new Date())+"]["+className+"]["+method+"]"+"\r\n");
            for (String one:contents){
                fileWriter.write(one+"\r\n");
            }
            fileWriter.write("---------------------------------------------\r\n");
            fileWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
