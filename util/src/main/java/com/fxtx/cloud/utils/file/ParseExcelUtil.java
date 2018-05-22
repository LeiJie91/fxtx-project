package com.fxtx.cloud.utils.file;

import cn.jpush.api.utils.StringUtils;
import com.fxtx.cloud.utils.CharUtils;
import com.fxtx.framework.util.upload.FileUploadUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public final class ParseExcelUtil {
    private static int breanOnResolveEmptyRowSize = 20;

    private ParseExcelUtil() {

    }

    /**
     * 根据Excel文件模板的路径创建 poi Workbook对象
     *
     * @param temPath excel模板文件路径
     * @return
     */
    public static Workbook getExcelWorkBook(String temPath) {

        return getExcelWorkBook(new File(temPath));
    }

    /**
     * 根据Excel文件模板的路径创建 poi Workbook对象
     *
     * @param temFile excel模板文件路径
     * @return
     */
    public static Workbook getExcelWorkBook(File temFile) {
        InputStream ins = null;
        try {
            ins = new FileInputStream(temFile);
            Workbook workBook = WorkbookFactory.create(ins);
            ins.close();
            return workBook;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Map<String, Object> readExcel(String path, Integer beginNum, int columnSize) throws IOException {
        return readExcel(new File(path), beginNum, columnSize);
    }

    public static Map<String, Object> readExcel(File file, Integer beginNum, int columnSize) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        String flag = "0";
        ArrayList<Excel> sheets = new ArrayList<Excel>();
        String fileName = file.getName();
        if (fileName.substring(fileName.lastIndexOf(".")).equals("xlsx")) {
            map.put("flag", flag);
            return map;
        } else {
            flag = "1";
            map.put("flag", flag);
        }
        Workbook workBook = getExcelWorkBook(file);

        for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
            Sheet sheet = workBook.getSheetAt(numSheet);
            //获取sheet页名称
//                String sheetName = sheet.getSheetName();
            //初始化Excel
            Excel excel = new Excel();
//                excel.setSheetName(sheetName);
            excel.setSheetContent(null);

            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            if (sheet == null) {
                continue;
            }
            // 循环行data
            int serialNum = 0;
            for (int rowNum = beginNum; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                // 循环列Cell
                ArrayList<String> arrCell = new ArrayList<String>();
                boolean emptyFlag = true;
                for (int cellNum = 0; cellNum <= row.getLastCellNum() - 1 && cellNum < columnSize; cellNum++) {
                    Cell cell = row.getCell((short) cellNum);
                    if (cell == null) {
                        String str = "";
                        arrCell.add(str);
                        continue;
                    }
                    String cellValue = getValue(cell);
                    if(StringUtils.isEmpty(cellValue)){
                        String str = "";
                        arrCell.add(str);
                        continue;
                    }
                    arrCell.add(getValue(cell));
                    if (emptyFlag) {
                        emptyFlag = false;
                    }
                }
                if (emptyFlag) {
                    ++serialNum;
                } else {
                    data.add(arrCell);
                    serialNum = 0;
                }
                if (serialNum >= breanOnResolveEmptyRowSize) {
                    break;
                }

            }
            excel.setSheetContent(data);
            sheets.add(numSheet, excel);
        }
        map.put("data", sheets);
        return map;
    }

    /**
     * 获取单元格内容
     *
     * @param cell 单元格对象
     * @return
     */
    public static String getValue(Cell cell) {
        String str = "";
        if (cell != null) {
            int cellType = cell.getCellType();

            switch (cellType) {
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    str = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    str = String.valueOf(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cell.setCellType(CellType.STRING);
                    str = String.valueOf(cell.getStringCellValue());
                    break;
            }
            return CharUtils.replaceBlank(str);
        } else {
            return "";
        }

    }

    /**
     * 获取excel格式文件
     *
     * @param parentPath
     * @return
     */
    public static File getExcelFile(String parentPath) {
        if (StringUtils.isNotEmpty(parentPath)) {
            File file = new File(parentPath);
            if (file.exists()) {
                File[] listFile = file.listFiles(new FxFileNameFilter("xls"));
                if (listFile != null && listFile.length > 0) {
                    return listFile[0];
                }
            }
        }
        return null;
    }

    /**
     * 获取第一个xls路径
     *
     * @param parentPath
     * @return
     */
    public static File getXlsFile(String parentPath) {
        if (StringUtils.isNotEmpty(parentPath)) {
            File file = new File(parentPath);
            return getXlsFile(file);
        }
        return null;

    }

    /**
     * 获取第一个xls路径
     *
     * @param dirFile
     * @return
     */
    public static File getXlsFile(File dirFile) {
        File[] files = dirFile.listFiles();
        boolean findFlag = false;
        if (files != null && files.length > 0) {
            int dirDept = 0;
            String parentDir = null;
            for (File file : files) {
                if (file.isFile()) {
                    String filePath = file.getPath();
                    String ext = FileUploadUtils.getExtension(filePath);
                    if ("xls".equals(ext)) {
                        findFlag = true;
                        return file;
                    }
                } else {
                    dirDept++;
                    parentDir = file.getPath();
                }
            }
            if (dirDept > 1) {
                return null;
            } else {
                return getXlsFile(parentDir);
            }
        }
        return null;
    }

    public static List<File> sortFile(File[] fileArray) {
        if (fileArray != null && fileArray.length > 0) {
            List list = Arrays.asList(fileArray);
            Collections.sort(list, new Comparator<File>() {
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return list;
        }
        return null;
    }

    public static void main(String args[]) {
        File filePath = getXlsFile("D:\\临时目录\\data");
        System.out.println(filePath);
    }
}
