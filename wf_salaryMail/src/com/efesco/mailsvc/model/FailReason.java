package com.efesco.mailsvc.model;


public class FailReason {
	private int failCode;
	private String failMessage;

	public FailReason(int failCode, String failMessage) 
	{
		this.failCode = failCode;
		this.failMessage = failMessage;
	}
	
	public int getFailCode() {
		return failCode;
	}

	public void setFailCode(int failCode) {
		this.failCode = failCode;
	}

	public String getFailMessage() {
		return failMessage;
	}
	
	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	public static FailReason getInstance(int code)
	{
		switch(code)
		{
		    case 1:   return Constants.FAIL_REASON_00;
			case -1:  return Constants.FAIL_REASON_00;
			case 101: return Constants.FAIL_REASON_03;
			case 305: return Constants.FAIL_REASON_04;
			case 307: return Constants.FAIL_REASON_05;
			case 997: return Constants.FAIL_REASON_07;
			case 303: return Constants.FAIL_REASON_08;
			default:  return null;
		}
	}

}
