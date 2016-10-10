package com.salaryMail.service.impl;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.efesco.mailsvc.model.EmailBatchStatus;
import com.efesco.mailsvc.model.EmailModel;
import com.jcraft.jsch.JSchException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.salaryMail.common.Constants;
import com.salaryMail.dao.SalaryServerDao;
import com.salaryMail.entity.ClientConfigInfo;
import com.salaryMail.entity.ClientInfo;
import com.salaryMail.entity.SalaryBaseModel;
import com.salaryMail.entity.SalaryItemCAndPModel;
import com.salaryMail.entity.SalaryItemModel;
import com.salaryMail.entity.SalaryTitleBean;
import com.salaryMail.service.ArrowSalaryServerService;
import com.salaryMail.util.JSchChannel;
import com.salaryMail.util.SendEmail;

@Service("arrowSalaryServerService")
public class ArrowSalaryServerServiceImpl implements ArrowSalaryServerService {

	@Resource
	private SalaryServerDao salaryServerDao;

	public SalaryServerDao getSalaryServerDao() {
		return salaryServerDao;
	}

	public void setSalaryServerDao(SalaryServerDao salaryServerDao) {
		this.salaryServerDao = salaryServerDao;
	}

	private final Logger log = Logger.getLogger(ArrowSalaryServerServiceImpl.class);

	/**
	 * 艾睿电子发送工资单邮件
	 * 
	 * @throws Exception
	 */
	@Override
	public void sendMailForArrow() throws Exception {
		Date date = new Date();

		// 获取当天需要发送的雇员信息
		List<Map<String, Object>> salaryInfoList = getArrowSelectedEmployeeSalary();

		if (salaryInfoList.size() > 0) {
			// 艾睿电子数据整理
			List<SalaryBaseModel> dealSalaryList = dealSalaryForArrow(salaryInfoList);

			// 艾睿电子PDF文件生成
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			String path = System.getProperty("user.dir");
			String arrowLogoFilePath = path + "\\" + Constants.ARROW_LOGO;
			String arrowLineFilePath = path + "\\" + Constants.ARROW_LINE;
			downloadPicFile(Constants.PIC_DIRECTORY + Constants.ARROW_LOGO, arrowLogoFilePath);
			downloadPicFile(Constants.PIC_DIRECTORY + Constants.ARROW_LINE, arrowLineFilePath);

			for (int i = 0; i < salaryInfoList.size(); i++) {
				Map<String, Object> salaryInfo = salaryInfoList.get(i);
				String id = salaryInfo.get("ID").toString();
				String password = "";
				if (id.length() > 6) {
					password = id.substring(id.length() - 6, id.length());
				} else {
					password = id;
				}

				String filePath = writePdfForArrow(Constants.ARROW_FILE_NAME + "_"
						+ salaryInfo.get("EMP_NO").toString() + "_"
						// ***** EPAY2.0 BEGIN *******
//						+ salaryInfo.get("ACC_YM").toString() + "_"
						+ salaryInfo.get("REAL_ACC_YM").toString() + "_"
						// ***** EPAY2.0 END *******
						+ df.format(date), password, i, dealSalaryList, arrowLogoFilePath, arrowLineFilePath);

				// 上传文件
				// ***** EPAY2.0 BEGIN *******
//				String sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), filePath);
				String sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), filePath);
				// ***** EPAY2.0 END *******
				salaryInfo.put("FILE_PATH", sendFilePath);

				File file = new File(filePath);
				file.delete();
			}

			File arrowLogoFile = new File(arrowLogoFilePath);
			arrowLogoFile.delete();
			File arrowLineFile = new File(arrowLineFilePath);
			arrowLineFile.delete();

			// 追加文件上传信息表中的记录
			insertMailResultInfo(salaryInfoList,Constants.ARROW_MAIL_TITLE);

			// 发送邮件
			EmailBatchStatus result = SendEmail.sendEmailForArrow(salaryInfoList);

