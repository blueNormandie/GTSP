package com.salaryMail.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.internal.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.salaryMail.dao.SalaryServerDao;
import com.salaryMail.entity.BaseItemConfig;
import com.salaryMail.entity.ClientConfigInfo;
import com.salaryMail.entity.ClientInfo;
import com.salaryMail.entity.CodeDetail;
import com.salaryMail.entity.FormatInfo;
import com.salaryMail.entity.MailSendInfo;
import com.salaryMail.entity.SalaryExecuteInfo;
import com.salaryMail.entity.SalaryFieldConfig;
import com.salaryMail.entity.SalaryItemConfig;
import com.salaryMail.entity.SalaryTitleBean;

public class SalaryServerDaoImpl extends JdbcDaoSupport implements SalaryServerDao {

	/**
	 * 获取工资项目
	 * 
	 * @throws Exception
	 */
	// ***** EPAY2.0 BEGIN *******	
//	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String accym) throws Exception {
	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String realAccYm) throws Exception {
	// ***** EPAY2.0 END *******	

		String sql = "Select NVL(a.item_name, a.item_name_en) ITEM_NAME,"
				+ " NVL(a.item_name_en, a.item_name) ITEM_NAME_EN,"
				+ " b.fieldname, b.item_code, 0 displayCode, b.add_flag "
				+ " From SFSC.SD_CLIENT_SALARY_ITEM a, SFSC.SD_SALARY_ITEM b"
				+ " Where a.company_no=? And a.acc_ym=?"
				+ " And a.is_valid='1' And a.is_print='1' And a.item_code=b.item_code";

		// ***** EPAY2.0 BEGIN *******	
//		return this.getJdbcTemplate().query(sql, new Object[] { companyNo, accym },
//				new SalaryTitleBean());
		return this.getJdbcTemplate().query(sql, new Object[] { companyNo, realAccYm },
				new SalaryTitleBean());
		// ***** EPAY2.0 END *******	
	}
	
	/**
	 * 获取工资项目
	 * 
	 * @throws Exception
	 */
	public List<SalaryTitleBean> findSalaryTitleForCoach(String companyNo, String realAccYm) throws Exception {

		String sql = "Select NVL(a.item_name, a.item_name_en) ITEM_NAME,"
				+ " NVL(a.item_name_en, a.item_name) ITEM_NAME_EN,"
				+ " b.fieldname, b.item_code, 0 displayCode, b.add_flag "
				+ " From SFSC.SD_CLIENT_SALARY_ITEM a, SFSC.SD_SALARY_ITEM b"
				+ " Where a.company_no=? And a.acc_ym=?"
				+ " And a.is_valid='1' And a.is_print='1' And a.item_code=b.item_code order by a.excel_sno,a.item_code";

		return this.getJdbcTemplate().query(sql, new Object[] { companyNo, realAccYm },
				new SalaryTitleBean());
	}

	/**
	 * 获取工资项目
	 * 
	 * @throws Exception
	 */
	// ***** EPAY2.0 BEGIN *******
