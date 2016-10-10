package com.salaryMail.entity;

public class TempletSalaryInfo {
	private Integer bigTitlePosition;//大标题位置
	private String bigTitle;//大标题显示名称
	private String bigRemarks;//大标题标题备注信息
	private String isCount;//是否显示合计
	private String bigSum;//合计
	
	/**
	 * @return the bigTitlePosition
	 */
	public Integer getBigTitlePosition() {
		return bigTitlePosition;
	}
	/**
	 * @param bigTitlePosition the bigTitlePosition to set
	 */
	public void setBigTitlePosition(Integer bigTitlePosition) {
		this.bigTitlePosition = bigTitlePosition;
	}
	/**
	 * @return the bigTitle
	 */
	public String getBigTitle() {
		return bigTitle;
	}
	/**
	 * @param bigTitle the bigTitle to set
	 */
	public void setBigTitle(String bigTitle) {
		this.bigTitle = bigTitle;
	}
	/**
	 * @return the bigRemarks
	 */
	public String getBigRemarks() {
		return bigRemarks;
	}
	/**
	 * @param bigRemarks the bigRemarks to set
	 */
	public void setBigRemarks(String bigRemarks) {
		this.bigRemarks = bigRemarks;
	}
	
	
	public String getBigSum() {
		return bigSum;
	}
	public void setBigSum(String bigSum) {
		this.bigSum = bigSum;
	}
	/**
	 * @return the isCount
	 */
	public String getIsCount() {
		return isCount;
	}
	/**
	 * @param isCount the isCount to set
	 */
	public void setIsCount(String isCount) {
		this.isCount = isCount;
	}
	public TempletSalaryInfo(Integer bigTitlePosition, String bigTitle,
			String bigRemarks, String bigSum,String isCount) {
		super();
		this.bigTitlePosition = bigTitlePosition;
		this.bigTitle = bigTitle;
		this.bigRemarks = bigRemarks;
		this.bigSum = bigSum;
		this.isCount=isCount;
	}
	public TempletSalaryInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
