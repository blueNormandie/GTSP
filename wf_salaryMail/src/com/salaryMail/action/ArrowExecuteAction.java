package com.salaryMail.action;

import com.salaryMail.service.ArrowSalaryServerService;
import com.salaryMail.util.SpringUtil;

public class ArrowExecuteAction {

	private ArrowSalaryServerService arrowSalaryServerService = (ArrowSalaryServerService) SpringUtil
			.getApplicationContext().getBean("arrowSalaryServerService");

	public String initData() {
		try {

			arrowSalaryServerService.sendMailForArrow();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