//	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String accym, String addFlag,
//			String taxFlag, String wfFlag) throws Exception {
	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String realAccYm, String addFlag,
			String taxFlag, String wfFlag) throws Exception {
	// ***** EPAY2.0 END *******

		String sql = "Select a.item_name, a.item_name_en,"
				+ " b.fieldname, b.item_code, 0 displayCode, b.add_flag "
				+ " From SFSC.SD_CLIENT_SALARY_ITEM a, SFSC.SD_SALARY_ITEM b"
				+ " Where a.company_no=? And a.acc_ym=?"
				+ " And a.is_valid='1' And a.is_print='1' And a.item_code=b.item_code"
				+ " And b.tax_flag=? And b.add_flag=? And b.wf_flag=?";

		// ***** EPAY2.0 BEGIN *******
//		return this.getJdbcTemplate().query(sql,
//				new Object[] { companyNo, accym, taxFlag, addFlag, wfFlag }, new SalaryTitleBean());
		return this.getJdbcTemplate().query(sql,
				new Object[] { companyNo, realAccYm, taxFlag, addFlag, wfFlag }, new SalaryTitleBean());
		// ***** EPAY2.0 END *******
	}

	/**
	 * 获得客户配置信息
	 */
	@Override
	public List<ClientConfigInfo> getClientConfigInfo() {

		return this.getJdbcTemplate().query(
				"Select *  From sfsc.fs_client_config_info Where is_valid = '1' and format_sno <> 0 and is_mail_sent = '1' Order By company_no",
						new ClientConfigInfo());
	}

	/**
	 * 获得客户配置信息
	 */
	@Override
	public ClientConfigInfo getClientConfigInfoByCompanyNo(String companyNo) {

		return this.getJdbcTemplate().queryForObject(
						"Select a.*  From sfsc.fs_client_config_info a Where a.company_no = ? and a.is_valid = '1'",
						new Object[] { companyNo }, new ClientConfigInfo());
	}

	/**
	 * 获得客户配置信息
	 * 
	 * @throws Exception
	 */
	@Override
	public ClientInfo getClientInfoByCompanyNo(String companyNo) throws Exception {

		String sql = "Select b.company_no, b.name_ch,b.name_en,b.shortname COMPANY_NAME,"
			+" b.manage_dept BELONG_TO_DEPT,"
			+"	 f.grp_id DEPT_NAME,"
		    +"	 b.sales USER_ID,"
		    +" g.name USER_NAME,"
			+"	 b.comp_grp_code," 
		    +" v.sfsc_code"
			+"	 From sfsc.fs_client b,"
			+" (SELECT SFSC_CODE FROM (SELECT SFSC_CODE FROM SFSC.FS_FINC_RELATED_PARTY WHERE IS_LOCAL = '1' ) WHERE rownum = 1) v,"
			+"	 sfsc.fs_comp_group d,"
			+"	 sfsc.fs_depts f, sfsc.fs_users g"
			+"	 Where b.company_no = ?"
			+"	 and b.comp_grp_code = d.comp_grp_code(+)"
			+"	 and b.sales = g.user_id(+)"
			+"	 and b.manage_dept = f.grp_id(+)";

		return this.getJdbcTemplate().queryForObject(sql, new Object[] { companyNo },
				new ClientInfo());
	}

	/**
	 * 获得艾睿电子雇员工资信息
	 * 
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getSalaryInfoForArrow(String companyNo, String sendBeginYm,
			String sendDateType) throws SQLException {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, Object> map = null;

		String selectedKey = "to_char(to_date(get_company_payroll_date(a.ACC_YM, a.COMPANY_NO), 'yyyy/mm/dd'), 'yyyy/mm/dd') as \"Pay Day\" , "
				+ " to_char(trunc(to_date(a.ACC_YM, 'yyyy/mm'), 'MONTH'), 'yyyymmdd') || '-' || to_char(last_day(trunc(to_date(a.ACC_YM, 'yyyy/mm'), 'MONTH')), 'yyyymmdd') as \"Salary Period\" , "
				+ " a.DISPLAY1 as \"Legal Entity(Company Code)\" , "
				+ " a.DISPLAY2 as \"Location\" , "
				+ " a.NAME as \"Chinese Name\", "
				+ " a.DISPLAY3 as \"Department\", "
				+ " to_char(get_company_join_date(a.EMP_NO, a.COMPANY_NO),'yyyy/mm/dd') as \"Join Date\", "
				+ " a.EMPLOYEE_CODE as \"Global ID\","
				+ " a.DISPLAY8 as \"Base Salary\","
				+ " a.BASIC_SALARY as \"This Month Salary\", "
				+ " a.MONTH_BONUS as \"Last Month Back Pay\", "
				+ " a.OVERTIME_SALARY as \"Housing Allowance\", "
				+ " a.TRAFFIC_ALLOWANCE as \"High Temperature Allowance\", "
				+ " a.LUNCH_ALLOWANCE as \"Incentive\", "
				+ " a.ALLOWANCE1 as \"G&A Bonus\", "
				+ " a.ALLOWANCE2 as \"Backpay Salary\", "
				+ " a.ALLOWANCE3 as \"Referal Award\", "
				+ " a.ALLOWANCE4 as \"Bonus from Supplier\", "
				+ " a.ALLOWANCE5 as \"Overtime Payment\", "
				+ " a.ALLOWANCE6 as \"Annual Leave Pay\", "
				+ " a.ALLOWANCE7 as \"Wages in lieu of notice\", "
				+ " a.ALLOWANCE8 as \"Others additional\" , "
				+ " a.ALLOWANCE9 as \"Daily Wages\", "
				+ " a.ALLOWANCE10 as \"Unity Retention Bonus\", "
				+ " a.ALLOWANCE11 as \"Trainee Bonus\", "
				+ " a.ALLOWANCE12 as \"Other Bonus\", "
				+ " a.ALLOWANCE13 as \"Salary Adjustment\", "
				+ " a.ALLOWANCE14 as \"Maternity Allowance\", "
				+ " a.ALLOWANCE15 as \"Other Allowance\", "
				+ " a.ALLOWANCE16 as \"Other Adjustment\", "
				+ " a.ALLOWANCE17 as \"No Pay Leave\", "
				+ " a.ALLOWANCE18 as \"Sick Leave\", "
				+ " a.ALLOWANCE19 as \"Other Deduction\", "
				+ " a.ALLOWANCE20 as \"Pension-Individual\", "
				+ " a.ALLOWANCE21 as \"Medical-Individual\", "
				+ " a. ALLOWANCE22 as \"Unemployement-Individual\", "
				+ " a.ALLOWANCE23 as \"Housing Fund-Individual\", "
				+ " a.DISPLAY12 as \"Housing Fund Taxable\", "
				+ " a.FREERCV_ALLOWANCE1 as \"Taiping Premium\", "
				+ " a.TAX as \"Basic Tax\", "
				+ " a.DISPLAY9 as \"Severance Payment\", "
				+ " a.DISPLAY10 as \"Severance Payment Tax\", "
				+ " a.FREE_TAX_ALLOWANCE1 as \"One-Child Allowance\", "
				+ " a.FOREIGN_DEDUCT_BEFORE_TAX as \"Tax Free\", "
				+ " a.BONUS as \"Year-end Bonus\", "
				+ " a.BONUS_TAX as \"Year-End Bonus Tax\", "
				+ " a.DEDUCT1 as \"Stock IIT\", "
				+ " a.FREE_TAX_ALLOWANCE2 as \"IIT Adjustment\", "
				+ " a.DISPLAY11 as \"Taiping Insurance Tax\", "
				+ " nvl(a.REAL_PAY, 0.00) + nvl(a.BONUS_REAL_PAY, 0.00) as \"Month End Pay\", "
				+ " a.oep_c as \"Pension-Company\", "
				+ " a.medical_c as \"Medical-Company\", "
				+ " a.lose_job_c as \"Unemployment-Company\", "
				+ " a.injury_insu as \"Work Injury-Company\", "
				+ " a.childbirth_insu as \"Maternity-Company\", "
				+ " a.ACCFUND_C as \"Housing Fund-Company\", "
				+ " nvl(a.TAX, 0.00) + nvl(a.BONUS_TAX, 0.00) as \"Total IIT\"";

		String sql = "Select a.EMP_NO, a.COMPANY_NO, a.ACC_YM, a.REAL_ACC_YM, a.BATCH_NO,"
				+ " a.BONUS, a.BONUS_CARD_SNO, a.TAX_SALARY, a.BASIC_SALARY, a.REAL_PAY,"
				+ " a.SALARY_CARD_SNO, b.ID, b.MAIL, "
				+ selectedKey
				+ " From SFSC.WF_SALARY a, SFSC.FS_HUMBAS b"
				+ " Where a.emp_no = b.emp_no"
				+ " And a.bonus > 0 And a.bonus_card_sno > 0"
				+ " And a.real_acc_ym >= '"
				+ sendBeginYm
				+ "'"
				+ " And a.company_no = '"
				+ companyNo
				+ "'"
				// ***** S20160161 BEGIN *******
//				+ " And Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				+ " And ( Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				// ***** S20160161 END    *******
				+ " Where a.batch_no = c. batch_no"
				// ***** EPAY2.0 BEGIN *******
//				+ " And a.acc_ym = c.acc_ym"
				// ***** EPAY2.0 END *******
				+ " And a.real_acc_ym = c.real_acc_ym"
				+ " And a.company_no =c.company_no"
				+ " And a.emp_no = c.emp_no"
				+ " And c.is_year_bonus_send <> '0')"
				// ***** S20160161 BEGIN *******
				+ " OR ( Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT h,"
				+ " SFSC.FS_MAIL_SEND_INFO j, SFSC.FS_HUMBAS e"
				+ " Where e.emp_no = h.emp_no"
				+ " And e.mail is not null"
				+ " And h.result_sno = j.result_sno"
				+ " And j.send_status = '3'"
				+ " And a.batch_no = h. batch_no"
				+ " And a.real_acc_ym = h.real_acc_ym"
				+ " And a.company_no = h.company_no"
				+ " And a.emp_no = h.emp_no"
				+ " And h.is_year_bonus_send = '1' ) ) )"
				// ***** S20160161 END    *******
				+ " And Exists(Select * From SFSC.WF_SALARY_CHECK c "
				// ***** EPAY2.0 BEGIN *******
//				+ " Where a.acc_ym = c.acc_ym"
				+ " Where c.acc_ym = (CASE WHEN nvl(a.acc_ym, '201601')>='201601' THEN a.REAL_ACC_YM ELSE a.ACC_YM END) "
				// ***** EPAY2.0 END *******
				+ " And a.company_no = c.company_no And a.batch_no = c.batch_no)"
				+ " Union Select a.EMP_NO, a.COMPANY_NO, a.ACC_YM, a.REAL_ACC_YM, a.BATCH_NO,"
				+ " a.BONUS, a.BONUS_CARD_SNO, a.TAX_SALARY, a.BASIC_SALARY, a.REAL_PAY,"
				+ " a.SALARY_CARD_SNO, b.ID, b.MAIL, "
				+ selectedKey
				+ " From SFSC.WF_SALARY a, SFSC.FS_HUMBAS b"
				+ " Where a.emp_no = b.emp_no"
				+ " And (a.tax_salary > 0 Or a.basic_salary > 0 Or a.real_pay > 0 )"
				+ " And decode("
				+ sendDateType
				+ ", 3, a.salary_card_sno, 1) > 0"
				+ " And a.real_acc_ym >= '"
				+ sendBeginYm
				+ "'"
				+ " And a.company_no = '"
				+ companyNo
				+ "'"
				// ***** S20160161 BEGIN *******
//				+ " And Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				+ " And ( Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				// ***** S20160161 END    *******
				+ " Where a.batch_no = c. batch_no"
				// ***** EPAY2.0 BEGIN *******
//				+ " And a.acc_ym = c.acc_ym"
				// ***** EPAY2.0 END *******
				+ " And a.real_acc_ym = c.real_acc_ym"
				+ " And a.company_no = c.company_no"
				+ " And a.emp_no = c.emp_no"
				+ " And c.is_salary_send <> '0')"
				// ***** S20160161 BEGIN *******
				+ " OR ( Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT h,"
				+ " SFSC.FS_MAIL_SEND_INFO j, SFSC.FS_HUMBAS e"
				+ " Where e.emp_no = h.emp_no"
				+ " And e.mail is not null"
				+ " And h.result_sno = j.result_sno"
				+ " And j.send_status = '3'"
				+ " And a.batch_no = h. batch_no"
				+ " And a.real_acc_ym = h.real_acc_ym"
				+ " And a.company_no = h.company_no"
				+ " And a.emp_no = h.emp_no"
				+ " And h.is_salary_send = '1' ) ) )"
				// ***** S20160161 END    *******
				+ " And Exists(Select * From SFSC.WF_SALARY_CHECK c "
				// ***** EPAY2.0 BEGIN *******
//				+ " Where a.acc_ym = c.acc_ym"
				+ " Where c.acc_ym = (CASE WHEN nvl(a.acc_ym, '201601')>='201601' THEN a.REAL_ACC_YM ELSE a.ACC_YM END) "
				// ***** EPAY2.0 END *******
				+ " And a.company_no = c.company_no And a.batch_no = c.batch_no)";

		List<Map<String, Object>> result = this.getJdbcTemplate().queryForList(sql);
		return result;
	}

	/**
	 * 获得雇员工资信息
	 * 
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getSalaryInfo(String companyNo, String sendBeginYm,
			String sendDateType) throws SQLException {

		String sql = "Select a.*, to_char(b.BIRTHDAY,'MMDD') BIRTHDAY, b.ID, b.MAIL, b.NAME as \"Chinese Name\""
				+ " From SFSC.WF_SALARY a, SFSC.FS_HUMBAS b" + " Where a.emp_no = b.emp_no  "
				+ " And a.bonus > 0" + " And decode("
				+ sendDateType
				// ***** S20160161 BEGIN *******
//				+ ", 3, a.bonus_card_sno, 1) > 0"
				+ ", 3, a.bonus_card_sno, 4, a.bonus_card_sno, 1) > 0"
				// ***** S20160161 END    *******
				+ " And a.real_acc_ym >= '"
				+ sendBeginYm
				+ "'"
				+ " And a.company_no = '"
				+ companyNo
				+ "'"
				// ***** S20160161 BEGIN *******
//				+ " And Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				+ " And ( Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				// ***** S20160161 END    *******
				+ " Where a.batch_no = c. batch_no"
				// ***** EPAY2.0 BEGIN *******
//				+ " And a.acc_ym = c.acc_ym"
				// ***** EPAY2.0 END *******
				+ " And a.real_acc_ym = c.real_acc_ym"
				+ " And a.company_no =c.company_no"
				+ " And a.emp_no = c.emp_no"
				+ " And c.is_year_bonus_send <> '0' )"
				// ***** S20160161 BEGIN *******
				+ " OR ( Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT h,"
				+ " SFSC.FS_MAIL_SEND_INFO j, SFSC.FS_HUMBAS e"
				+ " Where e.emp_no = h.emp_no"
				+ " And e.mail is not null"
				+ " And h.result_sno = j.result_sno"
				+ " And j.send_status = '3'"
				+ " And a.batch_no = h. batch_no"
				+ " And a.real_acc_ym = h.real_acc_ym"
				+ " And a.company_no = h.company_no"
				+ " And a.emp_no = h.emp_no"
				+ " And h.is_year_bonus_send = '1' ) ) )"
				// ***** S20160161 END    *******
				+ " And Exists(Select * From SFSC.WF_SALARY_CHECK c "
				// ***** EPAY2.0 BEGIN *******
//				+ " Where a.acc_ym = c.acc_ym"
				+ " Where c.acc_ym = (CASE WHEN nvl(a.acc_ym, '201601')>='201601' THEN a.REAL_ACC_YM ELSE a.ACC_YM END)"
				// ***** EPAY2.0 END *******
				+ " And a.company_no = c.company_no And a.batch_no = c.batch_no)"
				+ " Union Select a.*, to_char(b.BIRTHDAY,'MMDD') BIRTHDAY, b.ID, b.MAIL, b.NAME as \"Chinese Name\""
				+ " From SFSC.WF_SALARY a, SFSC.FS_HUMBAS b"
				+ " Where a.emp_no = b.emp_no"
				+ " And (a.tax_salary > 0 Or a.basic_salary > 0 Or a.real_pay > 0 )"
				+ " And decode("
				+ sendDateType
				// ***** S20160161 BEGIN *******
//				+ ", 3, a.salary_card_sno, 1) > 0"
				+ ", 3, a.salary_card_sno, 4, a.salary_card_sno, 1) > 0"
				// ***** S20160161 END    *******
				+ " And a.real_acc_ym >= '"
				+ sendBeginYm
				+ "'"
				+ " And a.company_no = '"
				+ companyNo
				+ "'"
			    // ***** S20160161 BEGIN *******
//				+ " And Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				+ " And ( Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				// ***** S20160161 END    *******
				+ " Where a.batch_no = c. batch_no"
				// ***** EPAY2.0 BEGIN *******
//				+ " And a.acc_ym = c.acc_ym"
				// ***** EPAY2.0 END *******
				+ " And a.real_acc_ym = c.real_acc_ym"
				+ " And a.company_no = c.company_no"
				+ " And a.emp_no = c.emp_no"
				+ " And c.is_salary_send <> '0' )"
				// ***** S20160161 BEGIN *******
				+ " OR ( Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT h,"
				+ " SFSC.FS_MAIL_SEND_INFO j, SFSC.FS_HUMBAS e"
				+ " Where e.emp_no = h.emp_no"
				+ " And e.mail is not null"
				+ " And h.result_sno = j.result_sno"
				+ " And j.send_status = '3'"
				+ " And a.batch_no = h. batch_no"
				+ " And a.real_acc_ym = h.real_acc_ym"
				+ " And a.company_no = h.company_no"
				+ " And a.emp_no = h.emp_no"
				+ " And h.is_salary_send = '1' ) ) )"
				// ***** S20160161 END    *******
				+ " And Exists(Select * From SFSC.WF_SALARY_CHECK c "
				// ***** EPAY2.0 BEGIN *******
//				+ " Where a.acc_ym = c.acc_ym"
				+ " Where c.acc_ym = (CASE WHEN nvl(a.acc_ym, '201601')>='201601' THEN a.REAL_ACC_YM ELSE a.ACC_YM END)"
				// ***** EPAY2.0 END *******
				+ " And a.company_no = c.company_no And a.batch_no = c.batch_no)"
				+ " Union Select a.*, to_char(b.BIRTHDAY,'MMDD') BIRTHDAY, b.ID, b.MAIL, b.NAME as \"Chinese Name\""
				+ " From SFSC.WF_SALARY a, SFSC.FS_HUMBAS b"
				+ " Where a.emp_no = b.emp_no"
				+ " And a.stock > 0 "
				+ " And decode("
				+ sendDateType
				// ***** S20160161 BEGIN *******
//				+ ", 3, a.stock_card_sno, 1) > 0"
				+ ", 3, a.stock_card_sno, 4, a.stock_card_sno, 1) > 0"
				// ***** S20160161 END    *******
				+ " And a.real_acc_ym >= '"
				+ sendBeginYm
				+ "'"
				+ " And a.company_no = '"
				+ companyNo
				+ "'"
				// ***** S20160161 BEGIN *******
//				+ " And Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				+ " And ( Not Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT c"
				// ***** S20160161 END    *******
				+ " Where a.batch_no = c. batch_no"
				// ***** EPAY2.0 BEGIN *******
//				+ " And a.acc_ym = c.acc_ym"
				// ***** EPAY2.0 END *******
				+ " And a.real_acc_ym = c.real_acc_ym"
				+ " And a.company_no = c.company_no"
				+ " And a.emp_no = c.emp_no"
				+ " And c.is_stock_send <> '0' )"
				// ***** S20160161 BEGIN *******
				+ " OR ( Exists (Select * From SFSC.FS_SALARY_EXECUTE_RESULT h,"
				+ " SFSC.FS_MAIL_SEND_INFO j, SFSC.FS_HUMBAS e"
				+ " Where e.emp_no = h.emp_no"
				+ " And e.mail is not null"
				+ " And h.result_sno = j.result_sno"
				+ " And j.send_status = '3'"
				+ " And a.batch_no = h. batch_no"
				+ " And a.real_acc_ym = h.real_acc_ym"
				+ " And a.company_no = h.company_no"
				+ " And a.emp_no = h.emp_no"
				+ " And h.is_stock_send = '1' ) ) )"
				// ***** S20160161 END    *******
				+ " And Exists(Select * From SFSC.WF_SALARY_CHECK c "
				// ***** EPAY2.0 BEGIN *******
//				+ " Where a.acc_ym = c.acc_ym"
				+ " Where c.acc_ym = (CASE WHEN nvl(a.acc_ym, '201601')>='201601' THEN a.REAL_ACC_YM ELSE a.ACC_YM END)"
				// ***** EPAY2.0 END *******
				+ " And a.company_no = c.company_no And a.batch_no = c.batch_no)";

		List<Map<String, Object>> result = this.getJdbcTemplate().queryForList(sql);
		return result;
	}

	/**
	 * 获得雇员工资信息
	 * 
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getSalaryInfoBaseMonth(String companyNo, String empNo,
			String baseMonth, String batchNo) throws SQLException {

		String sql = "Select a.*, b.ID, b.MAIL, b.NAME as \"Chinese Name\""
				+ " From SFSC.WF_SALARY a, SFSC.FS_HUMBAS b"
				+ " Where a.emp_no = b.emp_no"
				// ***** EPAY2.0 BEGIN *******
//				+ " And a.acc_ym = (Select MAX(t.acc_ym) From SFSC.WF_SALARY t"
				+ " And a.real_acc_ym = (Select MAX(t.real_acc_ym) From SFSC.WF_SALARY t"
				// ***** EPAY2.0 END *******
				+ " Where t.company_no = '" + companyNo
				+ "' And t.emp_no = '" + empNo + "')"
				+ " And a.company_no = '" + companyNo
				+ "' And a.emp_no = '" + empNo
				+ "' And a.batch_no=" + batchNo
				// ***** EPAY2.0 BEGIN *******
//				+ " Order by acc_ym, batch_no" ;
				+ " Order by a.real_acc_ym, a.batch_no" ;
				// ***** EPAY2.0 END *******

		List<Map<String, Object>> retList = this.getJdbcTemplate().queryForList(sql);
		return retList;
	}

	/**
	 * 获得雇员工资信息
	 * 
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getSalaryInfoForResend(String companyNo, String empNo,
			String batchNo, String accYm, String realAccYm) throws SQLException {

		String sql = "Select a.*, b.ID, b.MAIL, b.NAME as \"Chinese Name\""
				+ " From SFSC.WF_SALARY a, SFSC.FS_HUMBAS b"
				+ " Where a.emp_no = b.emp_no"
				+ " And a.batch_no = '" + batchNo + "'"
				// ***** EPAY2.0 BEGIN *******
//				+ " And a.acc_ym = '" + accYm + "'"
				// ***** EPAY2.0 END *******
				+ " And a.real_acc_ym = '" + realAccYm + "'"
				+ " And a.company_no = '" + companyNo + "'"
				+ " And a.emp_no = '" + empNo + "'";

		Map<String, Object> result = this.getJdbcTemplate().queryForMap(sql);
		return result;
	}

	/**
	 * 判断雇员当月是否需要发送加密工资单邮件
	 * 
	 * @throws SQLException
	 */
	public String checkEmployee(Map<String, Object> paramMap) throws SQLException {
		// ***** EPAY2.0 BEGIN *******
//		String sql = "Select sfsc.check_employee_send(?,?,?,?,?,?,?) From DUAL";
		String sql = "Select sfsc.check_employee_send(?,?,?,?,?,?) From DUAL";
		// ***** EPAY2.0 END *******

	 	return this.getJdbcTemplate().queryForObject(
				sql,
				new Object[] { paramMap.get("si_company_no"), paramMap.get("si_emp_no"),
						// ***** EPAY2.0 BEGIN *******
						//paramMap.get("si_acc_ym"), paramMap.get("si_real_acc_ym"),
						paramMap.get("si_real_acc_ym"),
						// ***** EPAY2.0 END *******
						paramMap.get("si_send_date_type"), paramMap.get("ni_kind"),
						paramMap.get("ni_batch_no") }, java.lang.String.class);
	}

	/**
	 * 从字典表取出数据
	 */
	@Override
	public CodeDetail getCodeDetail(String codeClass, String code) {
		return this.getJdbcTemplate().queryForObject(
				"Select t.* From sfsc.sd_code_detail t Where t.code_class= ? and t.code = ?",
				new Object[] { codeClass, code }, new CodeDetail());
	}

	/**
	 * 追加邮件发送结果表中的记录
	 * 
	 * @param salaryInfo
	 * @param isYearBonusSend
	 * @param isSalarySend
	 * @param sendSuccess
	 * @param isSendAgain
	 * @param sender
	 * @return String
	 */
	@Override
	public String insertMailResultInfo(Map<String, Object> salaryInfo, String isYearBonusSend,
			String isSalarySend, String isStockSend, String isReleasePaySend,String mailTitle, String sendSuccess,
			String isSendAgain, String sender) {
		String result = "1";
		
		// ***** S20160161 BEGIN *******
		String sqlCnt = "Select count(*) From SFSC.FS_SALARY_EXECUTE_RESULT h, SFSC.FS_MAIL_SEND_INFO j,"
				+ " SFSC.FS_HUMBAS e"
				+ " Where e.emp_no = h.emp_no"
				+ " And e.mail is not null"
				+ " And h.result_sno = j.result_sno"
				+ " And j.send_status = '3'"
				+ " And h.batch_no = ?"
				+ " And h.real_acc_ym = ?"
				+ " And h.company_no = ?"
				+ " And h.emp_no = ?"
				+ " And h.is_year_bonus_send = ?"
				+ " And h.is_salary_send = ?"
				+ " And h.is_stock_send = ?";
		int count = this.getJdbcTemplate().queryForInt(sqlCnt,
				new Object[] { salaryInfo.get("BATCH_NO"),
					salaryInfo.get("REAL_ACC_YM"),
					salaryInfo.get("COMPANY_NO"),
					salaryInfo.get("EMP_NO"),
					isYearBonusSend,
					isSalarySend,
					isStockSend });

		if (count == 0 ) {
			// ***** S20160161 END    *******
			String sql1="insert into SFSC.FS_MAIL_SEND_INFO(RESULT_SNO,EMP_NO,COMPANY_NO,COMP_GRP_CODE,DEPT_NO,"
					+ " MAIL_TITLE,SEND_EMAIL,FILE_NAME,SEND_STATUS,IS_SEND_AGAIN,SEND_ID,SEND_DATE,SFSC_CODE)"
					+ " values(SFSC.SEQ_RESULT_SNO.NEXTVAL,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)";
	
			Object obj1[]={ salaryInfo.get("EMP_NO"),
					salaryInfo.get("COMPANY_NO"),
					salaryInfo.get("COMP_GRP_CODE") == null ? "" : salaryInfo.get("COMP_GRP_CODE").toString(),
					salaryInfo.get("BELONG_TO_DEPT"),
					mailTitle,
					salaryInfo.get("MAIL") == null ? "" : salaryInfo.get("MAIL").toString(),
					salaryInfo.get("FILE_PATH").toString(),
					sendSuccess,
					isSendAgain,
					sender,
					salaryInfo.get("SFSC_CODE")};
	
			this.getJdbcTemplate().update(sql1, obj1);
	
			String sql2="insert into SFSC.FS_SALARY_EXECUTE_RESULT(RESULT_SNO,EMP_NO,COMPANY_NO,BATCH_NO,ACC_YM,REAL_ACC_YM,"
					 + " IS_YEAR_BONUS_SEND,IS_SALARY_SEND,IS_STOCK_SEND,IS_RELEASE_PAY_SEND)"
					 + " values(SFSC.SEQ_RESULT_SNO.CURRVAL,?,?,?,?,?,?,?,?,?)";
			
			Object obj2[]={ salaryInfo.get("EMP_NO"),
					salaryInfo.get("COMPANY_NO"),
					salaryInfo.get("BATCH_NO"),
					// ***** EPAY2.0 BEGIN *******
	//				salaryInfo.get("ACC_YM"),salaryInfo.get("REAL_ACC_YM"),
					salaryInfo.get("REAL_ACC_YM"),
					salaryInfo.get("REAL_ACC_YM"),
					// ***** EPAY2.0 END *******
					isYearBonusSend,
					isSalarySend,
					isStockSend,
					isReleasePaySend};
	
			this.getJdbcTemplate().update(sql2, obj2);
			// ***** S20160161 BEGIN *******
		} else {
			String sql = "UPDATE SFSC.FS_MAIL_SEND_INFO j SET SEND_EMAIL = ?, FILE_NAME = ?, SEND_DATE = sysdate"
					+ " WHERE EXISTS (SELECT 1 FROM SFSC.FS_SALARY_EXECUTE_RESULT h "
					+ " WHERE h.result_sno = j.result_sno"
					+ " And j.send_status = '3'"
					+ " And h.batch_no = ?"
					+ " And h.real_acc_ym = ?"
					+ " And h.company_no = ?"
					+ " And h.emp_no = ?"
					+ " And h.is_year_bonus_send = ?"
					+ " And h.is_salary_send = ?"
					+ " And h.is_stock_send = ? )";
			Object obj[] = { salaryInfo.get("MAIL") == null ? "" : salaryInfo.get("MAIL").toString(),
					salaryInfo.get("FILE_PATH").toString(),
					salaryInfo.get("BATCH_NO"),
					salaryInfo.get("REAL_ACC_YM"),
					salaryInfo.get("COMPANY_NO"),
					salaryInfo.get("EMP_NO"),
					isYearBonusSend,
					isSalarySend,
					isStockSend };
			this.getJdbcTemplate().update(sql, obj);
		}
		// ***** S20160161 END    *******

		return result;
	}

	/**
	 * 更新邮件发送结果表中的发送状态
	 * 
	 * @param empCompChange
	 * @throws Exception
	 */
	@Override
	public int updateMailResultInfo(String sendSuccess, String empNo, String failReason,
			String bizType, Long mailBatchNo, String filePath) {
		String sql = "UPDATE SFSC.FS_MAIL_SEND_INFO SET"
				+ " SEND_STATUS = ?, FAIL_REASON = ?, BIZ_TYPE = ?, MAIL_BATCH_NO = ?, SEND_DATE = SYSDATE"
				+ " WHERE EMP_NO = ? AND FILE_NAME = ?";
		Object obj[] = { sendSuccess, failReason, bizType, mailBatchNo, empNo, filePath };
		return this.getJdbcTemplate().update(sql, obj);
	}

	/**
	 * 重发时更新邮件发送结果表中的发送状态
	 * 
	 * @param empCompChange
	 * @throws Exception
	 */
	@Override
	public int updateMailResultInfoForResend(String sendSuccess, String userId, String mail,
			String failReason, String sendFilePath, Long resultSno) {
		String sql = "UPDATE SFSC.FS_MAIL_SEND_INFO SET"
				+ " FILE_NAME = ?, SEND_STATUS = ?, FAIL_REASON = ?,"
				+ " IS_SEND_AGAIN = '1', SEND_EMAIL = ?, SEND_ID = ?, SEND_DATE = SYSDATE"
				+ " WHERE RESULT_SNO = ?";
		Object obj[] = { sendFilePath, sendSuccess, failReason, mail, userId, resultSno };
		return this.getJdbcTemplate().update(sql, obj);
	}

	/**
	 * 获得雇员所在部门名
	 * 
	 * @throws SQLException
	 */
	@Override
	public String getNameByDepartNo(String departNo) throws SQLException {

		String sqlCnt = "Select count(*) From sfsc.fs_comp_org Where depart_no = ?";
		String sql = "Select dept_name From sfsc.fs_comp_org Where depart_no = ?";

		int count = this.getJdbcTemplate().queryForInt(sqlCnt, new Object[] { departNo });

		if (count > 0) {
			return this.getJdbcTemplate().queryForObject(sql, new Object[] { departNo },
					java.lang.String.class);
		} else {
			return "";
		}
	}

	/**
	 * 获得雇员银行卡号
	 * 
	 * @throws SQLException
	 */
	@Override
	public String getBankAccountByEmpNo(String empNo) throws SQLException {

		String sqlCnt = "Select count(*) From sfsc.fs_emp_account "
				+ " Where is_default='1' and forbid_mark='0' and proc_status in('2','6') and emp_no= ?";
		String sql = "Select bank_account From sfsc.fs_emp_account "
				+ " Where is_default='1' and forbid_mark='0' and proc_status in('2','6') and emp_no= ?";

		int count = this.getJdbcTemplate().queryForInt(sqlCnt, new Object[] { empNo });

		if (count > 0) {
			return this.getJdbcTemplate().queryForObject(sql, new Object[] { empNo },
					java.lang.String.class);
		} else {
			return "";
		}
	}

	/**
	 * 获得雇员成本中心
	 * 
	 * @throws SQLException
	 */
	@Override
	public String getCostCenter(String companyNo, String empNo) throws SQLException {

		String sqlCnt = "Select count(*) From sfsc.fs_comp_org_emp Where company_no = ? and emp_no = ?";
		String sql = "Select cost_center From sfsc.fs_comp_org_emp Where company_no = ? and emp_no = ?";

		int count = this.getJdbcTemplate().queryForInt(sqlCnt, new Object[] { companyNo, empNo });

		if (count > 0) {
			return this.getJdbcTemplate().queryForObject(sql, new Object[] { companyNo, empNo },
					java.lang.String.class);
		} else {
			return "";
		}
	}

	/**
	 * 获得业务员邮箱
	 * 
	 * @param companyNo
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getSaleInfo(String companyNo) throws SQLException {

//		String sql = "Select a.COMPANY_NO, a.COMP_GRP_CODE, d.USER_ID, d.USER_NAME, d.EMAIL, e.GRP_ID, e.GRP_NAME"
//				+ " From fs_client a, fs_sale_group b, fs_sale_grpmember c, fs_users d, fs_depts e"
//				+ " Where a.company_no ='"
//				+ companyNo
//				+ "'"
//				+ " and a.sale_grp_code = b.group_code(+)"
//				+ " and b.group_code = c.group_code(+)"
//				+ " and c.user_id = d.user_id(+)" + " and b.belong_to_dept = e.grp_id(+)";
		
		String sql = "Select a.COMPANY_NO, a.SHORTNAME, a.COMP_GRP_CODE, d.USER_ID, d.USER_NAME, d.NAME, d.EMAIL, e.GRP_ID, e.GRP_NAME"
				+ " From sfsc.fs_client a, sfsc.fs_users d, sfsc.fs_depts e"
				+ " Where a.company_no ='"
				+ companyNo
				+ "'"
				+ " and a.sales = d.user_id(+)"
				+ " and a.manage_dept = e.grp_id(+)";

		List<Map<String, Object>> result = this.getJdbcTemplate().queryForList(sql);
		return result;
	}

	/**
	 * 获得模板工资区域配置表的数据
	 * 
	 * @param formatSno
	 */
	@Override
	public List<SalaryFieldConfig> getSalaryFieldConfig(int formatSno) {
		return this.getJdbcTemplate().query(
				"select * from SFSC.FS_SALARY_FIELD_CONFIG where FORMAT_SNO=? order by FIELD_NO",
				new Object[] { formatSno }, new SalaryFieldConfig());
	}

	/**
	 * 获得模板工资项目配置表的数据
	 * 
	 * @param formatSno
	 */
	@Override
	public List<SalaryItemConfig> getSalaryItemConfig(int formatSno) {
		return this.getJdbcTemplate().query(
				"select * from  SFSC.FS_SALARY_ITEM_CONFIG where FORMAT_SNO=? order by FIELD_NO,ITEM_POSITION,ITEM_CODE",
				new Object[] { formatSno }, new SalaryItemConfig());
	}

	/**
	 * 获得模板基本项目配置表的数据
	 * 
	 * @param formatSno
	 */
	@Override
	public List<BaseItemConfig> getBaseItemConfig(int formatSno) {
		return this.getJdbcTemplate().query("select * from SFSC.FS_BASE_ITEM_CONFIG where FORMAT_SNO=? order by ITEM_POSITION,ITEM_NO",
				new Object[] { formatSno }, new BaseItemConfig());
	}

	/**
	 * 获得模板信息表的数据
	 * 
	 * @param formatSno
	 */
	@Override
	public FormatInfo getFormatInfo(int formatSno) {
		return this.getJdbcTemplate().queryForObject(
				"select * from  SFSC.FS_FORMAT_INFO where FORMAT_SNO=?", new Object[] { formatSno },
				new FormatInfo());
	}

	@Override
	// ***** EPAY2.0 BEGIN *******	
