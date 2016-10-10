package com.salaryMail.action;

import com.salaryMail.service.CoachSalaryServerService;
import com.salaryMail.util.SpringUtil;

public class CoachExecuteAction {

	private CoachSalaryServerService coachSalaryServerService = (CoachSalaryServerService) SpringUtil
			.getApplicationContext().getBean("arrowSalaryServerService");

	public String initData() {
		try {

			coachSalaryServerService.sendMailForCoach();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
