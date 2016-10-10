package com.salaryMail.util;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {
	
	private static Logger log = Logger.getLogger(SpringUtil.class);

	/** Spring框架应用上下文对�?*/
	private static ApplicationContext factory;

	/**
	 * 获得Spring框架应用上下文对�?
	 * 
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return factory;
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {

		factory = context;
	}
}
