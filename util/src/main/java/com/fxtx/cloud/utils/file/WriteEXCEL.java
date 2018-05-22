package com.fxtx.cloud.utils.file;

import com.fxtx.cloud.utils.json.EntityUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class WriteEXCEL {
	/**
	 * 导出excel文件名字
	 */
	private String excelName ;
	/**
	 * sheet名字
	 */
	private String sheetTitle ;
	//private String dateFormat ;
	//private String numberFormat ;
	private SimpleDateFormat dateFormat ;
	//= new SimpleDateFormat(pattern)
	private LinkedHashMap<String,String> keyTitle ;

	private HSSFWorkbook workbook = new HSSFWorkbook();
	/**
	 * 实例化导出对象
	 * @param excelName 文件名字
	 * @param sheetTitle sheet名字
	 * @param dateFormat 日期类型
	 */
	public WriteEXCEL(String excelName,String sheetTitle,String dateFormat){
		this.excelName = excelName ;
		this.sheetTitle = sheetTitle ;
		this.dateFormat = new SimpleDateFormat(dateFormat) ;
	}
	public WriteEXCEL(String excelName,String sheetTitle){
		this.excelName = excelName ;
		this.sheetTitle = sheetTitle ;
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
	}
	/**
	 * 执行导出
	 * @param <E>
	 * @param response 相应对象
	 * @param dateList	数据列表
	 * @param keyTitle	读取对象属性
	 */
	public <E> void  write(HttpServletResponse response,List<E> dateList,LinkedHashMap<String,String> keyTitle,String sheetName,int startRow){
		this.keyTitle = keyTitle ;
		// 声明一个工作薄
		// HSSFWorkbook workbook = new HSSFWorkbook();
	    // 生成一个表格
	    //sheetTitle = sheetName==null?sheetTitle:sheetName ;
	    HSSFSheet sheet = workbook.getSheet(sheetTitle);
		if(sheet == null){
			sheet = workbook.createSheet(sheetTitle);
		}
		setColumnWidth(sheet);
	    setTitle(sheet,workbook,startRow) ;
	    setContent(sheet, dateList,startRow) ;
	    OutputStream out = exportHeader(response) ;
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
	public <E> void  write(HttpServletResponse response,List<E> dateList,LinkedHashMap<String,String> keyTitle){
		write(response,dateList,keyTitle,null,0);
	}
	public <E> void  write(HttpServletResponse response,List<E> dateList,LinkedHashMap<String,String> keyTitle,int startRow){
		write(response,dateList,keyTitle,null,startRow);
	}
    /****************************************内部用的私人方法*****************************************************/
	/**
	 * 设置表内容
	 */
	private <E> void setContent(HSSFSheet sheet ,List<E> dateList,int startRow){
		for (int i = 0; i < dateList.size(); i++) {
	    	HSSFRow row = sheet.createRow(i+startRow+1);
			row.setHeightInPoints(18F);
	    	int index = 0 ;
		    for (String key  : keyTitle.keySet()) {
				HSSFCell cell = row.createCell(index);
				cell.setCellStyle(setCellStyle(workbook, HorizontalAlignment.LEFT, HSSFFont.COLOR_NORMAL, (short) 10, false));
				cell.setCellType(CellType.STRING);
				try {
					if(dateList.get(i) instanceof Map){
						Map map = (Map)dateList.get(i);
						cell.setCellValue(new HSSFRichTextString(formatText(map.get(key))));
					}else{
						cell.setCellValue(new HSSFRichTextString(formatText(EntityUtil.getTer(dateList.get(i), key))));
					}

				} catch (Exception e) {
					cell.setCellValue(new HSSFRichTextString(""));
				}
				index++;
			}
		}
	}
	/**
	 * 设置表头
	 */
	private void setTitle(HSSFSheet sheet,HSSFWorkbook workbook,int startRow){
		//产生表格标题行
	    HSSFRow row = sheet.createRow(startRow);
		row.setHeightInPoints(18F);
		int index =0 ;
	    for (String key  : keyTitle.keySet()) {
	        HSSFCell cell = row.createCell(index) ;
			cell.setCellStyle(setCellStyle(workbook, HorizontalAlignment.CENTER, HSSFFont.COLOR_NORMAL, (short) 10, true));
	    	cell.setCellValue(new HSSFRichTextString(keyTitle.get(key)));
	    	index++ ;
		}
	}

	/**
	 * 设置列宽
	 * @param sheet
	 */
	private void setColumnWidth(HSSFSheet sheet){
		int index = 0 ;
		for (String key  : keyTitle.keySet()) {
			String valueTemp = keyTitle.get(key) ;
			String[] valueArr = valueTemp.split(",") ;
			if(valueArr.length>1){
				keyTitle.put(key,valueArr[0]) ;
				Integer width = Integer.valueOf(valueArr[1]) ;
				width = width*600 ;
				sheet.setColumnWidth(index, width);
			}
			index++ ;
		}
	}
	/**
	 * 格式化读取的内容
	 */
	private <E> String formatText(E entity){
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
    private OutputStream exportHeader(
			HttpServletResponse response){
		try {
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

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	/**
	 * 设置单元格样式
	 * @param workbook
	 * @param align
	 * @param color
	 * @param fontSize
	 * @return
	 */
	private HSSFCellStyle setCellStyle(HSSFWorkbook workbook, HorizontalAlignment align, short color, short fontSize, Boolean bold) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		style.setAlignment(align);
		font.setBold(bold);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setFont(font);
		style.setFillBackgroundColor(color);
		return style;
	}
}
