package com.salaryMail.service;

public interface OnlinePayrollPreviewService {

	public String OnlineSalaryPreview(String companyNo, String empNo,String batchNo)
			throws Exception;
	
	public void clearHtml();
}
