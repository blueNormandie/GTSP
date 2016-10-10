package com.salaryMail.entity;

import java.util.List;

public class CoachSalary {
	
	private String payrollPeriod;
	private String eeid;
	private String employeeName;
	private String costCenter;
	private String department;
	private List<CoachEarnSalary> coachEarnSalaryList;
	private List<CoachDeductSalary> coachDeductSalary;
	private String netPay;
	
	
	public String getNetPay() {
		return netPay;
	}
	public void setNetPay(String netPay) {
		this.netPay = netPay;
	}
	public String getPayrollPeriod() {
		return payrollPeriod;
	}
	public void setPayrollPeriod(String payrollPeriod) {
		this.payrollPeriod = payrollPeriod;
	}
	public String getEeid() {
		return eeid;
	}
	public void setEeid(String eeid) {
		this.eeid = eeid;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public List<CoachEarnSalary> getCoachEarnSalaryList() {
		return coachEarnSalaryList;
	}
	public void setCoachEarnSalaryList(List<CoachEarnSalary> coachEarnSalaryList) {
		this.coachEarnSalaryList = coachEarnSalaryList;
	}
	public List<CoachDeductSalary> getCoachDeductSalary() {
		return coachDeductSalary;
	}
	public void setCoachDeductSalary(List<CoachDeductSalary> coachDeductSalary) {
		this.coachDeductSalary = coachDeductSalary;
	}
	
	
}
