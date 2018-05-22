package com.fxtx.cloud.utils.file;

import com.fxtx.cloud.utils.date.DateUtil;
import com.fxtx.cloud.utils.json.EntityUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @test
 * 			 @RequestMapping(value = "download")
public ModelAndView download(HttpServletResponse response,SignAttendMonthSearch search,@RequestParam(value = "year", required = false) String year
,@RequestParam(value = "month", required = false) String month, Model model) {
try {
if(null==year || "".equals(year.trim())){
search.setAttendYear(null);
if(null==month || "".equals(month.trim())){
search.setAttendMonth(null);
}else {
search.setAttendMonth("-" + month);
}
}else {
search.setAttendYear(year);
if(null==month || "".equals(month.trim())){
search.setAttendMonth(null);
}else {
search.setAttendMonth(year + "-" + month);
}
}
List<SignAttendMonth> downloadDataList = signAttendMonthService.getAllByMapList(search, new Sort("sign_name", Direction.ASC)) ;
WriteEXCEL writeEXCEL = new WriteEXCEL((search.getAttendMonth()==null?"全部":search.getAttendMonth())+"-考勤导出",search.getAttendMonth()==null?"全部":search.getAttendMonth());
LinkedHashMap<String,String> titleSet = new LinkedHashMap<String,String>();
titleSet.put("signName","姓名,5");
titleSet.put("signCode","工号,4");
titleSet.put("attendMonth","统计年月,4");
titleSet.put("normalWorkingDayNum","应出席,3");
titleSet.put("realityWorkingDayNum","实际出席,4");
titleSet.put("workingDayNumDifference","出席差,3");
titleSet.put("attendState0Num","正常,2");
titleSet.put("attendState1Num","迟到,2");
titleSet.put("attendState2Num","早退,2");
titleSet.put("attendState3Num","迟到且早退,5");
titleSet.put("attendState4Num","旷工,2");
titleSet.put("attendState5Num","事假,2");
titleSet.put("attendState6Num","病假,2");
titleSet.put("attendState7Num","调休,2");
titleSet.put("attendState8Num","加班,2");
titleSet.put("attendState9Num","休假,2");
titleSet.put("attendState10Num","忘打卡,3");
writeEXCEL.write(response,downloadDataList,titleSet);
} catch (Exception e) {
String errorMsg = "操作失败";
model.addAttribute("tips", Tips.createErrorTips(errorMsg));
LogUtils.logError(errorMsg, e);
}
return null ;
}
 * @author Administrator
 *
 */
public class FxWriteEXCEL {

