package com.salaryMail.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	public static final String FMT_yyyy_MM_dd = "yyyy-MM-dd";
	public static final String FMT_yyyyMMdd = "yyyyMMdd";
	public static final String FMT_yyMMdd = "yyMMdd";
	public static final String FMT_yyyyMM = "yyyyMM";
	public static final String FMT_MMdd = "MM-dd";
	public static final String FMT_dd = "dd";
	public static final String FMT_yy_MM_dd = "yy/MM/dd";
	public static final String FMT_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String FMT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String FMT_HH_mm_ss = "HH:mm:ss";
	public static final String FMT_HHmmss = "HHmmss";
	public static final String ORA_DATE_TIME = "yyyy-MM-dd HH:mm";

	public static String getCurrentTime(String fmt) {
		DateFormat format = new SimpleDateFormat(fmt);
		Date date = new Date();
		String currentTime = format.format(date);
		return currentTime;
	}
	
	public static String formatToString(Date date,String fmt) {
		DateFormat format = new SimpleDateFormat(fmt);
		String dateStr = format.format(date);
		return dateStr;
	}
	
	public static Date convertToDate(String date,String fmt){
		try {
			DateFormat format = new SimpleDateFormat(fmt);
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
    public static String getDateString(Date _date, String patternString){
        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat(patternString);
        dateString = formatter.format(_date);
        return dateString;
    }
}
