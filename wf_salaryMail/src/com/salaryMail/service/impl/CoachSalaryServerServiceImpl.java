package com.salaryMail.service.impl;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.efesco.mailsvc.model.EmailBatchStatus;
import com.jcraft.jsch.JSchException;
import com.salaryMail.common.Constants;
import com.salaryMail.dao.SalaryServerDao;
import com.salaryMail.entity.ClientInfo;
import com.salaryMail.entity.CoachDeductSalary;
import com.salaryMail.entity.CoachEarnSalary;
import com.salaryMail.entity.CoachSalary;
import com.salaryMail.entity.SalaryTitleBean;
import com.salaryMail.service.CoachSalaryServerService;
import com.salaryMail.util.CoachSpecialTemplet;
import com.salaryMail.util.EncryPdf;
import com.salaryMail.util.ExcellToPdf;
import com.salaryMail.util.JSchChannel;
import com.salaryMail.util.ObjectUtil;
import com.salaryMail.util.OpenPdfAddPicture;
import com.salaryMail.util.SendEmail;
import com.salaryMail.util.ZipFileUtil;

@Service("coachSalaryServerService")
public class CoachSalaryServerServiceImpl implements CoachSalaryServerService {

	@Resource
	private SalaryServerDao salaryServerDao;

	public SalaryServerDao getSalaryServerDao() {
		return salaryServerDao;
	}

	public void setSalaryServerDao(SalaryServerDao salaryServerDao) {
		this.salaryServerDao = salaryServerDao;
	}

	private final Logger log = Logger.getLogger(CoachSalaryServerService.class);

