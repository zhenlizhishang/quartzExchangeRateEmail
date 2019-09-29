package com.ljy.crawler.page;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 解析page类
 * @author admin
 *
 */
public class PageParserTool { 


    /* 通过选择器来选取页面的 */
    public static Elements select(Page page , String cssSelector) {
        return page.getDoc().select(cssSelector);
    }

    /*
     *  通过css选择器来得到指定元素;
     *
     *  */
    public static Element select(Page page , String cssSelector, int index) {
        Elements eles = select(page , cssSelector);
        int realIndex = index;
        if (index < 0) {
            realIndex = eles.size() + index;
        }
        return eles.get(realIndex);
    }
    
    
    /**
     * 获取指定id下的内容
     * PageParserTool.select(page, "#ctl00_Content_ucCrossRate_gvCrossRate_ctl02_lblSymbol").text()
     */

    /**
     * 获取满足选择器的元素中的链接 选择器cssSelector必须定位到具体的超链接
     * 例如我们想抽取id为content的div中的所有超链接，这里
     * 就要将cssSelector定义为div[id=content] a
     *  放入set 中 防止重复；
     * @param cssSelector
     * @return
     */
    public static  Set<String> getLinks(Page page ,String cssSelector) {
        Set<String> links  = new HashSet<String>() ;
        Elements es = select(page , cssSelector);
        Iterator<Element> iterator  = es.iterator();
        while(iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if ( element.hasAttr("href") ) {
                links.add(element.attr("abs:href"));
            }else if( element.hasAttr("src") ){
                links.add(element.attr("abs:src"));
            }
        }
        return links;
    }


    /**
     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
     * 例如通过getAttrs("img[src]","abs:src")可获取网页中所有图片的链接
     * @param cssSelector
     * @param attrName
     * @return
     */
    public static ArrayList<String> getAttrs(Page page , String cssSelector, String attrName) {
        ArrayList<String> result = new ArrayList<String>();
        Elements eles = select(page ,cssSelector);
        for (Element ele : eles) {
            if (ele.hasAttr(attrName)) {
                result.add(ele.attr(attrName));
            }
        }
        return result;
    }
    
    
    // add  ------------------------------------------------------------------------
    
    
    /**
     * 通过ID获得节点元素集合
     * @param page
     * @param id
     * @return
     */
    public static Element getById(Page page, String id) {
    	return page.getDoc().getElementById(id);
    }
    
    /**
     * 通过Class获取节点元素集合
     * @param page
     * @param name
     * @return
     */
    public static Elements getByClassName(Page page, String name) {
    	return page.getDoc().getElementsByClass(name);
    }

    /**
     * 解析中国银行(香港)数据  http://services1.aastocks.com/WEB/BCHK/BOCHK/mktinfo.aspx?BCHKLanguage=chn
     */
    public static List<Map<String, Object>> getAllByNameFromBOCHK(Page page, String name) {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	
    	Element e = page.getDoc().getElementsByClass(name).get(0);
    	Elements es = e.getElementsByClass("gvRowStyle");
    	Elements es2 = e.getElementsByClass("gvAlternatingRowStyle");
    	es.addAll(es2);
    	for (int i = 0; i < es.size(); i++) {
    		Element ee = es.get(i);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("name", ee.child(0).text());
    		map.put("买入", ee.child(1).text());
    		map.put("卖出", ee.child(2).text());
    		list.add(map);
		}
    	return list;
    }
    
    /**
     * 解析中国银行数据  http://srh.bankofchina.com/search/whpj/search.jsp
     */
    public static List<Map<String, Object>> getAllByNameFromBOC(Page page, String name) {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	Elements es = page.getDoc().getElementsByClass(name).get(0).select("table").select("tr");
    	for (int i = 1; i < es.size()-1; i++) {
    		Element ee = es.get(i);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("现汇买入价", ee.child(1).text());
    		map.put("现钞买入价", ee.child(2).text());
    		map.put("现汇卖出价", ee.child(3).text());
    		map.put("现钞卖出价", ee.child(4).text());
    		map.put("中行折算价", ee.child(5).text());
    		map.put("发布时间", ee.child(6).text());
    		list.add(map);
    	}
    	return list;
    }
    
    /**
     * 解析中国光大银行数据  http://www.cebbank.com/site/ygzx/whpj/index.html?page=1
     */
    public static List<Map<String, Object>> getAllByNameFromCBE(Page page, String name) {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	Element e = page.getDoc().getElementsByClass(name).get(0);
    	Elements es = e.select("tr");
    	for (int i = 2; i < es.size(); i++) {
    		Element ee = es.get(i);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put(ee.child(0).text() + "购汇汇率", ee.child(1).text());
    		map.put(ee.child(0).text() + "购钞汇率", ee.child(2).text());
    		map.put(ee.child(0).text() + "结汇汇率", ee.child(3).text());
    		map.put(ee.child(0).text() + "结钞汇率", ee.child(4).text());
    		list.add(map);
    	}
    	return list;
    }
    
    /**
     * 解析中国平安银行数据  http://bank.pingan.com/geren/waihuipaijia.shtml
     */
    public static List<Map<String, Object>> getAllByNameFromPAB(Page page, String name) {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	Elements es = page.getDoc().getElementsByClass("table").get(0).select("tr");
    	for (int i = 1; i < es.size(); i++) {
    		Element ee = es.get(i);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("name", ee.child(0).text());
    		map.put("我行中间价", ee.child(1).text());
    		map.put("现汇买入价", ee.child(2).text());
    		map.put("现钞买入价", ee.child(3).text());
    		map.put("现汇/现钞卖出价", ee.child(4).text());
    		map.put("人行中间价", ee.child(5).text());
    		list.add(map);
    	}
    	return list;
    }
    
}