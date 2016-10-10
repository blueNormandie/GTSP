package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class FormatInfo implements RowMapper {

	private Integer formatSno;
	private String formatType;
	private String createCompanyNo;
	private String sales;
	private String belongToDept;
	private String formatName;
	private String remarks;
	private String announcement;
	private String baseAccYm;
	private String sendDateDisplayName;
	private String sendDateIsDisplay;
	private String salaryPeriodDisplayName;
	private String salaryPeriodIsDisplay;
	private String creator;
	private Date createDate;
	private String modifier;
	private Date modifyDate;
	private String isEpayRemarksDisplay;

	/**
	 * @return the baseAccYm
	 */
	public String getBaseAccYm() {
		return baseAccYm;
	}

	/**
	 * @param baseAccYm the baseAccYm to set
	 */
	public void setBaseAccYm(String baseAccYm) {
		this.baseAccYm = baseAccYm;
	}

	/**
	 * @return the sales
	 */
	public String getSales() {
		return sales;
	}

	/**
	 * @param sales the sales to set
	 */
	public void setSales(String sales) {
		this.sales = sales;
	}

	/**
	 * @return the formatSno
	 */
	public Integer getFormatSno() {
		return formatSno;
	}

	/**
	 * @param formatSno
	 *            the formatSno to set
	 */
	public void setFormatSno(Integer formatSno) {
		this.formatSno = formatSno;
	}

	/**
	 * @return the formatType
	 */
	public String getFormatType() {
		return formatType;
	}

	/**
	 * @param formatType
	 *            the formatType to set
	 */
	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	/**
	 * @return the formatName
	 */
	public String getFormatName() {
		return formatName;
	}

	/**
	 * @param formatName
	 *            the formatName to set
	 */
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks
	 *            the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier
	 *            the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param modifyDate
	 *            the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	/**
	 * @return the announcement
	 */
	public String getAnnouncement() {
		return announcement;
	}

	/**
	 * @param announcement the announcement to set
	 */
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	
	/**
	 * @return the createCompanyNo
	 */
	public String getCreateCompanyNo() {
		return createCompanyNo;
	}

	/**
	 * @param createCompanyNo the createCompanyNo to set
	 */
	public void setCreateCompanyNo(String createCompanyNo) {
		this.createCompanyNo = createCompanyNo;
	}

	/**
	 * @return the belongToDept
	 */
	public String getBelongToDept() {
		return belongToDept;
	}

	/**
	 * @param belongToDept the belongToDept to set
	 */
	public void setBelongToDept(String belongToDept) {
		this.belongToDept = belongToDept;
	}

	/**
	 * @return the sendDateDisplayName
	 */
	public String getSendDateDisplayName() {
		return sendDateDisplayName;
	}

	/**
	 * @param sendDateDisplayName the sendDateDisplayName to set
	 */
	public void setSendDateDisplayName(String sendDateDisplayName) {
		this.sendDateDisplayName = sendDateDisplayName;
	}

	/**
	 * @return the sendDateIsDisplay
	 */
	public String getSendDateIsDisplay() {
		return sendDateIsDisplay;
	}

	/**
	 * @param sendDateIsDisplay the sendDateIsDisplay to set
	 */
	public void setSendDateIsDisplay(String sendDateIsDisplay) {
		this.sendDateIsDisplay = sendDateIsDisplay;
	}

	/**
	 * @return the salaryPeriodDisplayName
	 */
	public String getSalaryPeriodDisplayName() {
		return salaryPeriodDisplayName;
	}

	/**
	 * @param salaryPeriodDisplayName the salaryPeriodDisplayName to set
	 */
	public void setSalaryPeriodDisplayName(String salaryPeriodDisplayName) {
		this.salaryPeriodDisplayName = salaryPeriodDisplayName;
	}

	/**
	 * @return the salaryPeriodIsDisplay
	 */
	public String getSalaryPeriodIsDisplay() {
		return salaryPeriodIsDisplay;
	}

	/**
	 * @param salaryPeriodIsDisplay the salaryPeriodIsDisplay to set
	 */
	public void setSalaryPeriodIsDisplay(String salaryPeriodIsDisplay) {
		this.salaryPeriodIsDisplay = salaryPeriodIsDisplay;
	}
	
	/**
	 * @return the isEpayRemarksDisplay
	 */
	public String getIsEpayRemarksDisplay() {
		return isEpayRemarksDisplay;
	}

	/**
	 * @param isEpayRemarksDisplay the isEpayRemarksDisplay to set
	 */
	public void setIsEpayRemarksDisplay(String isEpayRemarksDisplay) {
		this.isEpayRemarksDisplay = isEpayRemarksDisplay;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		FormatInfo formatInfo = new FormatInfo();

		formatInfo.setCreateDate(rs.getDate("CREATE_DATE"));
		formatInfo.setCreator(rs.getString("CREATOR"));
		formatInfo.setFormatName(rs.getString("FORMAT_NAME"));
		formatInfo.setFormatSno(rs.getInt("FORMAT_SNO"));
		formatInfo.setFormatType(rs.getString("FORMAT_TYPE"));
		formatInfo.setModifier(rs.getString("MODIFIER"));
		formatInfo.setModifyDate(rs.getDate("MODIFY_DATE"));
		formatInfo.setRemarks(rs.getString("REMARKS"));
		formatInfo.setAnnouncement(rs.getString("ANNOUNCEMENT"));
		formatInfo.setBelongToDept(rs.getString("BELONG_TO_DEPT"));
		formatInfo.setSales(rs.getString("SALES"));
		formatInfo.setCreateCompanyNo(rs.getString("CREATE_COMPANY_NO"));
		formatInfo.setSendDateDisplayName(rs.getString("SEND_DATE_DISPLAY_NAME"));
		formatInfo.setSendDateIsDisplay(rs.getString("SEND_DATE_IS_DISPLAY"));
		formatInfo.setSalaryPeriodDisplayName(rs.getString("SALARY_PERIOD_DISPLAY_NAME"));
		formatInfo.setSalaryPeriodIsDisplay(rs.getString("SALARY_PERIOD_IS_DISPLAY"));
		formatInfo.setIsEpayRemarksDisplay(rs.getString("IS_EPAY_REMARKS_DISPLAY"));
		formatInfo.setBaseAccYm(rs.getString("BASE_ACC_YM"));
		return formatInfo;
	}

}
