package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class  SdClientSalaryItem implements RowMapper {

	private String accYm;
	private String companyNo;
	private String itemCode;
	private String itemName;
	private String itemNameEn;
	private String isValid;
	private String isInherit;
	private String isPrint;
	private String fieldName;

	/**
	 * @return the accYm
	 */
	public String getAccYm() {
		return accYm;
	}

	/**
	 * @param accYm the accYm to set
	 */
	public void setAccYm(String accYm) {
		this.accYm = accYm;
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
	 * @return the isValid
	 */
	public String getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return the isInherit
	 */
	public String getIsInherit() {
		return isInherit;
	}

	/**
	 * @param isInherit the isInherit to set
	 */
	public void setIsInherit(String isInherit) {
		this.isInherit = isInherit;
	}

	/**
	 * @return the isPrint
	 */
	public String getIsPrint() {
		return isPrint;
	}

	/**
	 * @param isPrint the isPrint to set
	 */
	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SdClientSalaryItem sdClientSalaryItem = new SdClientSalaryItem();

		sdClientSalaryItem.setAccYm(rs.getString("ACC_YM"));
		sdClientSalaryItem.setCompanyNo(rs.getString("COMPANY_NO"));
		sdClientSalaryItem.setItemCode(String.valueOf(rs.getInt("ITEM_CODE")));
		sdClientSalaryItem.setItemName(rs.getString("ITEM_NAME"));
		sdClientSalaryItem.setItemNameEn(rs.getString("ITEM_NAME_EN"));
		sdClientSalaryItem.setIsValid(rs.getString("IS_VALID"));
		sdClientSalaryItem.setIsInherit(rs.getString("IS_INHERIT"));
		sdClientSalaryItem.setIsPrint(rs.getString("IS_PRINT"));
		sdClientSalaryItem.setFieldName(rs.getString("FIELDNAME"));;

		return sdClientSalaryItem;
	}
}
