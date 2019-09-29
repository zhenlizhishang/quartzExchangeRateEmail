package com.ljy.crawler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ljy.crawler.page.Page;
import com.ljy.crawler.page.PageParserTool;
import com.ljy.crawler.page.RequestAndResponseTool;

/**
 * 中国银行
 * @author admin
 *
 */
public class BOCCrawler {
 
    /**
     * 抓取过程
     *
     * @param seeds
     * @return
     */
    public static List<Map<String, Object>> crawling(String seed) {
    	Map<String, Object> map = new HashMap<String, Object>();
//    	map.put("erectDate", "2018-10-15");
//    	map.put("nothing", "2018-10-15");
    	// 依次为   英镑 港币 美元 日元 欧元
    	String[] names = {"1314", "1315", "1316", "1323", "1326"};
    	String[] chainNames = {"英镑", "港币", "美元", "日元", "欧元"};
    	List<Map<String, Object>> returnlist = new ArrayList<Map<String,Object>>();
    	for(int i = 0;i<names.length;i++){
    		map.put("pjname", names[i]);
    		Page page = RequestAndResponseTool.sendPostRequstAndGetResponse(seed, map);
        	List<Map<String, Object>> list = PageParserTool.getAllByNameFromBOC(page, "BOC_main publish");
        	if(list != null && list.size() != 0){
        		list.get(0).put("name", chainNames[i]);
        		returnlist.add(list.get(0));
        	}
    	}
    	return returnlist;
    }
 
 
    //main 方法入口
    public static void main(String[] args) {
        System.out.println(BOCCrawler.crawling("http://srh.bankofchina.com/search/whpj/search.jsp"));
    }
}