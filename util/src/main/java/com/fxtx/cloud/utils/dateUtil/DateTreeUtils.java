package com.fxtx.cloud.utils.dateUtil;

import com.fxtx.cloud.utils.json.EntityUtil;
import com.fxtx.framework.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 时间工具类
 * \* Created with IntelliJ IDEA.
 * \* User: wugong.jie
 * \* Date: 2017/11/10 10:56
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DateTreeUtils {

    public static Map dataTree(Date startTime, Date endTime){
        List<FxDate> dateList = new ArrayList<FxDate>() ;
        String prevYear = "-1" ;
        String prevMonth = "-1" ;
        String prevDay = "-1" ;
        while (!startTime.after(endTime)){
            String nowYear = DateUtil.dateToStr(startTime,"yyyy") ;
            String nowMonth = DateUtil.dateToStr(startTime,"MM") ;
            String nowDay = DateUtil.dateToStr(startTime,"dd") ;
            if(!nowYear.equals(prevYear)){
                dateList.add(new FxDate("0",nowYear,nowYear)) ;
                prevYear = nowYear ;
            }
            if(!nowMonth.equals(prevMonth)){
                dateList.add(new FxDate(nowYear,nowYear+nowMonth,nowMonth)) ;
                prevMonth = nowMonth ;
            }
            if(!nowDay.equals(prevDay)){
                dateList.add(new FxDate(nowYear+nowMonth,nowYear+nowMonth+nowDay,nowDay)) ;
                prevDay = nowDay ;
            }
            startTime = DateUtil.dateGapDays(startTime,1) ;
        }
        Map tree = EntityUtil.listToTree(dateList,"0","parent","id","value");
        return tree ;
    }

    public static Map dataTreeSS(Date startTime, Date endTime){
        List<FxDate> dateList = new ArrayList<FxDate>() ;
        String prevYear = "-1" ;
        String prevMonth = "-1" ;
        String prevDay = "-1" ;
//		String prevHour = "-1" ;
//		String prevMinute = "-1" ;
//		String prevSecond = "-1" ;
        while (!startTime.after(endTime)){
            String nowYear = DateUtil.dateToStr(startTime,"yyyy") ;
            String nowMonth = DateUtil.dateToStr(startTime,"MM") ;
            String nowDay = DateUtil.dateToStr(startTime,"dd") ;
            if(!nowYear.equals(prevYear)){
                dateList.add(new FxDate("0",nowYear,nowYear)) ;
                prevYear = nowYear ;
            }
            if(!nowMonth.equals(prevMonth)){
                dateList.add(new FxDate(nowYear,nowYear+nowMonth,nowMonth)) ;
                prevMonth = nowMonth ;
            }
            if(!nowDay.equals(prevDay)){
                dateList.add(new FxDate(nowYear+nowMonth,nowYear+nowMonth+nowDay,nowDay)) ;
                prevDay = nowDay ;
            }

            for(int hour=0;hour<24;hour++){
                String hourStr = hour+"" ;
                if(hour<10){
                    hourStr = "0"+hourStr ;
                }
                dateList.add(new FxDate(nowYear+nowMonth+nowDay,nowYear+nowMonth+nowDay+hourStr,hourStr)) ;
                for(int minute=0;minute<60;minute++){
                    String minuteStr = minute+"" ;
                    if(minute<10){
                        minuteStr = "0"+minute ;
                    }
                    dateList.add(new FxDate(nowYear+nowMonth+nowDay+hourStr,nowYear+nowMonth+nowDay+hourStr+minuteStr,minuteStr)) ;
                    for(int second=0;second<60;second++){
                        String secondStr = second+"" ;
                        if(minute<10){
                            secondStr = "0"+second ;
                        }
                        dateList.add(new FxDate(nowYear+nowMonth+nowDay+hourStr+minuteStr,nowYear+nowMonth+nowDay+hourStr+minuteStr+secondStr,secondStr)) ;
                    }

                }
            }
            startTime = DateUtil.dateGapDays(startTime,1) ;
        }
        Map tree = EntityUtil.listToTree(dateList,"0","parent","id","value");
        return tree ;
    }

}