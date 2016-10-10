package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class SalaryExecuteInfo implements RowMapper {

	private Long resultSno;
	private String isYearBonusSend;
	private String isSalarySend;
	private String isStockSend;
	private String isReleasePaySend;
	private String accYm;
	private String realAccYm;
	private String empNo;
	private String companyNo;

	/**
	 * @return the resultSno
	 */
	public Long getResultSno() {
		return resultSno;
	}

	/**
	 * @param resultSno the resultSno to set
	 */
	public void setResultSno(Long resultSno) {
		this.resultSno = resultSno;
	}

	/**
	 * @return the isYearBonusSend
	 */
	public String getIsYearBonusSend() {
		return isYearBonusSend;
	}

	/**
	 * @param isYearBonusSend the isYearBonusSend to set
	 */
	public void setIsYearBonusSend(String isYearBonusSend) {
		this.isYearBonusSend = isYearBonusSend;
	}

	/**
	 * @return the isSalarySend
	 */
	public String getIsSalarySend() {
		return isSalarySend;
	}

	/**
	 * @param isSalarySend the isSalarySend to set
	 */
	public void setIsSalarySend(String isSalarySend) {
		this.isSalarySend = isSalarySend;
	}

	/**
	 * @return the isStockSend
	 */
	public String getIsStockSend() {
		return isStockSend;
	}

	/**
	 * @param isStockSend the isStockSend to set
	 */
	public void setIsStockSend(String isStockSend) {
		this.isStockSend = isStockSend;
	}

	/**
	 * @return the isReleasePaySend
	 */
	public String getIsReleasePaySend() {
		return isReleasePaySend;
	}

	/**
	 * @param isReleasePaySend the isReleasePaySend to set
	 */
	public void setIsReleasePaySend(String isReleasePaySend) {
		this.isReleasePaySend = isReleasePaySend;
	}

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
	 * @return the realAccYm
	 */
	public String getRealAccYm() {
		return realAccYm;
	}

	/**
	 * @param realAccYm the realAccYm to set
	 */
	public void setRealAccYm(String realAccYm) {
		this.realAccYm = realAccYm;
	}

	/**
	 * @return the empNo
	 */
	public String getEmpNo() {
		return empNo;
	}

	/**
	 * @param empNo the empNo to set
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
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
		SalaryExecuteInfo salaryExecuteInfo = new SalaryExecuteInfo();

		salaryExecuteInfo.setIsYearBonusSend(rs.getString("IS_YEAR_BONUS_SEND"));
		salaryExecuteInfo.setIsSalarySend(rs.getString("IS_SALARY_SEND"));
		salaryExecuteInfo.setIsStockSend(rs.getString("IS_STOCK_SEND"));
		salaryExecuteInfo.setIsReleasePaySend(rs.getString("IS_RELEASE_PAY_SEND"));

		return salaryExecuteInfo;
	}

}
