package com.salaryMail.action;

import com.salaryMail.service.OnlinePayrollPreviewService;
import com.salaryMail.service.SalaryServerService;
import com.salaryMail.util.SpringUtil;

public class ExecuteAction {

	private SalaryServerService salaryServerService = (SalaryServerService) SpringUtil
			.getApplicationContext().getBean("salaryServerService");
	private OnlinePayrollPreviewService onlinePayrollPreviewService = (OnlinePayrollPreviewService) SpringUtil
			.getApplicationContext().getBean("OnlinePayrollPreviewService");

	public String initData() {
		try {

			salaryServerService.salaryMailSend();
			onlinePayrollPreviewService.clearHtml();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