	/**
	 * 发送加密工资单邮件
	 * 
	 * @throws Exception
	 */
	@Override
	public void sendMailForCoach() throws Exception {

		String path = System.getProperty("user.dir");
		String downloadFile = Constants.COACH_LOGO;
		String imagName = "coach_companyLogo";
		String imagePath = path + "\\" + imagName + ".jpg";
		File file = new File(imagePath);
		if (!file.exists()) {
			downloadFile(downloadFile, imagePath);
		}

		// 获得客户配置信息
		String[] coachCompanyGroup = Constants.COACH_GROUP.split(",");

		for (int i = 0; i < coachCompanyGroup.length; i++) {

			String companyNo = coachCompanyGroup[i];

			// 获取当天需要生成工资单的雇员信息
			List<Map<String, Object>> salaryInfoList = getSelectedEmployeeSalary(companyNo);

			if (salaryInfoList.size() > 0) {
				int zipCount = 1;
				for (int j = 0; j < salaryInfoList.size(); j++) {

					if (salaryInfoList.get(j).get("EMPLOYEE_CODE") != null) {
						log.info("**********  Employee :  "
								+ salaryInfoList.get(j).get("EMPLOYEE_CODE").toString() + "  **********");
//						// 取得工资单加密密码（员工入职年份+员工出生月日）
//						String coachPwd = "";
//
//						if (salaryInfoList.get(j).get("BIRTHDAY") != null
//								&& salaryInfoList.get(j).get("JOIN_YEAR") != null) {
//							log.info("**********  BIRTHDAY :  "
//									+ salaryInfoList.get(j).get("BIRTHDAY").toString() + "  **********");
//							log.info("**********  JOIN_YEAR :  "
//									+ salaryInfoList.get(j).get("JOIN_YEAR").toString() + "  **********");
//							coachPwd = salaryInfoList.get(j).get("JOIN_YEAR").toString()
//									+ salaryInfoList.get(j).get("BIRTHDAY").toString();
//						}

						// 数据整理
						CoachSalary coachSalary = dealCoachSalary(salaryInfoList.get(j),companyNo);

						// 蔻驰工资单文件名
						String FileName = salaryInfoList.get(j).get("EMPLOYEE_CODE").toString()
								+ "-" + salaryInfoList.get(j).get("REAL_ACC_YM").toString();
						String xlsFilePathS = path + "\\" + FileName + ".xls";
						String pdfFilePathS = path + "\\" + FileName + "_S.pdf";
						String newPdfFilePathS = path + "\\" + FileName + ".pdf";

						// 生成EXCEL文件
						log.info("**********  Create Excel File **********");
						CoachSpecialTemplet.buildTemplet(coachSalary, xlsFilePathS);

						// 转为PDF文件
						log.info("**********  Save As Pdf **********");
						ExcellToPdf.saveAsPDF(xlsFilePathS, pdfFilePathS);

						// 给PDF添加公司图片
						log.info("**********  Add Company Logo **********");
						OpenPdfAddPicture.realPdf(pdfFilePathS, imagePath, newPdfFilePathS);

//						// PDF加密
//						log.info("**********  Pdf File Encrypt **********");
//						EncryPdf.encryptPdf(newPdfFilePathS, coachPwd);
//
//						// 每30个文件创建一个目录
//						File zipDir = new File(path + "\\"
//								+ salaryInfoList.get(j).get("REAL_ACC_YM").toString() + "_"
//								+ zipCount);
//						if (!zipDir.isDirectory()) {
//							zipDir.mkdir();
//						}
//
//						// 将生成的文件移动到目录
//						new File(newPdfFilePathS).renameTo(new File(zipDir + "\\"
//								+ new File(newPdfFilePathS).getName()));
//
//						// 每30个文件压缩为zip文件&&上传，并删除源文件
//						String zipFilePath = path + "\\"
//								+ salaryInfoList.get(j).get("REAL_ACC_YM").toString() + "_"
//								+ zipCount + ".zip";
//						if (salaryInfoList.size() <= 30 || (salaryInfoList.size() - j) <= 30) {
//							if (j == salaryInfoList.size() - 1) {
//								// 将包含生成文件的文件夹压缩为zip文件
//								ZipFileUtil.doZip(zipDir.getAbsolutePath(), zipFilePath);
//
//								// 上传文件
//								String sendFilePath = uploadFile(
//										salaryInfoList.get(j).get("COMPANY_NO").toString()
//												+ "_"
//												+ salaryInfoList.get(j).get("REAL_ACC_YM")
//														.toString(), zipFilePath);
//								salaryInfoList.get(j).put("FILE_PATH", sendFilePath);
//								deleteAll(zipDir);
//								new File(zipFilePath).delete();
//							}
//							if ((j + 1) % 30 == 0) {
//								// 将包含生成文件的文件夹压缩为zip文件
//								ZipFileUtil.doZip(zipDir.getAbsolutePath(), zipFilePath);
//
//								// 上传文件
//								String sendFilePath = uploadFile(
//										salaryInfoList.get(j).get("COMPANY_NO").toString()
//												+ "_" + salaryInfoList.get(j).get("REAL_ACC_YM").toString(), zipFilePath);
//								salaryInfoList.get(j).put("FILE_PATH", sendFilePath);
//								deleteAll(zipDir);
//								new File(zipFilePath).delete();
//								zipCount++;
//							}
//						} else if ((j + 1) % 30 == 0) {
//							// 将包含生成文件的文件夹压缩为zip文件
//							ZipFileUtil.doZip(zipDir.getAbsolutePath(), zipFilePath);
//
//							// 上传文件
//							String sendFilePath = uploadFile(salaryInfoList.get(j).get("COMPANY_NO").toString()
//									+ "_" + salaryInfoList.get(j).get("REAL_ACC_YM").toString(),
//									zipFilePath);
//							salaryInfoList.get(j).put("FILE_PATH", sendFilePath);
//							deleteAll(zipDir);
//							new File(zipFilePath).delete();
//							zipCount++;
//						}
						// 上传文件
						uploadFile(salaryInfoList.get(j).get("COMPANY_NO").toString()
								+ "_" + salaryInfoList.get(j).get("REAL_ACC_YM").toString(), newPdfFilePathS);
						log.info("********** " + newPdfFilePathS + " has been updated **********");

						// 删除本地文件，本地需要文件的可以选择性注释
						new File(xlsFilePathS).delete();
						new File(pdfFilePathS).delete();
						new File(newPdfFilePathS).delete();

						// 更新加密邮件推送结果信息表
						insertSalaryExecuteResult(salaryInfoList.get(j));
					}
				}
//				// 发送邮件
//				EmailBatchStatus result = SendEmail.sendEmailForCoach(salaryInfoList);
//				if (result == null) {
//					log.info("********** Mail Empty **********");
//				}
			} else {
				log.info("********** " + companyNo + " , No Data **********");
			}
		}
	}