	/**
	 * 模板导出
	 * @param response
	 * @param modelFile
	 * @param dateList
     * @param <E>
     */
	public static <E> void  write(HttpServletResponse response, File modelFile, List<String>... dateList){
		OutputStream out = exportHeaderXLSX(response,"导出") ;
		try {
			// 声明一个工作薄
			FileInputStream fis = new FileInputStream(modelFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			for (int i=0;i<dateList.length;i++) {
				List<String> oneDataList = dateList[i] ;
				if(null!=oneDataList){
					XSSFSheet sheet = workbook.getSheetAt(i);
					for (int x = 0; x < oneDataList.size(); x++) {
						XSSFRow row = sheet.createRow(x);
						int index = 0 ;
						XSSFCell cell = row.createCell(index);
						cell.setCellValue(new XSSFRichTextString(oneDataList.get(x)));
					}
				}
			}
			// 生成一个表格
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public <E> void  write(HttpServletResponse response, List<E> dateList, List<String> writeParam){
		write(response,null,null,Arrays.asList(dateList),Arrays.asList(writeParam),null);
	}
	public <E> void  write(HttpServletResponse response,String excelName, List<E> dateList, List<String> writeParam){
		write(response,excelName,null,Arrays.asList(dateList),Arrays.asList(writeParam),null);
	}
	public <E> void  write(HttpServletResponse response,List<List<E>> dateLists,List<List<String>> writeParams,String excelName){
		write(response,excelName,null,dateLists,writeParams,null);
	}
	/****************************************内部用的私人方法*****************************************************/

	/**
	 *
	 * @param response
	 * @param excelName
	 * @param dateFormat
	 * @param dateLists
	 * @param writeParams
	 * @param sheetNames
     * @param <E>
     */
	private static <E> void  write(HttpServletResponse response,String excelName,SimpleDateFormat dateFormat, List<List<E>> dateLists, List<List<String>> writeParams, List<String> sheetNames){
		if(dateLists==null || writeParams==null  || dateLists.size()!=writeParams.size()){
			return;
		}
		if(null==dateFormat){
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		}
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		for(int i=0;i<dateLists.size();i++){
			String sheetName = "Sheet"+i ;
			if(null!=sheetNames && null!=sheetNames.get(i)){
				sheetName = sheetNames.get(i) ;
			}
			Sheet sheet = workbook.createSheet(sheetName);
			List<Map<String,Object>> writeParam = strArrToParams(writeParams.get(i)) ;
			setColumnWidth(sheet,writeParam);
			setTitle(sheet,getTitleStyle(workbook),writeParam) ;
			setContent(sheet,dateFormat,dateLists.get(i),writeParam) ;
		}
		OutputStream out = exportHeader(response,excelName) ;
		try {
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private static String defaultExcelName(String excelName){
		if(null==excelName){
			return DateUtil.dateToStr(new Date(),"yyyyMMddHHmmssSSS")+"_导出下载";
		}else {
			return excelName ;
		}
	}
	private static List<Map<String,Object>> strArrToParams(List<String> strs){
		List<Map<String,Object>> writeParam = new ArrayList<Map<String, Object>>() ;
		for(int index=0;index<strs.size();index++){
			String[] valueArr = strs.get(index).split(":") ;
			Map<String,Object> one = new HashMap<String, Object>() ;
			one.put("title",null==valueArr?"标题":valueArr[0]);
			one.put("property",null==valueArr || valueArr.length<2 ?null:valueArr[1]);
			one.put("textNum",null==valueArr || valueArr.length<3 ?null:Integer.valueOf(valueArr[2]));
			one.put("index",index);
			writeParam.add(one) ;
		}
		return writeParam ;
	}

	private static CellStyle getTitleStyle(HSSFWorkbook workbook){
		CellStyle style = workbook.createCellStyle();
		Font f  = workbook.createFont();
		f.setBold(true);// 加粗
		style.setFont(f);
		style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		return style ;
	}

	/**
	 * 设置表内容
	 */
	private static <E> void setContent(Sheet sheet , SimpleDateFormat dateFormat, List<E> dateList, List<Map<String,Object>> writeParam){
		for (int i = 0; i < dateList.size(); i++) {
			Row row = sheet.createRow(i+1);
			for (Map<String,Object> one:writeParam) {
				Cell cell = row.createCell((Integer) one.get("index"));
				try {
					cell.setCellValue(new HSSFRichTextString(formatText(EntityUtil.getTer(dateList.get(i), one.get("property").toString()),dateFormat)));
				} catch (Exception e) {
					cell.setCellValue(new HSSFRichTextString(""));
				}
			}
		}
	}
	/**
	 * 设置表头
	 */
	private static void setTitle(Sheet sheet,CellStyle style,List<Map<String,Object>> writeParam){
		//产生表格标题行
		Row row = sheet.createRow(0);
		for (Map<String,Object> one:writeParam) {
			Cell cell = row.createCell((Integer)one.get("index")) ;
			cell.setCellStyle(style);
			cell.setCellValue(new HSSFRichTextString(one.get("title").toString()));
		}
	}

	/**
	 * 设置列宽
	 * @param sheet
	 */
	private static void setColumnWidth(Sheet sheet,List<Map<String,Object>> writeParam){
		for (Map<String,Object> one:writeParam) {
			if(null!=one.get("textNum")){
				sheet.setColumnWidth((Integer)one.get("index"), (Integer)one.get("textNum")*600);
			}
		}
	}
	/**
	 * 格式化读取的内容
	 */
	private static <E> String formatText(E entity,SimpleDateFormat dateFormat){
		if(null!=entity){
			if(entity instanceof Date || entity instanceof Timestamp){
				return dateFormat.format(entity) ;
			}else{
				return String.valueOf(entity) ;
			}
		}
		return "" ;
	}

	/**
	 * 简历输出流
	 * @param response
	 * @return
	 */
	private static OutputStream exportHeader(
			HttpServletResponse response,String excelName){
		try {
			excelName = defaultExcelName(excelName);
			String downLoadFileName = String.valueOf(System.currentTimeMillis()).trim()+".xls" ;
			if(null!=excelName && !"".equals(excelName.trim())){
				downLoadFileName = excelName+".xls" ;
			}
			downLoadFileName = new String(downLoadFileName.getBytes("utf-8"),"iso-8859-1");
			response.setCharacterEncoding("GBK");
			response.setHeader("Content-disposition",  "attachment;  filename="+downLoadFileName);
			response.setContentType("application/vnd.ms-excel");
			return response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static OutputStream exportHeaderXLSX(
			HttpServletResponse response,String excelName){
		try {
			excelName = defaultExcelName(excelName);
			String downLoadFileName = String.valueOf(System.currentTimeMillis()).trim()+".xlsx" ;
			if(null!=excelName && !"".equals(excelName.trim())){
				downLoadFileName = excelName+".xlsx" ;
			}
			downLoadFileName = new String(downLoadFileName.getBytes("utf-8"),"iso-8859-1");
			response.setCharacterEncoding("GBK");
			response.setHeader("Content-disposition",  "attachment;  filename="+downLoadFileName);
			response.setContentType("application/vnd.ms-excel");
			return response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}

