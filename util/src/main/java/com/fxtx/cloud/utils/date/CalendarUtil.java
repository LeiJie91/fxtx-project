package com.fxtx.cloud.utils.date;

import java.util.*;

/**
 * Created by nldjy on 2015/10/10.
 */
public class CalendarUtil {
    /**
     * 获取指定日期所在月份的日历嵌套列表，
     *
     * 嵌套规则如下（日历List嵌套星期List）下面范例是吧14年12月份的日历输出成table表格
     * @param month
     * @return
     */
    public static List<List<Map<String,String>>> calendarMonth(String month){
        Date date = DateUtil.strToDate(month+"-01") ;
        List<List<Map<String,String>>> outCalendarList = new ArrayList<List<Map<String,String>>>();
        int hour = DateUtil.dayOfWeek( DateUtil.monthFirstDay(date, 0));
        hour = hour==1?7:hour-1 ;
        int lastDay = Integer.parseInt( DateUtil.dateToStr( DateUtil.monthLastDay(date, 0), "dd"));
        int maxNum = (((hour-1+lastDay)+6)/7)*7 ;
        List<Map<String,String>> rowList = new ArrayList<Map<String,String>>();
        for(int i=1;i<=maxNum;i++){
            Integer dayNum = i<hour || i>hour-1+lastDay?null: i + 1 - hour ;
            if(null!=dayNum){
                Map<String,String> addTemp = new HashMap<String, String>() ;
                addTemp.put("showDay",dayNum.toString()) ;
                addTemp.put("dateTime",month+"-"+(dayNum<10?"0":"")+dayNum.toString()) ;
                rowList.add(addTemp) ;
            }else{
                rowList.add(null) ;
            }
            if(rowList.size()==7){
                outCalendarList.add(rowList) ;
                rowList = new ArrayList<Map<String,String>>();
            }
        }
        return outCalendarList;
    }
    //年历
    public static List<List<List<Map<String,String>>>> calendarYear(String year){
        List out = new ArrayList() ;
        for(int i=1;i<=12;i++){
            String monthStr = (i<10?"0":"")+i;
            List oneMonth = calendarMonth(year+"-"+monthStr) ;
            out.add(oneMonth) ;
        }
        return out ;
    }

    public static void main(String[] args) throws Exception{
        Integer hour = 2 ;
        System.out.print(7-1*(hour-1));
//        List<List<List<Map<String,String>>>> oneYear = calendarYear("2015") ;
//        int monthNum = 1 ;
//        for (List<List<Map<String,String>>> oneMonth:oneYear){
//            System.out.println();
//            System.out.println("------"+monthNum+"月-----------");
//            //月
//            for(List<Map<String,String>> oneWeek:oneMonth){
//                //周
//                for(Map<String,String> oneDay:oneWeek){
//                    //天oneDay.get("dateTime") 完整日期，包含年月日，oneDay.get("showDay")，天
//                    if(null!=oneDay){
//                        System.out.print(" "+oneDay.get("showDay"));
//                    }else{
//                        System.out.print("   ");
//                    }
//                }
//                System.out.println();
//            }
//            monthNum++;
//        }

    }
}
