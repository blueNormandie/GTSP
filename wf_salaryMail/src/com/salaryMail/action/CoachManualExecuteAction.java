package com.salaryMail.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salaryMail.service.CoachSalaryServerService;

@Controller
public class CoachManualExecuteAction {

	@Resource(name = "coachSalaryServerService")
	private CoachSalaryServerService coachSalaryServerService;

	@RequestMapping("coachMailSend.action")
	public String execute() {
		try {

			coachSalaryServerService.sendMailForCoach();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}
