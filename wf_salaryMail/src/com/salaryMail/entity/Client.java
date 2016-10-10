package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Client implements RowMapper {

	private String companyNo;
	private String nameCh;
	private String nameEn;
	private String shortName;
	private String sales;
	private String compGrpCode;

	/**
	 * @return the companyNo
	 */
	public String getCompanyNo() {
		return companyNo;
	}

	/**
	 * @param companyNo
	 *            the companyNo to set
	 */
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	/**
	 * @return the nameCh
	 */
	public String getNameCh() {
		return nameCh;
	}

	/**
	 * @param nameCh
	 *            the nameCh to set
	 */
	public void setNameCh(String nameCh) {
		this.nameCh = nameCh;
	}

	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn
	 *            the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the sales
	 */
	public String getSales() {
		return sales;
	}

	/**
	 * @param sales
	 *            the sales to set
	 */
	public void setSales(String sales) {
		this.sales = sales;
	}

	/**
	 * @return the compGrpCode
	 */
	public String getCompGrpCode() {
		return compGrpCode;
	}

	/**
	 * @param compGrpCode
	 *            the compGrpCode to set
	 */
	public void setCompGrpCode(String compGrpCode) {
		this.compGrpCode = compGrpCode;
	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Client client = new Client();

		client.setCompanyNo(rs.getString("COMPANY_NO"));
		client.setNameCh(rs.getString("NAME_CH"));
		client.setNameEn(rs.getString("NAME_EN"));
		client.setShortName(rs.getString("SHORTNAME"));
		client.setSales(rs.getString("SALES"));
		client.setCompGrpCode(rs.getString("COMP_GRP_CODE"));

		return client;
	}
}
