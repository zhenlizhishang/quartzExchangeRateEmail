package com.ljy.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ljy.crawler.service.CrawlerService;

public class GO implements Job {

	private Logger logger = LoggerFactory.getLogger(GO.class);
	
    @Override
    public void execute(JobExecutionContext context) {
    	logger.info("定时邮件爬虫系统启动!!!");
    	CrawlerService crawlerService = new CrawlerService();
		try {
			crawlerService.doService();
		} catch (Exception e) {
			logger.info(String.format("爬虫系统出错，重新开始:%s", e));
			try {
				crawlerService.doService();
			} catch (Exception e1) {
				logger.info(String.format("爬虫系统再次出错:%s", e));
			}
		}
    }

}