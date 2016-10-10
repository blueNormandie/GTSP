package com.salaryMail.service;

import java.util.List;

import com.efesco.mailsvc.model.EmailBlackList;
import com.efesco.mailsvc.model.EmailBatchModel;
import com.efesco.mailsvc.model.EmailBatchStatus;

public interface MailService {
	public EmailBatchStatus sendSalaryEmailBatch(String servicePassword,
			EmailBatchModel emailModel);

	public long sendEmailBatch(String servicePassword,
			EmailBatchModel emailModel) throws Exception;

	public EmailBatchStatus sendSynEmailBatch(String servicePassword,
			EmailBatchModel emailModel, String mailcode) throws Exception;

	public void dealconfigDbEmail(String servicePassword, String dbName)
			throws Exception;

	public EmailBatchStatus queryBatchStatus(String servicePassword,
			long batchNo) throws Exception;
	
	public List<EmailBlackList> getBlackMaps(String bizType,String servicePassword) throws Exception;
}
