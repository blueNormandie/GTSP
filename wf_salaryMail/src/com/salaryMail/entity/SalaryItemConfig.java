package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class SalaryItemConfig implements RowMapper {

	private Integer formatSno;
	private Integer fieldNo;
	private Integer itemCode;
	private Integer itemPosition;
	private String isZeroDisplay;
	private String creator;
	private Date createDate;
	private String modifier;
	private Date modifyDate;
	
	
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
	 * @return the fieldNo
	 */
	public Integer getfieldNo() {
		return fieldNo;
	}



	/**
	 * @param fieldNo the fieldNo to set
	 */
	public void setfieldNo(Integer fieldNo) {
		this.fieldNo = fieldNo;
	}



	/**
	 * @return the itemCode
	 */
	public Integer getItemCode() {
		return itemCode;
	}



	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(Integer itemCode) {
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



	/**
	 * @return the isZeroDisplay
	 */
	public String getIsZeroDisplay() {
		return isZeroDisplay;
	}



	/**
	 * @param isZeroDisplay the isZeroDisplay to set
	 */
	public void setIsZeroDisplay(String isZeroDisplay) {
		this.isZeroDisplay = isZeroDisplay;
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



	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SalaryItemConfig salaryItemConfig=new SalaryItemConfig();
		
		salaryItemConfig.setCreateDate(rs.getDate("CREATE_DATE"));
		salaryItemConfig.setCreator(rs.getString("CREATOR"));
		salaryItemConfig.setfieldNo(rs.getInt("FIELD_NO"));
		salaryItemConfig.setFormatSno(rs.getInt("FORMAT_SNO"));
		salaryItemConfig.setIsZeroDisplay(rs.getString("IS_ZERO_DISLAPY"));
		salaryItemConfig.setItemCode(rs.getInt("ITEM_CODE"));
		salaryItemConfig.setItemPosition(rs.getInt("ITEM_POSITION"));
		salaryItemConfig.setModifier(rs.getString("MODIFIER"));
		salaryItemConfig.setModifyDate(rs.getDate("MODIFY_DATE"));
		return salaryItemConfig;
	}

}