	/**
	 * 获取薪酬数据
	 * 
	 * @param clientConfigInfo
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	private List<Map<String, Object>> getSelectedEmployeeSalary(String companyNo)
			throws SQLException {
		List<Map<String, Object>> selectedSalaryInfoList = new ArrayList<Map<String, Object>>();

		try {

			// 获得商社的基本信息
			ClientInfo clientInfo = salaryServerDao.getClientInfoByCompanyNo(companyNo);

			// 查询取得雇员工资表中的雇员工资数据
			List<Map<String, Object>> salaryInfoList = salaryServerDao.getSalaryInfo(companyNo,
					Constants.COACH_SEND_BEGIN_YM, Constants.COACH_SEND_DATE_TYPE);

			for (int i = 0; i < salaryInfoList.size(); i++) {
				Map<String, Object> salaryMap = salaryInfoList.get(i);
				String empNo = salaryMap.get("EMP_NO").toString();

				// 商社名称
				salaryMap.put("COMPANY_NAME", clientInfo.getCompanyName());
				// 商社中文全称
				salaryMap.put("NAME_CH", clientInfo.getCompanyNameCh());
				// 商社英文全称
				salaryMap.put("NAME_EN", clientInfo.getCompanyNameEn());
				// 客户组
				salaryMap.put("COMP_GRP_CODE", clientInfo.getCompGrpCode());
				// 所属业务部
				salaryMap.put("BELONG_TO_DEPT", clientInfo.getBelongToDept());
				// 业务部名称
				salaryMap.put("DEPT_NAME", clientInfo.getDeptName());
				// 销售组长
				salaryMap.put("USER_ID", clientInfo.getUserId());
				// 销售组长姓名
				salaryMap.put("USER_NAME", clientInfo.getUserName());
				// 账套
				salaryMap.put("SFSC_CODE", clientInfo.getSfscCode());
				// 查询取得雇员工资项目
				List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitleForCoach(
						companyNo, salaryMap.get("ACC_YM").toString());
				salaryMap.put("SALARY TITLE", salaryTitleList);
				// 雇员所在部门名
				String departNo = salaryMap.get("DEPART_NO") != null ? salaryMap.get("DEPART_NO")
						.toString() : "";
				salaryMap.put("DEPART_NAME", salaryServerDao.getNameByDepartNo(departNo));

				// 雇员银行卡号
				salaryMap.put("BANK_ACCOUNT", salaryServerDao.getBankAccountByEmpNo(empNo));

				// 雇员成本中心
				salaryMap.put("COST_CENTER", salaryServerDao.getCostCenter(companyNo, empNo));

				// 雇员入职年份
//				salaryMap.put("JOIN_YEAR", salaryServerDao.getCompanyJoinYear(
//						salaryMap.get("REG_NO").toString(), companyNo));

				selectedSalaryInfoList.add(salaryMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return selectedSalaryInfoList;
	}

	/**
	 * 整理薪酬数据
	 * 
	 * @param Map
	 *            <String, Object>
	 * @return CoachSalary
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public CoachSalary dealCoachSalary(Map<String, Object> map,String companyNo) {
		CoachSalary coachSalary = new CoachSalary();
		// 基本信息整理(包括实发金额)
		String payrollPeriod = map.get("REAL_ACC_YM").toString();
		DateFormat df = new SimpleDateFormat("yyyyMM");
		Locale locale = new Locale("en");

		try {
			Date date = df.parse(payrollPeriod);
			String year = String.format("%tY", date);
			String month = String.format(locale, "%tb", date);
			payrollPeriod = month + "," + year;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String eeid = ObjectUtil.ObjectIsNull(map.get("EMPLOYEE_CODE"));
		String employeeName = ObjectUtil.ObjectIsNull(map.get("NAME"));
		String costCenter = ObjectUtil.ObjectIsNull(map.get("COST_CENTER"));
		String department = ObjectUtil.ObjectIsNull(map.get("DEPART_NAME"));
		String netPay = ObjectUtil.objectIsNull2(map.get("REAL_PAY"));
		coachSalary.setPayrollPeriod(payrollPeriod);
		coachSalary.setEeid(eeid);
		coachSalary.setEmployeeName(employeeName);
		coachSalary.setCostCenter(costCenter);
		coachSalary.setDepartment(department);
		coachSalary.setNetPay(netPay);
		// 加减项信息整理
		List<SalaryTitleBean> salaryTitleBeanList = (List<SalaryTitleBean>) map.get("SALARY TITLE");
		List<CoachEarnSalary> coachEarnSalaryList = new ArrayList<CoachEarnSalary>();
		List<CoachDeductSalary> coachDeductSalaryList = new ArrayList<CoachDeductSalary>();
		for (SalaryTitleBean salaryTitleBean : salaryTitleBeanList) {
			CoachEarnSalary coachEarnSalary = new CoachEarnSalary();
			CoachDeductSalary coachDeductSalary = new CoachDeductSalary();
			if (salaryTitleBean.getAddFlag() > 0) {
				if ("0.00".equals(ObjectUtil.objectIsNull2(map.get(ObjectUtil
						.ObjectIsNull(salaryTitleBean.getFieldname()))))) {
					continue;
				}
				coachEarnSalary.setEarnTitle(ObjectUtil.ObjectIsNull(salaryTitleBean
						.getItemName())+"\r\n"+ObjectUtil.ObjectIsNull(salaryTitleBean
								.getItemNameEn()));
				coachEarnSalary.setEarnValue(ObjectUtil.objectIsNull2(map.get(ObjectUtil
						.ObjectIsNull(salaryTitleBean.getFieldname()))));
				coachEarnSalaryList.add(coachEarnSalary);
			} else if (salaryTitleBean.getAddFlag() < 0) {
				if ("0.00".equals(ObjectUtil.objectIsNull2(map.get(ObjectUtil
						.ObjectIsNull(salaryTitleBean.getFieldname()))))) {
					continue;
				}
				coachDeductSalary.setDeductTitle(ObjectUtil.ObjectIsNull(salaryTitleBean
						.getItemName())+"\r\n"+ObjectUtil.ObjectIsNull(salaryTitleBean
								.getItemNameEn()));
				if("HK4683".equals(companyNo)||"HK5362".equals(companyNo)){
					coachDeductSalary.setDeductValue("("+ObjectUtil.objectIsNull2(map.get(ObjectUtil
							.ObjectIsNull(salaryTitleBean.getFieldname())))+")");
				}else{
					coachDeductSalary.setDeductValue(ObjectUtil.objectIsNull2(map.get(ObjectUtil
							.ObjectIsNull(salaryTitleBean.getFieldname()))));
				}
				coachDeductSalaryList.add(coachDeductSalary);
			}
		}
		// 添加工资税金
		if (!"0.00".equals(ObjectUtil.objectIsNull2(map.get("ADJUST_TAX") == null ? map.get("TAX")
				: map.get("ADJUST_TAX")))) {
			CoachDeductSalary coachDeductSalary = new CoachDeductSalary();
			if("HK4683".equals(companyNo) || "HK5362".equals(companyNo)) {
				coachDeductSalary.setDeductTitle("工资税金"+"\r\n"+"IIT DEDUCTION");
				Float deductValue = Float.valueOf(ObjectUtil.objectIsNull2(map.get("ADJUST_TAX") == null ?
						map.get("TAX") : map.get("ADJUST_TAX")))
						- Float.valueOf(ObjectUtil.objectIsNull2(map.get("ALLOWANCE10")));
				coachDeductSalary.setDeductValue("(" + ObjectUtil.objectIsNull2(deductValue) + ")");
			} else {
				coachDeductSalary.setDeductTitle("工资税金"+"\r\n"+"IIT DEDUCTION");
				coachDeductSalary.setDeductValue(ObjectUtil.objectIsNull2(map.get("ADJUST_TAX") == null ? map
						.get("TAX") : map.get("ADJUST_TAX")));
			}
			coachDeductSalaryList.add(coachDeductSalary);
		}
		coachSalary.setCoachEarnSalaryList(coachEarnSalaryList);
		coachSalary.setCoachDeductSalary(coachDeductSalaryList);
		return coachSalary;
	}

	/**
	 * 追加邮件发送结果信息表中的记录
	 * 
	 * @param salaryInfoList
	 * @param result
	 */
	private void insertSalaryExecuteResult(Map<String, Object> salaryInfo) {

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
		if (bonus > 0) {
			if (bonusCardSno > 0) {
				isYearBonusSend = Constants.SUCCESS;
			} else {
				isYearBonusSend = Constants.FAILURE;
			}
		} else {
			isYearBonusSend = Constants.FAILURE;
		}

		// 是否已发送股权激励
		String isStockSend = Constants.FAILURE;

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
			if (salaryCardSno > 0) {
				isSalarySend = Constants.SUCCESS;
			} else {
				isSalarySend = Constants.FAILURE;
			}
		} else {
			isSalarySend = Constants.FAILURE;
		}

