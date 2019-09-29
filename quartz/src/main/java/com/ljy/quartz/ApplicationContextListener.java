package com.ljy.quartz;

import static org.quartz.JobBuilder.newJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ljy.util.XMLData;

public class ApplicationContextListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

    public static Scheduler scheduler = null;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("定时Web应用开始...");

        /* 注册定时任务 */
        try {
            // 获取Scheduler实例
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            
            /* 第一个任务 */
            // 具体任务
            JobDetail job = newJob(GO.class).withIdentity("job1", "group1").build();

            // 触发时间点
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
            simpleScheduleBuilder.withIntervalInHours(XMLData.FIRSTJOBCYCLETIME); // 小时执行一次
            simpleScheduleBuilder.repeatForever();
            
//          一天的毫秒数
            long daySpan = 24 * 60 * 60 * 1000;
            
            final SimpleDateFormat sdf = new SimpleDateFormat(XMLData.FIRSTJOBSTARTTIME);
            // 首次运行时间
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
             
            // 如果今天的已经过了 首次运行时间就改为明天
            if(System.currentTimeMillis() > startTime.getTime()) {
            	startTime = new Date(startTime.getTime() + daySpan);
            }

            Trigger trigger = null;
            if("Y".equals(XMLData.IFNOW)){
            	trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule(simpleScheduleBuilder).build();
            } else {
            	trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime).withSchedule(simpleScheduleBuilder).build();
            }
            

            
            
            /* 第二个任务 */
            JobDetail job2 = newJob(GO2.class).withIdentity("job2", "group2").build();

            // 触发时间点
            SimpleScheduleBuilder simpleScheduleBuilder2 = SimpleScheduleBuilder.simpleSchedule();
            simpleScheduleBuilder2.withIntervalInMinutes(XMLData.SECONDJOBCYCLETIME); // 分钟执行一次
            simpleScheduleBuilder2.repeatForever();
             
            Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger2", "group2").startNow().withSchedule(simpleScheduleBuilder2).build();
//            
            
            
            // 交由Scheduler安排触发
            scheduler.scheduleJob(job2, trigger2);
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            logger.error(se.getMessage(), se);
        } catch (ParseException e) {
			e.printStackTrace();
		}
    }

    public void contextDestroyed(ServletContextEvent sce) {
//        logger.info("Web应用停止...");

        /* 注销定时任务 */
        try {
            // 关闭Scheduler
            scheduler.shutdown();

//            logger.info("调度器已关闭：The scheduler shutdown...");
        } catch (SchedulerException se) {
//            logger.error(se.getMessage(), se);
        }
    }

}