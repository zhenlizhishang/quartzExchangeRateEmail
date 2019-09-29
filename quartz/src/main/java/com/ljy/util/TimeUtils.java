package com.ljy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	//获取年月日
    public static String getCurrentyear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    //获取时分
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    //获取星期几
    public static String getCurrentWeek() {
        SimpleDateFormat format = new SimpleDateFormat("E");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }
    
    public static String getYYYYMMDDWeek(){
    	return getCurrentyear() + getCurrentWeek();
    }
    
    public static void main(String[] args) {
		System.out.println(getYYYYMMDDWeek());
	}
    
}
