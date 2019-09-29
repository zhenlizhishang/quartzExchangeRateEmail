package com.ljy.crawler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ljy.crawler.util.RandomUtils;
import com.ljy.jfreechart.CreateJFreeChartLine;
import com.ljy.mail.MailUtils;
import com.ljy.sql.DBHelper;
import com.ljy.util.ChangeCharset;
import com.ljy.util.NumberUtils;
import com.ljy.util.PropertiesUtils;
import com.ljy.util.TimeUtils;
import com.ljy.util.XMLData;
 
public class CrawlerService {
    
	private Logger logger = LoggerFactory.getLogger(CrawlerService.class);
	
	/**
	 * 启动程序
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void doService() throws Exception{
		logger.info("【定时邮件爬虫系统】开始爬取数据");
		logger.info("【定时邮件爬虫系统】开始爬取中国银行数据");
		List<Map<String, Object>> BOCList = BOCCrawler.crawling("http://srh.bankofchina.com/search/whpj/search.jsp");
		logger.info(String.format("【定时邮件爬虫系统】整理中国银行数据为：%s", BOCList));
		
		logger.info("【定时邮件爬虫系统】开始爬取中国银行香港数据");
		List<Map<String, Object>> BOCHKList = BOCHKCrawler.crawling("http://services1.aastocks.com/WEB/BCHK/BOCHK/mktinfo.aspx?BCHKLanguage=chn");
		logger.info(String.format("【定时邮件爬虫系统】整理中国银行香港数据为：%s", BOCList));
		
		logger.info("【定时邮件爬虫系统】开始爬取中国光大银行数据");
		List<Map<String, Object>> CEBList = CEBCrawler.crawling("http://www.cebbank.com/site/ygzx/whpj/index.html?page=1");
		logger.info(String.format("【定时邮件爬虫系统】整理中国光大银行数据为：%s", CEBList));

		logger.info("【定时邮件爬虫系统】开始爬取中信银行数据");
		List<Map<String, Object>> CITIList = CITICCrawler.crawling("https://etrade.citicbank.com/portalweb/cms/getForeignExchRate.htm?callback=");
		logger.info(String.format("【定时邮件爬虫系统】整理中信银行数据为：%s", CITIList));
		
		logger.info("【定时邮件爬虫系统】开始爬取平安银行数据");
		List<Map<String, Object>> PABList = PABCrawler.crawling("http://bank.pingan.com/geren/waihuipaijia.shtml");
		logger.info(String.format("【定时邮件爬虫系统】整理平安银行数据为：%s", PABList));
		
		logger.info("【定时邮件爬虫系统】开始爬取新浪财经数据");
		Map<String, Object> SinaMap = SinaCrawler.crawling("https://hq.sinajs.cn/rn="+ RandomUtils.get13RandomNum() +"list=DINIW");
		logger.info(String.format("【定时邮件爬虫系统】整理新浪财经数据为：%s", SinaMap));
		
		
		Map<String, Object> orgMap = new HashMap<String, Object>(); // 组织数据
		Map<String, Object> orgSinaMap = new HashMap<String, Object>();
		// 新浪信息
		if(SinaMap != null ){
			orgSinaMap.put("old", SinaMap.get("昨收"));
			orgSinaMap.put("today", SinaMap.get("今开"));
			orgSinaMap.put("now", SinaMap.get("现在"));
			orgMap.put("Sina", orgSinaMap);
		}
		
		/* 总表数据 */
		orgTotalMsg(CITIList, BOCList, BOCHKList, PABList, orgMap);
		logger.info(String.format("【定时邮件爬虫系统】整理总表数据为：%s", orgMap));
		
		orgMap.put("week", ChangeCharset.toUTF_8(TimeUtils.getCurrentyear()));
		orgMap.put("nowtime", ChangeCharset.toUTF_8(TimeUtils.getCurrentTime()));
		
		System.out.println(orgMap.get("time"));
		
		// 待存入数据库
		Map<String, Object> imgMap = (Map<String, Object>) orgMap.get("imgMsg");
		
