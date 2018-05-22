package com.fxtx.cloud.utils.date;

import org.apache.commons.io.FilenameUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author NLDJ
 *
 */
public class DateUtil {
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public final static String PNYYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public final static String PNYYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	/**
	 * yyyy-MM-dd HH
	 */
	public final static String PNYYYYMMDDHH = "yyyy-MM-dd HH";
	/**
	 * yyyy-MM-dd
	 */
	public final static String PNYYYYMMDD = "yyyy-MM-dd";

	/**
	 * HH
	 */
	public final static String HH = "HH";
	/**
	 * HH:mm:ss
	 */
	public final static String PNHHMMSS = "HH:mm:ss";
	/**
	 * HH:mm
	 */
	public final static String PNHHMM = "HH:mm";
	/**
	 * 日期格式日期转字符串格式日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(Date date,String pattern){
		if(isEmpty(date) || isEmpty(pattern)){
			return null ;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date) ;
	}
	public static String timeStampToDateStr(long timeStamp){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time=new Long(timeStamp);
		String d = format.format(time);
		return d ;
	}
	public static Date timeStampToDate(long timeStamp){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null ;
		try{
			date=format.parse(timeStampToDateStr(timeStamp));
		}catch (Exception e){}
		return date ;
	}
	/**
	 * 日期格式转字符串格式，精确到天
	 * 默认格式 2014-12-11
	 * @param date
	 * @return
	 */
	public static String dateToStrDD(Date date){
		return dateToStr(date, PNYYYYMMDD) ;
	}
	/**
	 * 日期格式转字符串格式，精确到秒
	 * 默认格式 2014-12-11 23:45:22
	 * @param date
	 * @return
	 */
	public static String dateToStrSS(Date date){
		return dateToStr(date, PNYYYYMMDDHHMMSS) ;
	}
	/**
	 * 字符串格式日期转日期格式日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date strToDate(String date,String pattern){
		if(isEmpty(date) || isEmpty(pattern)){
			return null ;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(date) ;
		} catch (ParseException e) {
			e.printStackTrace();
			return null ;
		}
	}
	/**
	 * 字符串日期转换成Date日期（支持[PM,AM][上午,下午]）
	 * @param date
	 * @return
	 */
	public static Date strToDate(String date){
		date = date.trim() ;
		if(date.replaceAll("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{2}$", "").length()==0){
			date = date.replaceAll(".\\d{2}$", "") ;
		}else if(date.replaceAll("[a-zA-Z ]", "").replaceAll("^\\d{4}:\\d{2}:\\d{6}$", "").length()==0){
			date = eDateNumToADateNum(date) ;
		}else{
			date = treatTime(cDateNumToADateNum(date)) ;
		}
		String dateTimeStr = date.split(" +").length>1?date.split(" +")[1]:"" ;
		return strToDate(date, checkPN(dateTimeStr)) ;
	}