//	public String getPreSendDate(String accYm, String companyNo) {
//		String preSendDate= this.getJdbcTemplate().queryForObject(
//				"select sfsc.get_company_payroll_date(?,?) from dual",
//				new Object[] { accYm, companyNo }, java.lang.String.class);
//		String day = preSendDate.substring(preSendDate.length()-2,preSendDate.length());
//		String month = accYm.substring(4, 6);
//		preSendDate = accYm.substring(0, 4) + accYm.substring(4, 6)+day;
//		if(day.equals("29")||day.equals("30")){
//			if(month.equals("02")){
//				int year = Integer.valueOf(accYm.substring(0, 4));
//				if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
//					preSendDate=accYm.substring(0, 4)+accYm.substring(4, 6)+"29";
//				}else{
//					preSendDate=accYm.substring(0, 4)+accYm.substring(4, 6)+"28";
//				}
//				
//			}
//		}
//		if(day.equals("31")){
//			if(month.equals("02")){
//				int year = Integer.valueOf(accYm.substring(0, 4));
//				if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
//					preSendDate=accYm.substring(0, 4)+accYm.substring(4, 6)+"29";
//				}else{
//					preSendDate=accYm.substring(0, 4)+accYm.substring(4, 6)+"28";
//				}
//				
//			}else if(month.equals("04")||month.equals("06")||month.equals("09")||month.equals("11")){
//				preSendDate=accYm.substring(0, 4)+accYm.substring(4, 6)+"30";
//			}
//		}
//		return preSendDate;
//	}
	public String getPreSendDate(String realAccYm, String companyNo) {

		String preSendDate = this.getJdbcTemplate().queryForObject(
				"select SFSC.GET_ADJUST_PAYROLL_DATE(?,?) from dual",
				new Object[] { realAccYm, companyNo }, java.lang.String.class);

		String day = preSendDate.substring(preSendDate.length() - 2, preSendDate.length());
		String month = preSendDate.substring(4, 6);

		if (day.equals("29") || day.equals("30")) {
			if (month.equals("02")) {
				int year = Integer.valueOf(preSendDate.substring(0, 4));
				if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
					preSendDate = preSendDate.substring(0, 6) + "29";
				} else {
					preSendDate = preSendDate.substring(0, 6) + "28";
				}
			}
		}
		if (day.equals("31")) {
			if (month.equals("02")) {
				int year = Integer.valueOf(preSendDate.substring(0, 4));
				if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
					preSendDate = preSendDate.substring(0, 6) + "29";
				} else {
					preSendDate = preSendDate.substring(0, 6) + "28";
				}

			} else if (month.equals("04") || month.equals("06") || month.equals("09")
					|| month.equals("11")) {
				preSendDate = preSendDate.substring(0, 6) + "30";
			}
		}
		return preSendDate;
	}
    // ***** EPAY2.0 END *******	

	@Override
	public Date getRealSendDate(String empNo, String companyNo, String realAccYm, Integer kind,
			Integer batchNo) {

		return this.getJdbcTemplate()
				.queryForObject("select sfsc.get_employee_payroll_day(?,?,?,?,?) from dual",
						new Object[] { empNo, companyNo, realAccYm, kind, batchNo },
						java.util.Date.class);
	}

	/**
	 * 获得经济补偿金
	 */
	@Override
	public Map<String, String> getDisSubsidy(String empNo, String companyNo, String realAccYm)
			throws SQLException, DataAccessException {
		Map<String, String> map = new HashMap<String, String>();
		CallableStatement call = null;
		try {
			call = this.getConnection().prepareCall(
					"{CALL sfsc.sp_get_employee_dis_subsidy(?,?,?,?,?,?,?,?,?,?,?,?)}");

			call.setString("si_emp_no", empNo);
			call.setString("si_company_no", companyNo);
			call.setString("si_real_acc_ym", realAccYm);

			call.registerOutParameter("no_before_tax", OracleTypes.NUMBER);// 税前应发经济补偿金
			call.registerOutParameter("no_before_tax_deduct", OracleTypes.NUMBER);// 税前应扣经济补偿金
			call.registerOutParameter("no_rcv_no_tax", OracleTypes.NUMBER);// 仅计税不发放经济补偿金
			call.registerOutParameter("no_deduct_tax", OracleTypes.NUMBER);// 仅抵扣计税项（经济补偿金）
			call.registerOutParameter("no_total", OracleTypes.NUMBER);// 计税经济补偿金总额
			call.registerOutParameter("no_tax", OracleTypes.NUMBER);// 经济补偿金个人所得税
			call.registerOutParameter("no_should_pay", OracleTypes.NUMBER);// 税后应发经济补偿金
			call.registerOutParameter("no_deduct_pay", OracleTypes.NUMBER);// 税后应扣经济补偿金
			call.registerOutParameter("no_real_pay", OracleTypes.NUMBER);// 实发经济补偿金

			call.execute();

			BigDecimal subsidyBefTax = (BigDecimal) call.getBigDecimal("no_before_tax");
			map.put("subsidyBefTax", subsidyBefTax != null ? subsidyBefTax.toString() : "0.00");

			BigDecimal subsidyBefTaxDeduct = (BigDecimal) call
					.getBigDecimal("no_before_tax_deduct");
			map.put("subsidyBefTaxDeduct",
					subsidyBefTaxDeduct != null ? subsidyBefTaxDeduct.toString() : "0.00");

			BigDecimal subsidyRcvNoTax = (BigDecimal) call.getBigDecimal("no_rcv_no_tax");
			map.put("subsidyRcvNoTax", subsidyRcvNoTax != null ? subsidyRcvNoTax.toString()
					: "0.00");

			BigDecimal subsidyDeductTax = (BigDecimal) call.getBigDecimal("no_deduct_tax");
			map.put("subsidyDeductTax", subsidyDeductTax != null ? subsidyDeductTax.toString()
					: "0.00");

			BigDecimal subsidyTotal = (BigDecimal) call.getBigDecimal("no_total");
			map.put("subsidyTotal", subsidyTotal != null ? subsidyTotal.toString() : "0.00");

			BigDecimal subsidyTax = (BigDecimal) call.getBigDecimal("no_tax");
			map.put("subsidyTax", subsidyTax != null ? subsidyTax.toString() : "0.00");

			BigDecimal subsidyShouldPay = (BigDecimal) call.getBigDecimal("no_should_pay");
			map.put("subsidyShouldPay", subsidyShouldPay != null ? subsidyShouldPay.toString()
					: "0.00");

			BigDecimal subsidyDeductPay = (BigDecimal) call.getBigDecimal("no_deduct_pay");
			map.put("subsidyDeductPay", subsidyDeductPay != null ? subsidyDeductPay.toString()
					: "0.00");

			BigDecimal subsidyRealPay = (BigDecimal) call.getBigDecimal("no_real_pay");
			map.put("subsidyRealPay", subsidyRealPay != null ? subsidyRealPay.toString() : "0.00");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (call != null) {
				call.close();
			}
		}
		return map;
	}

	/**
	 * 获得加密工资单邮件推送结果数据
	 * 
	 * @param formatSno
	 */
	@Override
	public SalaryExecuteInfo getMailResultInfo(Long resultSno) {
		return this.getJdbcTemplate().queryForObject(
				"select IS_YEAR_BONUS_SEND,IS_SALARY_SEND,IS_STOCK_SEND,"+
				"IS_RELEASE_PAY_SEND from  sfsc.FS_SALARY_EXECUTE_RESULT where result_sno=?",
				new Object[] { resultSno }, new SalaryExecuteInfo());
	}

	/**
	 * 获得雇员密码
	 * 
	 * @param empNo
	 */
	@Override
	public List<Map<String, Object>> getEmpPwd(String empNo) {

		String sql = 
				"Select * from  SFSC.FS_EMPLOYEE_SALARYMAIL_PWD where EMP_NO='" + empNo + "'";

		return this.getJdbcTemplate().queryForList(sql);
	}

	/**
	 * 获得录入人邮箱
	 * 
	 * @param name
	 */
	@Override
	public List<Map<String, Object>> getInputPersonMail(String name) {

		String sql = 
				"Select USER_ID, USER_NAME, EMAIL from  sfsc.fs_users where NAME='" + name + "'";

		return this.getJdbcTemplate().queryForList(sql);
	}
	
	/**
	  * 获得fieldName的英文显示名称
	  */
	@Override
	public List<Map<String, Object>> getFieldNameEn() {
		String sql = "Select CODE,CODE_DESC_1_CONTENT From sfsc.sd_code_detail Where code_class='PY61'";
		return this.getJdbcTemplate().queryForList(sql);
	}

	/**
	  * 获得基本信息英文显示字段（EPay数据）
	  * 
	  */
	 @Override
	 // ***** No：S20151358 BEGIN *******
