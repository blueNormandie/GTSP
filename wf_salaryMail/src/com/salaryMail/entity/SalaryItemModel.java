package com.salaryMail.entity;

import com.lowagie.text.Element;

public class SalaryItemModel {

	private String itemTitle;
	private String itemPeriod;
	private Double itemValue;
	private Boolean titleCol=false;
	private Boolean valueCol=false;
	private Integer align=Element.ALIGN_RIGHT;
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
	public Double getItemValue() {
		return itemValue;
	}
	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}
	public Integer getAlign() {
		return align;
	}
	public void setAlign(Integer align) {
		this.align = align;
	}
	public Boolean getTitleCol() {
		return titleCol;
	}
	public void setTitleCol(Boolean titleCol) {
		this.titleCol = titleCol;
	}
	public Boolean getValueCol() {
		return valueCol;
	}
	public void setValueCol(Boolean valueCol) {
		this.valueCol = valueCol;
	}
}

