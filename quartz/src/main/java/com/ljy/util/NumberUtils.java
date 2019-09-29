package com.ljy.util;

import java.math.BigDecimal;

public class NumberUtils {

	public static String getDivision100AndSub2(Object obj){
		double d = Double.parseDouble(obj.toString())/100;
		return NumberUtils.getSub2(d);
	}
	
	public static String getSub2(double d){
		BigDecimal bg = new BigDecimal(d);
		double f1 = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return "" + f1;
	}
	
	public static String getSub2(Object obj){
		double d = Double.parseDouble(obj.toString());
		return getSub2(d);
	}
	
	public static void main(String[] args) {
		System.out.println(NumberUtils.getDivision100AndSub2("1.05986"));
	}
}
