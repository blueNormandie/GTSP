package com.efesco.mailsvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailBatchStatus extends BaseObject {
	private Long batchNo;//序号
	private String isFinished;//是否结束
	private Date finishedDate;//结束时间
	private Long  successCnt;//发送成功数
	private Long failCnt;//发送失败数
	private Long denyCnt;//不可发送数
	private Long retryCnt;//发送次数
	private String isQueue;//是否在发送队列中
	private EmailStatusObject status;//发送状态 
	private List<EmailModel> failList;//失败列表
	private List<EmailModel> denyList;//不可发送列表
	public  String getIsQueue() {
		return isQueue;
	}
	public void setIsQueue(String isQueue) {
		this.isQueue = isQueue;
	}
	public Long getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(Long batchNo) {
		this.batchNo = batchNo;
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
	public void setFinishedDate(Date edDatefinishedDate) {
		this.finishedDate = edDatefinishedDate;
	}
	public Long getSuccessCnt() {
		if(successCnt==null)
			successCnt = new Long(0);
		return successCnt;
	}
	public void setSuccessCnt(Long successCnt) {
		this.successCnt = successCnt;
	}
	public Long getFailCnt() {
		if(failCnt==null)
			failCnt = new Long(0);
		return failCnt;
	}
	public void setFailCnt(Long failCnt) {
		this.failCnt = failCnt;
	}
	public Long getDenyCnt() {
		if(denyCnt==null)
			denyCnt = new Long(0);
		return denyCnt;
	}
	public void setDenyCnt(Long denyCnt) {
		this.denyCnt = denyCnt;
	}
	public Long getRetryCnt() {
		if(retryCnt==null)
			retryCnt = new Long(0);
		return retryCnt;
	}
	public void setRetryCnt(Long retryCnt) {
		this.retryCnt = retryCnt;
	}
	public List<EmailModel> getFailList() {
		if(failList==null)
			failList = new ArrayList<EmailModel>();
		return failList;
	}
	public void setFailList(List<EmailModel> failList) {
		this.failList = failList;
	}
	public List<EmailModel> getDenyList() {
		if(denyList==null)
			denyList = new ArrayList<EmailModel>();
		return denyList;
	}
	public void setDenyList(List<EmailModel> denyList) {
		this.denyList = denyList;
	}
	//是否结束
	public void finished() {
		this.isFinished = Constants.FINISHED_TRUE;
	}

	public void notFinished() {
		this.isFinished = Constants.FINISHED_FALSE;
	}
	public EmailStatusObject getStatus() {
		return status;
	}
	public void setStatus(EmailStatusObject status) {
		this.status = status;
	}

	public void incFailCnt() {
		this.failCnt = new Long(this.getFailCnt().longValue()+1L);
	}
	public void incDenyCnt() {
		this.denyCnt = new Long(this.getDenyCnt().longValue()+ 1L);
	}
	public void incsuccessCnt() {
		this.successCnt = new Long(this.getSuccessCnt().longValue()+ 1L);
	}
	public void incretryCnt() {
		this.retryCnt = new Long(this.getRetryCnt().longValue()+ 1L);
	}
	
}
