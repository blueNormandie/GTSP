package com.salaryMail.entity;

import com.lowagie.text.Element;

public class SalaryItemCAndPModel {


	private String itemTitle;
	private String itemPeriod;
	private Double itemValueC;
	private Double itemValueP;
	private Integer alignC=Element.ALIGN_RIGHT;
	private Integer alignP=Element.ALIGN_RIGHT;
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getItemPeriod() {
		return itemPeriod;
	}
	public void setItemPeriod(String itemPeriod) {
		this.itemPeriod = itemPeriod;
	}
	public Double getItemValueC() {
		return itemValueC;
	}
	public void setItemValueC(Double itemValueC) {
		this.itemValueC = itemValueC;
	}
	
	public Double getItemValueP() {
		return itemValueP;
	}
	public void setItemValueP(Double itemValueP) {
		this.itemValueP = itemValueP;
	}
	public Integer getAlignC() {
		return alignC;
	}
	public void setAlignC(Integer alignC) {
		this.alignC = alignC;
	}
	public Integer getAlignP() {
		return alignP;
	}
	public void setAlignP(Integer alignP) {
		this.alignP = alignP;
	}
	

}
