package com.ljy.crawler.util;

public class RandomUtils {

	/**
     * 生成13位随机数
     * @return
     */
    public static String get13RandomNum(){
		return (int)(Math.random()*100000) + "" + (int)(Math.random()*1000000);
    }
    
    /**
     * 生成测试汇率数
     * @return
     */
    public static String getRanNUm(){
    	for(;;){
    		double d = Math.random()*10;
    		if (d>=6&&d<=9) {
    			return ("" + d).substring(0, 6);
			}
    	}
    }
    
    public static void main(String[] args) {
		System.out.println(getRanNUm());
	}
}
