package com.yc.feed.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yusong on 2016/10/14.
 * 时间工具类
 */
public class DateUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /*
    *讲时间转换为秒级整数
    */
    public static int getNumber(Date time){
        if (null == time ){
            return 0;
        }else{
            return new Long(time.getTime()/1000).intValue();
        }
    }

    /*
    *获取时间
    */
    public static Date getDate(int seconds){
        Date date = new Date((long)seconds*1000);
        return date;
    }

    public static String getDateStr(int seconds){
        return sdf.format(getDate(seconds));
    }

    public static String getDateStrMs(){
        String res = null;
        try {
            Date date = new Date();
            res = sdf2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String getDateStr(long seconds){
        String res = null;
        try {
            Date date = new Date((long)seconds*1000);
            res = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public static int getMonthDays(int year, int month) {
		Calendar time=Calendar.getInstance(); 
		time.clear(); 
		time.set(Calendar.YEAR,year); 
		//year年
		time.set(Calendar.MONTH,month-1);
		//Calendar对象默认一月为0,month月            
		int day=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
		return day;
	}
	
    public static void main(String[] args){
        System.out.println(getDateStrMs());
    }

}
