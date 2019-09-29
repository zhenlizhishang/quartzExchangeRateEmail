package com.ljy.crawler.service;
 
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ljy.crawler.http.HttpUtils;
import com.ljy.util.ChangeCharset;

/**
 * 中信银行
 * @author admin
 *
 */
public class CITICCrawler {
 
    /**
     * 通过get请求获取中信数据
     *
     * @param seeds
     * @return
     * @throws UnsupportedEncodingException 
     */
    @SuppressWarnings("unchecked")
	public static List<Map<String, Object>> crawling(String seed) throws UnsupportedEncodingException {
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
    	seed = seed + df.format(new Date());
    	String returnJson = ChangeCharset.toUTF_8(HttpUtils.doGet(seed));
    	
    	int begin = returnJson.indexOf("(");
    	int end = returnJson.lastIndexOf(")");
    	// 删除无用前后片段，截取json字符串
    	returnJson  = returnJson.substring(begin + 1, end);
    	JSONObject jb = JSONObject.parseObject(returnJson);
    	Map<String, Object> map = (Map<String, Object>)jb;
    	List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    	if ("AAAAAAA".equals(map.get("retCode"))) {
    		Map<String, Object> resultMap = new HashMap<String, Object>(); 
    		resultMap = (Map<String, Object>)map.get("content");
    		returnList = (List<Map<String, Object>>)resultMap.get("resultList");
		}
    	return returnList;
    }
 
 
    //main 方法入口
    public static void main(String[] args) throws Exception {
        System.out.println(CITICCrawler.crawling("https://etrade.citicbank.com/portalweb/cms/getForeignExchRate.htm?callback="));
    }
}