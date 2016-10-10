package com.salaryMail.util;

import org.apache.commons.dbcp.BasicDataSource;

public class DynamicDataSource {
	public BasicDataSource createDataSource(String driverClassName, String url,
			String username, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setTestWhileIdle(true);
		return dataSource;
	}
}