		logger.info(String.format("【定时邮件爬虫系统】开始向数据库中保存数据，保存数据为：%s", imgMap));
		DBHelper.insertData(imgMap);
		
		logger.info(String.format("【定时邮件爬虫系统】开始向数据库中查询历史数据"));
		List<Map<String, Object>> list = DBHelper.findData();
		logger.info(String.format("【定时邮件爬虫系统】数据库历史数据： %s", list));
		
		Map<String, Double> MinMaxNumMap = DBHelper.getMinAndMax();
		logger.info(String.format("【定时邮件爬虫系统】最小值为： %s,  最大值为：%s", MinMaxNumMap.get("minNum"), MinMaxNumMap.get("maxNum")));
		
		
		logger.info(String.format("【定时邮件爬虫系统】开始生成汇率波动图"));
		try {
			CreateJFreeChartLine.generateImg(list, MinMaxNumMap.get("minNum")-0.01, MinMaxNumMap.get("maxNum")+0.01);
			logger.info(String.format("【定时邮件爬虫系统】汇率波动图生成路径：%s", XMLData.PICTUREURL));
		} catch (Exception e) {
			logger.info(String.format("【定时邮件爬虫系统】汇率波动图生成失败：%s", e));
		}
		
		
		logger.info(String.format("【定时邮件爬虫系统】开始发送汇率波动邮件"));
		try {
			MailUtils.sendHtmlMail(orgMap, XMLData.PICTUREURL);
			logger.info(String.format("【定时邮件爬虫系统】邮件发送成功"));
		} catch (Exception e) {
			logger.info(String.format("【定时邮件爬虫系统】邮件发送失败：%s", e));
		}
		
	}
	
	
	/**
	 *   整理信息
	 * @param CITIList
	 * @param BOCList
	 * @param BOCHKList
	 * @param PABList
	 * @return
	 */
	public void orgTotalMsg(List<Map<String, Object>> CITIList, List<Map<String, Object>> BOCList,
			List<Map<String, Object>> BOCHKList, List<Map<String, Object>> PABList, Map<String, Object> orgMap) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> contrastMap = new HashMap<String, Object>();
		Map<String, Object> imgMap = new HashMap<String, Object>();
		double spreads1 = 0;
		double spreads2 = 0;
		double spreads3 = 0;
		double spreads4 = 0;
		
		// 中信
		for (Map<String, Object> map: CITIList) {
			if("美元".equals(map.get("curName"))){
				retMap.put("CITIUSDcstexcBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcBuyPrice"))); // 现汇买入价
				retMap.put("CITIUSDcstpurBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstpurBuyPrice"))); // 现钞买入价
				retMap.put("CITIUSDcstexcSellPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcSellPrice"))); // 现汇卖出价
				retMap.put("CITIUSDmidPrice", NumberUtils.getDivision100AndSub2(map.get("midPrice"))); // 中行折算价
				spreads2 = Double.parseDouble(map.get("cstexcSellPrice").toString())/100; // 价差2
				spreads3 = Double.parseDouble(map.get("cstexcBuyPrice").toString())/100; // 价差2
				imgMap.put("CITIUSDcstexcSellPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcSellPrice"))); // 现汇卖出价
			}
			if("英镑".equals(map.get("curName"))) {
				retMap.put("CITIGBPcstexcBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcBuyPrice"))); // 现汇买入价
				retMap.put("CITIGBPcstpurBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstpurBuyPrice"))); // 现钞买入价
				retMap.put("CITIGBPcstexcSellPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcSellPrice"))); // 现汇卖出价
				retMap.put("CITIGBPmidPrice", NumberUtils.getDivision100AndSub2(map.get("midPrice"))); // 中行折算价
			}
			if("日元".equals(map.get("curName"))) {
				retMap.put("CITIJPYcstexcBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcBuyPrice"))); // 现汇买入价
				retMap.put("CITIJPYcstpurBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstpurBuyPrice"))); // 现钞买入价
				retMap.put("CITIJPYcstexcSellPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcSellPrice"))); // 现汇卖出价
				retMap.put("CITIJPYmidPrice", NumberUtils.getDivision100AndSub2(map.get("midPrice"))); // 中行折算价
			}
			if("港币".equals(map.get("curName"))) {
				retMap.put("CITIHKDcstexcBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcBuyPrice"))); // 现汇买入价
				retMap.put("CITIHKDcstpurBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstpurBuyPrice"))); // 现钞买入价
				retMap.put("CITIHKDcstexcSellPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcSellPrice"))); // 现汇卖出价
				retMap.put("CITIHKDmidPrice", NumberUtils.getDivision100AndSub2(map.get("midPrice"))); // 中行折算价
			}
			if("欧元".equals(map.get("curName"))) {
				retMap.put("CITIEURcstexcBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcBuyPrice"))); // 现汇买入价
				retMap.put("CITIEURcstpurBuyPrice", NumberUtils.getDivision100AndSub2(map.get("cstpurBuyPrice"))); // 现钞买入价
				retMap.put("CITIEURcstexcSellPrice", NumberUtils.getDivision100AndSub2(map.get("cstexcSellPrice"))); // 现汇卖出价
				retMap.put("CITIEURmidPrice", NumberUtils.getDivision100AndSub2(map.get("midPrice"))); // 中行折算价
			}
		}
		
		// 中行离岸
		for (Map<String, Object> map: BOCHKList) {
			if("美元(USD)兑人民币-离岸(CNH)".equals(map.get("name"))){
				retMap.put("BOCHKUSDSell", NumberUtils.getSub2(map.get("卖出")));
				retMap.put("BOCHKUSDBuy", NumberUtils.getSub2(map.get("买入")));
				imgMap.put("BOCHKUSDSell", NumberUtils.getSub2(map.get("卖出")));
				spreads1 = Double.parseDouble(NumberUtils.getSub2(map.get("卖出").toString())); // 价差1
				spreads4 = Double.parseDouble(map.get("买入").toString()); // 价差4
			} else if("英镑(GBP)兑人民币-离岸(CNH)".equals(map.get("name"))) {
				retMap.put("BOCHKGBPSell", NumberUtils.getSub2(map.get("卖出")));
				retMap.put("BOCHKGBPBuy", NumberUtils.getSub2(map.get("买入")));
			} else if("欧罗(EUR)兑人民币-离岸(CNH)".equals(map.get("name"))) {
				retMap.put("BOCHKEURSell", NumberUtils.getSub2(map.get("卖出")));
				retMap.put("BOCHKEURBuy", NumberUtils.getSub2(map.get("买入")));
			} else if("人民币-离岸(CNH)兑日圆(JPY)".equals(map.get("name"))) {
				retMap.put("BOCHKJPYSell", NumberUtils.getSub2(1/Double.parseDouble(map.get("卖出").toString())));
				retMap.put("BOCHKJPYBuy", NumberUtils.getSub2(1/Double.parseDouble(map.get("买入").toString())));
			} else if("人民币-离岸(CNH)兑港元(HKD)".equals(map.get("name"))) {
				retMap.put("BOCHKHKDSell", NumberUtils.getSub2(1/Double.parseDouble(map.get("卖出").toString())));
				retMap.put("BOCHKHKDBuy", NumberUtils.getSub2(1/Double.parseDouble(map.get("买入").toString())));
			}
		}
		
		// 中行
		for (Map<String, Object> map: BOCList) {
			if("英镑".equals(map.get("name"))){
				retMap.put("BOCGBPBuyingRateOfSpotExchange", NumberUtils.getDivision100AndSub2(map.get("现汇买入价")));
				retMap.put("BOCGBPBuyingRateOfCash", NumberUtils.getDivision100AndSub2(map.get("现钞买入价")));
				retMap.put("BOCGBPSpotRate", NumberUtils.getDivision100AndSub2(map.get("现汇卖出价")));
				retMap.put("BOCGBPDiscountPrice", NumberUtils.getDivision100AndSub2(map.get("中行折算价")));
			} else if("港币".equals(map.get("name"))) {
				retMap.put("BOCHKDBuyingRateOfSpotExchange", NumberUtils.getDivision100AndSub2(map.get("现汇买入价")));
				retMap.put("BOCHKDBuyingRateOfCash", NumberUtils.getDivision100AndSub2(map.get("现钞买入价")));
				retMap.put("BOCHKDSpotRate", NumberUtils.getDivision100AndSub2(map.get("现汇卖出价")));
				retMap.put("BOCHKDDiscountPrice", NumberUtils.getDivision100AndSub2(map.get("中行折算价")));
			} else if("美元".equals(map.get("name"))) {
				retMap.put("BOCUSDBuyingRateOfSpotExchange", NumberUtils.getDivision100AndSub2(map.get("现汇买入价")));
				retMap.put("BOCUSDBuyingRateOfCash", NumberUtils.getDivision100AndSub2(map.get("现钞买入价")));
				retMap.put("BOCUSDSpotRate", NumberUtils.getDivision100AndSub2(map.get("现汇卖出价")));
				retMap.put("BOCUSDDiscountPrice", NumberUtils.getDivision100AndSub2(map.get("中行折算价")));
			} else if("日元".equals(map.get("name"))) {
				retMap.put("BOCJPYBuyingRateOfSpotExchange", NumberUtils.getDivision100AndSub2(map.get("现汇买入价")));
				retMap.put("BOCJPYBuyingRateOfCash", NumberUtils.getDivision100AndSub2(map.get("现钞买入价")));
				retMap.put("BOCJPYSpotRate", NumberUtils.getDivision100AndSub2(map.get("现汇卖出价")));
				retMap.put("BOCJPYDiscountPrice", NumberUtils.getDivision100AndSub2(map.get("中行折算价")));
			} else if("欧元".equals(map.get("name"))) {
				retMap.put("BOCEURBuyingRateOfSpotExchange", NumberUtils.getDivision100AndSub2(map.get("现汇买入价")));
				retMap.put("BOCEURBuyingRateOfCash", NumberUtils.getDivision100AndSub2(map.get("现钞买入价")));
				retMap.put("BOCEURSpotRate", NumberUtils.getDivision100AndSub2(map.get("现汇卖出价")));
				retMap.put("BOCEURDiscountPrice", NumberUtils.getDivision100AndSub2(map.get("中行折算价")));
			}
		}
		
		// 平安
		for (Map<String, Object> map: PABList) {
			if("英镑".equals(map.get("name"))){
				retMap.put("GBPPABSell", NumberUtils.getDivision100AndSub2(map.get("现汇/现钞卖出价")));
			} else if("港元".equals(map.get("name"))) {
				retMap.put("HKDPABSell", NumberUtils.getDivision100AndSub2(map.get("现汇/现钞卖出价")));
			} else if("美元".equals(map.get("name"))) {
				retMap.put("USDPABSell", NumberUtils.getDivision100AndSub2(map.get("现汇/现钞卖出价")));
				imgMap.put("USDPABSell", NumberUtils.getDivision100AndSub2(map.get("现汇/现钞卖出价")));
			} else if("日元".equals(map.get("name"))) {
				retMap.put("JPYPABSell", NumberUtils.getDivision100AndSub2(map.get("现汇/现钞卖出价")));
			} else if("欧元".equals(map.get("name"))) {
				retMap.put("EURPABSell", NumberUtils.getDivision100AndSub2(map.get("现汇/现钞卖出价")));
			}
		}
		
		contrastMap.put("spreads1", (int)(10000*(spreads1-spreads2)));
		contrastMap.put("spreads2", (int)(10000*(spreads3-spreads4)));
		
		orgMap.put("total", retMap);
		orgMap.put("contrast", contrastMap);
		orgMap.put("imgMsg", imgMap);
		// 价差
	}
	
	public static void main(String[] args) throws Exception {
		CrawlerService crawlerService = new CrawlerService();
		crawlerService.doService();
	}
}