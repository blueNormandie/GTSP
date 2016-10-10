package com.salaryMail.entity;

public class TempletSalaryItemInfo {
	private String itemTitle;//工资标题
	private String itemValue;//项目值
	private Integer itemIsShow;//项目值为0是否显示
	private Integer itemBelongPosition;//所在大标题位置
	private String itemCode;//项目编号
	private Integer itemPosition;//项目位置
	/**
	 * @return the itemTitle
	 */
	public String getItemTitle() {
		return itemTitle;
	}
	/**
	 * @param itemTitle the itemTitle to set
	 */
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
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
	 * @return the itemIsShow
	 */
	public Integer getItemIsShow() {
		return itemIsShow;
	}
	/**
	 * @param itemIsShow the itemIsShow to set
	 */
	public void setItemIsShow(Integer itemIsShow) {
		this.itemIsShow = itemIsShow;
	}
	/**
	 * @return the itemBelongPosition
	 */
	public Integer getItemBelongPosition() {
		return itemBelongPosition;
	}
	/**
	 * @param itemBelongPosition the itemBelongPosition to set
	 */
	public void setItemBelongPosition(Integer itemBelongPosition) {
		this.itemBelongPosition = itemBelongPosition;
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
	public TempletSalaryItemInfo(String itemTitle, String itemValue,
			Integer itemIsShow, Integer itemBelongPosition, String itemCode,
			Integer itemPosition) {
		super();
		this.itemTitle = itemTitle;
		this.itemValue = itemValue;
		this.itemIsShow = itemIsShow;
		this.itemBelongPosition = itemBelongPosition;
		this.itemCode = itemCode;
		this.itemPosition = itemPosition;
	}
	public TempletSalaryItemInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
