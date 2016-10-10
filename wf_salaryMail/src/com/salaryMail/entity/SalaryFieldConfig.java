package com.salaryMail.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class SalaryFieldConfig implements RowMapper {

	private Integer formatSno;
	private Integer fieldNo;
	private String displayName;
	private String remarks;
	private String creator;
	private Date createDate;
	private String modifier;
	private Date modifyDate;
	private String isCount;
	private String displayNameEn;
	
	
	
	public String getDisplayNameEn() {
		return displayNameEn;
	}



	public void setDisplayNameEn(String displayNameEn) {
		this.displayNameEn = displayNameEn;
	}



	/**
	 * @return the formatSno
	 */
	public Integer getFormatSno() {
		return formatSno;
	}



	/**
	 * @param formatSno the formatSno to set
	 */
	public void setFormatSno(Integer formatSno) {
		this.formatSno = formatSno;
	}



	/**
	 * @return the fieldSno
	 */
	public Integer getFieldNo() {
		return fieldNo;
	}



	/**
	 * @param fieldSno the fieldSno to set
	 */
	public void setFieldNo(Integer fieldNo) {
		this.fieldNo = fieldNo;
	}



	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}



	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}



	/**
	 * @param creator the creator to set
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
	 * @param createDate the createDate to set
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
	 * @param modifier the modifier to set
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
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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



	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SalaryFieldConfig salaryFieldConfig=new SalaryFieldConfig();
		
		salaryFieldConfig.setCreateDate(rs.getDate("CREATE_DATE"));
		salaryFieldConfig.setCreator(rs.getString("CREATOR"));
		salaryFieldConfig.setDisplayName(rs.getString("DISPLAY_NAME"));
		salaryFieldConfig.setDisplayNameEn(rs.getString("DISPLAY_NAME_EN"));
		salaryFieldConfig.setFieldNo(rs.getInt("FIELD_NO"));
		salaryFieldConfig.setFormatSno(rs.getInt("FORMAT_SNO"));
		salaryFieldConfig.setModifier(rs.getString("MODIFIER"));
		salaryFieldConfig.setModifyDate(rs.getDate("MODIFY_DATE"));
		salaryFieldConfig.setRemarks(rs.getString("REMARKS"));
		salaryFieldConfig.setIsCount(rs.getString("IS_COUNT"));
		return salaryFieldConfig;
	}

}
