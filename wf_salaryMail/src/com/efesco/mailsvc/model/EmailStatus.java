package com.efesco.mailsvc.model;

import java.util.Date;

public class EmailStatus {
  private Long  smsNo;//序号
  private EmailStatusObject status;//发送状态
  private String denyRemarks;//错误原因
  private String isFinished;//是否结束
  private Date finishedDate;//结束时间
  private Date submitTime;//结束时间
public Long getSmsNo() {
	return smsNo;
}
public void setSmsNo(Long smsNo) {
	this.smsNo = smsNo;
}
public String getDenyRemarks() {
	return denyRemarks;
}
public void setDenyRemarks(String denyRemarks) {
	this.denyRemarks = denyRemarks;
}
public String getIsFinished() {
	return isFinished;
}
public void setIsFinished(String isFinished) {
	this.isFinished = isFinished;
}
public Date getFinishedDate() {
	return finishedDate;
}
public void setFinishedDate(Date finishedDate) {
	this.finishedDate = finishedDate;
}
public EmailStatusObject getStatus() {
	return status;
}
public void setStatus(EmailStatusObject status) {
	this.status = status;
}
public void finished() {
	this.isFinished = Constants.FINISHED_TRUE;
}


public void notFinished() {
	this.isFinished = Constants.FINISHED_FALSE;
}
public Date getSubmitTime() {
	return submitTime;
}
public void setSubmitTime(Date submitTime) {
	this.submitTime = submitTime;
}
}
