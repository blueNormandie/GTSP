package com.efesco.mailsvc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EmailBatchModel extends BaseObject implements Serializable {
	
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public Long getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(Long batchNo) {
		this.batchNo = batchNo;
	}

	public String getHtmlTempAddr() {
		return htmlTempAddr;
	}
	public void setHtmlTempAddr(String htmlTempAddr) {
		this.htmlTempAddr = htmlTempAddr;
	}
	
	public String getIsMail() {
		return isMail;
	}
	public void setIsMail(String isMail) {
		this.isMail = isMail;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getSendDepts() {
		return sendDepts;
	}
	public void setSendDepts(String sendDepts) {
		this.sendDepts = sendDepts;
	}
	public String getSendDeptsName() {
		return sendDeptsName;
	}
	public void setSendDeptsName(String sendDeptsName) {
		this.sendDeptsName = sendDeptsName;
	}
	public String getSendUser() {
		return sendUser;
	}
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	public String getSendUserName() {
		return sendUserName;
	}
	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	public String getSendUserEmail() {
		return sendUserEmail;
	}
	public void setSendUserEmail(String sendUserEmail) {
		this.sendUserEmail = sendUserEmail;
	}
	public String getFremarks() {
		return fremarks;
	}
	public void setFremarks(String fremarks) {
		this.fremarks = fremarks;
	}
	
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	private String htmlTemptype;
	private Long batchNo;//���
	private Date beginSendDate;//��������ʱ��
	private Date endSendDate;//���?��ʱ��
	private String ydBingSendDate;//Լ����������ʱ��
	private String ydEndSendDate;//Լ�����?��ʱ��
	private String fileType;//�ļ�����
	private String fileFormatType;//�ļ���ʽ����;
	private String htmlTempAddr;//HTMLģ���ַ
	private String allowRetryCnt;//ʧ���ط�����
	private String isMail;//�Ƿ��ʼ��������
	private String bizType;//��Դҵ������
	private String sendDepts;//���Ͳ���
	private String sendDeptsName;//���Ͳ������
	private String sendUser;//������
	private String sendUserName;//����������
	private String sendUserEmail;//���������� 
	private String isUrgent;//�Ƿ���� 1������0��ͨ
	private String fremarks;//��ע
	private String caller;//��ԴӦ��	
	private double costTime;//���ͺ�ʱ
	private Date submitTime;//��������ʱ��
	//private List<EmailBizTypeModel> bizTypelist;
	private List<EmailModel> emaillist;//�ʼ��б�
	private boolean isLocked; //��������������
	private EmailBatchStatus batchStatus;//����EmailBatchStatus
	public EmailBatchStatus getBatchStatus() {
		return batchStatus;
	}
	public void setBatchStatus(EmailBatchStatus batchStatus) {
		this.batchStatus = batchStatus;
	}
	public List<EmailModel> getEmaillist() {
		return emaillist;
	}
	public void setEmaillist(List<EmailModel> emaillist) {
		this.emaillist = emaillist;
	}
    public void unlock() {
		this.isLocked = false;
	 }
	public boolean isLocked() {
		return isLocked;
	}
	public int hashCode() {
		return (int)(batchNo % Integer.MAX_VALUE);
	}
	public synchronized void lock() {
		while(isLocked)
		{
			try
			{
				Thread.sleep(Constants.LOCK_CHECK_INTERVAL);
			}
			catch(Exception e)
			{}
		}
		this.isLocked = true;
	}
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(! (o instanceof EmailBatchModel))		
			return false;
		EmailBatchModel b = (EmailBatchModel)o;
		if(b.getBatchNo() == this.getBatchNo())
			return true;
		else
			return false;
	}
	public String getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
	}
	
	public double getCostTime() {
		return costTime;
	}
	public void setCostTime(double costTime) {
		this.costTime = costTime;
	}
	public Date getBeginSendDate() {
		return beginSendDate;
	}
	public void setBeginSendDate(Date beginSendDate) {
		this.beginSendDate = beginSendDate;
	}
	public Date getEndSendDate() {
		return endSendDate;
	}
	public void setEndSendDate(Date endSendDate) {
		this.endSendDate = endSendDate;
	}
	public String getAllowRetryCnt() {
		return allowRetryCnt;
	}
	public void setAllowRetryCnt(String allowRetryCnt) {
		this.allowRetryCnt = allowRetryCnt;
	}
	public String getYdBingSendDate() {
		return ydBingSendDate;
	}
	public void setYdBingSendDate(String ydBingSendDate) {
		this.ydBingSendDate = ydBingSendDate;
	}
	public String getYdEndSendDate() {
		return ydEndSendDate;
	}
	public void setYdEndSendDate(String ydEndSendDate) {
		this.ydEndSendDate = ydEndSendDate;
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileFormatType() {
		return fileFormatType;
	}
	public void setFileFormatType(String fileFormatType) {
		this.fileFormatType = fileFormatType;
	}
	public void setHtmlTemptype(String htmlTemptype) {
		this.htmlTemptype = htmlTemptype;
	}
	public String getHtmlTemptype() {
		return htmlTemptype;
	}
}