			// 更新文件上传信息表中的记录
			updateMailResultInfo(salaryInfoList, result);
		} else {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			log.info(df.format(date) + " 艾睿电子不需要发送加密工资单邮件");
		}
	}

	/**
	 * 处理薪酬数据
	 * 
	 * @param values
	 * @return List<SalaryBaseModel>
	 * @throws Exception
	 */
	private List<SalaryBaseModel> dealSalaryForArrow(List<Map<String, Object>> values)
			throws Exception {
		List<SalaryBaseModel> result = new ArrayList<SalaryBaseModel>();
		Map<String, Integer> order = new HashMap<String, Integer>();

		order.put("67", 9);// wf_salary.DISPLAY8 as "Base Salary"
		order.put("1", 10);// wf_salary.BASIC_SALARY as "This Month Salary"
		order.put("2", 11);// wf_salary.MONTH_BONUS as "Last Month Back Pay"
		order.put("3", 12);// wf_salary.OVERTIME_SALARY as "Housing Allowance"
		order.put("4", 13);// wf_salary.TRAFFIC_ALLOWANCE as
							// "High Temperature Allowance"
		order.put("5", 14);// wf_salary.LUNCH_ALLOWANCE as "Incentive"
		order.put("11", 15);// wf_salary.ALLOWANCE1 as "G&A Bonus"
		order.put("12", 16);// wf_salary.ALLOWANCE2 as "Backpay Salary"
		order.put("13", 17);// wf_salary.ALLOWANCE3 as "Referal Award"
		order.put("14", 18);// wf_salary.ALLOWANCE4 as "Bonus from Supplier"
		order.put("15", 19);// wf_salary.ALLOWANCE5 as "Overtime Payment"
		order.put("51", 20);// wf_salary.ALLOWANCE6 as "Annual Leave Pay"
		order.put("52", 21);// wf_salary.ALLOWANCE7 as
							// "Wages in lieu of notice (Taxable)"
		order.put("53", 22);// wf_salary.ALLOWANCE8 as "Others additional"
		order.put("54", 23);// wf_salary.ALLOWANCE9 as "Daily Wages"
		order.put("55", 24);// wf_salary.ALLOWANCE10 as "Unity Retention Bonus"
		order.put("111", 25);// wf_salary.ALLOWANCE11 as "Trainee Bonus"
		order.put("112", 26);// wf_salary.ALLOWANCE12 as "Other Bonus"
		order.put("113", 27);// wf_salary.ALLOWANCE13 as "Salary Adjustment"
		order.put("114", 28);// wf_salary.ALLOWANCE14 as "Maternity Allowance"
		order.put("115", 29);// wf_salary.ALLOWANCE15 as "Other Allowance"
		order.put("116", 30);// wf_salary.ALLOWANCE16 as "Other Adjustment"
		order.put("117", 31);// wf_salary.ALLOWANCE17 as "No Pay Leave"
		order.put("118", 32);// wf_salary.ALLOWANCE18 as "Sick Leave"
		order.put("119", 33);// wf_salary.ALLOWANCE19 as "Other Deduction"

		// 第二段
		order.put("120", 35);// wf_salary.ALLOWANCE20 as "Pension-Individual"
		order.put("121", 36);// wf_salary.ALLOWANCE21 as "Medical-Individual"
		order.put("122", 37);// wf_salary.ALLOWANCE22 as
								// "Unemployement-Individual"
		order.put("123", 38);// wf_salary.ALLOWANCE23 as
								// "Housing Fund-Individual"
		order.put("102", 39);// wf_salary.DISPLAY12 as "Housing Fund Taxable"
		order.put("Tax Free", 40);
		order.put("31", 41);// wf_salary.FREERCV_ALLOWANCE1 as "Taiping Premium"
		order.put("Basic Tax", 42);// wf_salary.tax as "Basic Tax"
		order.put("68", 43);// wf_salary.DISPLAY9 as "Severance Payment"
		order.put("69", 44);// wf_salary.DISPLAY10 as "Severance Payment Tax"
		order.put("16", 45);// wf_salary.FREE_TAX_ALLOWANCE1 as
							// "One-Child Allowance"
		order.put("Special Payment", 46);
		order.put("Year-end Bonus", 47);
		order.put("Year-End Bonus Tax", 48);
		order.put("21", 49);// wf_salary.DEDUCT1 as "Stock IIT"
		order.put("17", 50);// wf_salary.FREE_TAX_ALLOWANCE2 as "IIT Adjustment"
		order.put("Labor Tax", 51);
		order.put("101", 52);// wf_salary.DISPLAY11 as "Taiping Insurance Tax"
		order.put("Total IIT", 53);
		order.put("Month End Pay", 54);

		// 第三段
		order.put("Pension-Company", 55);// wf_salary.oep_c as "Pension-Company"
		order.put("Medical-Company", 56);// wf_salary.medical_c as
											// Medical-Company"
		order.put("Unemployment-Company", 57);// wf_salary.lose_job_c as
												// nemployment-Company"
		order.put("Housing Fund-Company", 58);// wf_salary.ALLOWANCE23 as
												// "Housing Fund-Company"
		order.put("Work Injury-Company", 59);// wf_salary.injury_insu
												// as"Work Injury-Company"
		order.put("Maternity-Company", 60);// wf_salary.childbirth_insu as
											// "Maternity-Company"

		// 整理数据
		for (Map<String, Object> t : values) {
			List<SalaryTitleBean> titles = (List<SalaryTitleBean>) t.get("TITLE_BEAN");
			List<SalaryTitleBean> dealTitles = new ArrayList<SalaryTitleBean>();
			// 追加14个bean
			// order.put("Tax Free", 40);
			SalaryTitleBean b01 = new SalaryTitleBean();
			b01.setAddFlag(1);
			b01.setFieldname("");
			b01.setItemCode("Tax Free");
			b01.setItemName("Tax Free");
			b01.setItemNameEn("Tax Free");
			titles.add(b01);
			// order.put("Basic Tax", 42); // wf_salary.tax as "Basic Tax"
			SalaryTitleBean b02 = new SalaryTitleBean();
			b02.setAddFlag(-1);
			b02.setFieldname("");
			b02.setItemCode("Basic Tax");
			b02.setItemName("Basic Tax");
			b02.setItemNameEn("Basic Tax");
			titles.add(b02);
			// order.put("Special Payment", 46);
			SalaryTitleBean b03 = new SalaryTitleBean();
			b03.setAddFlag(1);
			b03.setFieldname("");
			b03.setItemCode("Special Payment");
			b03.setItemName("Special Payment");
			b03.setItemNameEn("Special Payment");
			titles.add(b03);
			// order.put("Year-end Bonus", 47);
			SalaryTitleBean b04 = new SalaryTitleBean();
			b04.setAddFlag(1);
			b04.setFieldname("");
			b04.setItemCode("Year-end Bonus");
			b04.setItemName("Year-end Bonus");
			b04.setItemNameEn("Year-end Bonus");
			titles.add(b04);
			// order.put("Year-End Bonus Tax", 48);
			SalaryTitleBean b05 = new SalaryTitleBean();
			b05.setAddFlag(-1);
			b05.setFieldname("");
			b05.setItemCode("Year-End Bonus Tax");
			b05.setItemName("Year-End Bonus Tax");
			b05.setItemNameEn("Year-End Bonus Tax");
			titles.add(b05);
			// order.put("Labor Tax", 51);
			SalaryTitleBean b06 = new SalaryTitleBean();
			b06.setAddFlag(1);
			b06.setFieldname("");
			b06.setItemCode("Labor Tax");
			b06.setItemName("Labor Tax");
			b06.setItemNameEn("Labor Tax");
			titles.add(b06);
			// order.put("Total IIT", 53);
			SalaryTitleBean b07 = new SalaryTitleBean();
			b07.setAddFlag(-1);
			b07.setFieldname("");
			b07.setItemCode("Total IIT");
			b07.setItemName("Total IIT");
			b07.setItemNameEn("Total IIT");
			titles.add(b07);
			// order.put("Month End Pay", 54);
			SalaryTitleBean b08 = new SalaryTitleBean();
			b08.setAddFlag(1);
			b08.setFieldname("");
			b08.setItemCode("Month End Pay");
			b08.setItemName("Month End Pay");
			b08.setItemNameEn("Month End Pay");
			titles.add(b08);
			// order.put("Pension-Company", 55);
			SalaryTitleBean b09 = new SalaryTitleBean();
			b09.setAddFlag(1);
			b09.setFieldname("");
			b09.setItemCode("Pension-Company");
			b09.setItemName("Pension-Company");
			b09.setItemNameEn("Pension-Company");
			titles.add(b09);
			// order.put("Medical-Company", 56);
			SalaryTitleBean b10 = new SalaryTitleBean();
			b10.setAddFlag(1);
			b10.setFieldname("");
			b10.setItemCode("Medical-Company");
			b10.setItemName("Medical-Company");
			b10.setItemNameEn("Medical-Company");
			titles.add(b10);
			// order.put("Unemployment-Company", 57);
			SalaryTitleBean b11 = new SalaryTitleBean();
			b11.setAddFlag(1);
			b11.setFieldname("");
			b11.setItemCode("Unemployment-Company");
			b11.setItemName("Unemployment-Company");
			b11.setItemNameEn("Unemployment-Company");
			titles.add(b11);
			// order.put("Work Injury-Company", 58);
			SalaryTitleBean b12 = new SalaryTitleBean();
			b12.setAddFlag(1);
			b12.setFieldname("");
			b12.setItemCode("Work Injury-Company");
			b12.setItemName("Work Injury-Company");
			b12.setItemNameEn("Work Injury-Company");
			titles.add(b12);
			// order.put("Maternity-Company", 59);
			SalaryTitleBean b13 = new SalaryTitleBean();
			b13.setAddFlag(1);
			b13.setFieldname("");
			b13.setItemCode("Maternity-Company");
			b13.setItemName("Maternity-Company");
			b13.setItemNameEn("Maternity-Company");
			titles.add(b13);
			// order.put("Housing Fund-Company", 60); // wf_salary.ALLOWANCE23
			// as "Housing Fund-Company"
			SalaryTitleBean b14 = new SalaryTitleBean();
			b14.setAddFlag(1);
			b14.setFieldname("");
			b14.setItemCode("Housing Fund-Company");
			b14.setItemName("Housing Fund-Company");
			b14.setItemNameEn("Housing Fund-Company");
			titles.add(b14);

			// 排序
			for (SalaryTitleBean tb : titles) {
				// 检索出map中需要的值
				if (order.get(tb.getItemCode()) != null) {
					tb.setDisplaycode(order.get(tb.getItemCode()));
					dealTitles.add(tb);
				}
			}
			Collections.sort(dealTitles, new Comparator<SalaryTitleBean>() {
				public int compare(SalaryTitleBean arg0, SalaryTitleBean arg1) {
					return arg0.getDisplaycode().compareTo(arg1.getDisplaycode());
				}
			});

			SalaryBaseModel r = new SalaryBaseModel();
			// 基本信息
			r.setPayDay(t.get("Pay Day") == null ? "" : t.get("Pay Day").toString());
			r.setName(t.get("Chinese Name") == null ? "" : t.get("Chinese Name").toString());
			r.setGlobalId(t.get("Global ID") == null ? "" : t.get("Global ID").toString());
			r.setLegalEntity(t.get("Legal Entity(Company Code)") == null ? "" : t.get(
					"Legal Entity(Company Code)").toString());
			r.setLocation(t.get("Location") == null ? "" : t.get("Location").toString());
			r.setDepartment(t.get("Department") == null ? "" : t.get("Department").toString());
			r.setJoinDate(t.get("Join Date") == null ? "" : t.get("Join Date").toString());
			r.setBaseSalary(Double.valueOf(t.get("Base Salary") == null ? "0.00" : t.get(
					"Base Salary").toString()));
			r.setMail(t.get("mail") == null ? "" : t.get("mail").toString());
			r.setId(t.get("id") == null ? "" : t.get("id").toString());
			// 设置周期
			String salaryPeriod = t.get("Salary Period").toString();

			List<SalaryItemModel> firstDatas = new ArrayList<SalaryItemModel>();
			// 整理第一段数据并计算34(Gross Salary)
			Double gs = 0.00;
			for (int i = 1; i <= 24; i++) {
				SalaryTitleBean tb = dealTitles.get(i);
				// System.out.println(i + "-----------" + tb.getItemName());
				if (tb.getItemName().startsWith("Wages in lieu of notice")) {
					tb.setItemName("Wages in lieu of notice");
				}
				SalaryItemModel im = new SalaryItemModel();
				if (tb.getItemName().startsWith("Wages in lieu of notice")) {
					im.setItemTitle("Wages in lieu of notice (Taxable)");
				} else {
					im.setItemTitle(tb.getItemName());
				}
				im.setItemPeriod(salaryPeriod);
				if (t.get(tb.getItemName()) != null) {
					Double val = Double.valueOf(t.get(tb.getItemName()).toString());
					if (tb.getAddFlag() == -1) {
						val = val * (-1);
					} else {

					}
					im.setItemValue(val);
					gs = gs + val;
				} else {
					im.setItemValue(0.00);
				}
				firstDatas.add(im);
			}
			SalaryItemModel gsim = new SalaryItemModel();
			gsim.setAlign(Element.ALIGN_CENTER);
			gsim.setItemTitle("Gross Salary");
			gsim.setItemPeriod(salaryPeriod);
			gsim.setItemValue(gs);
			gsim.setTitleCol(true);
			gsim.setValueCol(false);
			firstDatas.add(gsim);
			// System.out.println("----------------------------------------------");
			List<SalaryItemModel> secondDatas = new ArrayList<SalaryItemModel>();
			for (int i = 0; i <= 19; i++) {
				int j = i + 25;
				SalaryTitleBean tb = dealTitles.get(j);
				// System.out.println(j + "-----------" + tb.getItemName());
				SalaryItemModel im = new SalaryItemModel();
				im.setItemTitle(tb.getItemName());
				im.setItemPeriod(salaryPeriod);
				if (t.get(tb.getItemName()) != null) {
					Double val = Double.valueOf(t.get(tb.getItemName()).toString());
					if (tb.getAddFlag() == -1) {
						val = val * (-1);
					} else {

					}
					im.setItemValue(val);
				} else {
					im.setItemValue(0.00);
				}
				im.setTitleCol(true);
				secondDatas.add(im);
			}
			// System.out.println("----------------------------------------------");

			List<SalaryItemCAndPModel> thirdDatas = new ArrayList<SalaryItemCAndPModel>();
			// Individual
			for (int i = 0; i <= 5; i++) {
				int j = i + 45;
				SalaryTitleBean tb = dealTitles.get(j);
				// System.out.println(j + "-----------" + tb.getItemName());
				SalaryItemCAndPModel im = new SalaryItemCAndPModel();
				im.setItemTitle(tb.getItemName().split("-")[0]);
				im.setItemPeriod(salaryPeriod);
				if (t.get(tb.getItemName()) != null) {
					im.setItemValueC(Double.valueOf(t.get(tb.getItemName()).toString()));
				} else {
					im.setItemValueC(0.00);
				}

				if (tb.getItemCode().equals("Pension-Company")) {
					for (SalaryTitleBean ts : dealTitles) {
						if (ts.getItemCode().equals("120")) {
							if (t.get(ts.getItemName()) != null) {
								im.setItemValueP(Double.valueOf(t.get(ts.getItemName()).toString()));
							} else {
								im.setItemValueP(0.00);
							}
						}
					}
				}
				if (tb.getItemCode().equals("Medical-Company")) {
					for (SalaryTitleBean ts : dealTitles) {
						if (ts.getItemCode().equals("121")) {
							if (t.get(ts.getItemName()) != null) {
								im.setItemValueP(Double.valueOf(t.get(ts.getItemName()).toString()));
							} else {
								im.setItemValueP(0.00);
							}
						}
					}
				}
				if (tb.getItemCode().equals("Unemployment-Company")) {
					for (SalaryTitleBean ts : dealTitles) {
						if (ts.getItemCode().equals("122")) {
							if (t.get(ts.getItemName()) != null) {
								im.setItemValueP(Double.valueOf(t.get(ts.getItemName()).toString()));
							} else {
								im.setItemValueP(0.00);
							}
						}
					}
				}
				if (tb.getItemCode().equals("Work Injury-Company")) {
					for (SalaryTitleBean ts : dealTitles) {
						im.setItemValueP(0.00);
					}
				}
				if (tb.getItemCode().equals("Maternity-Company")) {
					for (SalaryTitleBean ts : dealTitles) {
						im.setItemValueP(0.00);
					}
				}
				if (tb.getItemCode().equals("Housing Fund-Company")) {
					for (SalaryTitleBean ts : dealTitles) {
						if (ts.getItemCode().equals("123")) {
							if (t.get(ts.getItemName()) != null) {
								im.setItemValueP(Double.valueOf(t.get(ts.getItemName()).toString()));
							} else {
								im.setItemValueP(0.00);
							}
						}
					}
				}

				thirdDatas.add(im);
			}
			// System.out.println("----------------------------------------------");
			r.setFirstDatas(firstDatas);
			r.setSecondDatas(secondDatas);
			r.setThirdDatas(thirdDatas);
			result.add(r);
		}
		return result;
	}

	/**
	 * 获取薪酬名称
	 * 
	 * @param companyNo
	 * @param accym
	 * @return List<SalaryTitleBean>
	 */
	// ***** EPAY2.0 BEGIN *******
