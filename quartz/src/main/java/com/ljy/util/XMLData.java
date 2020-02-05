package com.ljy.util;

public class XMLData {

	public static String PICTUREURL = "D:\\jfreechart\\line.png";
//	public static String PICTUREURL = "/usr/mpsp/tomcat6/temp/jfreechart.png"; // 汇率波动图地址
	
	public static String FTLURL = "D:/Install/work/apache/apache-tomcat-6.0.33/webapps/quartz/WEB-INF/classes"; // 邮件模板文件地址
//	public static String FTLURL = "/usr/mpsp/tomcat6/webapps/quartz/WEB-INF/classes"; // 邮件模板文件地址
	
	public static String JDBCDRIVER = "com.mysql.jdbc.Driver"; // jdbc驱动
	
	public static String JDBCURL = "jdbc:mysql://127.0.0.1:3306/umf";
	
	public static String JDBCUSERNAME = "root"; // jdbc账号
	
	public static String JDBCPASSWORD = "root"; // jdbc密码
	
	public static String FTLNAME = "mail.ftl"; // 邮件模板文件地址
	
	public static String SENDERACCOUNT = "1111@11111.com"; // 发件人账户名
	
	public static String SENDERPASSWORD = "111111"; // 发件人账户名密码
	
	public static String SENDERADDRESS = "111@111.com"; // 发件人地址
	
	public static String[] recipientTOAddresses = {"111@111.com"}; // 收件人地址   发送
	
	public static String[] recipientCCAddresses = {}; // 收件人地址   抄送 
	
	public static String SUBJECTNAME = "银行汇率自动抓取"; // 邮件主题
	
	public static String MAILSMTPHOST = "smtp.111.com"; // 发件人的SMTP服务器地址
	
	public static int FIRSTJOBCYCLETIME = 24; // 第一个定时任务循环时间  小时单位
	
	public static String FIRSTJOBSTARTTIME = "yyyy-MM-dd '16:00:00'"; // 第一个定时任务开始时间   每天16点
	
	public static int SECONDJOBCYCLETIME = 10; // 第二个定时任务循环时间  分钟单位
	
	public static String IFNOW = "Y"; // 是现在开始执行还是固定时间开始执行
	
}
