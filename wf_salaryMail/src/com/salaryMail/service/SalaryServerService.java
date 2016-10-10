package com.salaryMail.service;

import java.util.List;

public interface SalaryServerService {

	public void salaryMailSend() throws Exception;

	public String salaryPreview(String companyNo, String empNo,String batchNo)
			throws Exception;

	public boolean salaryMailResend(String companyNo, String empNo, String batchNo, String accYm,
			String realAccYm, String userId, Long resultSno, String filePath) throws Exception;
}