//	 public List<Map<String, Object>> getbaseInfoNameEn(String companyNo, String accYm) {
//		 String sql = "select ITEM_CODE,ITEM_NAME_EN from SFSC.SD_CLIENT_SALARY_ITEM where company_no='"
//				 +companyNo+"' and acc_ym='"+accYm+"' and is_valid='1' And is_print='1'";
//		 return this.getJdbcTemplate().queryForList(sql);
//	 }
	 public List<Map<String, Object>> getbaseInfoNameEn(String companyNo, String realAccYm) {
		 String sql = "Select ITEM_CODE, ITEM_NAME_EN From SFSC.SD_CLIENT_SALARY_ITEM"
				 +" Where company_no='" + companyNo
				 + "' And acc_ym='" + realAccYm
				 + "' And is_valid='1' And is_print='1'";
		 return this.getJdbcTemplate().queryForList(sql);
	 }
	 // ***** No：S20151358 END *******
	 
	/**
	 * 黑名单中的人员并且邮箱为空的，更新邮件发送信息表
	 * 
	 * @param empNo
	 */
	@Override
	public int updateRcvBlackCode(String failReason, String empNo, String fileName) {

		String sql = "UPDATE SFSC.FS_MAIL_SEND_INFO SET SEND_STATUS = '4', FAIL_REASON=?"
				+ " WHERE EMP_NO = ? AND FILE_NAME = ?";
		Object obj[] = { failReason, empNo, fileName };
		return this.getJdbcTemplate().update(sql, obj);
	}
	
	/**
	 * 黑名单中的人员并且邮箱为空的，更新邮件发送信息表
	 * 
	 * @param empNo
	 */
	@Override
	public int updateRcvBlackCode(Long resultSno, String failReason) {

		String sql = "UPDATE SFSC.FS_MAIL_SEND_INFO SET SEND_STATUS = '4', FAIL_REASON=?"
				+ " WHERE RESULT_SNO = ?";
		Object obj[] = { failReason, resultSno };
		return this.getJdbcTemplate().update(sql, obj);
	}

	/**
	 * 取得邮件发送信息
	 * 
	 * @param resultSno
	 */
	@Override
	public MailSendInfo getMailSendInfo(Long resultSno) {

		return this.getJdbcTemplate().queryForObject(
						"SELECT * FROM SFSC.FS_MAIL_SEND_INFO WHERE RESULT_SNO = ?",
						new Object[] { resultSno }, new MailSendInfo());
	}

	/**
	 * 取得雇员入职的年份
	 * 
	 * @param empNo
	 * @param companyNo
	 */
	@Override
	public String getCompanyJoinYear(String regNo, String companyNo) {

			return this.getJdbcTemplate()
					.queryForObject("select to_char(sfsc.get_company_begin_date_new(?,?),'YYYY') from dual",
							new Object[] { regNo, companyNo },
							java.lang.String.class);
	}
	
	/**
	 * 追加蔻驰加密邮件推送结果信息表中的记录
	 * 
	 * @param salaryInfo
	 * @param isYearBonusSend
	 * @param isSalarySend
	 * @param sendSuccess
	 * @param isSendAgain
	 */
	@Override
	public int insertSalaryExecuteInfo(Map<String, Object> salaryInfo, String isYearBonusSend,
			String isSalarySend, String isStockSend, String isReleasePaySend) {
		
		String sql="INSERT INTO SFSC.FS_SALARY_EXECUTE_RESULT(RESULT_SNO,EMP_NO,"
					+ "COMPANY_NO,BATCH_NO,ACC_YM,REAL_ACC_YM,IS_YEAR_BONUS_SEND,"
					+ "IS_SALARY_SEND,IS_STOCK_SEND,IS_RELEASE_PAY_SEND)"
					+ " VALUES(SFSC.SEQ_RESULT_SNO.NEXTVAL,?,?,?,?,?,?,?,?,?)";

		Object obj[]={ salaryInfo.get("EMP_NO"),
				salaryInfo.get("COMPANY_NO"),
				salaryInfo.get("BATCH_NO"),
				// ***** EPAY2.0 BEGIN *******
//				salaryInfo.get("ACC_YM"),
				salaryInfo.get("REAL_ACC_YM"),
				// ***** EPAY2.0 END *******
				salaryInfo.get("REAL_ACC_YM"),
				isYearBonusSend,
				isSalarySend,
				isStockSend,
				isReleasePaySend };

		return this.getJdbcTemplate().update(sql, obj);
	}
	
	// ***** No：S20151358 BEGIN *******
	/**
	  * 获得薪资期间的开始日期和结束日期
	  */
	@Override
	public Map<String, Object> getSalaryPeriod(String CompanyNo,String realAccYm) {
		String sql = "Select to_char(to_date(sfsc.pkg_payroll_function.payroll_begin_date(?, ?), 'yyyy/MM/dd'), 'yyyy/MM/dd') BEGIN_DATE,"
				+ " to_char(to_date(sfsc.pkg_payroll_function.payroll_end_date(?, ?), 'yyyy/MM/dd'), 'yyyy/MM/dd') END_DATE"
				+ " From DUAL";

		Object obj[]={ CompanyNo, realAccYm, CompanyNo, realAccYm };
		
		return this.getJdbcTemplate().queryForMap(sql, obj);
	}
	// ***** No：S20151358 END *******

}
