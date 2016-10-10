package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class BaseItemConfig implements RowMapper {
	
	private Integer formatSno;
	private String itemNo;
	private String isNullDisplay;
	private String displayName;
	private Integer itemPosition;
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
	 * @return the itemNo
	 */
	public String getItemNo() {
		return itemNo;
	}




	/**
	 * @param itemNo the itemNo to set
	 */
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}


	/**
	 * @return the isNullDisplay
	 */
	public String getIsNullDisplay() {
		return isNullDisplay;
	}




	/**
	 * @param isNullDisplay the isNullDisplay to set
	 */
	public void setIsNullDisplay(String isNullDisplay) {
		this.isNullDisplay = isNullDisplay;
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
		BaseItemConfig baseItemConfig=new BaseItemConfig();
		
		baseItemConfig.setCreateDate(rs.getDate("CREATE_DATE"));
		baseItemConfig.setCreator(rs.getString("CREATOR"));
		baseItemConfig.setDisplayName(rs.getString("DISPLAY_NAME"));
		baseItemConfig.setFormatSno(rs.getInt("FORMAT_SNO"));
		baseItemConfig.setIsNullDisplay(rs.getString("IS_NULL_DISPLAY"));
		baseItemConfig.setItemNo(rs.getString("ITEM_NO"));
		baseItemConfig.setItemPosition(rs.getInt("ITEM_POSITION"));
		baseItemConfig.setModifier(rs.getString("MODIFIER"));
		baseItemConfig.setModifyDate(rs.getDate("MODIFY_DATE"));
		return baseItemConfig;
	}
	
}
