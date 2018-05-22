package com.fxtx.cloud.utils.file;

//import org.apache.poi.hssf.model.Sheet;
import com.fxtx.cloud.utils.date.DateUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class WriteExcelUtils {

    /**
     * Excel导出方法
     * @param wb          ----- poi 对象
     * @param datas       ----- 待写入Excel文件的数据
     * @param filePath    ----- poi对象添加完数据后，需要将poi对象数据写入Excel, 此参数为待生成Excel文件的路径
     */
    public static void exportExcel(Workbook wb, List<List<Object>> datas, String filePath, String fileName){

        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
//        font.setColor(HSSFColor.GREY_50_PERCENT.index);
//        font.setBoldweight((short)-8);
        CellStyle style1 = wb.createCellStyle();
        style1.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        style1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style1.setFillPattern(FillPatternType.NO_FILL);
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setFont(font);
        CellStyle style2 = wb.createCellStyle();
        style2.setFont(font);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);

        Sheet sheet = null;
        for(int x=0; x < datas.size(); x++){
            if(x % 100 == 0){
                sheet = wb.getSheetAt(x);
                if(sheet == null){
                    sheet = wb.createSheet();
                }
            }
            Row row = null;
            row = sheet.getRow(x+1);
            if(row == null){
                row = sheet.createRow(x+1);
            }
            row.setHeight((short)480);
            for(int y=0; y< datas.get(x).size(); y++){
                Object value = datas.get(x).get(y);
                Cell cell = null;
                cell = row.getCell((short)y);
                if(cell == null){
                    cell = row.createCell((short)y);
                }
                cell.setCellStyle(style2);
//                cell.setEncoding(Cell.ENCODING_UTF_16);
                if(value instanceof String){
                    cell.setCellValue((String)value);
                }else if(value instanceof Date){
                    cell.setCellValue(DateUtil.dateToStr((Date) value, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        }

        createExcelFile(wb, filePath, fileName);
    }

    /**
     * 生成excel
     * @param wb        excel工作薄
     * @param filename  文件名称
     */
    public static void createExcelFile(Workbook wb, String filePath, String filename) {
        FileOutputStream out = null;
        try {
            File file = new File(filePath);
            if(!file.exists()){
                file.mkdirs();
            }
            out = new FileOutputStream(filePath+filename);
            wb.write(out);
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

}
