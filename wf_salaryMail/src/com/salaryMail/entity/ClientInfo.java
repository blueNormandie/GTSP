package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class ClientInfo implements RowMapper {

	private String companyNo;
	private String companyNameCh;
	private String companyNameEn;
	private String companyName;
	private String belongToDept;
	private String deptName;
	private String userId;
	private String userName;
	private String compGrpCode;
	private String sfscCode;

	
	public String getSfscCode() {
		return sfscCode;
	}

	public void setSfscCode(String sfscCode) {
		this.sfscCode = sfscCode;
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

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the compGrpCode
	 */
	public String getCompGrpCode() {
		return compGrpCode;
	}

	/**
	 * @param compGrpCode the compGrpCode to set
	 */
	public void setCompGrpCode(String compGrpCode) {
		this.compGrpCode = compGrpCode;
	}

	/**
	 * @return the companyNo
	 */
	public String getCompanyNo() {
		return companyNo;
	}

	/**
	 * @param companyNo the companyNo to set
	 */
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClientInfo clientInfo=new ClientInfo();
		
		clientInfo.setCompanyNo(rs.getString("COMPANY_NO"));
		clientInfo.setCompanyName(rs.getString("COMPANY_NAME"));
		clientInfo.setBelongToDept(rs.getString("BELONG_TO_DEPT"));
		clientInfo.setDeptName(rs.getString("DEPT_NAME"));
		clientInfo.setUserId(rs.getString("USER_ID"));
		clientInfo.setUserName(rs.getString("USER_NAME"));
		clientInfo.setCompGrpCode(rs.getString("COMP_GRP_CODE"));
		clientInfo.setCompanyNameCh(rs.getString("NAME_CH"));
		clientInfo.setCompanyNameEn(rs.getString("NAME_EN"));
		clientInfo.setSfscCode(rs.getString("SFSC_CODE"));
		return clientInfo;
	}

}
