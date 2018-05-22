/**
 *  解析excel(2007)
 * @author nldjyhl
 * poi-3.7.jar
 * poi-ooxml-3.7.jar
 * poi-ooxml-schemas-3.7.jar
 * xmlbeans-2.3.0.jar
 *	2013-01-07
 */
package com.fxtx.cloud.utils.file;

import com.fxtx.cloud.utils.json.EntityUtil;
import com.fxtx.framework.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public class ReadEXCEL {
    /**
     * 日期读取格式
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    /**
     * 数字读取格式
     */
    private String numberFormat = "#########.#######";
    /**
     * 非空列
     */
    private Integer[] notNullColumn = {0};
    /**
     * 去重组合列
     */
    private Integer[] keyColumn = {0};
    /**
     * 读取总列数
     */
    private Integer columnNum = 1;

    public ReadEXCEL() {

    }

    /**
     * 简历指定参数的excel
     *
     * @param dateFormat    日期格式
     * @param numberFormat  数值格式
     * @param columnNum     读取总列数
     * @param notNullColumn 非空列集合
     * @param keyColumn     去重主键列（一般为第一列）
     */
    public ReadEXCEL(String dateFormat, String numberFormat, Integer columnNum, Integer[] notNullColumn, Integer[] keyColumn) {
        this.dateFormat = dateFormat;
        this.numberFormat = numberFormat;
        this.columnNum = columnNum;
        this.notNullColumn = notNullColumn;
        this.keyColumn = keyColumn;
    }

    /**
     * 指定列数，所有列去重，所有列非空
     *
     * @param columnNum
     */
    public ReadEXCEL(Integer columnNum) {
        this.columnNum = columnNum;
        this.notNullColumn = new Integer[columnNum];
        this.keyColumn = new Integer[columnNum];
        for (int i = 0; i < columnNum; i++) {
            this.notNullColumn[i] = i;
            this.keyColumn[i] = i;
        }
    }

    /**
     * 把excel映射成对象集合
     *
     * @param file
     * @param entityClass
     * @param properties
     * @param <E>
     * @return
     */
    public <E> ReadResult read(File file, Class<E> entityClass, String[] properties) {
        ReadResult<E> out = new ReadResult();
        if (null != file && null != entityClass && null != properties && properties.length > 0) {
            LinkedHashMap<String, String[]> readMap = null;
            if (file.getPath().endsWith(".xls") || file.getPath().endsWith(".XLS")) {
                try {
                    readMap = readXLS(file, 0);
                } catch (Exception e) {
                }

            } else if (file.getPath().endsWith(".xlsx") || file.getPath().endsWith(".XLSX")) {
                try {
                    readMap = readXLSX(file, 0);
                } catch (Exception e) {
                }
            }
            if (null != readMap && readMap.size() > 0) {
                int rowNum = 2;
                for (String key : readMap.keySet()) {
                    if (key.startsWith("errData")) {
                        out.addErr(key);
                    } else {
                        int columnNum = 1;
                        try {
                            E outOne = (E) Class.forName(entityClass.getName()).newInstance();
                            String[] dataArr = readMap.get(key);
                            for (int i = 0; i < properties.length; i++) {
                                columnNum = i + 1;
                                EntityUtil.setTer(outOne, properties[i], dataArr[i]);
                            }
                            out.addData(outOne);
                        } catch (Exception e) {
                            out.addErr("errData第" + rowNum + "行,第" + columnNum + "列,数据类型错误");
                            String err = e.getMessage();
                        }
                    }
                    rowNum++;
                }
            }
        }
        return out;
    }

    /**
     * 解析EXCEL xx.xls
     *
     * @param file
     * @return Map
     * @throws IOException
     */
    public LinkedHashMap<String, String[]> readXLS(File file, Integer sheetNum) throws IOException {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
        FileInputStream fis = new FileInputStream(file);
        try {
            Workbook workBook = null;
            try {
                workBook = new XSSFWorkbook(fis);
            } catch (Exception ex) {
                workBook = new HSSFWorkbook(fis);
            }
            Sheet sheet = workBook.getSheetAt(sheetNum);
            if (sheet != null) {
                // 循环行Row
                for (int rowNum = 3; rowNum <= sheet.getLastRowNum(); rowNum++) {// 从第二行开始
                    Row hssfRow = sheet.getRow(rowNum);
                    if (hssfRow != null) {
                        // 获取行的全部值
                        String[] arr = setDataForXLS(hssfRow);
                        if (!isEmpty(arr)) {
                            String key = makeKey(arr, rowNum);
                            if (null == map.get(key)) {
                                map.put(key, arr);
                            } else {
                                key = "errData第" + (rowNum + 1) + "行,重复导入了";
                                map.put(key, arr);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            fis.close();
        }
        return map;
    }

    /**
     * 解析EXCEL第一个Sheet xx.xls
     *
     * @param file
     * @return Map
     * @throws IOException
     */
    public LinkedHashMap<String, String[]> readXLSFirstSheet(File file) throws IOException {
        return readXLS(file, 0);
    }

    // ______________________________2007以上_________________________________

    /**
     * 解析EXCEL xx.xlsx
     *
     * @param file
     * @return LinkedHashMap
     * @throws IOException
     * @throws Exception
     */
    public LinkedHashMap<String, String[]> readXLSX(File file, Integer sheetNum) throws IOException {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
        FileInputStream fis = new FileInputStream(file);
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetNum);
            if (xssfSheet != null) {
                // 循环行Row
                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {// 从第二行开始
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (null != xssfRow) {
                        // 获取行的全部值
                        String[] arr = setData(xssfRow);
                        map.put(String.valueOf(rowNum), arr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        return map;
    }

    /**
     * 解析EXCEL的第一个sheet xx.xlsx
     *
     * @param file
     * @return LinkedHashMap
     * @throws IOException
     * @throws Exception
     */
    public LinkedHashMap<String, String[]> readXLSXFirstSheet(File file) throws IOException {
        return readXLSX(file, 0);
    }

    private String[] setData(Row row) {
        String[] arr = new String[row.getLastCellNum()];
        Integer count = 0;
        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell == null) {
                continue;
            }
            String cellStr = getValue(cell).trim();
            if(StringUtils.isNotEmpty(cellStr)){
                count = cellNum+1;
                arr[cellNum] = cellStr;
            }
        }
        return ArrayUtils.subarray(arr, 0 , count);
    }

    // 2007以上 xx.xlsx
    @SuppressWarnings("static-access")
    private String getValue(Cell cell) {
        /**
         * CELL_TYPE_NUMERIC 数值型 0 CELL_TYPE_STRING 字符串型 1 CELL_TYPE_FORMULA 公式型
         * 2 CELL_TYPE_BLANK 空值 3 CELL_TYPE_BOOLEAN 布尔型 4 CELL_TYPE_ERROR 错误 5
         */
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            HSSFDateUtil h = new HSSFDateUtil();
            if (h.isCellDateFormatted(cell)) {// 如果是时间类型
                return format(cell.getDateCellValue(), dateFormat);
            } else {
                DecimalFormat df = new DecimalFormat(numberFormat);
                return String.valueOf(df.format(cell.getNumericCellValue()));
            }
        } else if (cell.getCellType() == cell.CELL_TYPE_FORMULA) {
            return String.valueOf(cell.getCellFormula());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 读取一列数据XLS
     *
     * @param hssfRow
     * @return
     */
    private String[] setDataForXLS(Row hssfRow) {
        String[] arr = new String[columnNum];
        for (int cellNum = 0; cellNum < columnNum; cellNum++) {
            if (cellNum <= hssfRow.getLastCellNum()) {
                Cell cell = hssfRow.getCell(cellNum);
                if (cell == null) {
                    continue;
                }
                arr[cellNum] = getValueForXLS(cell).trim();
            }
        }
        return arr;
    }

    // 2007以下 xx.xls
    @SuppressWarnings("static-access")
    private String getValueForXLS(Cell cell) {

        /**
         * CELL_TYPE_NUMERIC 数值型 0 CELL_TYPE_STRING 字符串型 1 CELL_TYPE_FORMULA 公式型
         * 2 CELL_TYPE_BLANK 空值 3 CELL_TYPE_BOOLEAN 布尔型 4 CELL_TYPE_ERROR 错误 5
         */
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            HSSFDateUtil h = new HSSFDateUtil();
            if (h.isCellDateFormatted(cell)) {// 如果是时间类型
                return format(cell.getDateCellValue(), dateFormat);
            } else {
                DecimalFormat df = new DecimalFormat(numberFormat);
                return String
                        .valueOf(df.format(cell.getNumericCellValue()));
            }
        } else if (cell.getCellType() == cell.CELL_TYPE_FORMULA) {
            return String.valueOf(cell.getCellFormula());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     * @author lindujia
     */
    private String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    private boolean isEmpty(String str) {
        return null == str ? true : "".equals(str.trim());
    }

    /**
     * 检查字符串集是否为全空
     *
     * @param arr
     * @return
     */
    private boolean isEmpty(String[] arr) {
        for (String string : arr) {
            if (!isEmpty(string)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取去重Key
     *
     * @param arrString
     * @return
     */
    private String makeKey(String[] arrString, Integer rowNum) {
        String keyOut = "";
        Integer nullColumn = checkNull(arrString);
        if (null != nullColumn) {
            keyOut = "errData第" + (rowNum + 1) + "行,第" + (nullColumn + 1) + "列,不能为空";
        } else {
            for (Integer key : keyColumn) {
                keyOut += arrString[key];
            }
        }
        return keyOut;
    }

    /**
     * 检查非空
     *
     * @param arrString
     * @return
     */
    private Integer checkNull(String[] arrString) {
        for (Integer key : notNullColumn) {
            if (isEmpty(arrString[key])) {
                return key;
            }
        }
        return null;
    }
//	public static void main(String[] argv) {
//		DecimalFormat df = new DecimalFormat("##.##%");
//		System.out.println(11/3);
//
//	}

}