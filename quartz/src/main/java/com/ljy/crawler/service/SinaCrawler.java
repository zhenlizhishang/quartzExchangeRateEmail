package com.ljy.crawler.service;
 
import java.util.HashMap;
import java.util.Map;

import com.ljy.crawler.http.HttpUtils;

/**
 * 新浪财经
 * @author admin
 *
 */
public class SinaCrawler {
 
    public static Map<String, Object> crawling(String seed) {
		String returnMsg = HttpUtils.doGet(seed);
		int begin = returnMsg.indexOf("\"");
    	int end = returnMsg.lastIndexOf("\"");
    	// 删除无用前后片段，截取json字符串
    	returnMsg  = returnMsg.substring(begin + 1, end);
		String[] str = returnMsg.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("时间", str[0]);
		map.put("现在", str[1]);
		map.put("昨收", str[3]);
		map.put("今开", str[5]);
		map.put("最高", str[6]);
		map.put("最低", str[7]);
    	return map;
    }
 
 
    //main 方法入口
    public static void main(String[] args) {
    	SinaCrawler.crawling("https://hq.sinajs.cn/rn="+ getRandomNum() +"list=DINIW");
    }
    
    /**
     * 生成13位随机数
     * @return
     */
    public static String getRandomNum(){
		return (int)(Math.random()*100000) + "" + (int)(Math.random()*1000000);
    }
}