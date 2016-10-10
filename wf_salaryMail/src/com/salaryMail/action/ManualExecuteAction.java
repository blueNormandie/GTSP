package com.salaryMail.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salaryMail.service.SalaryServerService;

@Controller
public class ManualExecuteAction {

	@Resource(name = "salaryServerService")
	private SalaryServerService salaryServerService;

	@RequestMapping("mailSend.action")
	public String execute() {
		try {

			salaryServerService.salaryMailSend();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}