		// 是否已发送经济补偿金
		String isRelasePaySend = Constants.FAILURE;

		salaryServerDao.insertSalaryExecuteInfo(salaryInfo, isYearBonusSend, isSalarySend,
				isStockSend, isRelasePaySend);
	}

	/**
	 * 文件上传
	 * 
	 * @param realAccYm
	 * @param filePath
	 * @return String
	 */
	private String uploadFile(String realAccYm, String filePath) {

		String uploadPath = Constants.COACH_FORMAT_FILE_PATH + realAccYm;
		String sendFileName = new String();

		JSchChannel jSchChannel = new JSchChannel(Constants.SERVER_ADDR,
				Constants.SERVER_USER_NAME, Constants.SERVER_USER_PWD, Constants.PORT, 20 * 1000);

		try {
			jSchChannel.open();
			jSchChannel.upload(uploadPath, filePath);

			String[] file = filePath.split("\\\\");
			sendFileName = file[file.length - 1];

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
	private void downloadFile(String downloadFile, String saveFile) {

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

	/**
	 * 删除文件夹及下面的所有文件
	 * 
	 * @param downloadFile
	 * @param saveFile
	 */
	public static void deleteAll(File path) {

		if (!path.exists()) // 路径存在
			return;
		if (path.isFile()) { // 是文件
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAll(files[i]);
		}
		path.delete();
	}
}
