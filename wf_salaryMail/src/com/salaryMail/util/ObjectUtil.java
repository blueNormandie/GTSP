package com.salaryMail.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class ObjectUtil {
	/**
	 * map  get()对象为空处理
	 */
	public static String ObjectIsNull(Object o){
		if(o==null){
			return "";
		}
		return o.toString();
	}
	
	public static String objectIsNull2(Object o){
		if(o==null){
			return "0.00";
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		if(Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$").matcher(o.toString()).matches()==true){
			return df.format(Double.valueOf(o.toString()));
		}else{
			return o.toString();
		}
	}
}