//	private List<SalaryTitleBean> findSalaryTitleForArrow(String companyNo, String accym)
//			throws Exception {
//		return salaryServerDao.findSalaryTitle(companyNo, accym);
//	}
	private List<SalaryTitleBean> findSalaryTitleForArrow(String companyNo, String realAccYm)
			throws Exception {
		return salaryServerDao.findSalaryTitle(companyNo, realAccYm);
	}
	// ***** EPAY2.0 END *******

	/**
	 * 获取薪酬数据
	 * 
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	private List<Map<String, Object>> getArrowSelectedEmployeeSalary() throws SQLException {

		// DynamicDataSource ds = new DynamicDataSource();
		// Connection conn = null;
		List<Map<String, Object>> selectedSalaryInfoList = new ArrayList<Map<String, Object>>();

		try {
			// 获得艾睿电子的商社代码
			String[] arrowCompanyNo = Constants.ARROW_GROUP.split(",");
			// String arrowSfscCode = Constants.ARROW_SFSC_CODE;

			// 获得艾睿电子的客户配置信息
			ClientConfigInfo clientConfigInfo = salaryServerDao
					.getClientConfigInfoByCompanyNo(arrowCompanyNo[0]);

			// // 获得艾睿电子的数据库信息
			// SwitchDatabaseInfo databaseInfo = salaryServerDao
			// .getSwitchDatabaseInfo(clientConfigInfo.getDbSno());
			//
			// String url = "jdbc:oracle:thin:@ " + databaseInfo.getDbIp()
			// + ":" + databaseInfo.getIpPort() + ":"
			// + databaseInfo.getDbSid();
			//
			// // 获取base64密钥
			// CodeDetail base64CodeDetail = salaryServerDao.getCodeDetail(
			// Constants.CODE_DEFINE_KEY, Constants.CODE_CLASS_BASE64);
			// String databasePwd = Base64Util.decrypt(
			// databaseInfo.getPasswd(),
			// base64CodeDetail.getCodeDesc1Content());
			//
			// BasicDataSource baseDataSource = ds.createDataSource(
			// "oracle.jdbc.driver.OracleDriver", url,
			// databaseInfo.getUserName(), databasePwd);
			// conn = baseDataSource.getConnection();

			for (int m = 0; m < arrowCompanyNo.length; m++) {
				String companyNo = arrowCompanyNo[m];

				// 获得艾睿电子的基本信息
				ClientInfo clientInfo = salaryServerDao.getClientInfoByCompanyNo(companyNo);

				// 查询取得雇员工资表中的雇员工资数据
				List<Map<String, Object>> salaryInfoList = salaryServerDao.getSalaryInfoForArrow(
						companyNo, clientConfigInfo.getSendBeginYm(),
						clientConfigInfo.getSendDateType());

				for (int i = 0; i < salaryInfoList.size(); i++) {
					Map<String, Object> salaryMap = salaryInfoList.get(i);

					// ***** EPAY2.0 BEGIN *******
//					List<SalaryTitleBean> titleList = this.findSalaryTitleForArrow(
//							salaryMap.get("COMPANY_NO").toString(), salaryMap.get("ACC_YM").toString());
					List<SalaryTitleBean> titleList = this.findSalaryTitleForArrow(
							salaryMap.get("COMPANY_NO").toString(), salaryMap.get("REAL_ACC_YM").toString());
					// ***** EPAY2.0 END *******
					//
					// Map<String, String> paramMap = new HashMap<String,
					// String>();
					// paramMap.put("si_company_no", companyNo);
					// paramMap.put("si_emp_no", salaryMap.get("EMP_NO")
					// .toString());
					// paramMap.put("si_acc_ym", salaryMap.get("ACC_YM")
					// .toString());
					// paramMap.put("si_send_date_type",
					// clientConfigInfo.getSendDateType());
					//
					// if ("1".equals(salaryServerDao.checkEmployee(paramMap)))
					// {
					// 商社名称
					salaryMap.put("COMPANY_NAME", clientInfo.getCompanyName());
					// 客户组
					salaryMap.put("COMP_GRP_CODE", clientInfo.getCompGrpCode());
//					// 销售组
//					salaryMap.put("SALE_GRP_CODE", clientInfo.getSaleGrpCode());
					// 所属业务部
					salaryMap.put("BELONG_TO_DEPT", clientInfo.getBelongToDept());
					// 业务部名称
					salaryMap.put("DEPT_NAME", clientInfo.getDeptName());
					// 销售组长编号
					salaryMap.put("USER_ID", clientInfo.getUserId());
					// 销售组长姓名
					salaryMap.put("USER_NAME", clientInfo.getUserName());
					// // 账套
					// salaryMap.put("SFSC_CODE", clientInfo.getSfscCode());
					// 发送日期类型
					salaryMap.put("SEND_DATE_TYPE", clientConfigInfo.getSendDateType());

					salaryMap.put("TITLE_BEAN", titleList);

					selectedSalaryInfoList.add(salaryMap);
				}
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finally {
		// if (conn != null)
		// conn.close();
		// }

		return selectedSalaryInfoList;
	}

	/**
	 * 生成PDF
	 * 
	 * @param fileName
	 * @param pwd
	 * @param m
	 * @param dealSalary
	 * @param arrowLogoFile
	 * @param arrowLineFile
	 * @return String
	 */
	private String writePdfForArrow(String fileName, String pwd, int m,
			List<SalaryBaseModel> dealSalary, String arrowLogoFile, String arrowLineFile)
			throws Exception {

		// 新建document对象
		// 第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。
		Document document = new Document(PageSize.A4, 50, 50, 8, 0);

		String path = System.getProperty("user.dir");
		String filePath = path + "\\" + fileName + ".pdf";
		FileOutputStream fo = new FileOutputStream(filePath);

		// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
		// 创建 PdfWriter 对象：第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
		PdfWriter writer = PdfWriter.getInstance(document, fo);
		writer.setEncryption(pwd.getBytes(), pwd.getBytes(), PdfWriter.AllowPrinting,
				PdfWriter.STRENGTH128BITS);

		document.open();

		// 空白格子
		Cell cellBlank = new Cell("");
		cellBlank.setBackgroundColor(Constants.WHITE);
		cellBlank.setBorder(0);

		// logoTable
		Table logoTable = new Table(1, 1);
		logoTable.setBorderWidth(0);
		logoTable.setBorder(0);
		logoTable.setBorderColor(Color.WHITE);
		// String path = System.getProperty("user.home");
		// String imgLogo = path + "../../jpg/ArrowLogo.jpg";
		Image image = writeImg(arrowLogoFile, 30);

		Cell celllogo1 = new Cell(image);
		celllogo1.setHorizontalAlignment(Element.ALIGN_LEFT);
		celllogo1.setBorder(0);
		logoTable.addCell(celllogo1);
		document.add(logoTable);

		// Pay Slip Pay Day
		Paragraph graphInfo = new Paragraph();
		Table table = new Table(5, 1);
		table.setBorderWidth(0);
		table.setBorderColor(Color.WHITE);
		table.setAutoFillEmptyCells(true);

		Chunk chunkt1 = convertChunkByChinese("Pay Slip", 12, Constants.BOLD, Constants.BLACK);
		Cell cellt1 = new Cell(chunkt1);
		cellt1.setBackgroundColor(Constants.WHITE);
		cellt1.setBorder(0);
		table.addCell(cellt1, new Point(0, 0));
		table.addCell(cellBlank, new Point(0, 1));
		table.addCell(cellBlank, new Point(0, 2));

		Chunk chunkt2 = convertChunkByChinese("Pay Day:", 12, Constants.BOLD, Constants.BLACK);
		Cell cellt2 = new Cell(chunkt2);
		cellt2.setBackgroundColor(Constants.WHITE);
		cellt2.setBorder(0);
		table.addCell(cellt2, new Point(0, 3));

		Chunk chunkt3 = convertChunkByChinese(dealSalary.get(m).getPayDay(), 12, Constants.BOLD,
				Constants.BLACK);
		Cell cellt3 = new Cell(chunkt3);
		cellt3.setBackgroundColor(Constants.WHITE);
		cellt3.setBorder(0);
		table.addCell(cellt3, new Point(0, 4));
		graphInfo.add(table);
		graphInfo.setSpacingBefore(-15);
		document.add(graphInfo);

		// 基础信息
		Paragraph graphBaseInfo = new Paragraph();
		Table aTable = new Table(5, 7);
		aTable.setBorderWidth(0);
		aTable.setBorder(0);
		aTable.setBorderColor(Color.WHITE);
		aTable.setAutoFillEmptyCells(true);

		// String imgPathII = "../jpg/line1.jpg";
		Image imageLine = writeImg(arrowLineFile, 100);

		Cell cellimageLine = new Cell(imageLine);
		cellimageLine.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellimageLine.setBorder(0);
		cellimageLine.setColspan(5);
		aTable.addCell(cellimageLine, new Point(0, 0));

		// 第一行
		Chunk chunk11 = convertChunkByChinese("Chinese Name:", 12, 0, Constants.BLACK);
		Cell cellNameTitle = new Cell(chunk11);
		cellNameTitle.setBackgroundColor(Constants.WHITE);
		cellNameTitle.setBorder(0);
		aTable.addCell(cellNameTitle, new Point(1, 0));
		Chunk chunk12 = convertChunkByChinese(dealSalary.get(m).getName(), 12, 0, Constants.BLACK);
		Cell cellName = new Cell(chunk12);
		cellName.setBackgroundColor(Constants.WHITE);
		cellName.setBorder(0);
		aTable.addCell(cellName, new Point(1, 1));
		aTable.addCell(cellBlank, new Point(1, 2));
		Chunk chunk13 = convertChunkByChinese("Global ID", 12, 0, Constants.BLACK);
		Cell cellIdTitle = new Cell(chunk13);
		cellIdTitle.setBackgroundColor(Constants.WHITE);
		cellIdTitle.setBorder(0);
		aTable.addCell(cellIdTitle, new Point(1, 3));
		Chunk chunk14 = convertChunkByChinese(dealSalary.get(m).getGlobalId(), 12, 0,
				Constants.BLACK);
		Cell cellId = new Cell(chunk14);
		cellId.setBackgroundColor(Constants.WHITE);
		cellId.setBorder(0);
		aTable.addCell(cellId, new Point(1, 4));

		// 第二行
		Chunk chunk21 = convertChunkByChinese("Legal Entity:", 12, 0, Constants.BLACK);
		Cell cellLegalTitle = new Cell(chunk21);
		cellLegalTitle.setBackgroundColor(Constants.WHITE);
		cellLegalTitle.setBorder(0);
		aTable.addCell(cellLegalTitle, new Point(2, 0));
		Chunk chunk22 = convertChunkByChinese(dealSalary.get(m).getLegalEntity(), 12, 0,
				Constants.BLACK);
		Cell cellLegal = new Cell(chunk22);
		cellLegal.setBackgroundColor(Constants.WHITE);
		cellLegal.setBorder(0);
		aTable.addCell(cellLegal, new Point(2, 1));
		aTable.addCell(cellBlank, new Point(2, 2));
		Chunk chunk23 = convertChunkByChinese("Location：", 12, 0, Constants.BLACK);
		Cell cellLocationTitle = new Cell(chunk23);
		cellLocationTitle.setBackgroundColor(Constants.WHITE);
		cellLocationTitle.setBorder(0);
		aTable.addCell(cellLocationTitle, new Point(2, 3));
		Chunk chunk24 = convertChunkByChinese(dealSalary.get(m).getLocation(), 12, 0,
				Constants.BLACK);
		Cell cellLocation = new Cell(chunk24);
		cellLocation.setBackgroundColor(Constants.WHITE);
		cellLocation.setBorder(0);
		aTable.addCell(cellLocation, new Point(2, 4));

		// 第三行
		Chunk chunk31 = convertChunkByChinese("Department:", 12, 0, Constants.BLACK);
		Cell cellDepartTitle = new Cell(chunk31);
		cellDepartTitle.setBackgroundColor(Constants.WHITE);
		cellDepartTitle.setBorder(0);
		aTable.addCell(cellDepartTitle, new Point(3, 0));
		Chunk chunk32 = convertChunkByChinese(dealSalary.get(m).getDepartment(), 12, 0,
				Constants.BLACK);
		Cell cellDepart = new Cell(chunk32);
		cellDepart.setBackgroundColor(Constants.WHITE);
		cellDepart.setBorder(0);
		cellDepart.setColspan(2);
		aTable.addCell(cellDepart, new Point(3, 1));
		aTable.addCell(cellBlank, new Point(3, 3));
		aTable.addCell(cellBlank, new Point(3, 4));

		// 第四行
		Chunk chunk41 = convertChunkByChinese("Join Date:", 12, 0, Constants.BLACK);
		Cell cellJoinDateTitle = new Cell(chunk41);
		cellJoinDateTitle.setBackgroundColor(Constants.WHITE);
		cellJoinDateTitle.setBorder(0);
		aTable.addCell(cellJoinDateTitle, new Point(4, 0));
		Chunk chunk42 = convertChunkByChinese(dealSalary.get(m).getJoinDate(), 12, 0,
				Constants.BLACK);
		Cell cellJoinDate = new Cell(chunk42);
		cellJoinDate.setBackgroundColor(Constants.WHITE);
		cellJoinDate.setBorder(0);
		aTable.addCell(cellJoinDate, new Point(4, 1));
		aTable.addCell(cellBlank, new Point(4, 2));
		aTable.addCell(cellBlank, new Point(4, 3));
		aTable.addCell(cellBlank, new Point(4, 4));

		// 第五行
		Chunk chunk51 = convertChunkByChinese("Base Salary:", 12, 0, Constants.BLACK);
		Cell cellBaseSalaryTitle = new Cell(chunk51);
		cellBaseSalaryTitle.setBackgroundColor(Constants.WHITE);
		cellBaseSalaryTitle.setBorder(0);
		aTable.addCell(cellBaseSalaryTitle, new Point(5, 0));
		Chunk chunk52 = convertChunkByChinese(String.valueOf(dealSalary.get(m).getBaseSalary()),
				12, 0, Constants.BLACK);
		Cell cellBaseSalary = new Cell(chunk52);
		cellBaseSalary.setBackgroundColor(Constants.WHITE);
		cellBaseSalary.setBorder(0);
		aTable.addCell(cellBaseSalary, new Point(5, 1));
		aTable.addCell(cellBlank, new Point(5, 2));
		aTable.addCell(cellBlank, new Point(5, 3));
		aTable.addCell(cellBlank, new Point(5, 4));

		Cell cellimageLine2 = new Cell(imageLine);
		cellimageLine2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellimageLine2.setBorder(0);
		cellimageLine2.setColspan(5);
		aTable.addCell(cellimageLine2, new Point(6, 0));

		graphBaseInfo.add(aTable);
		graphBaseInfo.setSpacingBefore(-10);
		document.add(graphBaseInfo);

		// Payroll Details
		List<SalaryItemModel> result = new ArrayList<SalaryItemModel>();
		List<SalaryItemModel> resultTemp = dealSalary.get(m).getFirstDatas();
		for (int i = 0; i < resultTemp.size(); i++) {
			if (resultTemp.get(i).getItemValue() != 0.00
					&& resultTemp.get(i).getItemValue() != -0.00) {
				result.add(resultTemp.get(i));
			}
		}

		Paragraph graphDetailsInfo = new Paragraph();
		// System.out.println(result.size() + "测试中间循环数据的条数！");
		Table bTable = new Table(4);
		bTable.setBorder(0);
		bTable.setBorderColor(Constants.WHITE);
		// bTable.setBorderColor(Constants.BLACK);
		// bTable.setBorder(0);
		bTable.setCellspacing(2);
		bTable.setWidths(new float[] { 35f, 30f, 15f, 15f });
		bTable.setAutoFillEmptyCells(true);
		int j = 0;
		// 标题
		Chunk chunkTableTitle = convertChunkByChinese("Payroll Details", 12, Constants.BOLD,
				Constants.BLACK);
		Cell cellTableTitle = new Cell(chunkTableTitle);
		cellTableTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellTableTitle.setColspan(4);
		cellTableTitle.setBorder(0);
		bTable.addCell(cellTableTitle, new Point(j, 0));
		++j;

		// 表格标题
		Chunk chunkTableTitle001 = convertChunkByChinese("Salary Items", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle001 = new Cell(chunkTableTitle001);
		cellTableTitle001.setBackgroundColor(Constants.BLUE);
		// cellTableTitle001.setBorder(1);
		bTable.addCell(cellTableTitle001, new Point(j, 0));

		Chunk chunkTableTitle002 = convertChunkByChinese("Salary Period", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle002 = new Cell(chunkTableTitle002);
		cellTableTitle002.setBackgroundColor(Constants.BLUE);
		// cellTableTitle002.setBorder(1);
		bTable.addCell(cellTableTitle002, new Point(j, 1));

		Chunk chunkTableTitle003 = convertChunkByChinese("Earning +", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle003 = new Cell(chunkTableTitle003);
		cellTableTitle003.setBackgroundColor(Constants.BLUE);
		// cellTableTitle003.setBorder(1);
		bTable.addCell(cellTableTitle003, new Point(j, 2));

		Chunk chunkTableTitle004 = convertChunkByChinese("Deduction -", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle004 = new Cell(chunkTableTitle004);
		cellTableTitle004.setBackgroundColor(Constants.BLUE);
		// cellTableTitle004.setBorder(1);
		bTable.addCell(cellTableTitle004, new Point(j, 3));
		Double sumAdd = 0.00;
		Double sumMin = 0.00;
		DecimalFormat df = new DecimalFormat("0.00");
		++j;

		for (int i = 0; i < result.size(); i++) {
			initSalaryData(bTable, result.get(i), j);
			j = j + 1;
			if (result.get(i).getItemValue() != null && 0.00 != result.get(i).getItemValue()) {
				if (result.get(i).getItemValue() > 0) {
					sumAdd = sumAdd + result.get(i).getItemValue();
					sumAdd = Double.valueOf(df.format(sumAdd));
				} else {
					sumMin = sumMin + result.get(i).getItemValue();
					sumMin = Double.valueOf(df.format(sumMin));
				}
			}
		}

		// j后行(第一行为统计行)
		/*
		 * Chunk chunkSubTitle = convertChunkByChinese("Subtotal", 10,
		 * Constants.NORMAL, Constants.BLACK); Cell cellSubTitle = new
		 * Cell(chunkSubTitle); cellSubTitle.setColspan(2);
		 * cellSubTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
		 * bTable.addCell(cellSubTitle, new Point(j, 0));
		 * 
		 * Chunk chunkSubAdd = convertChunkByChinese(String.valueOf(sumAdd), 10,
		 * Constants.NORMAL, Constants.BLACK); Cell cellSubAdd = new
		 * Cell(chunkSubAdd);
		 * cellSubAdd.setHorizontalAlignment(Element.ALIGN_RIGHT);
		 * bTable.addCell(cellSubAdd, new Point(j, 2));
		 * 
		 * Chunk chunkSubMin = convertChunkByChinese(String.valueOf(sumMin), 10,
		 * Constants.NORMAL, Constants.BLACK); Cell cellSubMin = new
		 * Cell(chunkSubMin);
		 * cellSubMin.setHorizontalAlignment(Element.ALIGN_RIGHT);
		 * bTable.addCell(cellSubMin, new Point(j, 3)); ++j;
		 */

		// 处理第二块工资
		// 第一行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Gross Salary")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Gross Salary", 10, Constants.BOLD,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第2行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Taiping Premium")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese(
						"Tax adjustment-Taiping Medical Insurance Premium", 10, Constants.NORMAL,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第3行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Housing Fund Taxable")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Tax Adjustment-Housing Fund Taxable",
						10, Constants.NORMAL, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第4行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Pension-Individual")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Pension-Individual", 10,
						Constants.BOLD, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第5行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Medical-Individual")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Medical-Individual", 10,
						Constants.BOLD, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第6行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Unemployement-Individual")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Unemployement-Individual", 10,
						Constants.BOLD, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第7行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Housing Fund-Individual")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Housing Fund-Individual", 10,
						Constants.BOLD, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第8行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Basic Tax")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Basic Tax", 10, Constants.NORMAL,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第9行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Taiping Insurance Tax")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese(
						"Taiping Insurance(Included in Basic Tax)", 10, Constants.NORMAL,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第10行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Stock IIT")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Stock IIT", 10, Constants.NORMAL,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第11行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("IIT Adjustment")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("IIT Adjustment", 10, Constants.NORMAL,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第12行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Labor Tax")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Labor Tax", 10, Constants.NORMAL,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第13行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Severance Payment")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Severance Payment", 10,
						Constants.NORMAL, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第14行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Severance Payment Tax")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Severance Payment Tax", 10,
						Constants.NORMAL, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第15行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Year-end Bonus")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Year-end Bonus", 10, Constants.BOLD,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第16行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("Year-End Bonus Tax")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Year-End Bonus Tax", 10,
						Constants.NORMAL, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第17行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Total IIT")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Total IIT", 10, Constants.BOLD,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.BOLD,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第18行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle()
					.equals("One-Child Allowance")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("One-Child Allowance", 10,
						Constants.NORMAL, Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第19行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Special Payment")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Special Payment", 10, Constants.NORMAL,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				if (dealSalary.get(m).getSecondDatas().get(i).getItemValue() > 0) {
					Chunk chunkTableE2 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_LEFT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				} else {
					Chunk chunkTableE2 = convertChunkByChinese("", 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTableE2 = new Cell(chunkTableE2);
					cellTableE2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTableE2, new Point(j, 2));

					Chunk chunkTable3 = convertChunkByChinese(String.valueOf(dealSalary.get(m)
							.getSecondDatas().get(i).getItemValue()), 10, Constants.NORMAL,
							Constants.BLACK);
					Cell cellTable3 = new Cell(chunkTable3);
					cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
					bTable.addCell(cellTable3, new Point(j, 3));
				}
				j++;
			}
		}

		// 第20行
		for (int i = 0; i < dealSalary.get(m).getSecondDatas().size(); i++) {
			if (dealSalary.get(m).getSecondDatas().get(i).getItemTitle().equals("Month End Pay")
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != 0.00
					&& dealSalary.get(m).getSecondDatas().get(i).getItemValue() != -0.00) {
				Chunk chunkTableE1 = convertChunkByChinese("Month End Pay", 10, Constants.BOLD,
						Constants.BLACK);
				Cell cellTableE1 = new Cell(chunkTableE1);
				cellTableE1.setColspan(2);
				cellTableE1.setHorizontalAlignment(Element.ALIGN_LEFT);
				bTable.addCell(cellTableE1, new Point(j, 0));
				Chunk chunkTableE2 = convertChunkByChinese(
						String.valueOf(dealSalary.get(m).getSecondDatas().get(i).getItemValue()),
						10, Constants.BOLD, Constants.BLACK);
				Cell cellTableE2 = new Cell(chunkTableE2);
				cellTableE2.setColspan(2);
				cellTableE2.setHorizontalAlignment(Element.ALIGN_CENTER);
				bTable.addCell(cellTableE2, new Point(j, 2));
			}
		}

		graphDetailsInfo.add(bTable);
		graphDetailsInfo.setSpacingBefore(-20);
		document.add(graphDetailsInfo);
		// document.add(bTable2222);

		// 处理五险一金
		// List<SalaryItemCAndPModel> resultThird = initSalaryItemModelIII();
		Paragraph graphDetailsSecInfo = new Paragraph();
		// Table bTableSec = new Table(4,resultThird.size()+2);
		Table bTableSec = new Table(4, 7);
		bTableSec.setBorderColor(Constants.BLACK);
		bTableSec.setBorder(0);
		bTableSec.setCellspacing(2);
		bTableSec.setWidths(new float[] { 35f, 30f, 15f, 15f });
		bTableSec.setAutoFillEmptyCells(true);
		j = 0;
		// 标题
		Chunk chunkTableTitle2 = convertChunkByChinese("Staff Benefits", 12, Constants.BOLD,
				Constants.BLACK);
		Cell cellTableTitle2 = new Cell(chunkTableTitle2);
		cellTableTitle2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellTableTitle2.setColspan(4);
		cellTableTitle2.setBorder(0);
		bTableSec.addCell(cellTableTitle2, new Point(j, 0));
		++j;

		// 表格标题
		Chunk chunkTableTitle011 = convertChunkByChinese("Items", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle011 = new Cell(chunkTableTitle011);
		cellTableTitle011.setBackgroundColor(Constants.BLUE);
		bTableSec.addCell(cellTableTitle011, new Point(j, 0));

		Chunk chunkTableTitle012 = convertChunkByChinese("Contribution Period", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle012 = new Cell(chunkTableTitle012);
		cellTableTitle012.setBackgroundColor(Constants.BLUE);
		bTableSec.addCell(cellTableTitle012, new Point(j, 1));

		Chunk chunkTableTitle013 = convertChunkByChinese("Company", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle013 = new Cell(chunkTableTitle013);
		cellTableTitle013.setBackgroundColor(Constants.BLUE);
		bTableSec.addCell(cellTableTitle013, new Point(j, 2));

		Chunk chunkTableTitle014 = convertChunkByChinese("Individual", 12, Constants.BOLD,
				Constants.WHITE);
		Cell cellTableTitle014 = new Cell(chunkTableTitle014);
		cellTableTitle014.setBackgroundColor(Constants.BLUE);
		bTableSec.addCell(cellTableTitle014, new Point(j, 3));
		++j;
		// 内容
		for (int i = 0; i < dealSalary.get(m).getThirdDatas().size(); i++) {
			Chunk chunkTable1 = convertChunkByChinese(dealSalary.get(m).getThirdDatas().get(i)
					.getItemTitle(), 10, Constants.NORMAL, Constants.BLACK);
			Cell cellTable1 = new Cell(chunkTable1);
			cellTable1.setHorizontalAlignment(Element.ALIGN_LEFT);
			bTableSec.addCell(cellTable1, new Point(j, 0));

			Chunk chunkTable2 = convertChunkByChinese(dealSalary.get(m).getThirdDatas().get(i)
					.getItemPeriod(), 10, Constants.NORMAL, Constants.BLACK);
			Cell cellTable2 = new Cell(chunkTable2);
			cellTable2.setHorizontalAlignment(Element.ALIGN_LEFT);
			bTableSec.addCell(cellTable2, new Point(j, 1));

			Chunk chunkTable3 = convertChunkByChinese(
					String.valueOf(dealSalary.get(m).getThirdDatas().get(i).getItemValueC()), 10,
					Constants.NORMAL, Constants.BLACK);
			Cell cellTable3 = new Cell(chunkTable3);
			cellTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
			bTableSec.addCell(cellTable3, new Point(j, 2));

			Chunk chunkTable4 = convertChunkByChinese(
					String.valueOf(dealSalary.get(m).getThirdDatas().get(i).getItemValueP()), 10,
					Constants.NORMAL, Constants.BLACK);
			Cell cellTable4 = new Cell(chunkTable4);
			cellTable4.setHorizontalAlignment(Element.ALIGN_RIGHT);
			bTableSec.addCell(cellTable4, new Point(j, 3));
			++j;
		}

		graphDetailsSecInfo.add(bTableSec);
		graphDetailsSecInfo.setSpacingBefore(-18);
		document.add(graphDetailsSecInfo);
		document.close();

		return filePath;
	}

	/**
	 * 同行字体转换
	 * 
	 * @param text
	 * @param fontsize
	 * @param fontStyle
	 * @param color
	 * @return Chunk
	 * @throws Exception
	 */
	private Chunk convertChunkByChinese(String text, int fontsize, int fontStyle, Color color)
			throws Exception {
		BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
				BaseFont.NOT_EMBEDDED);

		Font fontChinese = new Font(baseFontChinese, fontsize, fontStyle, color);
		Chunk chunk = new Chunk(text, fontChinese);
		return chunk;
	}

	private void initSalaryData(Table bTable, SalaryItemModel model, int i) throws Exception {
		Chunk t1;
		Cell cell1;
		if (model.getItemTitle() == "Gross Salary") {
			t1 = convertChunkByChinese(model.getItemTitle(), 10, Constants.BOLD, Constants.BLACK);
			cell1 = new Cell(t1);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		} else {
			t1 = convertChunkByChinese(model.getItemTitle(), 10, Constants.NORMAL, Constants.BLACK);
			cell1 = new Cell(t1);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		}

		// 是否需要合并标题格子
		if (model.getTitleCol()) {
			cell1.setColspan(2);
			bTable.addCell(cell1, new Point(i, 0));
		} else {
			bTable.addCell(cell1, new Point(i, 0));
			Chunk t2 = convertChunkByChinese(model.getItemPeriod(), 10, Constants.NORMAL,
					Constants.BLACK);
			Cell cell2 = new Cell(t2);
			cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			bTable.addCell(cell2, new Point(i, 1));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		Chunk t3;
		Cell cell3;
		if (model.getItemTitle() == "Gross Salary") {
			t3 = convertChunkByChinese(df.format(model.getItemValue()), 10, Constants.BOLD,
					Constants.BLACK);
			cell3 = new Cell(t3);
			cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		} else {
			t3 = convertChunkByChinese(df.format(model.getItemValue()), 10, Constants.NORMAL,
					Constants.BLACK);
			cell3 = new Cell(t3);
			cell3.setHorizontalAlignment(model.getAlign());
		}

		// 是否需要合并数据格子
		if (model.getValueCol()) {
			cell3.setColspan(2);
			bTable.addCell(cell3, new Point(i, 2));
		} else {
			if (model.getItemValue() > 0) {
				bTable.addCell(cell3, new Point(i, 2));
			} else {
				bTable.addCell(cell3, new Point(i, 3));
			}
		}
	}

	/**
	 * 插入图片
	 * 
	 * @param imgPath
	 * @param percent
	 * @return Image
	 * @throws Exception
	 */
	private Image writeImg(String imgPath, int percent) throws Exception {
		Image img = Image.getInstance(imgPath);
		// 控制图片大小
		// img.scaleAbsolute(100, 100);
		img.scalePercent(percent);
		return img;
	}

	/**
	 * 追加邮件发送结果信息表中的记录
	 * 
	 * @param salaryInfoList
	 * @param result
	 */
	private void insertMailResultInfo(List<Map<String, Object>> salaryInfoList,String mailTitle) {

		for (Map<String, Object> salaryInfo : salaryInfoList) {
			String empNo = salaryInfo.get("EMP_NO").toString();

			// 判断邮件发送状态
			String sendSuccess = Constants.MAIL_NOT_SEND;

			// 是否已发送年终奖
			String isYearBonusSend = "";
			Float bonus = new Float(0.0f);
			Integer bonusCardSno = new Integer(0);
			if (salaryInfo.get("BONUS") != null) {
				bonus = Float.parseFloat(salaryInfo.get("BONUS").toString());
			}
			if (salaryInfo.get("BONUS_CARD_SNO") != null) {
				bonusCardSno = Integer.parseInt(salaryInfo.get("BONUS_CARD_SNO").toString());
			}
			if (bonus > 0 && bonusCardSno > 0) {
				isYearBonusSend = Constants.SUCCESS;
			} else {
				isYearBonusSend = Constants.FAILURE;
			}

			// 是否已发送当月薪酬
			String isSalarySend = "";
			Float taxSalary = new Float(0.0f);
			Float basicSalary = new Float(0.0f);
			Float realPay = new Float(0.0f);
			Integer salaryCardSno = new Integer(0);
			if (salaryInfo.get("TAX_SALARY") != null) {
				taxSalary = Float.parseFloat(salaryInfo.get("TAX_SALARY").toString());
			}
			if (salaryInfo.get("BASIC_SALARY") != null) {
				basicSalary = Float.parseFloat(salaryInfo.get("BASIC_SALARY").toString());
			}
			if (salaryInfo.get("REAL_PAY") != null) {
				realPay = Float.parseFloat(salaryInfo.get("REAL_PAY").toString());
			}
			if (salaryInfo.get("SALARY_CARD_SNO") != null) {
				salaryCardSno = Integer.parseInt(salaryInfo.get("SALARY_CARD_SNO").toString());
			}
			if (taxSalary > 0 || basicSalary > 0 || realPay > 0) {
				if ("3".equals(salaryInfo.get("SEND_DATE_TYPE"))) {
					if (salaryCardSno > 0) {
						isSalarySend = Constants.SUCCESS;
					} else {
						isSalarySend = Constants.FAILURE;
					}
				} else {
					isSalarySend = Constants.SUCCESS;
				}
			} else {
				isSalarySend = Constants.FAILURE;
			}

			// 是否已发送股权激励
			String isStockSend = Constants.FAILURE;
			// 是否已发送经济补偿金
			String isRelasePaySend = Constants.FAILURE;

			salaryServerDao.insertMailResultInfo(salaryInfo, isYearBonusSend, isSalarySend,
					isStockSend, isRelasePaySend, mailTitle, sendSuccess, "0", Constants.ADMIN_USER_ID);
		}
	}

	/**
	 * 更新邮件发送结果信息表中的发送状态
	 * 
	 * @param salaryInfoList
	 * @param result
	 */
	private void updateMailResultInfo(List<Map<String, Object>> salaryInfoList,
			EmailBatchStatus result) {
		List<EmailModel> failList = result.getFailList();
		// List<EmailModel> denyList = result.getDenyList();
		Long mailBatchNo = result.getBatchNo();
		String bizType = Constants.BIZ_TYPE;
		String failReason = "";

		// 邮件发送状态
		String sendSuccess = Constants.MAIL_SEND_SUCCEED;

		for (Map<String, Object> salaryInfo : salaryInfoList) {
			// String sfscCode = salaryInfo.get("SFSC_CODE").toString();
			String filePath = salaryInfo.get("FILE_PATH").toString();
			String empNo = salaryInfo.get("EMP_NO").toString();
			String mail = salaryInfo.get("MAIL") != null ? salaryInfo.get("MAIL").toString() : "";

			// 判断邮件发送状态
			if (!StringUtils.isEmpty(mail)) {
				for (EmailModel failEmp : failList) {
					if (empNo.equals(failEmp.getRcvId())) {
						sendSuccess = Constants.MAIL_SEND_FAILURE;
						failReason = failEmp.getEmailStatus().getDenyRemarks();
						break;
					}
				}

				// for (EmailModel denyEmp : denyList) {
				// if (empNo.equals(denyEmp.getRcvId())) {
				// sendSuccess = Constants.MAIL_NOT_SEND;
				// failReason = denyEmp.getEmailStatus().getDenyRemarks();
				// break;
				// }
				// }
				// }

				salaryServerDao.updateMailResultInfo(sendSuccess, empNo, failReason, bizType,
						mailBatchNo, filePath);
			}
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param realAccYm
	 * @param filePath
	 * @return String
	 */
	private String uploadFile(String realAccYm, String filePath) {

		String uploadPath = Constants.FILE_DIRECTORY + realAccYm;
		String sendFileName = new String();

		JSchChannel jSchChannel = new JSchChannel(Constants.SERVER_ADDR,
				Constants.SERVER_USER_NAME, Constants.SERVER_USER_PWD, Constants.PORT, 20 * 1000);

		try {
			jSchChannel.open();
			jSchChannel.upload(uploadPath, filePath);

			String[] file = filePath.split("\\\\");
			sendFileName = file[file.length - 1];
			// File file = new File(filePath);
			// salaryServerDao.insertUploadInfo(file.getName(), realAccYm);
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			jSchChannel.close();
		}
		return uploadPath + "/" + sendFileName;
	}

	/**
	 * 文件下载
	 * 
	 * @param downloadFile
	 * @param saveFile
	 */
	private void downloadPicFile(String downloadFile, String saveFile) {

		JSchChannel jSchChannel = new JSchChannel(Constants.SERVER_ADDR,
				Constants.SERVER_USER_NAME, Constants.SERVER_USER_PWD, Constants.PORT, 20 * 1000);

		try {
			jSchChannel.open();
			jSchChannel.download(Constants.PIC_DIRECTORY, downloadFile, saveFile);

		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			jSchChannel.close();
		}
	}
}
