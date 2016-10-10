package com.salaryMail.schedule;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import com.salaryMail.action.ArrowExecuteAction;
import com.salaryMail.action.ExecuteAction;
import com.salaryMail.service.impl.SalaryServerServiceImpl;

public class InitSalarySchedule extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private final Logger log = Logger.getLogger(InitSalarySchedule.class);

	private Scheduler scheduler;

	private final String taskName = "SalaryTask";

	public InitSalarySchedule() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void init() throws ServletException {
		log.info("********** Task Strat Begin **********");
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();

			MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();

			// 任务信息配置
			jobDetail.setName(taskName);
			jobDetail.setTargetObject(new ExecuteAction());
			jobDetail.setTargetMethod("initData");
			jobDetail.setConcurrent(false);
			jobDetail.afterPropertiesSet();

			// 触发器配置
			CronTrigger cronTrigger = new CronTriggerBean();
			cronTrigger.setName(taskName);
			cronTrigger.setCronExpression("0 10 19 * * ?");

//			long startTime = System.currentTimeMillis() + 5000L;
//			SimpleTrigger trigger = new SimpleTrigger(taskName,
//					null, new Date(startTime), null, 0, 0L);

			scheduler.scheduleJob((JobDetail) jobDetail.getObject(), cronTrigger);

			scheduler.start();

			log.info("********** Task Start End **********");
		} catch (Exception e) {
			log.info("********** Task Start Fail : " + e.getMessage() + " **********");
		}
	}
}
