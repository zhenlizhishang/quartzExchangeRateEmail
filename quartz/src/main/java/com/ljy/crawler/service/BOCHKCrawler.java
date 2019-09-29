package com.ljy.crawler.service;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ljy.crawler.page.Page;
import com.ljy.crawler.page.PageParserTool;
import com.ljy.crawler.page.RequestAndResponseTool;

/**
 * 中国银行香港
 * @author admin
 *
 */
public class BOCHKCrawler {
 
 
 
    /**
     * 抓取过程
     *
     * @param seeds
     * @return
     */
    public static List<Map<String, Object>> crawling(String seed) {
 
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("__EVENTTARGET", ""); // 事件目标
    	map.put("__EVENTARGUMENT", ""); // 时间参数
    	map.put("ctl00$Content$ucCrossRate$ddlCrossRate", "CNH");
        //根据URL得到page;
    	Page page = RequestAndResponseTool.sendPostRequstAndGetResponse(seed, map);
 
		/**
		 * 获取中国银行（香港）数据
		 */
    	List<Map<String, Object>> list = PageParserTool.getAllByNameFromBOCHK(page, "gvTable2 commonText");
    	return list;
    }
 
 
    //main 方法入口
    public static void main(String[] args) {
        System.out.println(BOCHKCrawler.crawling("http://services1.aastocks.com/WEB/BCHK/BOCHK/mktinfo.aspx?BCHKLanguage=chn"));
    }
}