package com.ljy.crawler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ljy.sql.DBHelper;
import com.ljy.util.NumberUtils;
 
/**
 * 每天定时拉取中国银行数据
 * @author admin
 *
 */
public class BankService {
    
	private Logger logger = LoggerFactory.getLogger(BankService.class);
	
	/**
	 * 启动程序
	 * @throws Exception 
	 */
	public void doService() throws Exception{
		logger.info("【定时爬取中行】开始爬取数据");
		logger.info("【定时爬取中行】开始爬取中国银行数据");
		List<Map<String, Object>> BOCList = BOCCrawler.crawling("http://srh.bankofchina.com/search/whpj/search.jsp");
		logger.info(String.format("【定时爬取中行】整理中国银行数据为：%s", BOCList));
		
		logger.info("【定时爬取中行】开始爬取中国银行香港数据");
		List<Map<String, Object>> BOCHKList = BOCHKCrawler.crawling("http://services1.aastocks.com/WEB/BCHK/BOCHK/mktinfo.aspx?BCHKLanguage=chn");
		logger.info(String.format("【定时爬取中行】整理中国银行香港数据为：%s", BOCList));
		
		List<Map<String, Object>> orgList = orgReqMsg(BOCList, BOCHKList); // 组织数据
		
		logger.info(String.format("【定时爬取中行】整理待插入数据库数据为：%s", orgList));
		
		DBHelper.insertBOCBankData(orgList);
		
	}
	
	
	/**
	 *   整理信息
	 * @param CITIList
	 * @param BOCList
	 * @param BOCHKList
	 * @param PABList
	 * @return
	 */
	public List<Map<String, Object>> orgReqMsg(List<Map<String, Object>> BOCList, List<Map<String, Object>> BOCHKList) {
		List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		
		
		// 中行离岸
		for (Map<String, Object> map: BOCHKList) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			if("美元(USD)兑人民币-离岸(CNH)".equals(map.get("name"))){
				retMap.put("BOCHKUSDSell", NumberUtils.getSub2(map.get("卖出")));
				retMap.put("BOCHKUSDBuy", NumberUtils.getSub2(map.get("买入")));
				retList.add(retMap);
			}
		}
		
		// 中行
		for (Map<String, Object> map: BOCList) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			if("美元".equals(map.get("name"))) {
				retMap.put("BOCUSDBuyingRateOfSpotExchange", NumberUtils.getDivision100AndSub2(map.get("现汇买入价")));
				retMap.put("BOCUSDBuyingRateOfCash", NumberUtils.getDivision100AndSub2(map.get("现钞买入价")));
				retMap.put("BOCUSDSpotRate", NumberUtils.getDivision100AndSub2(map.get("现汇卖出价")));
				retMap.put("BOCUSDDiscountPrice", NumberUtils.getDivision100AndSub2(map.get("中行折算价")));
				retList.add(retMap);
			}
		}
		
		return retList;
	}
	
	public static void main(String[] args) throws Exception {
		BankService crawlerService = new BankService();
		crawlerService.doService();
	}
}