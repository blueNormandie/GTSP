package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Salary implements RowMapper {

	private String sno;
	private String reg_no;
	private String dept_no;
	private String batch_no;
	private String emp_no;
	private String name;
	private String nationality;
	private String depart_no;
	private String position_code;
	private String employee_type;
	private String real_acc_ym;
	private String unit_no;
	private String province_code;
	private String city_code;
	private String bank_code;
	private String revenue_code;
	private String currency_type;
	private String pretax_salary;
	private String month_bonus;
	private String overtime_salary;
	private String overtime_salary_deduct_tax;
	private String traffic_allowance;
	private String traffic_allowance_deduct_tax;
	private String lunch_allowance;
	private String lunch_allowance_deduct_tax;
	private String oep_basic;
	private String oep_per;
	private String lose_job_insu;
	private String medical_insu;
	private String accfund_insu;
	private String append_accfund;
	private String person_pay_total;
	private String allowance1;
	private String allowance2;
	private String allowance3;
	private String allowance4;
	private String allowance5;
	private String free_tax_allowance1;
	private String free_tax_allowance2;
	private String free_tax_allowance3;
	private String free_tax_allowance4;
	private String free_tax_allowance5;
	private String deduct1;
	private String deduct2;
	private String deduct3;
	private String deduct4;
	private String deduct5;
	private String free_tax_deduct1;
	private String free_tax_deduct2;
	private String free_tax_deduct3;
	private String free_tax_deduct4;
	private String free_tax_deduct5;
	private String calc_field1;
	private String calc_field2;
	private String display1;
	private String display2;
	private String display3;
	private String outer_insu;
	private String should_pay_tax;
	private String tax;
	private String adjust_tax;
	private String salary_card_sno;
	private String real_tax_acc_ym;
	private String salary_payrevenu_sno;
	private String bonus_type;
	private String bonus_adjust_tax;
	private String bonus_tax_adjust_reason;
	private String bonus_card_sno;
	private String bonus_read_tax_acc_ym;
	private String bonus_payrevenu_sno;
	private String compute_flag;
	private String input_person;
	private String input_date;
	private String wf_rcvsalary;
	private String oep_c;
	private String medical_c;
	private String lose_job_c;
	private String freercv_allowance1;
	private String freercv_allowance2;
	private String freercv_allowance3;
	private String allowance6;
	private String allowance7;
	private String allowance8;
	private String allowance9;
	private String allowance10;
	private String free_tax_allowance6;
	private String free_tax_allowance7;
	private String free_tax_allowance8;
	private String free_tax_allowance9;
	private String free_tax_allowance10;
	private String accfund_base;
	private String fund_total;
	private String work_place;
	private String display4;
	private String display5;
	private String display6;
	private String display7;
	private String display8;
	private String display9;
	private String display10;
	private String display11;
	private String display12;
	private String display13;
	private String display14;
	private String display15;
	private String injury_insu;
	private String childbirth_insu;
	private String supply_social_insu;
	private String free_tax_deduct6;
	private String free_tax_deduct7;
	private String free_tax_deduct8;
	private String free_tax_deduct9;
	private String free_tax_deduct10;
	private String accfund_c;
	private String salary_sno;
	private String deduct6;
	private String deduct7;
	private String deduct8;
	private String deduct9;
	private String deduct10;
	private String allowance_no_pay3;
	private String allowance_no_pay4;
	private String allowance_no_pay5;
	private String allowance11;
	private String allowance12;
	private String allowance13;
	private String allowance14;
	private String allowance15;
	private String allowance16;
	private String allowance17;
	private String allowance18;
	private String allowance19;
	private String allowance20;
	private String allowance21;
	private String allowance22;
	private String allowance23;
	private String allowance24;
	private String allowance25;
	private String bonus_year;
	private String payroll_type;
	private String injury_per;
	private String childbirth_per;
	private String bonus_emp;
	private String bonus_comp_tax_rate;
	private String stock_months;
	private String stock_adjust_tax;
	private String wage_tax_deduct;
	private String bonus_tax_deduct;
	private String foreign_allowance;
	private String foreign_deduct_before_tax;
	private String foreign_housing_subsidy;
	private String foreign_meal_allowance;
	private String foreign_laundry_fee;
	private String foreign_moving_fee;
	private String foreign_travel_allowance;
	private String foreign_family_allowance;
	private String foreign_training_fee;
	private String foreign_education_expense;
	private String stock_card_sno;
	private String deduct_before_tax;
	private String employer_tax_rate;
	private String id;
	private String company_no;
	private String acc_ym;
	private String basic_salary;
	private String calc_field3;
	private String bonus_no_pay;
	private String logo_url;
	private String remarks;
	private String tax_salary;
	private String employee_code;
	private String bonus;
	private String bonus_tax;
	private String bonus_free_tax_deduct;
	private String bonus_real_pay;
	private String stock;
	private String stock_no_pay;
	private String real_pay;
	private String stock_tax;
	private String stock_free_tax_deduct;
	private String stock_real_pay;
	private String annuity_per;
	private String annuity_exceed;
	private String annuity_comp;

	/**
	 * @return the annuity_per
	 */
	public String getAnnuity_per() {
		return annuity_per;
	}

	/**
	 * @param annuity_per the annuity_per to set
	 */
	public void setAnnuity_per(String annuity_per) {
		this.annuity_per = annuity_per;
	}

	/**
	 * @return the annuity_exceed
	 */
	public String getAnnuity_exceed() {
		return annuity_exceed;
	}

	/**
	 * @param annuity_exceed the annuity_exceed to set
	 */
	public void setAnnuity_exceed(String annuity_exceed) {
		this.annuity_exceed = annuity_exceed;
	}

	/**
	 * @return the annuity_comp
	 */
	public String getAnnuity_comp() {
		return annuity_comp;
	}

	/**
	 * @param annuity_comp the annuity_comp to set
	 */
	public void setAnnuity_comp(String annuity_comp) {
		this.annuity_comp = annuity_comp;
	}

	/**
	 * @return the sno
	 */
	public String getSno() {
		return sno;
	}

	/**
	 * @param sno the sno to set
	 */
	public void setSno(String sno) {
		this.sno = sno;
	}

	/**
	 * @return the reg_no
	 */
	public String getReg_no() {
		return reg_no;
	}

	/**
	 * @param reg_no the reg_no to set
	 */
	public void setReg_no(String reg_no) {
		this.reg_no = reg_no;
	}

	/**
	 * @return the dept_no
	 */
	public String getDept_no() {
		return dept_no;
	}

	/**
	 * @param dept_no the dept_no to set
	 */
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}

	/**
	 * @return the batch_no
	 */
	public String getBatch_no() {
		return batch_no;
	}

	/**
	 * @param batch_no the batch_no to set
	 */
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	/**
	 * @return the emp_no
	 */
	public String getEmp_no() {
		return emp_no;
	}

	/**
	 * @param emp_no the emp_no to set
	 */
	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the depart_no
	 */
	public String getDepart_no() {
		return depart_no;
	}

	/**
	 * @param depart_no the depart_no to set
	 */
	public void setDepart_no(String depart_no) {
		this.depart_no = depart_no;
	}

	/**
	 * @return the position_code
	 */
	public String getPosition_code() {
		return position_code;
	}

	/**
	 * @param position_code the position_code to set
	 */
	public void setPosition_code(String position_code) {
		this.position_code = position_code;
	}

	/**
	 * @return the employee_type
	 */
	public String getEmployee_type() {
		return employee_type;
	}

	/**
	 * @param employee_type the employee_type to set
	 */
	public void setEmployee_type(String employee_type) {
		this.employee_type = employee_type;
	}

	/**
	 * @return the real_acc_ym
	 */
	public String getReal_acc_ym() {
		return real_acc_ym;
	}

	/**
	 * @param real_acc_ym the real_acc_ym to set
	 */
	public void setReal_acc_ym(String real_acc_ym) {
		this.real_acc_ym = real_acc_ym;
	}

	/**
	 * @return the unit_no
	 */
	public String getUnit_no() {
		return unit_no;
	}

	/**
	 * @param unit_no the unit_no to set
	 */
	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}

	/**
	 * @return the province_code
	 */
	public String getProvince_code() {
		return province_code;
	}

	/**
	 * @param province_code the province_code to set
	 */
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}

	/**
	 * @return the city_code
	 */
	public String getCity_code() {
		return city_code;
	}

	/**
	 * @param city_code the city_code to set
	 */
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	/**
	 * @return the bank_code
	 */
	public String getBank_code() {
		return bank_code;
	}

	/**
	 * @param bank_code the bank_code to set
	 */
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	/**
	 * @return the revenue_code
	 */
	public String getRevenue_code() {
		return revenue_code;
	}

	/**
	 * @param revenue_code the revenue_code to set
	 */
	public void setRevenue_code(String revenue_code) {
		this.revenue_code = revenue_code;
	}

	/**
	 * @return the currency_type
	 */
	public String getCurrency_type() {
		return currency_type;
	}

	/**
	 * @param currency_type the currency_type to set
	 */
	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}

	/**
	 * @return the pretax_salary
	 */
	public String getPretax_salary() {
		return pretax_salary;
	}

	/**
	 * @param pretax_salary the pretax_salary to set
	 */
	public void setPretax_salary(String pretax_salary) {
		this.pretax_salary = pretax_salary;
	}

	/**
	 * @return the month_bonus
	 */
	public String getMonth_bonus() {
		return month_bonus;
	}

	/**
	 * @param month_bonus the month_bonus to set
	 */
	public void setMonth_bonus(String month_bonus) {
		this.month_bonus = month_bonus;
	}

	/**
	 * @return the overtime_salary
	 */
	public String getOvertime_salary() {
		return overtime_salary;
	}

	/**
	 * @param overtime_salary the overtime_salary to set
	 */
	public void setOvertime_salary(String overtime_salary) {
		this.overtime_salary = overtime_salary;
	}

	/**
	 * @return the overtime_salary_deduct_tax
	 */
	public String getOvertime_salary_deduct_tax() {
		return overtime_salary_deduct_tax;
	}

	/**
	 * @param overtime_salary_deduct_tax the overtime_salary_deduct_tax to set
	 */
	public void setOvertime_salary_deduct_tax(String overtime_salary_deduct_tax) {
		this.overtime_salary_deduct_tax = overtime_salary_deduct_tax;
	}

	/**
	 * @return the traffic_allowance
	 */
	public String getTraffic_allowance() {
		return traffic_allowance;
	}

	/**
	 * @param traffic_allowance the traffic_allowance to set
	 */
	public void setTraffic_allowance(String traffic_allowance) {
		this.traffic_allowance = traffic_allowance;
	}

	/**
	 * @return the traffic_allowance_deduct_tax
	 */
	public String getTraffic_allowance_deduct_tax() {
		return traffic_allowance_deduct_tax;
	}

	/**
	 * @param traffic_allowance_deduct_tax the traffic_allowance_deduct_tax to set
	 */
	public void setTraffic_allowance_deduct_tax(String traffic_allowance_deduct_tax) {
		this.traffic_allowance_deduct_tax = traffic_allowance_deduct_tax;
	}

	/**
	 * @return the lunch_allowance
	 */
	public String getLunch_allowance() {
		return lunch_allowance;
	}

	/**
	 * @param lunch_allowance the lunch_allowance to set
	 */
	public void setLunch_allowance(String lunch_allowance) {
		this.lunch_allowance = lunch_allowance;
	}

	/**
	 * @return the lunch_allowance_deduct_tax
	 */
	public String getLunch_allowance_deduct_tax() {
		return lunch_allowance_deduct_tax;
	}

	/**
	 * @param lunch_allowance_deduct_tax the lunch_allowance_deduct_tax to set
	 */
	public void setLunch_allowance_deduct_tax(String lunch_allowance_deduct_tax) {
		this.lunch_allowance_deduct_tax = lunch_allowance_deduct_tax;
	}

	/**
	 * @return the oep_basic
	 */
	public String getOep_basic() {
		return oep_basic;
	}

	/**
	 * @param oep_basic the oep_basic to set
	 */
	public void setOep_basic(String oep_basic) {
		this.oep_basic = oep_basic;
	}

	/**
	 * @return the oep_per
	 */
	public String getOep_per() {
		return oep_per;
	}

	/**
	 * @param oep_per the oep_per to set
	 */
	public void setOep_per(String oep_per) {
		this.oep_per = oep_per;
	}

	/**
	 * @return the lose_job_insu
	 */
	public String getLose_job_insu() {
		return lose_job_insu;
	}

	/**
	 * @param lose_job_insu the lose_job_insu to set
	 */
	public void setLose_job_insu(String lose_job_insu) {
		this.lose_job_insu = lose_job_insu;
	}

	/**
	 * @return the medical_insu
	 */
	public String getMedical_insu() {
		return medical_insu;
	}

	/**
	 * @param medical_insu the medical_insu to set
	 */
	public void setMedical_insu(String medical_insu) {
		this.medical_insu = medical_insu;
	}

	/**
	 * @return the accfund_insu
	 */
	public String getAccfund_insu() {
		return accfund_insu;
	}

	/**
	 * @param accfund_insu the accfund_insu to set
	 */
	public void setAccfund_insu(String accfund_insu) {
		this.accfund_insu = accfund_insu;
	}

	/**
	 * @return the append_accfund
	 */
	public String getAppend_accfund() {
		return append_accfund;
	}

	/**
	 * @param append_accfund the append_accfund to set
	 */
	public void setAppend_accfund(String append_accfund) {
		this.append_accfund = append_accfund;
	}

	/**
	 * @return the person_pay_total
	 */
	public String getPerson_pay_total() {
		return person_pay_total;
	}

	/**
	 * @param person_pay_total the person_pay_total to set
	 */
	public void setPerson_pay_total(String person_pay_total) {
		this.person_pay_total = person_pay_total;
	}

	/**
	 * @return the allowance1
	 */
	public String getAllowance1() {
		return allowance1;
	}

	/**
	 * @param allowance1 the allowance1 to set
	 */
	public void setAllowance1(String allowance1) {
		this.allowance1 = allowance1;
	}

	/**
	 * @return the allowance2
	 */
	public String getAllowance2() {
		return allowance2;
	}

	/**
	 * @param allowance2 the allowance2 to set
	 */
	public void setAllowance2(String allowance2) {
		this.allowance2 = allowance2;
	}

	/**
	 * @return the allowance3
	 */
	public String getAllowance3() {
		return allowance3;
	}

	/**
	 * @param allowance3 the allowance3 to set
	 */
	public void setAllowance3(String allowance3) {
		this.allowance3 = allowance3;
	}

	/**
	 * @return the allowance4
	 */
	public String getAllowance4() {
		return allowance4;
	}

	/**
	 * @param allowance4 the allowance4 to set
	 */
	public void setAllowance4(String allowance4) {
		this.allowance4 = allowance4;
	}

	/**
	 * @return the allowance5
	 */
	public String getAllowance5() {
		return allowance5;
	}

	/**
	 * @param allowance5 the allowance5 to set
	 */
	public void setAllowance5(String allowance5) {
		this.allowance5 = allowance5;
	}

	/**
	 * @return the free_tax_allowance1
	 */
	public String getFree_tax_allowance1() {
		return free_tax_allowance1;
	}

	/**
	 * @param free_tax_allowance1 the free_tax_allowance1 to set
	 */
	public void setFree_tax_allowance1(String free_tax_allowance1) {
		this.free_tax_allowance1 = free_tax_allowance1;
	}

	/**
	 * @return the free_tax_allowance2
	 */
	public String getFree_tax_allowance2() {
		return free_tax_allowance2;
	}

	/**
	 * @param free_tax_allowance2 the free_tax_allowance2 to set
	 */
	public void setFree_tax_allowance2(String free_tax_allowance2) {
		this.free_tax_allowance2 = free_tax_allowance2;
	}

	/**
	 * @return the free_tax_allowance3
	 */
	public String getFree_tax_allowance3() {
		return free_tax_allowance3;
	}

	/**
	 * @param free_tax_allowance3 the free_tax_allowance3 to set
	 */
	public void setFree_tax_allowance3(String free_tax_allowance3) {
		this.free_tax_allowance3 = free_tax_allowance3;
	}

	/**
	 * @return the free_tax_allowance4
	 */
	public String getFree_tax_allowance4() {
		return free_tax_allowance4;
	}

	/**
	 * @param free_tax_allowance4 the free_tax_allowance4 to set
	 */
	public void setFree_tax_allowance4(String free_tax_allowance4) {
		this.free_tax_allowance4 = free_tax_allowance4;
	}

	/**
	 * @return the free_tax_allowance5
	 */
	public String getFree_tax_allowance5() {
		return free_tax_allowance5;
	}

	/**
	 * @param free_tax_allowance5 the free_tax_allowance5 to set
	 */
	public void setFree_tax_allowance5(String free_tax_allowance5) {
		this.free_tax_allowance5 = free_tax_allowance5;
	}

	/**
	 * @return the deduct1
	 */
	public String getDeduct1() {
		return deduct1;
	}

	/**
	 * @param deduct1 the deduct1 to set
	 */
	public void setDeduct1(String deduct1) {
		this.deduct1 = deduct1;
	}

	/**
	 * @return the deduct2
	 */
	public String getDeduct2() {
		return deduct2;
	}

	/**
	 * @param deduct2 the deduct2 to set
	 */
	public void setDeduct2(String deduct2) {
		this.deduct2 = deduct2;
	}

	/**
	 * @return the deduct3
	 */
	public String getDeduct3() {
		return deduct3;
	}

	/**
	 * @param deduct3 the deduct3 to set
	 */
	public void setDeduct3(String deduct3) {
		this.deduct3 = deduct3;
	}

	/**
	 * @return the deduct4
	 */
	public String getDeduct4() {
		return deduct4;
	}

	/**
	 * @param deduct4 the deduct4 to set
	 */
	public void setDeduct4(String deduct4) {
		this.deduct4 = deduct4;
	}

	/**
	 * @return the deduct5
	 */
	public String getDeduct5() {
		return deduct5;
	}

	/**
	 * @param deduct5 the deduct5 to set
	 */
	public void setDeduct5(String deduct5) {
		this.deduct5 = deduct5;
	}

	/**
	 * @return the free_tax_deduct1
	 */
	public String getFree_tax_deduct1() {
		return free_tax_deduct1;
	}

	/**
	 * @param free_tax_deduct1 the free_tax_deduct1 to set
	 */
	public void setFree_tax_deduct1(String free_tax_deduct1) {
		this.free_tax_deduct1 = free_tax_deduct1;
	}

	/**
	 * @return the free_tax_deduct2
	 */
	public String getFree_tax_deduct2() {
		return free_tax_deduct2;
	}

	/**
	 * @param free_tax_deduct2 the free_tax_deduct2 to set
	 */
	public void setFree_tax_deduct2(String free_tax_deduct2) {
		this.free_tax_deduct2 = free_tax_deduct2;
	}

	/**
	 * @return the free_tax_deduct3
	 */
	public String getFree_tax_deduct3() {
		return free_tax_deduct3;
	}

	/**
	 * @param free_tax_deduct3 the free_tax_deduct3 to set
	 */
	public void setFree_tax_deduct3(String free_tax_deduct3) {
		this.free_tax_deduct3 = free_tax_deduct3;
	}

	/**
	 * @return the free_tax_deduct4
	 */
	public String getFree_tax_deduct4() {
		return free_tax_deduct4;
	}

	/**
	 * @param free_tax_deduct4 the free_tax_deduct4 to set
	 */
	public void setFree_tax_deduct4(String free_tax_deduct4) {
		this.free_tax_deduct4 = free_tax_deduct4;
	}

	/**
	 * @return the free_tax_deduct5
	 */
	public String getFree_tax_deduct5() {
		return free_tax_deduct5;
	}

	/**
	 * @param free_tax_deduct5 the free_tax_deduct5 to set
	 */
	public void setFree_tax_deduct5(String free_tax_deduct5) {
		this.free_tax_deduct5 = free_tax_deduct5;
	}

	/**
	 * @return the calc_field1
	 */
	public String getCalc_field1() {
		return calc_field1;
	}

	/**
	 * @param calc_field1 the calc_field1 to set
	 */
	public void setCalc_field1(String calc_field1) {
		this.calc_field1 = calc_field1;
	}

	/**
	 * @return the calc_field2
	 */
	public String getCalc_field2() {
		return calc_field2;
	}

	/**
	 * @param calc_field2 the calc_field2 to set
	 */
	public void setCalc_field2(String calc_field2) {
		this.calc_field2 = calc_field2;
	}

	/**
	 * @return the display1
	 */
	public String getDisplay1() {
		return display1;
	}

	/**
	 * @param display1 the display1 to set
	 */
	public void setDisplay1(String display1) {
		this.display1 = display1;
	}

	/**
	 * @return the display2
	 */
	public String getDisplay2() {
		return display2;
	}

	/**
	 * @param display2 the display2 to set
	 */
	public void setDisplay2(String display2) {
		this.display2 = display2;
	}

	/**
	 * @return the display3
	 */
	public String getDisplay3() {
		return display3;
	}

	/**
	 * @param display3 the display3 to set
	 */
	public void setDisplay3(String display3) {
		this.display3 = display3;
	}

	/**
	 * @return the outer_insu
	 */
	public String getOuter_insu() {
		return outer_insu;
	}

	/**
	 * @param outer_insu the outer_insu to set
	 */
	public void setOuter_insu(String outer_insu) {
		this.outer_insu = outer_insu;
	}

	/**
	 * @return the should_pay_tax
	 */
	public String getShould_pay_tax() {
		return should_pay_tax;
	}

	/**
	 * @param should_pay_tax the should_pay_tax to set
	 */
	public void setShould_pay_tax(String should_pay_tax) {
		this.should_pay_tax = should_pay_tax;
	}

	/**
	 * @return the tax
	 */
	public String getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(String tax) {
		this.tax = tax;
	}

	/**
	 * @return the adjust_tax
	 */
	public String getAdjust_tax() {
		return adjust_tax;
	}

	/**
	 * @param adjust_tax the adjust_tax to set
	 */
	public void setAdjust_tax(String adjust_tax) {
		this.adjust_tax = adjust_tax;
	}

	/**
	 * @return the salary_card_sno
	 */
	public String getSalary_card_sno() {
		return salary_card_sno;
	}

	/**
	 * @param salary_card_sno the salary_card_sno to set
	 */
	public void setSalary_card_sno(String salary_card_sno) {
		this.salary_card_sno = salary_card_sno;
	}

	/**
	 * @return the real_tax_acc_ym
	 */
	public String getReal_tax_acc_ym() {
		return real_tax_acc_ym;
	}

	/**
	 * @param real_tax_acc_ym the real_tax_acc_ym to set
	 */
	public void setReal_tax_acc_ym(String real_tax_acc_ym) {
		this.real_tax_acc_ym = real_tax_acc_ym;
	}

	/**
	 * @return the salary_payrevenu_sno
	 */
	public String getSalary_payrevenu_sno() {
		return salary_payrevenu_sno;
	}

	/**
	 * @param salary_payrevenu_sno the salary_payrevenu_sno to set
	 */
	public void setSalary_payrevenu_sno(String salary_payrevenu_sno) {
		this.salary_payrevenu_sno = salary_payrevenu_sno;
	}

	/**
	 * @return the bonus_type
	 */
	public String getBonus_type() {
		return bonus_type;
	}

	/**
	 * @param bonus_type the bonus_type to set
	 */
	public void setBonus_type(String bonus_type) {
		this.bonus_type = bonus_type;
	}

	/**
	 * @return the bonus_adjust_tax
	 */
	public String getBonus_adjust_tax() {
		return bonus_adjust_tax;
	}

	/**
	 * @param bonus_adjust_tax the bonus_adjust_tax to set
	 */
	public void setBonus_adjust_tax(String bonus_adjust_tax) {
		this.bonus_adjust_tax = bonus_adjust_tax;
	}

	/**
	 * @return the bonus_tax_adjust_reason
	 */
	public String getBonus_tax_adjust_reason() {
		return bonus_tax_adjust_reason;
	}

	/**
	 * @param bonus_tax_adjust_reason the bonus_tax_adjust_reason to set
	 */
	public void setBonus_tax_adjust_reason(String bonus_tax_adjust_reason) {
		this.bonus_tax_adjust_reason = bonus_tax_adjust_reason;
	}

	/**
	 * @return the bonus_card_sno
	 */
	public String getBonus_card_sno() {
		return bonus_card_sno;
	}

	/**
	 * @param bonus_card_sno the bonus_card_sno to set
	 */
	public void setBonus_card_sno(String bonus_card_sno) {
		this.bonus_card_sno = bonus_card_sno;
	}

	/**
	 * @return the bonus_read_tax_acc_ym
	 */
	public String getBonus_read_tax_acc_ym() {
		return bonus_read_tax_acc_ym;
	}

	/**
	 * @param bonus_read_tax_acc_ym the bonus_read_tax_acc_ym to set
	 */
	public void setBonus_read_tax_acc_ym(String bonus_read_tax_acc_ym) {
		this.bonus_read_tax_acc_ym = bonus_read_tax_acc_ym;
	}

	/**
	 * @return the bonus_payrevenu_sno
	 */
	public String getBonus_payrevenu_sno() {
		return bonus_payrevenu_sno;
	}

	/**
	 * @param bonus_payrevenu_sno the bonus_payrevenu_sno to set
	 */
	public void setBonus_payrevenu_sno(String bonus_payrevenu_sno) {
		this.bonus_payrevenu_sno = bonus_payrevenu_sno;
	}

	/**
	 * @return the compute_flag
	 */
	public String getCompute_flag() {
		return compute_flag;
	}

	/**
	 * @param compute_flag the compute_flag to set
	 */
	public void setCompute_flag(String compute_flag) {
		this.compute_flag = compute_flag;
	}

	/**
	 * @return the input_person
	 */
	public String getInput_person() {
		return input_person;
	}

	/**
	 * @param input_person the input_person to set
	 */
	public void setInput_person(String input_person) {
		this.input_person = input_person;
	}

	/**
	 * @return the input_date
	 */
	public String getInput_date() {
		return input_date;
	}

	/**
	 * @param input_date the input_date to set
	 */
	public void setInput_date(String input_date) {
		this.input_date = input_date;
	}

	/**
	 * @return the wf_rcvsalary
	 */
	public String getWf_rcvsalary() {
		return wf_rcvsalary;
	}

	/**
	 * @param wf_rcvsalary the wf_rcvsalary to set
	 */
	public void setWf_rcvsalary(String wf_rcvsalary) {
		this.wf_rcvsalary = wf_rcvsalary;
	}

	/**
	 * @return the oep_c
	 */
	public String getOep_c() {
		return oep_c;
	}

	/**
	 * @param oep_c the oep_c to set
	 */
	public void setOep_c(String oep_c) {
		this.oep_c = oep_c;
	}

	/**
	 * @return the medical_c
	 */
	public String getMedical_c() {
		return medical_c;
	}

	/**
	 * @param medical_c the medical_c to set
	 */
	public void setMedical_c(String medical_c) {
		this.medical_c = medical_c;
	}

	/**
	 * @return the lose_job_c
	 */
	public String getLose_job_c() {
		return lose_job_c;
	}

	/**
	 * @param lose_job_c the lose_job_c to set
	 */
	public void setLose_job_c(String lose_job_c) {
		this.lose_job_c = lose_job_c;
	}

	/**
	 * @return the freercv_allowance1
	 */
	public String getFreercv_allowance1() {
		return freercv_allowance1;
	}

	/**
	 * @param freercv_allowance1 the freercv_allowance1 to set
	 */
	public void setFreercv_allowance1(String freercv_allowance1) {
		this.freercv_allowance1 = freercv_allowance1;
	}

	/**
	 * @return the freercv_allowance2
	 */
	public String getFreercv_allowance2() {
		return freercv_allowance2;
	}

	/**
	 * @param freercv_allowance2 the freercv_allowance2 to set
	 */
	public void setFreercv_allowance2(String freercv_allowance2) {
		this.freercv_allowance2 = freercv_allowance2;
	}

	/**
	 * @return the freercv_allowance3
	 */
	public String getFreercv_allowance3() {
		return freercv_allowance3;
	}

	/**
	 * @param freercv_allowance3 the freercv_allowance3 to set
	 */
	public void setFreercv_allowance3(String freercv_allowance3) {
		this.freercv_allowance3 = freercv_allowance3;
	}

	/**
	 * @return the allowance6
	 */
	public String getAllowance6() {
		return allowance6;
	}

	/**
	 * @param allowance6 the allowance6 to set
	 */
	public void setAllowance6(String allowance6) {
		this.allowance6 = allowance6;
	}

	/**
	 * @return the allowance7
	 */
	public String getAllowance7() {
		return allowance7;
	}

	/**
	 * @param allowance7 the allowance7 to set
	 */
	public void setAllowance7(String allowance7) {
		this.allowance7 = allowance7;
	}

	/**
	 * @return the allowance8
	 */
	public String getAllowance8() {
		return allowance8;
	}

	/**
	 * @param allowance8 the allowance8 to set
	 */
	public void setAllowance8(String allowance8) {
		this.allowance8 = allowance8;
	}

	/**
	 * @return the allowance9
	 */
	public String getAllowance9() {
		return allowance9;
	}

	/**
	 * @param allowance9 the allowance9 to set
	 */
	public void setAllowance9(String allowance9) {
		this.allowance9 = allowance9;
	}

	/**
	 * @return the allowance10
	 */
	public String getAllowance10() {
		return allowance10;
	}

	/**
	 * @param allowance10 the allowance10 to set
	 */
	public void setAllowance10(String allowance10) {
		this.allowance10 = allowance10;
	}

	/**
	 * @return the free_tax_allowance6
	 */
	public String getFree_tax_allowance6() {
		return free_tax_allowance6;
	}

	/**
	 * @param free_tax_allowance6 the free_tax_allowance6 to set
	 */
	public void setFree_tax_allowance6(String free_tax_allowance6) {
		this.free_tax_allowance6 = free_tax_allowance6;
	}

	/**
	 * @return the free_tax_allowance7
	 */
	public String getFree_tax_allowance7() {
		return free_tax_allowance7;
	}

	/**
	 * @param free_tax_allowance7 the free_tax_allowance7 to set
	 */
	public void setFree_tax_allowance7(String free_tax_allowance7) {
		this.free_tax_allowance7 = free_tax_allowance7;
	}

	/**
	 * @return the free_tax_allowance8
	 */
	public String getFree_tax_allowance8() {
		return free_tax_allowance8;
	}

	/**
	 * @param free_tax_allowance8 the free_tax_allowance8 to set
	 */
	public void setFree_tax_allowance8(String free_tax_allowance8) {
		this.free_tax_allowance8 = free_tax_allowance8;
	}

	/**
	 * @return the free_tax_allowance9
	 */
	public String getFree_tax_allowance9() {
		return free_tax_allowance9;
	}

	/**
	 * @param free_tax_allowance9 the free_tax_allowance9 to set
	 */
	public void setFree_tax_allowance9(String free_tax_allowance9) {
		this.free_tax_allowance9 = free_tax_allowance9;
	}

	/**
	 * @return the free_tax_allowance10
	 */
	public String getFree_tax_allowance10() {
		return free_tax_allowance10;
	}

	/**
	 * @param free_tax_allowance10 the free_tax_allowance10 to set
	 */
	public void setFree_tax_allowance10(String free_tax_allowance10) {
		this.free_tax_allowance10 = free_tax_allowance10;
	}

	/**
	 * @return the accfund_base
	 */
	public String getAccfund_base() {
		return accfund_base;
	}

	/**
	 * @param accfund_base the accfund_base to set
	 */
	public void setAccfund_base(String accfund_base) {
		this.accfund_base = accfund_base;
	}

	/**
	 * @return the fund_total
	 */
	public String getFund_total() {
		return fund_total;
	}

	/**
	 * @param fund_total the fund_total to set
	 */
	public void setFund_total(String fund_total) {
		this.fund_total = fund_total;
	}

	/**
	 * @return the work_place
	 */
	public String getWork_place() {
		return work_place;
	}

	/**
	 * @param work_place the work_place to set
	 */
	public void setWork_place(String work_place) {
		this.work_place = work_place;
	}

	/**
	 * @return the display4
	 */
	public String getDisplay4() {
		return display4;
	}

	/**
	 * @param display4 the display4 to set
	 */
	public void setDisplay4(String display4) {
		this.display4 = display4;
	}

	/**
	 * @return the display5
	 */
	public String getDisplay5() {
		return display5;
	}

	/**
	 * @param display5 the display5 to set
	 */
	public void setDisplay5(String display5) {
		this.display5 = display5;
	}

	/**
	 * @return the display6
	 */
	public String getDisplay6() {
		return display6;
	}

	/**
	 * @param display6 the display6 to set
	 */
	public void setDisplay6(String display6) {
		this.display6 = display6;
	}

	/**
	 * @return the display7
	 */
	public String getDisplay7() {
		return display7;
	}

	/**
	 * @param display7 the display7 to set
	 */
	public void setDisplay7(String display7) {
		this.display7 = display7;
	}

	/**
	 * @return the display8
	 */
	public String getDisplay8() {
		return display8;
	}

	/**
	 * @param display8 the display8 to set
	 */
	public void setDisplay8(String display8) {
		this.display8 = display8;
	}

	/**
	 * @return the display9
	 */
	public String getDisplay9() {
		return display9;
	}

	/**
	 * @param display9 the display9 to set
	 */
	public void setDisplay9(String display9) {
		this.display9 = display9;
	}

	/**
	 * @return the display10
	 */
	public String getDisplay10() {
		return display10;
	}

	/**
	 * @param display10 the display10 to set
	 */
	public void setDisplay10(String display10) {
		this.display10 = display10;
	}

	/**
	 * @return the display11
	 */
	public String getDisplay11() {
		return display11;
	}

	/**
	 * @param display11 the display11 to set
	 */
	public void setDisplay11(String display11) {
		this.display11 = display11;
	}

	/**
	 * @return the display12
	 */
	public String getDisplay12() {
		return display12;
	}

	/**
	 * @param display12 the display12 to set
	 */
	public void setDisplay12(String display12) {
		this.display12 = display12;
	}

	/**
	 * @return the display13
	 */
	public String getDisplay13() {
		return display13;
	}

	/**
	 * @param display13 the display13 to set
	 */
	public void setDisplay13(String display13) {
		this.display13 = display13;
	}

	/**
	 * @return the display14
	 */
	public String getDisplay14() {
		return display14;
	}

	/**
	 * @param display14 the display14 to set
	 */
	public void setDisplay14(String display14) {
		this.display14 = display14;
	}

	/**
	 * @return the display15
	 */
	public String getDisplay15() {
		return display15;
	}

	/**
	 * @param display15 the display15 to set
	 */
	public void setDisplay15(String display15) {
		this.display15 = display15;
	}

	/**
	 * @return the injury_insu
	 */
	public String getInjury_insu() {
		return injury_insu;
	}

	/**
	 * @param injury_insu the injury_insu to set
	 */
	public void setInjury_insu(String injury_insu) {
		this.injury_insu = injury_insu;
	}

	/**
	 * @return the childbirth_insu
	 */
	public String getChildbirth_insu() {
		return childbirth_insu;
	}

	/**
	 * @param childbirth_insu the childbirth_insu to set
	 */
	public void setChildbirth_insu(String childbirth_insu) {
		this.childbirth_insu = childbirth_insu;
	}

	/**
	 * @return the supply_social_insu
	 */
	public String getSupply_social_insu() {
		return supply_social_insu;
	}

	/**
	 * @param supply_social_insu the supply_social_insu to set
	 */
	public void setSupply_social_insu(String supply_social_insu) {
		this.supply_social_insu = supply_social_insu;
	}

	/**
	 * @return the free_tax_deduct6
	 */
	public String getFree_tax_deduct6() {
		return free_tax_deduct6;
	}

	/**
	 * @param free_tax_deduct6 the free_tax_deduct6 to set
	 */
	public void setFree_tax_deduct6(String free_tax_deduct6) {
		this.free_tax_deduct6 = free_tax_deduct6;
	}

	/**
	 * @return the free_tax_deduct7
	 */
	public String getFree_tax_deduct7() {
		return free_tax_deduct7;
	}

	/**
	 * @param free_tax_deduct7 the free_tax_deduct7 to set
	 */
	public void setFree_tax_deduct7(String free_tax_deduct7) {
		this.free_tax_deduct7 = free_tax_deduct7;
	}

	/**
	 * @return the free_tax_deduct8
	 */
	public String getFree_tax_deduct8() {
		return free_tax_deduct8;
	}

	/**
	 * @param free_tax_deduct8 the free_tax_deduct8 to set
	 */
	public void setFree_tax_deduct8(String free_tax_deduct8) {
		this.free_tax_deduct8 = free_tax_deduct8;
	}

	/**
	 * @return the free_tax_deduct9
	 */
	public String getFree_tax_deduct9() {
		return free_tax_deduct9;
	}

	/**
	 * @param free_tax_deduct9 the free_tax_deduct9 to set
	 */
	public void setFree_tax_deduct9(String free_tax_deduct9) {
		this.free_tax_deduct9 = free_tax_deduct9;
	}

	/**
	 * @return the free_tax_deduct10
	 */
	public String getFree_tax_deduct10() {
		return free_tax_deduct10;
	}

	/**
	 * @param free_tax_deduct10 the free_tax_deduct10 to set
	 */
	public void setFree_tax_deduct10(String free_tax_deduct10) {
		this.free_tax_deduct10 = free_tax_deduct10;
	}

	/**
	 * @return the accfund_c
	 */
	public String getAccfund_c() {
		return accfund_c;
	}

	/**
	 * @param accfund_c the accfund_c to set
	 */
	public void setAccfund_c(String accfund_c) {
		this.accfund_c = accfund_c;
	}

	/**
	 * @return the salary_sno
	 */
	public String getSalary_sno() {
		return salary_sno;
	}

	/**
	 * @param salary_sno the salary_sno to set
	 */
	public void setSalary_sno(String salary_sno) {
		this.salary_sno = salary_sno;
	}

	/**
	 * @return the deduct6
	 */
	public String getDeduct6() {
		return deduct6;
	}

	/**
	 * @param deduct6 the deduct6 to set
	 */
	public void setDeduct6(String deduct6) {
		this.deduct6 = deduct6;
	}

	/**
	 * @return the deduct7
	 */
	public String getDeduct7() {
		return deduct7;
	}

	/**
	 * @param deduct7 the deduct7 to set
	 */
	public void setDeduct7(String deduct7) {
		this.deduct7 = deduct7;
	}

	/**
	 * @return the deduct8
	 */
	public String getDeduct8() {
		return deduct8;
	}

	/**
	 * @param deduct8 the deduct8 to set
	 */
	public void setDeduct8(String deduct8) {
		this.deduct8 = deduct8;
	}

	/**
	 * @return the deduct9
	 */
	public String getDeduct9() {
		return deduct9;
	}

	/**
	 * @param deduct9 the deduct9 to set
	 */
	public void setDeduct9(String deduct9) {
		this.deduct9 = deduct9;
	}

	/**
	 * @return the deduct10
	 */
	public String getDeduct10() {
		return deduct10;
	}

	/**
	 * @param deduct10 the deduct10 to set
	 */
	public void setDeduct10(String deduct10) {
		this.deduct10 = deduct10;
	}

	/**
	 * @return the allowance_no_pay3
	 */
	public String getAllowance_no_pay3() {
		return allowance_no_pay3;
	}

	/**
	 * @param allowance_no_pay3 the allowance_no_pay3 to set
	 */
	public void setAllowance_no_pay3(String allowance_no_pay3) {
		this.allowance_no_pay3 = allowance_no_pay3;
	}

	/**
	 * @return the allowance_no_pay4
	 */
	public String getAllowance_no_pay4() {
		return allowance_no_pay4;
	}

	/**
	 * @param allowance_no_pay4 the allowance_no_pay4 to set
	 */
	public void setAllowance_no_pay4(String allowance_no_pay4) {
		this.allowance_no_pay4 = allowance_no_pay4;
	}

	/**
	 * @return the allowance_no_pay5
	 */
	public String getAllowance_no_pay5() {
		return allowance_no_pay5;
	}

	/**
	 * @param allowance_no_pay5 the allowance_no_pay5 to set
	 */
	public void setAllowance_no_pay5(String allowance_no_pay5) {
		this.allowance_no_pay5 = allowance_no_pay5;
	}

	/**
	 * @return the allowance11
	 */
	public String getAllowance11() {
		return allowance11;
	}

	/**
	 * @param allowance11 the allowance11 to set
	 */
	public void setAllowance11(String allowance11) {
		this.allowance11 = allowance11;
	}

	/**
	 * @return the allowance12
	 */
	public String getAllowance12() {
		return allowance12;
	}

	/**
	 * @param allowance12 the allowance12 to set
	 */
	public void setAllowance12(String allowance12) {
		this.allowance12 = allowance12;
	}

	/**
	 * @return the allowance13
	 */
	public String getAllowance13() {
		return allowance13;
	}

	/**
	 * @param allowance13 the allowance13 to set
	 */
	public void setAllowance13(String allowance13) {
		this.allowance13 = allowance13;
	}

	/**
	 * @return the allowance14
	 */
	public String getAllowance14() {
		return allowance14;
	}

	/**
	 * @param allowance14 the allowance14 to set
	 */
	public void setAllowance14(String allowance14) {
		this.allowance14 = allowance14;
	}

	/**
	 * @return the allowance15
	 */
	public String getAllowance15() {
		return allowance15;
	}

	/**
	 * @param allowance15 the allowance15 to set
	 */
	public void setAllowance15(String allowance15) {
		this.allowance15 = allowance15;
	}

	/**
	 * @return the allowance16
	 */
	public String getAllowance16() {
		return allowance16;
	}

	/**
	 * @param allowance16 the allowance16 to set
	 */
	public void setAllowance16(String allowance16) {
		this.allowance16 = allowance16;
	}

	/**
	 * @return the allowance17
	 */
	public String getAllowance17() {
		return allowance17;
	}

	/**
	 * @param allowance17 the allowance17 to set
	 */
	public void setAllowance17(String allowance17) {
		this.allowance17 = allowance17;
	}

	/**
	 * @return the allowance18
	 */
	public String getAllowance18() {
		return allowance18;
	}

	/**
	 * @param allowance18 the allowance18 to set
	 */
	public void setAllowance18(String allowance18) {
		this.allowance18 = allowance18;
	}

	/**
	 * @return the allowance19
	 */
	public String getAllowance19() {
		return allowance19;
	}

	/**
	 * @param allowance19 the allowance19 to set
	 */
	public void setAllowance19(String allowance19) {
		this.allowance19 = allowance19;
	}

	/**
	 * @return the allowance20
	 */
	public String getAllowance20() {
		return allowance20;
	}

	/**
	 * @param allowance20 the allowance20 to set
	 */
	public void setAllowance20(String allowance20) {
		this.allowance20 = allowance20;
	}

	/**
	 * @return the allowance21
	 */
	public String getAllowance21() {
		return allowance21;
	}

	/**
	 * @param allowance21 the allowance21 to set
	 */
	public void setAllowance21(String allowance21) {
		this.allowance21 = allowance21;
	}

	/**
	 * @return the allowance22
	 */
	public String getAllowance22() {
		return allowance22;
	}

	/**
	 * @param allowance22 the allowance22 to set
	 */
	public void setAllowance22(String allowance22) {
		this.allowance22 = allowance22;
	}

	/**
	 * @return the allowance23
	 */
	public String getAllowance23() {
		return allowance23;
	}

	/**
	 * @param allowance23 the allowance23 to set
	 */
	public void setAllowance23(String allowance23) {
		this.allowance23 = allowance23;
	}

	/**
	 * @return the allowance24
	 */
	public String getAllowance24() {
		return allowance24;
	}

	/**
	 * @param allowance24 the allowance24 to set
	 */
	public void setAllowance24(String allowance24) {
		this.allowance24 = allowance24;
	}

	/**
	 * @return the allowance25
	 */
	public String getAllowance25() {
		return allowance25;
	}

	/**
	 * @param allowance25 the allowance25 to set
	 */
	public void setAllowance25(String allowance25) {
		this.allowance25 = allowance25;
	}

	/**
	 * @return the bonus_year
	 */
	public String getBonus_year() {
		return bonus_year;
	}

	/**
	 * @param bonus_year the bonus_year to set
	 */
	public void setBonus_year(String bonus_year) {
		this.bonus_year = bonus_year;
	}

	/**
	 * @return the payroll_type
	 */
	public String getPayroll_type() {
		return payroll_type;
	}

	/**
	 * @param payroll_type the payroll_type to set
	 */
	public void setPayroll_type(String payroll_type) {
		this.payroll_type = payroll_type;
	}

	/**
	 * @return the injury_per
	 */
	public String getInjury_per() {
		return injury_per;
	}

	/**
	 * @param injury_per the injury_per to set
	 */
	public void setInjury_per(String injury_per) {
		this.injury_per = injury_per;
	}

	/**
	 * @return the childbirth_per
	 */
	public String getChildbirth_per() {
		return childbirth_per;
	}

	/**
	 * @param childbirth_per the childbirth_per to set
	 */
	public void setChildbirth_per(String childbirth_per) {
		this.childbirth_per = childbirth_per;
	}

	/**
	 * @return the bonus_emp
	 */
	public String getBonus_emp() {
		return bonus_emp;
	}

	/**
	 * @param bonus_emp the bonus_emp to set
	 */
	public void setBonus_emp(String bonus_emp) {
		this.bonus_emp = bonus_emp;
	}

	/**
	 * @return the bonus_comp_tax_rate
	 */
	public String getBonus_comp_tax_rate() {
		return bonus_comp_tax_rate;
	}

	/**
	 * @param bonus_comp_tax_rate the bonus_comp_tax_rate to set
	 */
	public void setBonus_comp_tax_rate(String bonus_comp_tax_rate) {
		this.bonus_comp_tax_rate = bonus_comp_tax_rate;
	}

	/**
	 * @return the stock_months
	 */
	public String getStock_months() {
		return stock_months;
	}

	/**
	 * @param stock_months the stock_months to set
	 */
	public void setStock_months(String stock_months) {
		this.stock_months = stock_months;
	}

	/**
	 * @return the stock_adjust_tax
	 */
	public String getStock_adjust_tax() {
		return stock_adjust_tax;
	}

	/**
	 * @param stock_adjust_tax the stock_adjust_tax to set
	 */
	public void setStock_adjust_tax(String stock_adjust_tax) {
		this.stock_adjust_tax = stock_adjust_tax;
	}

	/**
	 * @return the wage_tax_deduct
	 */
	public String getWage_tax_deduct() {
		return wage_tax_deduct;
	}

	/**
	 * @param wage_tax_deduct the wage_tax_deduct to set
	 */
	public void setWage_tax_deduct(String wage_tax_deduct) {
		this.wage_tax_deduct = wage_tax_deduct;
	}

	/**
	 * @return the bonus_tax_deduct
	 */
	public String getBonus_tax_deduct() {
		return bonus_tax_deduct;
	}

	/**
	 * @param bonus_tax_deduct the bonus_tax_deduct to set
	 */
	public void setBonus_tax_deduct(String bonus_tax_deduct) {
		this.bonus_tax_deduct = bonus_tax_deduct;
	}

	/**
	 * @return the foreign_allowance
	 */
	public String getForeign_allowance() {
		return foreign_allowance;
	}

	/**
	 * @param foreign_allowance the foreign_allowance to set
	 */
	public void setForeign_allowance(String foreign_allowance) {
		this.foreign_allowance = foreign_allowance;
	}

	/**
	 * @return the foreign_deduct_before_tax
	 */
	public String getForeign_deduct_before_tax() {
		return foreign_deduct_before_tax;
	}

	/**
	 * @param foreign_deduct_before_tax the foreign_deduct_before_tax to set
	 */
	public void setForeign_deduct_before_tax(String foreign_deduct_before_tax) {
		this.foreign_deduct_before_tax = foreign_deduct_before_tax;
	}

	/**
	 * @return the foreign_housing_subsidy
	 */
	public String getForeign_housing_subsidy() {
		return foreign_housing_subsidy;
	}

	/**
	 * @param foreign_housing_subsidy the foreign_housing_subsidy to set
	 */
	public void setForeign_housing_subsidy(String foreign_housing_subsidy) {
		this.foreign_housing_subsidy = foreign_housing_subsidy;
	}

	/**
	 * @return the foreign_meal_allowance
	 */
	public String getForeign_meal_allowance() {
		return foreign_meal_allowance;
	}

	/**
	 * @param foreign_meal_allowance the foreign_meal_allowance to set
	 */
	public void setForeign_meal_allowance(String foreign_meal_allowance) {
		this.foreign_meal_allowance = foreign_meal_allowance;
	}

	/**
	 * @return the foreign_laundry_fee
	 */
	public String getForeign_laundry_fee() {
		return foreign_laundry_fee;
	}

	/**
	 * @param foreign_laundry_fee the foreign_laundry_fee to set
	 */
	public void setForeign_laundry_fee(String foreign_laundry_fee) {
		this.foreign_laundry_fee = foreign_laundry_fee;
	}

	/**
	 * @return the foreign_moving_fee
	 */
	public String getForeign_moving_fee() {
		return foreign_moving_fee;
	}

	/**
	 * @param foreign_moving_fee the foreign_moving_fee to set
	 */
	public void setForeign_moving_fee(String foreign_moving_fee) {
		this.foreign_moving_fee = foreign_moving_fee;
	}

	/**
	 * @return the foreign_travel_allowance
	 */
	public String getForeign_travel_allowance() {
		return foreign_travel_allowance;
	}

	/**
	 * @param foreign_travel_allowance the foreign_travel_allowance to set
	 */
	public void setForeign_travel_allowance(String foreign_travel_allowance) {
		this.foreign_travel_allowance = foreign_travel_allowance;
	}

	/**
	 * @return the foreign_family_allowance
	 */
	public String getForeign_family_allowance() {
		return foreign_family_allowance;
	}

	/**
	 * @param foreign_family_allowance the foreign_family_allowance to set
	 */
	public void setForeign_family_allowance(String foreign_family_allowance) {
		this.foreign_family_allowance = foreign_family_allowance;
	}

	/**
	 * @return the foreign_training_fee
	 */
	public String getForeign_training_fee() {
		return foreign_training_fee;
	}

	/**
	 * @param foreign_training_fee the foreign_training_fee to set
	 */
	public void setForeign_training_fee(String foreign_training_fee) {
		this.foreign_training_fee = foreign_training_fee;
	}

	/**
	 * @return the foreign_education_expense
	 */
	public String getForeign_education_expense() {
		return foreign_education_expense;
	}

	/**
	 * @param foreign_education_expense the foreign_education_expense to set
	 */
	public void setForeign_education_expense(String foreign_education_expense) {
		this.foreign_education_expense = foreign_education_expense;
	}

	/**
	 * @return the stock_card_sno
	 */
	public String getStock_card_sno() {
		return stock_card_sno;
	}

	/**
	 * @param stock_card_sno the stock_card_sno to set
	 */
	public void setStock_card_sno(String stock_card_sno) {
		this.stock_card_sno = stock_card_sno;
	}

	/**
	 * @return the deduct_before_tax
	 */
	public String getDeduct_before_tax() {
		return deduct_before_tax;
	}

	/**
	 * @param deduct_before_tax the deduct_before_tax to set
	 */
	public void setDeduct_before_tax(String deduct_before_tax) {
		this.deduct_before_tax = deduct_before_tax;
	}

	/**
	 * @return the employer_tax_rate
	 */
	public String getEmployer_tax_rate() {
		return employer_tax_rate;
	}

	/**
	 * @param employer_tax_rate the employer_tax_rate to set
	 */
	public void setEmployer_tax_rate(String employer_tax_rate) {
		this.employer_tax_rate = employer_tax_rate;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the company_no
	 */
	public String getCompany_no() {
		return company_no;
	}

	/**
	 * @param company_no the company_no to set
	 */
	public void setCompany_no(String company_no) {
		this.company_no = company_no;
	}

	/**
	 * @return the acc_ym
	 */
	public String getAcc_ym() {
		return acc_ym;
	}

	/**
	 * @param acc_ym the acc_ym to set
	 */
	public void setAcc_ym(String acc_ym) {
		this.acc_ym = acc_ym;
	}

	/**
	 * @return the basic_salary
	 */
	public String getBasic_salary() {
		return basic_salary;
	}

	/**
	 * @param basic_salary the basic_salary to set
	 */
	public void setBasic_salary(String basic_salary) {
		this.basic_salary = basic_salary;
	}

	/**
	 * @return the calc_field3
	 */
	public String getCalc_field3() {
		return calc_field3;
	}

	/**
	 * @param calc_field3 the calc_field3 to set
	 */
	public void setCalc_field3(String calc_field3) {
		this.calc_field3 = calc_field3;
	}

	/**
	 * @return the bonus_no_pay
	 */
	public String getBonus_no_pay() {
		return bonus_no_pay;
	}

	/**
	 * @param bonus_no_pay the bonus_no_pay to set
	 */
	public void setBonus_no_pay(String bonus_no_pay) {
		this.bonus_no_pay = bonus_no_pay;
	}

	/**
	 * @return the logo_url
	 */
	public String getLogo_url() {
		return logo_url;
	}

	/**
	 * @param logo_url the logo_url to set
	 */
	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
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
	 * @return the tax_salary
	 */
	public String getTax_salary() {
		return tax_salary;
	}

	/**
	 * @param tax_salary the tax_salary to set
	 */
	public void setTax_salary(String tax_salary) {
		this.tax_salary = tax_salary;
	}

	/**
	 * @return the employee_code
	 */
	public String getEmployee_code() {
		return employee_code;
	}

	/**
	 * @param employee_code the employee_code to set
	 */
	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	/**
	 * @return the bonus
	 */
	public String getBonus() {
		return bonus;
	}

	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	/**
	 * @return the bonus_tax
	 */
	public String getBonus_tax() {
		return bonus_tax;
	}

	/**
	 * @param bonus_tax the bonus_tax to set
	 */
	public void setBonus_tax(String bonus_tax) {
		this.bonus_tax = bonus_tax;
	}

	/**
	 * @return the bonus_free_tax_deduct
	 */
	public String getBonus_free_tax_deduct() {
		return bonus_free_tax_deduct;
	}

	/**
	 * @param bonus_free_tax_deduct the bonus_free_tax_deduct to set
	 */
	public void setBonus_free_tax_deduct(String bonus_free_tax_deduct) {
		this.bonus_free_tax_deduct = bonus_free_tax_deduct;
	}

	/**
	 * @return the bonus_real_pay
	 */
	public String getBonus_real_pay() {
		return bonus_real_pay;
	}

	/**
	 * @param bonus_real_pay the bonus_real_pay to set
	 */
	public void setBonus_real_pay(String bonus_real_pay) {
		this.bonus_real_pay = bonus_real_pay;
	}

	/**
	 * @return the stock
	 */
	public String getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}

	/**
	 * @return the stock_no_pay
	 */
	public String getStock_no_pay() {
		return stock_no_pay;
	}

	/**
	 * @param stock_no_pay the stock_no_pay to set
	 */
	public void setStock_no_pay(String stock_no_pay) {
		this.stock_no_pay = stock_no_pay;
	}

	/**
	 * @return the real_pay
	 */
	public String getReal_pay() {
		return real_pay;
	}

	/**
	 * @param real_pay the real_pay to set
	 */
	public void setReal_pay(String real_pay) {
		this.real_pay = real_pay;
	}

	/**
	 * @return the stock_tax
	 */
	public String getStock_tax() {
		return stock_tax;
	}

	/**
	 * @param stock_tax the stock_tax to set
	 */
	public void setStock_tax(String stock_tax) {
		this.stock_tax = stock_tax;
	}

	/**
	 * @return the stock_free_tax_deduct
	 */
	public String getStock_free_tax_deduct() {
		return stock_free_tax_deduct;
	}

	/**
	 * @param stock_free_tax_deduct the stock_free_tax_deduct to set
	 */
	public void setStock_free_tax_deduct(String stock_free_tax_deduct) {
		this.stock_free_tax_deduct = stock_free_tax_deduct;
	}

	/**
	 * @return the stock_real_pay
	 */
	public String getStock_real_pay() {
		return stock_real_pay;
	}

	/**
	 * @param stock_real_pay the stock_real_pay to set
	 */
	public void setStock_real_pay(String stock_real_pay) {
		this.stock_real_pay = stock_real_pay;
	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Salary salary = new Salary();

		salary.setSno(String.valueOf(rs.getInt("SNO")));
		salary.setReg_no(rs.getString("REG_NO"));
		salary.setAcc_ym(rs.getString("ACC_YM"));
		salary.setDept_no(rs.getString("DEPT_NO"));
		salary.setCompany_no(rs.getString("COMPANY_NO"));
		salary.setBatch_no(String.valueOf(rs.getInt("BATCH_NO")));
		salary.setEmp_no(rs.getString("EMP_NO"));
		salary.setName(rs.getString("NAME"));
		salary.setNationality(rs.getString("NATIONALITY"));
		salary.setEmployee_code(rs.getString("EMPLOYEE_CODE"));
		salary.setDepart_no(rs.getString("DEPART_NO"));
		salary.setPosition_code(rs.getString("POSITION_CODE"));
		salary.setEmployee_type(rs.getString("EMPLOYEE_TYPE"));
		salary.setReal_acc_ym(rs.getString("REAL_ACC_YM"));
		salary.setUnit_no(rs.getString("UNIT_NO"));
		salary.setProvince_code(String.valueOf(rs.getInt("PROVINCE_CODE")));
		salary.setCity_code(rs.getString("CITY_CODE"));
		salary.setBank_code(rs.getString("BANK_CODE"));
		salary.setRevenue_code(rs.getString("REVENUE_CODE"));
		salary.setCurrency_type(rs.getString("CURRENCY_TYPE"));
		salary.setPretax_salary(String.valueOf(rs.getBigDecimal("PRETAX_SALARY")));
		salary.setBasic_salary(String.valueOf(rs.getBigDecimal("BASIC_SALARY")));
		salary.setMonth_bonus(String.valueOf(rs.getBigDecimal("MONTH_BONUS")));
		salary.setOvertime_salary(String.valueOf(rs.getBigDecimal("OVERTIME_SALARY")));
		salary.setOvertime_salary_deduct_tax(rs.getString("OVERTIME_SALARY_DEDUCT_TAX"));
		salary.setTraffic_allowance(String.valueOf(rs.getBigDecimal("TRAFFIC_ALLOWANCE")));
		salary.setTraffic_allowance_deduct_tax(rs.getString("TRAFFIC_ALLOWANCE_DEDUCT_TAX"));
		salary.setOep_basic(String.valueOf(rs.getBigDecimal("OEP_BASIC")));
		salary.setOep_per(String.valueOf(rs.getBigDecimal("OEP_PER")));
		salary.setLose_job_insu(String.valueOf(rs.getBigDecimal("LOSE_JOB_INSU")));
		salary.setMedical_insu(String.valueOf(rs.getBigDecimal("MEDICAL_INSU")));
		salary.setAccfund_insu(String.valueOf(rs.getBigDecimal("ACCFUND_INSU")));
		salary.setAppend_accfund(String.valueOf(rs.getBigDecimal("APPEND_ACCFUND")));
		salary.setPerson_pay_total(String.valueOf(rs.getBigDecimal("PERSON_PAY_TOTAL")));
		salary.setAllowance1(String.valueOf(rs.getBigDecimal("ALLOWANCE1")));
		salary.setAllowance2(String.valueOf(rs.getBigDecimal("ALLOWANCE2")));
		salary.setAllowance3(String.valueOf(rs.getBigDecimal("ALLOWANCE3")));
		salary.setAllowance4(String.valueOf(rs.getBigDecimal("ALLOWANCE4")));
		salary.setAllowance5(String.valueOf(rs.getBigDecimal("ALLOWANCE5")));
		salary.setAllowance6(String.valueOf(rs.getBigDecimal("ALLOWANCE6")));
		salary.setAllowance7(String.valueOf(rs.getBigDecimal("ALLOWANCE7")));
		salary.setAllowance8(String.valueOf(rs.getBigDecimal("ALLOWANCE8")));
		salary.setAllowance9(String.valueOf(rs.getBigDecimal("ALLOWANCE9")));
		salary.setAllowance10(String.valueOf(rs.getBigDecimal("ALLOWANCE10")));
		salary.setAllowance11(String.valueOf(rs.getBigDecimal("ALLOWANCE11")));
		salary.setAllowance12(String.valueOf(rs.getBigDecimal("ALLOWANCE12")));
		salary.setAllowance13(String.valueOf(rs.getBigDecimal("ALLOWANCE13")));
		salary.setAllowance14(String.valueOf(rs.getBigDecimal("ALLOWANCE14")));
		salary.setAllowance15(String.valueOf(rs.getBigDecimal("ALLOWANCE15")));
		salary.setAllowance16(String.valueOf(rs.getBigDecimal("ALLOWANCE16")));
		salary.setAllowance17(String.valueOf(rs.getBigDecimal("ALLOWANCE17")));
		salary.setAllowance18(String.valueOf(rs.getBigDecimal("ALLOWANCE18")));
		salary.setAllowance19(String.valueOf(rs.getBigDecimal("ALLOWANCE19")));
		salary.setAllowance20(String.valueOf(rs.getBigDecimal("ALLOWANCE20")));
		salary.setAllowance21(String.valueOf(rs.getBigDecimal("ALLOWANCE21")));
		salary.setAllowance22(String.valueOf(rs.getBigDecimal("ALLOWANCE22")));
		salary.setAllowance23(String.valueOf(rs.getBigDecimal("ALLOWANCE23")));
		salary.setAllowance24(String.valueOf(rs.getBigDecimal("ALLOWANCE24")));
		salary.setAllowance25(String.valueOf(rs.getBigDecimal("ALLOWANCE25")));
		salary.setFree_tax_allowance1(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE1")));
		salary.setFree_tax_allowance2(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE2")));
		salary.setFree_tax_allowance3(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE3")));
		salary.setFree_tax_allowance4(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE4")));
		salary.setFree_tax_allowance5(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE5")));
		salary.setFree_tax_allowance6(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE6")));
		salary.setFree_tax_allowance7(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE7")));
		salary.setFree_tax_allowance8(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE8")));
		salary.setFree_tax_allowance9(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE9")));
		salary.setFree_tax_allowance10(String.valueOf(rs.getBigDecimal("FREE_TAX_ALLOWANCE10")));
		salary.setDeduct1(String.valueOf(rs.getBigDecimal("DEDUCT1")));
		salary.setDeduct2(String.valueOf(rs.getBigDecimal("DEDUCT2")));
		salary.setDeduct3(String.valueOf(rs.getBigDecimal("DEDUCT3")));
		salary.setDeduct4(String.valueOf(rs.getBigDecimal("DEDUCT4")));
		salary.setDeduct5(String.valueOf(rs.getBigDecimal("DEDUCT5")));
		salary.setDeduct6(String.valueOf(rs.getBigDecimal("DEDUCT6")));
		salary.setDeduct7(String.valueOf(rs.getBigDecimal("DEDUCT7")));
		salary.setDeduct8(String.valueOf(rs.getBigDecimal("DEDUCT8")));
		salary.setDeduct9(String.valueOf(rs.getBigDecimal("DEDUCT9")));
		salary.setDeduct10(String.valueOf(rs.getBigDecimal("DEDUCT10")));
		salary.setFree_tax_deduct1(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT1")));
		salary.setFree_tax_deduct2(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT2")));
		salary.setFree_tax_deduct3(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT3")));
		salary.setFree_tax_deduct4(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT4")));
		salary.setFree_tax_deduct5(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT5")));
		salary.setFree_tax_deduct6(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT6")));
		salary.setFree_tax_deduct7(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT7")));
		salary.setFree_tax_deduct8(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT8")));
		salary.setFree_tax_deduct9(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT9")));
		salary.setFree_tax_deduct10(String.valueOf(rs.getBigDecimal("FREE_TAX_DEDUCT10")));
		salary.setCalc_field1(String.valueOf(rs.getBigDecimal("CALC_FIELD1")));
		salary.setCalc_field2(String.valueOf(rs.getBigDecimal("CALC_FIELD2")));
		salary.setCalc_field3(String.valueOf(rs.getBigDecimal("CALC_FIELD3")));
		salary.setDisplay1(rs.getString("DISPLAY1"));
		salary.setDisplay2(rs.getString("DISPLAY2"));
		salary.setDisplay3(rs.getString("DISPLAY3"));
		salary.setDisplay4(rs.getString("DISPLAY4"));
		salary.setDisplay5(rs.getString("DISPLAY5"));
		salary.setDisplay6(rs.getString("DISPLAY6"));
		salary.setDisplay7(rs.getString("DISPLAY7"));
		salary.setDisplay8(rs.getString("DISPLAY8"));
		salary.setDisplay9(rs.getString("DISPLAY9"));
		salary.setDisplay10(rs.getString("DISPLAY10"));
		salary.setOuter_insu(String.valueOf(rs.getBigDecimal("OUTER_INSU")));
		salary.setShould_pay_tax(String.valueOf(rs.getBigDecimal("SHOULD_PAY_TAX")));
		salary.setTax(String.valueOf(rs.getBigDecimal("TAX")));
		salary.setAdjust_tax(String.valueOf(rs.getBigDecimal("ADJUST_TAX")));
		salary.setReal_pay(String.valueOf(rs.getBigDecimal("REAL_PAY")));
		salary.setSalary_card_sno(String.valueOf(rs.getInt("SALARY_CARD_SNO")));
		salary.setReal_acc_ym(rs.getString("REAL_TAX_ACC_YM"));
		salary.setSalary_payrevenu_sno(String.valueOf(rs.getBigDecimal("SALARY_PAYREVENU_SNO")));
		salary.setBonus_type(rs.getString("BONUS_TYPE"));
		salary.setBonus_tax_adjust_reason(rs.getString("BONUS_TAX_ADJUST_REASON"));
		salary.setBonus_read_tax_acc_ym(rs.getString("BONUS_READ_TAX_ACC_YM"));
		salary.setBonus(String.valueOf(rs.getBigDecimal("BONUS")));
		salary.setBonus_tax(String.valueOf(rs.getBigDecimal("BONUS_TAX")));
		salary.setBonus_adjust_tax(String.valueOf(rs.getBigDecimal("BONUS_ADJUST_TAX")));
		salary.setBonus_real_pay(String.valueOf(rs.getBigDecimal("BONUS_REAL_PAY")));
		salary.setSalary_card_sno(String.valueOf(rs.getInt("BONUS_CARD_SNO")));
		salary.setBonus_payrevenu_sno(String.valueOf(rs.getInt("BONUS_PAYREVENU_SNO")));
		salary.setCompute_flag(rs.getString("COMPUTE_FLAG"));
		salary.setRemarks(rs.getString("REMARKS"));
		salary.setInput_person(rs.getString("INPUT_PERSON"));
		salary.setInput_date(String.valueOf(rs.getDate("INPUT_DATE")));
		salary.setWf_rcvsalary(String.valueOf(rs.getBigDecimal("WF_RCVSALARY")));
		salary.setOep_c(String.valueOf(rs.getBigDecimal("OEP_C")));
		salary.setMedical_c(String.valueOf(rs.getBigDecimal("MEDICAL_C")));
		salary.setLose_job_c(String.valueOf(rs.getBigDecimal("LOSE_JOB_C")));
		salary.setTax_salary(String.valueOf(rs.getBigDecimal("TAX_SALARY")));
		salary.setFreercv_allowance1(String.valueOf(rs.getBigDecimal("FREERCV_ALLOWANCE1")));
		salary.setFreercv_allowance2(String.valueOf(rs.getBigDecimal("FREERCV_ALLOWANCE2")));
		salary.setFreercv_allowance3(String.valueOf(rs.getBigDecimal("FREERCV_ALLOWANCE3")));
		salary.setAccfund_base(String.valueOf(rs.getBigDecimal("ACCFUND_BASE")));
		salary.setFund_total(String.valueOf(rs.getBigDecimal("FUND_TOTAL")));
		salary.setWork_place(rs.getString("WORK_PLACE"));
		salary.setInjury_insu(String.valueOf(rs.getBigDecimal("INJURY_INSU")));
		salary.setChildbirth_insu(String.valueOf(rs.getBigDecimal("CHILDBIRTH_INSU")));
		salary.setSupply_social_insu(String.valueOf(rs.getBigDecimal("SUPPLY_SOCIAL_INSU")));
		salary.setAccfund_c(String.valueOf(rs.getBigDecimal("ACCFUND_C")));
		salary.setSalary_sno(String.valueOf(rs.getInt("SALARY_SNO")));
		salary.setAllowance_no_pay3(String.valueOf(rs.getBigDecimal("ALLOWANCE_NO_PAY3")));
		salary.setAllowance_no_pay4(String.valueOf(rs.getBigDecimal("ALLOWANCE_NO_PAY4")));
		salary.setAllowance_no_pay5(String.valueOf(rs.getBigDecimal("ALLOWANCE_NO_PAY5")));
		salary.setBonus_year(rs.getString("BONUS_YEAR"));
		salary.setBonus_no_pay(String.valueOf(rs.getBigDecimal("BONUS_NO_PAY")));
		salary.setBonus_free_tax_deduct(String.valueOf(rs.getBigDecimal("BONUS_FREE_TAX_DEDUCT")));
		salary.setPayroll_type(rs.getString("PAYROLL_TYPE"));
		salary.setInjury_per(String.valueOf(rs.getBigDecimal("INJURY_PER")));
		salary.setChildbirth_per(String.valueOf(rs.getBigDecimal("CHILDBIRTH_PER")));
		salary.setBonus_emp(String.valueOf(rs.getBigDecimal("BONUS_EMP")));
		salary.setStock(String.valueOf(rs.getBigDecimal("STOCK")));
		salary.setStock_no_pay(String.valueOf(rs.getBigDecimal("STOCK_NO_PAY")));
		salary.setStock_free_tax_deduct(String.valueOf(rs.getBigDecimal("STOCK_FREE_TAX_DEDUCT")));
		salary.setStock_tax(String.valueOf(rs.getBigDecimal("STOCK_TAX")));
		salary.setStock_adjust_tax(String.valueOf(rs.getBigDecimal("STOCK_ADJUST_TAX")));
		salary.setStock_real_pay(String.valueOf(rs.getBigDecimal("STOCK_REAL_PAY")));
		salary.setStock_months(String.valueOf(rs.getInt("STOCK_MONTHS")));
		salary.setWage_tax_deduct(String.valueOf(rs.getBigDecimal("WAGE_TAX_DEDUCT")));
		salary.setBonus_tax_deduct(String.valueOf(rs.getBigDecimal("BONUS_TAX_DEDUCT")));
		salary.setForeign_allowance(String.valueOf(rs.getBigDecimal("FOREIGN_ALLOWANCE")));
		salary.setForeign_deduct_before_tax(String.valueOf(rs.getBigDecimal("FOREIGN_DEDUCT_BEFORE_TAX")));
		salary.setForeign_housing_subsidy(String.valueOf(rs.getBigDecimal("FOREIGN_HOUSING_SUBSIDY")));
		salary.setForeign_meal_allowance(String.valueOf(rs.getBigDecimal("FOREIGN_MEAL_ALLOWANCE")));
		salary.setForeign_laundry_fee(String.valueOf(rs.getBigDecimal("FOREIGN_LAUNDRY_FEE")));
		salary.setForeign_moving_fee(String.valueOf(rs.getBigDecimal("FOREIGN_MOVING_FEE")));
		salary.setForeign_travel_allowance(String.valueOf(rs.getBigDecimal("FOREIGN_TRAVEL_ALLOWANCE")));
		salary.setForeign_family_allowance(String.valueOf(rs.getBigDecimal("FOREIGN_FAMILY_ALLOWANCE")));
		salary.setForeign_training_fee(String.valueOf(rs.getBigDecimal("FOREIGN_TRAINING_FEE")));
		salary.setForeign_education_expense(String.valueOf(rs.getBigDecimal("FOREIGN_EDUCATION_EXPENSE")));
		salary.setStock_card_sno(String.valueOf(rs.getInt("STOCK_CARD_SNO")));
		salary.setDeduct_before_tax(String.valueOf(rs.getBigDecimal("DEDUCT_BEFORE_TAX")));
		salary.setEmployer_tax_rate(String.valueOf(rs.getBigDecimal("EMPLOYER_TAX_RATE")));
		salary.setAnnuity_per(String.valueOf(rs.getBigDecimal("ANNUITY_PER")));
		salary.setAnnuity_exceed(String.valueOf(rs.getBigDecimal("ANNUITY_EXCEED")));
		salary.setAnnuity_comp(String.valueOf(rs.getBigDecimal("ANNUITY_COMP")));
		
		return salary;
	}
}
