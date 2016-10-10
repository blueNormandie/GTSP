package com.efesco.mailsvc.model;

public class EmailStatusObject{

	private String statusCode;
	private String nameCh;

	public EmailStatusObject(){}

	 public EmailStatusObject (String statusCode,String nameCh ){

			this.statusCode = statusCode;
			this.nameCh = nameCh;
	 }
	public static EmailStatusObject getStatucObject(String code)
	{
		switch(Integer.parseInt(code)) 
		{
			case 1: return Constants.STATUS_WAIT_SEND;
			case 2: return Constants.STATUS_NOW_SENDING;
			case 3: return Constants.STATUS_SEND_SUCCESS;
			case 4: return Constants.STATUS_SEND_FAIL;
			case 5: return Constants.STATUS_CANNOT_SEND;
			case 6: return Constants.STATUS_CANCEL;
			default: return null;
		}		
	}
	
	
	public  boolean  equals(Object   obj)   { 
        if   (obj   ==   this)   { 
                return   true;      
        } 
        if   (!(obj   instanceof   EmailStatusObject))   { 
                return   false; 
        } 
        EmailStatusObject   other   =   (EmailStatusObject)obj; 
        return   statusCode.equals(other.statusCode); 
}
	
	public String getStatusCode() {
		return statusCode;
	} 
	public String getNameCh() {
		return nameCh;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public void setNameCh(String nameCh) {
		this.nameCh = nameCh;
	}
}
