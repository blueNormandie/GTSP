package com.efesco.mailsvc.model;

import java.sql.Date;

public class EmailModel extends BaseObject{
	public Long getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(Long batchNo) {
		this.batchNo = batchNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachAddr() {
		return attachAddr;
	}
	public void setAttachAddr(String attachAddr) {
		this.attachAddr = attachAddr;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getCompGrpCode() {
		return compGrpCode;
	}
	public void setCompGrpCode(String compGrpCode) {
		this.compGrpCode = compGrpCode;
	}
	public String getCompanyNo() {
		return companyNo;
	}
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}
	public String getRcvType() {
		return rcvType;
	}
	public void setRcvType(String rcvType) {
		this.rcvType = rcvType;
	}
	public String getRcvId() {
		return rcvId;
	}
	public void setRcvId(String rcvId) {
		this.rcvId = rcvId;
	}
	public String getRcvName() {
		return rcvName;
	}
	public void setRcvName(String rcvName) {
		this.rcvName = rcvName;
	}
	public String getRcvMailAddr() {
		return rcvMailAddr;
	}
	public void setRcvMailAddr(String rcvMailAddr) {
		this.rcvMailAddr = rcvMailAddr;
	}
	private Long batchNo;//批次号
	private Long smsNo;//序号
	private String subject;//邮件主题
	private String content;//邮件内容
	private String attachAddr;//附件地址
	private Date submitTime;//提交发送时间
	private String encryptKey;//附件加密密钥
	private String isEncryptAttach;//附件是否要加密 ：1是，2否
	private String deptNo;//接收者所属业务部
	private String compGrpCode;//接收者所属客户组
	private String companyNo;//接收者所属客户
	private String rcvType;//接收者类别
	private String rcvId;//接收者ID
	private String rcvName;//接收者名称
	private String rcvMailAddr;//收件人邮箱
	private EmailStatus emailStatus; //引入类
	public Long getSmsNo() {
		return smsNo;
	}
	public void setSmsNo(Long smsNo) {
		this.smsNo = smsNo;
	}
	public EmailStatus getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(EmailStatus emailStatus) {
		this.emailStatus = emailStatus;
	}
	
	public String getEncryptKey() {
		return encryptKey;
	}
	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}
	public String getIsEncryptAttach() {
		return isEncryptAttach;
	}
	public void setIsEncryptAttach(String isEncryptAttach) {
		this.isEncryptAttach = isEncryptAttach;
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	
	
	
}
