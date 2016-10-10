package com.salaryMail.entity;

public class TempletBaseInfo {
	/**
	 * 工资模板第一部分基本信息
	 */
	private String titleValue;//标题
	private String itemValue;//数据
	private Integer isShow;//数据为空是否显示1：显示；0：不显示
	private Integer itemPosition;//项目所在位置
	private String itemCode;//项目编号
	
	/**
	 * @return the titleValue
	 */
	public String getTitleValue() {
		return titleValue;
	}
	/**
	 * @param titleValue the titleValue to set
	 */
	public void setTitleValue(String titleValue) {
		this.titleValue = titleValue;
	}
	/**
	 * @return the itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}
	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	/**
	 * @return the isShow
	 */
	public Integer getIsShow() {
		return isShow;
	}
	/**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}
	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	/**
	 * @return the itemPosition
	 */
	public Integer getItemPosition() {
		return itemPosition;
	}
	/**
	 * @param itemPosition the itemPosition to set
	 */
	public void setItemPosition(Integer itemPosition) {
		this.itemPosition = itemPosition;
	}
	public TempletBaseInfo(String titleValue, String itemValue, Integer isShow,
			Integer itemPosition, String itemCode) {
		super();
		this.titleValue = titleValue;
		this.itemValue = itemValue;
		this.isShow = isShow;
		this.itemPosition = itemPosition;
		this.itemCode = itemCode;
	}
	public TempletBaseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
