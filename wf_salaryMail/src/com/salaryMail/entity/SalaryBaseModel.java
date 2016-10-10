package com.salaryMail.entity;

import java.util.List;

import com.salaryMail.entity.SalaryItemCAndPModel;
import com.salaryMail.entity.SalaryItemModel;

public class SalaryBaseModel {

	private String payDay;
	private String name;
	private String globalId;
	private String legalEntity;
	private String location;
	private String department;
	private String joinDate;
	private Double baseSalary;
	private String mail;
	private String id;
	private String empNo;
	
	private List<SalaryItemModel> firstDatas;
	private List<SalaryItemModel> secondDatas;
	private List<SalaryItemCAndPModel> thirdDatas;

	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getPayDay() {
		return payDay;
	}
	public void setPayDay(String payDay) {
		this.payDay = payDay;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGlobalId() {
		return globalId;
	}
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	public String getLegalEntity() {
		return legalEntity;
	}
	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public Double getBaseSalary() {
		return baseSalary;
	}
	public void setBaseSalary(Double baseSalary) {
		this.baseSalary = baseSalary;
	}
	public List<SalaryItemModel> getFirstDatas() {
		return firstDatas;
	}
	public void setFirstDatas(List<SalaryItemModel> firstDatas) {
		this.firstDatas = firstDatas;
	}
	public List<SalaryItemModel> getSecondDatas() {
		return secondDatas;
	}
	public void setSecondDatas(List<SalaryItemModel> secondDatas) {
		this.secondDatas = secondDatas;
	}
	public List<SalaryItemCAndPModel> getThirdDatas() {
		return thirdDatas;
	}
	public void setThirdDatas(List<SalaryItemCAndPModel> thirdDatas) {
		this.thirdDatas = thirdDatas;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
