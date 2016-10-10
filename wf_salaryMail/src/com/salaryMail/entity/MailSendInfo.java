package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class MailSendInfo implements RowMapper {

	private Long resultSno;
	private Long mailBatchNo;
	private String bizType;
	private String empNo;
	private String companyNo;
	private String sendMail;
	private String fileName;
	private String sendStatus;
	private String failReason;
	private String isSendAgain;

	/**
	 * @return the resultSno
	 */
	public Long getResultSno() {
		return resultSno;
	}

	/**
	 * @param resultSno the resultSno to set
	 */
	public void setResultSno(Long resultSno) {
		this.resultSno = resultSno;
	}

	/**
	 * @return the mailBatchNo
	 */
	public Long getMailBatchNo() {
		return mailBatchNo;
	}

	/**
	 * @param mailBatchNo the mailBatchNo to set
	 */
	public void setMailBatchNo(Long mailBatchNo) {
		this.mailBatchNo = mailBatchNo;
	}

	/**
	 * @return the bizType
	 */
	public String getBizType() {
		return bizType;
	}

	/**
	 * @param bizType the bizType to set
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	/**
	 * @return the empNo
	 */
	public String getEmpNo() {
		return empNo;
	}

	/**
	 * @param empNo the empNo to set
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	/**
	 * @return the companyNo
	 */
	public String getCompanyNo() {
		return companyNo;
	}

	/**
	 * @param companyNo the companyNo to set
	 */
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	/**
	 * @return the sendMail
	 */
	public String getSendMail() {
		return sendMail;
	}

	/**
	 * @param sendMail the sendMail to set
	 */
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the sendStatus
	 */
	public String getSendStatus() {
		return sendStatus;
	}

	/**
	 * @param sendStatus the sendStatus to set
	 */
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	/**
	 * @return the failReason
	 */
	public String getFailReason() {
		return failReason;
	}

	/**
	 * @param failReason the failReason to set
	 */
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	/**
	 * @return the isSendAgain
	 */
	public String getIsSendAgain() {
		return isSendAgain;
	}

	/**
	 * @param isSendAgain the isSendAgain to set
	 */
	public void setIsSendAgain(String isSendAgain) {
		this.isSendAgain = isSendAgain;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		MailSendInfo mailSendInfo = new MailSendInfo();

		mailSendInfo.setResultSno(new Long(rs.getString("RESULT_SNO")));
		mailSendInfo.setMailBatchNo(new Long(rs.getString("MAIL_BATCH_NO")));
		mailSendInfo.setBizType(rs.getString("BIZ_TYPE"));
		mailSendInfo.setEmpNo(rs.getString("EMP_NO"));
		mailSendInfo.setCompanyNo(rs.getString("COMPANY_NO"));
		mailSendInfo.setSendMail(rs.getString("SEND_EMAIL"));
		mailSendInfo.setFileName(rs.getString("FILE_NAME"));
		mailSendInfo.setSendStatus(rs.getString("SEND_STATUS"));
		mailSendInfo.setFailReason(rs.getString("FAIL_REASON"));
		mailSendInfo.setIsSendAgain(rs.getString("IS_SEND_AGAIN"));

		return mailSendInfo;
	}

}
