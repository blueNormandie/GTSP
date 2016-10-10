package com.salaryMail.entity;

import java.util.List;

public class SalaryTempletData {
	/**
	 * 分三部分
	 * 1、基本信息
	 * 2、工资信息
	 * 3、其他信息
	 */
	private FormatInfo formatInfo;
	private String sendDate;
	private String datePeriod;
	private List<TempletBaseInfo> firstData;
	private List<TempletSalaryInfo> secondData;
	private List<TempletSalaryItemInfo> secondItemData;
	private String remarks;
	private String ItemNameType;
	private String isCompnameDisplay;
	private String companyNameCh;
	private String companyNameEn;
	private String bigTitleRemarksIsDisplay;
	private String logoPath;
	
	
	
	public String getLogoPath() {
		return logoPath;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	public String getBigTitleRemarksIsDisplay() {
		return bigTitleRemarksIsDisplay;
	}
	public void setBigTitleRemarksIsDisplay(String bigTitleRemarksIsDisplay) {
		this.bigTitleRemarksIsDisplay = bigTitleRemarksIsDisplay;
	}
	public String getCompanyNameCh() {
		return companyNameCh;
	}
	public void setCompanyNameCh(String companyNameCh) {
		this.companyNameCh = companyNameCh;
	}
	public String getCompanyNameEn() {
		return companyNameEn;
	}
	public void setCompanyNameEn(String companyNameEn) {
		this.companyNameEn = companyNameEn;
	}
	public String getIsCompnameDisplay() {
		return isCompnameDisplay;
	}
	public void setIsCompnameDisplay(String isCompnameDisplay) {
		this.isCompnameDisplay = isCompnameDisplay;
	}
	/**
	 * @return the firstData
	 */
	public List<TempletBaseInfo> getfirstData() {
		return firstData;
	}
	/**
	 * @param firstData the firstData to set
	 */
	public void setfirstData(List<TempletBaseInfo> firstData) {
		this.firstData = firstData;
	}
	/**
	 * @return the secondData
	 */
	public List<TempletSalaryInfo> getSecondData() {
		return secondData;
	}
	/**
	 * @param secondData the secondData to set
	 */
	public void setSecondData(List<TempletSalaryInfo> secondData) {
		this.secondData = secondData;
	}
	
	/**
	 * @return the secondItemData
	 */
	public List<TempletSalaryItemInfo> getSecondItemData() {
		return secondItemData;
	}
	/**
	 * @param secondItemData the secondItemData to set
	 */
	public void setSecondItemData(List<TempletSalaryItemInfo> secondItemData) {
		this.secondItemData = secondItemData;
	}
	/**
	 * @return the formatInfo
	 */
	public FormatInfo getFormatInfo() {
		return formatInfo;
	}
	/**
	 * @param formatInfo the formatInfo to set
	 */
	public void setFormatInfo(FormatInfo formatInfo) {
		this.formatInfo = formatInfo;
	}
	/**
	 * @return the firstData
	 */
	public List<TempletBaseInfo> getFirstData() {
		return firstData;
	}
	/**
	 * @param firstData the firstData to set
	 */
	public void setFirstData(List<TempletBaseInfo> firstData) {
		this.firstData = firstData;
	}
	/**
	 * @return the sendDate
	 */
	public String getSendDate() {
		return sendDate;
	}
	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	/**
	 * @return the datePeriod
	 */
	public String getDatePeriod() {
		return datePeriod;
	}
	/**
	 * @param datePeriod the datePeriod to set
	 */
	public void setDatePeriod(String datePeriod) {
		this.datePeriod = datePeriod;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the itemNameType
	 */
	public String getItemNameType() {
		return ItemNameType;
	}
	/**
	 * @param itemNameType the itemNameType to set
	 */
	public void setItemNameType(String itemNameType) {
		ItemNameType = itemNameType;
	}
	
}