	/**
	 * 获得当前日期指定时间点的date对象
	 * 例如：想要获得今天9:00 的日期对象 dateSpliceTime("9:00")
	 * @param timeStr 时分秒字符串
	 * @return
	 */
	public static Date getDateByTimeStr(String timeStr){
		Date date = new Date() ;
		if(isEmpty(date) || isEmpty(timeStr)){
			return null ;
		}
		String dateStr = dateToStr(date,PNYYYYMMDD)+" "+timeStr.trim() ;
		return strToDate(dateStr) ;
	}
	/**
	 * 获得当前日期指定时间点的date对象
	 * 例如：想要获得今天9:00 的日期对象 dateSpliceTime("9:00")
	 * @param timeStr 时分秒字符串
	 * @return
	 */
	public static Date getDateByTimeStr(Date date,String timeStr){
		if(isEmpty(date) || isEmpty(timeStr)){
			return null ;
		}
		String dateStr = dateToStr(date,PNYYYYMMDD)+" "+timeStr.trim() ;
		return strToDate(dateStr) ;
	}
	public static Date geLastWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMonday(date));
		cal.add(Calendar.DATE, -7);
		return cal.getTime();
	}

	public static Date getThisWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		return cal.getTime();
	}

	public static Date getNextWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMonday(date));
		cal.add(Calendar.DATE, 7);
		return cal.getTime();
	}

	/**
	 * 获取指定日期 所在月份的上一个月或下一个月（由传入的month值而定）的第一天的Date对象
	 * @param month：0代表当前月 1是代表下一个月，-1代表上一个月 其他以此类推
	 * @return
	 */
	public static Date monthFirstDay(Date date ,int month){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateByTimeStr(date, "00:00:00"));
		cal.add(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 *  获取指定日期 所在月份的上一个月或下一个月（由传入的month值而定）的最后一天的Date对象
	 * @param month：0代表当前月 1是代表下一个月，-1代表上一个月 其他以此类推
	 * @return
	 */
	public static Date monthLastDay(Date date ,int month){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateByTimeStr(date, "23:59:59"));
		cal.add(Calendar.MONTH, month+1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 月份前进/后退{num正负数}
	 * @Author wugong
	 * @Date 2017/12/13 17:54
	 * @Modify if true,please enter your name or update time
	 * @params
	 */
	public static Date monthGapMonths(Date nowMonth,Integer num){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowMonth);
		calendar.add(Calendar.MONTH, num);
		return calendar.getTime();
	}

	/**
	 * 获取传入日期是星期几
	 * @param date
	 * @return 范围 1~7,1=星期日 7=星期六，其他类推
	 */
	public static Integer dayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 范围 1~7,1=星期日 7=星期六，其他类推
		return c.get(Calendar.DAY_OF_WEEK);
	}
	public static String dayCnOfWeek(Date date) {
		Integer numWeek = dayOfWeek(date)-1;
		String[] cnWeek = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"} ;
		return cnWeek[numWeek];
	}

	/**
	 * 是否周末，周六日算周末
	 * @param date
	 * @return 范围 1~7,1=星期日 7=星期六，其他类推
	 */
	public static boolean isWeekend(Date date) {
		Integer weekNum = dayOfWeek(date) ;
		if(weekNum.intValue()==1||weekNum.intValue()==7){
			return true ;
		}else{
			return false ;
		}
	}

	/**
	 * 获得指定日期所在周周一的 date对象
	 * 一周为 周一到周日
	 * @param date
	 * @return
	 */
	public static Date dayOfMonday(Date date) {
		int dayOfWeek = dayOfWeek(date) ;
		Date outDate ;
		if(dayOfWeek==1){
			outDate = dateGapDays(date,-6) ;
		}else {
			outDate = dateGapDays(date,-(dayOfWeek-2)) ;
		}
		return getDateByTimeStr(outDate, "00:00:00") ;
	}

	/**
	 * 获得指定日期所在周周日的 date对象
	 * 一周为 周一到周日
	 * @param date
	 * @return
	 */
	public static Date dayOfSunday(Date date) {
		int dayOfWeek = dayOfWeek(date) ;
		Date outDate ;
		if(dayOfWeek==1){
			outDate = dateGapDays(date,0) ;
		}else {
			outDate = dateGapDays(date,6-(dayOfWeek-2)) ;
		}
		return getDateByTimeStr(outDate, "23:59:59") ;
	}



	/**
	 * 日期间隔的天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer betweenDateDD(Date startDate,Date endDate){
		return  Long.bitCount((betweenDateHH(startDate, endDate) / 24)) ;
	}
	/**
	 * 日期的间隔小时数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long betweenDateHH(Date startDate,Date endDate){
		return betweenDateMin(startDate, endDate)/60;
	}
	/**
	 * 日期的间隔分钟数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long betweenDateMin(Date startDate,Date endDate){
		return betweenDateSS(startDate, endDate)/60 ;
	}
	/**
	 * 日期的间隔秒数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long betweenDateSS(Date startDate,Date endDate){
		return betweenDateSSS(startDate, endDate)/1000;
	}
	/**
	 * 日期的间隔毫秒数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long betweenDateSSS(Date startDate,Date endDate){
		return null==startDate||null==endDate?0:endDate.getTime()-startDate.getTime();
	}
	/**
	 * 获得指定日期的前一天或者后一天（由传入的num决定）的 date 对象 后几天的日期
	 * @param date
	 * @param num：0表示当前天 -1 表示上一天，1表示后一天，其他以此类推
	 * @return
	 */
	public static Date dateGapDays(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, num);
		return cal.getTime();
	}
	/**
	 * 获取系统当前时间，精确到天
	 * @return
	 */
	public static Date getNowDD(){
		Date date = new Date();
		String dateStr = dateToStr(date, PNYYYYMMDD);
		return strToDate(dateStr, PNYYYYMMDD);
	}
	/**
	 * 获取系统时间，精确到秒
	 * @return
	 */
	public static Date getNowSS(){
		Date date = new Date();
		String dateStr = dateToStr(date, PNYYYYMMDDHHMMSS);
		return strToDate(dateStr, PNYYYYMMDDHHMMSS);
	}
	/*************************************************以下内部自用*********************************************/

	/**
	 * //TODO 未知的应用场景
	 * 传入年月日，获得日期
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private static Date madeDate(String year, String month, String day) {
		Date outDate = strToDate(year + "-01-01");
		Integer monthInt = Integer.parseInt(month);
		outDate = monthFirstDay(outDate, monthInt - 1);
		Integer dayInt = Integer.parseInt(day);
		outDate = dateGapDays(outDate, dayInt - 1);
		return outDate;
	}


	/**
	 * 检查Map
	 * @param map map集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean isEmpty(Map map) {
		return null == map ? true : map.isEmpty();
	}

	/**
	 * 检查List集合
	 * @param list
	 * @return
	 */
	private static boolean isEmpty(List list){
		return null == list ? true : list.isEmpty();
	}

	/**
	 * 检查字符串
	 * @param str
	 * @return
	 */
	private static boolean isEmpty(String str) {
		return null == str ? true : "".equals(str.replaceAll(" +", ""));
	}

	/**
	 * 检测Integer
	 * @param value
	 * @return
	 */
	private static boolean isEmpty(Integer value){
		return null == value ? true : "".equals(value.toString().replaceAll("[ 0]+", ""));
	}

	/**
	 * 检测Double
	 * @param value
	 * @return
	 */
	private static boolean isEmpty(Double value){
		return null == value ?true : "".equals(value.toString().replaceAll("[ .0]+", "")) ;
	}

	/**
	 * 检测Date
	 * @param date
	 * @return
	 */
	private static boolean isEmpty(Date date){
		return null == date ? true : isEmpty(date.toString()) ;
	}

	/**
	 * 检测所有类型对象是否为空
	 * @param obj
	 * @return
	 */
	private static boolean isEmpty(Object obj){
		return null == obj? true : isEmpty(obj.toString()) ;
	}


	/************************************************智能日期转换方法(开始)********************************************************/
	/**
	 * 根据传入的时间判断日期格式
	 * @param timeStr
	 * @return
	 */
	private static String checkPN(String timeStr){
		int timeStrLength = timeStr==null?0:timeStr.trim().length() ;
		return timeStrLength==2?PNYYYYMMDDHH:
				timeStrLength==5?PNYYYYMMDDHHMM:
						timeStrLength==8?PNYYYYMMDDHHMMSS:
								PNYYYYMMDD ;
	}



	/**
	 * 把各种常用字符串日期转换成yyyy-MM-dd的时间字符串
	 * @param dateStr
	 * @return
	 */
	private static String formatDate(String dateStr){
		String[] dateArry = dateStr.split("\\D+") ;
		String outDate = "" ;
		for (int i = 0; i < 3; i++) {
			outDate = dateArry[i].length()==4?dateArry[i]+outDate:outDate+"-"+dateArry[i];
		}
		return outDate ;
	}

	/**
	 * 拆分中文格式的日期，然后重新组合成阿拉伯数值格式的日期
	 * @param cDate
	 * @return
	 */
	private static String cDateNumToADateNum(String cDate){
		String outADateNum = "" ;
		String[] cDateArray = cDate.split("[^○一二三四五六七八九十]+") ;
		String[] otherArray = cDate.split("[○一二三四五六七八九十]+") ;
		for (int i = 0; (i < cDateArray.length || i < otherArray.length); i++) {
			outADateNum+=(i<otherArray.length?otherArray[i]:"")+(i<cDateArray.length && !isEmpty(cDateArray[i])?dateDNumToANum(cDateArray[i]):"");
		}
		return outADateNum ;
	}
	/**
	 * 默认字符串日期格式组合成阿拉伯数字格式日期
	 * @param cDate
	 * @return
	 */
	private static String eDateNumToADateNum(String cDate){
		String outADateNum = cDate ;
		String[] englishMonth = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"} ;
		String[] arabMonth = {"01","02","03","04","05","06","07","08","09","10","11","12"} ;
		for (int i = 0; (i < englishMonth.length || i < arabMonth.length); i++) {
			outADateNum = outADateNum.replace(englishMonth[i], arabMonth[i]) ;
		}
		arabMonth = outADateNum.split(" +") ;
		outADateNum = arabMonth[5]+"-"+arabMonth[1]+"-"+arabMonth[2]+" "+arabMonth[3] ;
		return outADateNum ;
	}

	/**
	 * 中文数值日期转换成 阿拉伯数值日期
	 * @param cNum
	 * @return
	 */
	private static String dateDNumToANum(String cNum){
		if(cNum.length()<4){
			return cNumToANum(cNum).split("\\.")[0] ;
		}
		String[] chinaNum = {"○","一","二","三","四","五","六","七","八","九"} ;
		String[] arabNum = {"0","1","2","3","4","5","6","7","8","9"} ;
		for (int i=0;i<chinaNum.length;i++) {
			cNum = cNum.replaceAll(chinaNum[i], arabNum[i]);
		}
		return cNum ;
	}
	/**
	 * 中文数值转换成阿拉伯数值
	 * @param cNum
	 * @return
	 */
	private static String cNumToANum(String cNum){
		String[] unit = {"毛","厘","分","个","十","百","千","万","亿"};
		String[] unitValue = {"0.001","0.01","0.1","1","10","100","1000","10000","100000000"};
		String[] chinaNum = {"零","一","二","三","四","五","六","七","八","九"} ;
		String[] arabNum = {"0","1","2","3","4","5","6","7","8","9"} ;
		//〇
		return cNumToANumByFormat(cNum, unit, unitValue, chinaNum, arabNum) ;
	}
	/**
	 * 传入中文数值单位格式和数字格式，获取阿拉伯数值
	 * @param cNum 中文数值
	 * @param unitFormat 单位格式（跨度是千分到亿，既0.001~100000000）
	 * @param numFormat 数值格式（0~9中文表述法）
	 * @return
	 */
	/**
	 * 传入中文数值单位格式和数字格式，获取阿拉伯数值
	 * @param cNum 中文数值
	 * @param unit 中问单位
	 * @param unitValue 数值
	 * @param chinaNum 中文数字
	 * @param arabNum 数值
	 * @return
	 */
	private static String cNumToANumByFormat(String cNum,String[] unit,String[] unitValue,String[] chinaNum,String[] arabNum){
		String[] unitFormulaValue = arrToArrAddFirstStr(unitValue, "*") ;
		String[] numValue = arrToArrAddFirstStr(arabNum, "+") ;
		StringBuffer unitStr = new StringBuffer() ;
		for (String string : unit) {
			unitStr.append(string);
		}
		for (String string : chinaNum) {
			unitStr.append(string);
		}
		cNum = cNum.replaceAll("[^"+unitStr+"]", "") ;
		for (int i=0;i<unit.length;i++) {
			cNum = cNum.replaceAll(unit[i], unitFormulaValue[i]);
		}
		for (int i=0;i<chinaNum.length;i++) {
			cNum = cNum.replaceAll(chinaNum[i], numValue[i]);
		}
		return calculateFormula(cNum.replaceFirst("^[*+]", "")) ;
	}
	/**
	 * 把一个字符串数值加上前缀返回一个新的数组
	 * @param arr 数组
	 * @param firstStr 前缀字符串
	 * @return 数组
	 */
	private static String[] arrToArrAddFirstStr(String[] arr,String firstStr) {
		String[] temp = new String[arr.length] ;
		for (int i = 0; i < arr.length; i++) {
			temp[i]=firstStr+arr[i] ;
		}
		return temp ;
	}
	/**
	 * 计数字符串公式结果
	 * @param formula
	 * @return
	 */
	private static String calculateFormula(String formula){
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
		try {
			return formatDouble((Double)jse.eval(formula), "0.000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0" ;
		}
	}
	/**
	 * 格式化Double显示模式
	 * @param dobl
	 * @param pn
	 * @return
	 */
	private static String formatDouble(Double dobl,String pn){
		DecimalFormat df = new DecimalFormat(pn);
		return df.format(dobl) ;
	}
	/**
	 * 检测字符串是否包含另一个字符串
	 * @param str 检测的字符串
	 * @param containRegex 包含的字符串（支持正则）
	 * @return 返回检查结果
	 */
	public static boolean contentContain(String str,String containRegex){
		return null==str || null==containRegex?false:
				str.replaceAll(containRegex, "").length()<str.length() ;
	}

	/**
	 * 处理时间，把带【上午,下午,AM,PM】的时间字符串，带下午和PM的小时加上12
	 * @param dateStr
	 * @return
	 */
	private static String treatTime(String dateStr){
		dateStr = dateStr.trim() ;
		if(contentContain(dateStr, "上午|(?i)am")){
			String[] dateStrArray = dateStr.split(" *上午 *| *(?i)am *| +") ;
			return formatDate(dateStrArray[0])+" "+formatTime(dateStrArray[1], 0) ;
		}else if(contentContain(dateStr, "下午|(?i)pm")){
			String[] dateStrArray = dateStr.split(" *下午 *| *(?i)pm *| +") ;
			return formatDate(dateStrArray[0])+" "+formatTime(dateStrArray[1], 12) ;
		}else{
			String[] dateStrArray = dateStr.split(" +") ;
			return formatDate(dateStrArray[0])+(dateStrArray.length>1?" "+formatTime(dateStrArray[1], 0):"") ;
		}
	}
	/**
	 * 时间字符串的小时加上小时量生成新的时间字符串
	 * @param timeStr
	 * @param hh
	 * @return
	 */
	private static String formatTime(String timeStr,Integer hh ){
		String[] timeArr = timeStr.split("\\D+") ;
		int HH = Integer.parseInt(timeArr.length > 0 ? timeArr[0] : "0")+hh ;
		String outTime = Integer.toString(HH) ;
		outTime = outTime.length()<2?"0"+outTime:outTime ;
		for(int i=1;i<timeArr.length;i++){
			outTime+=":"+(timeArr[i].length()<2?"0"+timeArr[i]:timeArr[i]) ;
		}
		return outTime ;
	}

	/**
	 * 传入指定日期后的几天的List<Date>
	 * @param date
	 * @param num
	 * @return
	 */
	public static List<Date> dayOfWeeks(Date date,int num) {
		List<Date> list = new ArrayList<Date>();
		for (int i = 0; i <num ; i++) {
			list.add(dateGapDays(date, i));
		}
		return list;
	}
	/**
	 * 传入指定日期后的几天的List<String>
	 * @param date
	 * @param num
	 * @return
	 */
	public static List<String> dayOfWeekStr(Date date,int num) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <num ; i++) {

			list.add(dateToStr(dateGapDays(date, i),"MM-dd"));
		}
		return list;
	}
	/**
	 * 判断传入的日期是否为同一天
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static boolean isSameDay(Date day1, Date day2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ds1 = sdf.format(day1);
		String ds2 = sdf.format(day2);
		if (ds1.equals(ds2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得8点到19点的Row索引
	 //     * @param day1
	 * @return
	 */
	public static int getRowId(Date date) {
		double outTemp = DateUtil.betweenDateMin(getDateByTimeStr(date, "00:00:00"), date)/30 ;
		int out = (int)outTemp ;
//        (int)(DateUtil.betweenDateHH(schedule.getStartTime(),schedule.getEndTime())/0.5)
//        String dateStr = dateToStrHH(date);
//        int result = (int)(Integer.parseInt(dateStr)/0.5);
		return out-18;
	}

	/**
	 * 日期格式转字符串格式，精确到天
	 * 默认格式 2014-12-11
	 * @param date
	 * @return
	 */
	public static String dateToStrHH(Date date){
		return dateToStr(date, HH) ;
	}
	/************************************************智能日期转换方法(结束)********************************************************/
	public static Date getDateDayEndTime(Date thisDate){
		String str = DateUtil.dateToStr(thisDate,"yyyy-MM-dd")+" 23:59:59";
		Date date23 = DateUtil.strToDate(str,"yyyy-MM-dd HH:mm:ss");
		return date23;
	}
	public static Date getDateDayBeginTime(Date thisDate){
		String str = DateUtil.dateToStr(thisDate,"yyyy-MM-dd") +" 00:00:00";
		Date date00 = DateUtil.strToDate(str,"yyyy-MM-dd HH:mm:ss");
		return date00;
	}
	public static void main(String[] args) throws Exception{
//        String strDate = "12月11日2014年 12时11分14秒" ;
//        System.out.print(strToDate(strDate)) ;
//        Date now = strToDate("2014-12-15") ;
//        System.out.println(dateToStrSS(dayOfMonday(now)));
//        System.out.println(dateToStrSS(dayOfSunday(now)));
		String ext = FilenameUtils.getExtension("d:\\迪拉索网站建设报价.xlsx");
		System.out.println(ext);
	}
}
