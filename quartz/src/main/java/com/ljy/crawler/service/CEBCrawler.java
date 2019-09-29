package com.ljy.crawler.service;
 
import java.util.List;
import java.util.Map;

import org.jsoup.select.Elements;

import com.ljy.crawler.page.Page;
import com.ljy.crawler.page.PageParserTool;
import com.ljy.crawler.page.RequestAndResponseTool;
 
/**
 * 中国光大银行
 * @author admin
 *
 */
public class CEBCrawler {
 
    /**
     * 抓取过程
     *
     * @param seeds
     * @return
     */
    public static List<Map<String, Object>> crawling(String seeds) {
        //根据URL得到page;
        Page page = RequestAndResponseTool.sendRequstAndGetResponse(seeds);
 
        // 对page进行处理： 访问DOM的某个标签
        Elements es = PageParserTool.select(page, "iframe");
        String url = "";
    	for (int i = 0; i < es.size(); i++) {
            if(!es.get(i).attr("src").contains("http")){
            	url = es.get(i).attr("src");
            }
		}
        
    	url = "http://www.cebbank.com" + url;
        
        Page newPage = RequestAndResponseTool.sendRequstAndGetResponse(url);
        
        List<Map<String, Object>> list = PageParserTool.getAllByNameFromCBE(newPage, "lczj_box");
        return list;
    }
 
    //main 方法入口
    public static void main(String[] args) {
        CEBCrawler.crawling("http://www.cebbank.com/site/ygzx/whpj/index.html?page=1");
    }
}