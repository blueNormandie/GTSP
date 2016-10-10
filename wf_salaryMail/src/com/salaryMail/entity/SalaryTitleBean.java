package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SalaryTitleBean  implements RowMapper{
	private String itemName;
	private String itemNameEn;
	private String fieldname;
	private String itemCode;
	private Integer displaycode;
	private Integer addFlag;

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemNameEn
	 */
	public String getItemNameEn() {
		return itemNameEn;
	}

	/**
	 * @param itemNameEn the itemNameEn to set
	 */
	public void setItemNameEn(String itemNameEn) {
		this.itemNameEn = itemNameEn;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldname() {
		return fieldname;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
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
	 * @return the displayCode
	 */
	public Integer getDisplaycode() {
		return displaycode;
	}

	/**
	 * @param displayCode the displayCode to set
	 */
	public void setDisplaycode(Integer displaycode) {
		this.displaycode = displaycode;
	}

	/**
	 * @return the addFlag
	 */
	public Integer getAddFlag() {
		return addFlag;
	}

	/**
	 * @param addFlag the addFlag to set
	 */
	public void setAddFlag(Integer addFlag) {
		this.addFlag = addFlag;
	}

	@Override
	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		SalaryTitleBean salaryTitleBean = new SalaryTitleBean();
		salaryTitleBean.setAddFlag(rs.getInt("add_flag"));
		salaryTitleBean.setFieldname(rs.getString("fieldname"));
		salaryTitleBean.setItemCode(String.valueOf(rs.getInt("item_code")));
		salaryTitleBean.setItemName(rs.getString("item_name"));
		salaryTitleBean.setItemNameEn(rs.getString("item_name_en"));
		salaryTitleBean.setDisplaycode(rs.getInt("displayCode"));

		return salaryTitleBean;
	}

}
