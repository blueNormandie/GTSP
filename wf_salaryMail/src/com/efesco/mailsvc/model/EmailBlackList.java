package com.efesco.mailsvc.model;

public class EmailBlackList {
    private  String biz_type;
    private  String filter_type;
    private String black_code;
	public String getBiz_type() {
		return biz_type;
	}
	public void setBiz_type(String biz_type) {
		this.biz_type = biz_type;
	}
	public String getFilter_type() {
		return filter_type;
	}
	public void setFilter_type(String filter_type) {
		this.filter_type = filter_type;
	}
	public String getBlack_code() {
		return black_code;
	}
	public void setBlack_code(String black_code) {
		this.black_code = black_code;
	}
    
}
