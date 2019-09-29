package com.ljy.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ljy.crawler.service.BankService;

public class GO2 implements Job {

	private Logger logger = LoggerFactory.getLogger(GO2.class);
	
    @Override
    public void execute(JobExecutionContext context) {
    	logger.info("BOC定时爬虫系统启动!!!");
    	BankService bankService = new BankService();
		try {
			bankService.doService();
		} catch (Exception e) {
			
		}
    }

}