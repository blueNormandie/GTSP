package com.salaryMail.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.efesco.mailsvc.model.EmailBatchStatus;
import com.efesco.mailsvc.model.EmailBlackList;
import com.efesco.mailsvc.model.EmailModel;
import com.jcraft.jsch.JSchException;
import com.salaryMail.common.Constants;
import com.salaryMail.dao.SalaryServerDao;
import com.salaryMail.entity.BaseItemConfig;
import com.salaryMail.entity.ClientConfigInfo;
import com.salaryMail.entity.ClientInfo;
import com.salaryMail.entity.FormatInfo;
import com.salaryMail.entity.SalaryExecuteInfo;
import com.salaryMail.entity.SalaryFieldConfig;
import com.salaryMail.entity.SalaryItemConfig;
import com.salaryMail.entity.SalaryTempletData;
import com.salaryMail.entity.SalaryTitleBean;
import com.salaryMail.entity.TempletBaseInfo;
import com.salaryMail.entity.TempletSalaryInfo;
import com.salaryMail.entity.TempletSalaryItemInfo;
import com.salaryMail.service.SalaryServerService;
import com.salaryMail.util.Base64Util;
import com.salaryMail.util.Excell07FreeTemplet;
import com.salaryMail.util.Excell07StandardTemplet;
import com.salaryMail.util.ExcellFreeTemplet;
import com.salaryMail.util.ExcellStandardTemplet;
import com.salaryMail.util.ExcellToPdf;
import com.salaryMail.util.JSchChannel;
import com.salaryMail.util.ObjectUtil;
import com.salaryMail.util.SendEmail;
import com.salaryMail.util.WordFreeTemplet;
import com.salaryMail.util.WordStandardTemplet;

@Service("salaryServerService")
public class SalaryServerServiceImpl implements SalaryServerService {

	@Resource
	private SalaryServerDao salaryServerDao;

	public SalaryServerDao getSalaryServerDao() {
		return salaryServerDao;
	}

	public void setSalaryServerDao(SalaryServerDao salaryServerDao) {
		this.salaryServerDao = salaryServerDao;
	}

	private final Logger log = Logger.getLogger(SalaryServerServiceImpl.class);

	/**
	 * 发送加密工资单邮件
	 * 
	 * @throws Exception
	 */
	@Override
	public void salaryMailSend() throws Exception {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		String path = System.getProperty("user.dir");

		// 获得客户配置信息
		List<ClientConfigInfo> clientConfigInfoList = salaryServerDao.getClientConfigInfo();

		for (ClientConfigInfo clientConfigInfo : clientConfigInfoList) {
//			ClientConfigInfo clientConfigInfo=salaryServerDao.getClientConfigInfoByCompanyNo("CH1626");
			String companyNo = clientConfigInfo.getCompanyNo();

			// 获取当天需要发送的雇员信息
			List<Map<String, Object>> salaryInfoList = getSelectedEmployeeSalary(clientConfigInfo);

			FormatInfo formatInfo = salaryServerDao.getFormatInfo(clientConfigInfo.getFormatSno());

			if (salaryInfoList.size() > 0) {

				// 数据整理
				List<SalaryTempletData> dealSalaryInfo = dealSalaryInfo(salaryInfoList, formatInfo,
						clientConfigInfo);

				// 下载公司logo图片
				String downloadFile = clientConfigInfo.getLogoPath();
				String imagName = clientConfigInfo.getCompanyNo() + "_companyLogo";
				String imagePath = "";
				if (!StringUtils.isEmpty(downloadFile)) {
					imagePath = path + "\\" + imagName + ".jpg";
					downloadFile(downloadFile, imagePath);
				}

				// 工资单文件生成
				for (int i = 0; i < salaryInfoList.size(); i++) {
					Map<String, Object> salaryInfo = salaryInfoList.get(i);
					String empNo = salaryInfo.get("EMP_NO").toString();

					String FileName = salaryInfo.get("REAL_ACC_YM") + Constants.SALARY_FILE_NAME + "_"
							+ salaryInfo.get("EMP_NO") + "_"
							+ salaryInfo.get("SALARY_SNO") + df.format(date);

					if ("1".equals(formatInfo.getFormatType())) {// 标准模板工资单文件
						// PDF
						if ("1".equals(clientConfigInfo.getFileType())) {
							String xlsFilePathS = path + "\\" + FileName + ".xlsx";
							String pdfFilePathS = path + "\\" + FileName + ".pdf";
							Excell07StandardTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathS,
									imagePath, i);
							ExcellToPdf.saveAsPDF(xlsFilePathS, pdfFilePathS);

							// 上传文件
							// ***** EPAY2.0 BEGIN *******
//							String sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), pdfFilePathS);
							String sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), pdfFilePathS);
							// ***** EPAY2.0 END *******
							salaryInfo.put("FILE_PATH", sendFilePath);

							new File(xlsFilePathS).delete();
							new File(pdfFilePathS).delete();

							log.info("********** " + companyNo + " , " + empNo
									+ " : Standard Pdf Finished **********");
						} else if ("2".equals(clientConfigInfo.getFileType())) {
							// Word
							String wordFilePathS = path + "\\" + FileName + ".doc";
							WordStandardTemplet.writeWordFile(
									dealSalaryInfo,
									wordFilePathS,
									downloadFile == null ? "" : (downloadFile.replace(
											Constants.PIC_REFELCT_DIRECTORY, "http://"
													+ Constants.SERVER_ADDR)), i);

							// 上传文件
							// ***** EPAY2.0 BEGIN *******	
//							String sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), wordFilePathS);
							String sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), wordFilePathS);
							// ***** EPAY2.0 END *******	
							salaryInfo.put("FILE_PATH", sendFilePath);

							new File(wordFilePathS).delete();

							log.info("********** " + companyNo + " , " + empNo
									+ " : Standard Word Finished **********");
						} else if ("3".equals(clientConfigInfo.getFileType())) {
							// Excel
							String xlsFilePathS = path + "\\" + FileName + ".xlsx";
							Excell07StandardTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathS,
									imagePath, i);

							// 上传文件
							// ***** EPAY2.0 BEGIN *******	
//							String sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), xlsFilePathS);
							String sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), xlsFilePathS);
							// ***** EPAY2.0 END *******	
							salaryInfo.put("FILE_PATH", sendFilePath);

							new File(xlsFilePathS).delete();

							log.info("********** " + companyNo + " , " + empNo
									+ " : Standard Excel Finished **********");
						} else {
							log.info("********** Not Exist This Type **********");
						}
					} else if ("2".equals(formatInfo.getFormatType())) {// 自定义模板工资单文件
						if ("1".equals(clientConfigInfo.getFileType())) {
							// PDF
							String xlsFilePathF = path + "\\" + FileName + ".xlsx";
							String pdfFilePathF = path + "\\" + FileName + ".pdf";
							ExcellToPdf.saveAsPDF(Excell07FreeTemplet.sfscExcellPay(dealSalaryInfo,
									xlsFilePathF, imagePath, i), pdfFilePathF);

							// 上传文件
							// ***** EPAY2.0 BEGIN *******	
//							String sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), pdfFilePathF);
							String sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), pdfFilePathF);
							// ***** EPAY2.0 END *******	
							salaryInfo.put("FILE_PATH", sendFilePath);

							new File(xlsFilePathF).delete();
							new File(pdfFilePathF).delete();

							log.info("********** " + companyNo + " , " + empNo
									+ " : Free Pdf Finished **********");
						} else if ("2".equals(clientConfigInfo.getFileType())) {
							// Word
							String wordFilePathF = path + "\\" + FileName + ".doc";

							WordFreeTemplet.writeWordFile(
									dealSalaryInfo,
									wordFilePathF,
									downloadFile == null ? "" : (downloadFile.replace(
											Constants.PIC_REFELCT_DIRECTORY, "http://"
													+ Constants.SERVER_ADDR)), i);
							// 上传文件
							// ***** EPAY2.0 BEGIN *******	
//							String sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), wordFilePathF);
							String sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), wordFilePathF);
							// ***** EPAY2.0 END *******	
							salaryInfo.put("FILE_PATH", sendFilePath);

							new File(wordFilePathF).delete();

							log.info("********** " + companyNo + " , " + empNo
									+ " : Free Word Finished **********");
						} else if ("3".equals(clientConfigInfo.getFileType())) {
							// Excel
							String xlsFilePathF = path + "\\" + FileName + ".xlsx";
							Excell07FreeTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathF,
									imagePath, i);

							// 上传文件
							// ***** EPAY2.0 BEGIN *******	
//							String sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), xlsFilePathF);
							String sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), xlsFilePathF);
							// ***** EPAY2.0 END *******	
							salaryInfo.put("FILE_PATH", sendFilePath);

							new File(xlsFilePathF).delete();

							log.info("********** " + companyNo + " , " + empNo
									+ " : Free Excel Finished **********");
						} else {
							log.info("********** Not Exist This Type **********");
						}
					} else {
						log.info("********** Not Exist This Type **********");
					}
					// 让线程阻塞3秒，保证上传文件成功
					Thread.sleep(3000);
				}

				// 删除本地公司logo图片
				File file = new File(imagePath);
				file.delete();

				// 追加邮件发送信息表中的记录
				insertMailResultInfo(salaryInfoList, clientConfigInfo.getMailTitle());
				// 发送邮件
				EmailBatchStatus result = SendEmail.sendSalaryEmail(salaryInfoList,
						clientConfigInfo);
				// 发送提醒邮件给业务员和录入人
				sendWarningMail(clientConfigInfo.getCompanyNo(), salaryInfoList, result);

				if (result != null) {
					// 更新文件上传信息表中的记录
					updateMailResultInfo(salaryInfoList, result);
				} else {
					log.info("********** Mail Empty **********");
				}

				// 黑名单中的人员并且邮箱为空的，更新邮件发送信息表
				updateRcvBlackCode(salaryInfoList);

			} else {
				log.info("********** " + clientConfigInfo.getCompanyNo() + " , No Data **********");
			}
		}
	}
	
	/**
	 * 黑名单中的人员并且邮箱为空的，更新邮件发送信息表
	 * 
	 * @param salaryInfoList
	 * @param result
	 */
	private void updateRcvBlackCode(List<Map<String, Object>> salaryInfoList) {

		List<EmailBlackList> list = SendEmail.getBlackCode();

		for (EmailBlackList entity : list) {
			if (Constants.BLACKCODE_RCV.equals(entity.getFilter_type())) {
				for (Map<String, Object> salaryMap : salaryInfoList) {
					String empNo = salaryMap.get("EMP_NO").toString();
					
					if (empNo.equals(entity.getBlack_code())) {
						String filePath = salaryMap.get("FILE_PATH").toString();
						String failReason = "接收者 雇员" + empNo + "在发送黑名单中";
						salaryServerDao.updateRcvBlackCode(failReason, empNo, filePath);
					}
				}
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
	private List<Map<String, Object>> getSelectedEmployeeSalary(ClientConfigInfo clientConfigInfo)
			throws SQLException {
		String companyNo = clientConfigInfo.getCompanyNo();
		List<Map<String, Object>> selectedSalaryInfoList = new ArrayList<Map<String, Object>>();

		try {

			// 获得商社的基本信息
			ClientInfo clientInfo = salaryServerDao.getClientInfoByCompanyNo(companyNo);

			// 查询取得雇员工资表中的雇员工资数据
			List<Map<String, Object>> salaryInfoList = salaryServerDao.getSalaryInfo(companyNo,
					clientConfigInfo.getSendBeginYm(), clientConfigInfo.getSendDateType());

			List<String> empSubsidyList = new ArrayList<String>();
			for (int i = 0; i < salaryInfoList.size(); i++) {
				Map<String, Object> salaryMap = salaryInfoList.get(i);
				// ***** No：S20151312 BEGIN *******
				// 是否为外划工资
				String isEntrust = salaryMap.get("UNIT_NO") == null ? "0" :  "1";
				// ***** No：S20151312 END *******
				String empNo = salaryMap.get("EMP_NO").toString();
				// ***** EPAY2.0 BEGIN *******
//				String accYm = salaryMap.get("ACC_YM").toString();
				// ***** EPAY2.0 END *******
				String realAccYm = salaryMap.get("REAL_ACC_YM").toString();
				Integer batchNo = Integer.parseInt(salaryMap.get("BATCH_NO").toString());
				String payrollType =  salaryMap.get("PAYROLL_TYPE").toString(); //是否为劳务报酬
				Integer kind = null;
				String isSalarySend = "0";// 是否要发工资薪酬
				String isBonusSend = "0";// 是否要发年终奖
				String isStockSend = "0";// 是否要发股权激励
				String isSubsidySend = "0";// 是否要发经济补偿金

				// 工资薪酬
				Float taxSalary = new Float(0.0f);
				Float basicSalary = new Float(0.0f);
				Float realPay = new Float(0.0f);
				if (salaryMap.get("TAX_SALARY") != null) {
					taxSalary = Float.parseFloat(salaryMap.get("TAX_SALARY").toString());
				}
				if (salaryMap.get("BASIC_SALARY") != null) {
					basicSalary = Float.parseFloat(salaryMap.get("BASIC_SALARY").toString());
				}
				if (salaryMap.get("REAL_PAY") != null) {
					realPay = Float.parseFloat(salaryMap.get("REAL_PAY").toString());
				}
				if (taxSalary > 0 || basicSalary > 0 || realPay > 0) {
					if (Constants.PAYROLL_TYPE_SALARY.equals(payrollType)) {
						kind = Constants.KIND_SALARY;
					}
					else if (Constants.PAYROLL_TYPE_REMUNERATION.equals(payrollType)) {
						kind = Constants.KIND_REMUNERATION;
					}

					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("si_company_no", companyNo);
					paramMap.put("si_emp_no", empNo);
					// ***** EPAY2.0 BEGIN *******
//					paramMap.put("si_acc_ym", accYm);
					// ***** EPAY2.0 END *******
					paramMap.put("si_real_acc_ym", realAccYm);
					// ***** No：S20151312 BEGIN *******
					// 外划工资实际发薪日推送当作预计发薪日推送处理
//					paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					if ("1".equals(isEntrust) && "3".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "2");
					}
					// ***** S20160161 BEGIN *******
					else if ("1".equals(isEntrust) && "4".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "5");
					}
					// ***** S20160161 END    *******
					else {
						paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					}
					// ***** No：S20151312 END *******
					paramMap.put("ni_kind", kind);
					paramMap.put("ni_batch_no", batchNo);

					isSalarySend = salaryServerDao.checkEmployee(paramMap);
				}

				// 股权激励
				Float stock = new Float(0.0f);
				if (salaryMap.get("STOCK") != null) {
					stock = Float.parseFloat(salaryMap.get("STOCK").toString());
				}
				if (stock > 0) {
					kind = Constants.KIND_STOCK;

					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("si_company_no", companyNo);
					paramMap.put("si_emp_no", empNo);
					// ***** EPAY2.0 BEGIN *******
//					paramMap.put("si_acc_ym", accYm);
					// ***** EPAY2.0 END *******
					paramMap.put("si_real_acc_ym", realAccYm);
					// ***** No：S20151312 BEGIN *******
					// 外划工资实际发薪日推送当作预计发薪日推送处理
//					paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					if ("1".equals(isEntrust) && "3".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "2");
					}
					// ***** S20160161 BEGIN *******
					else if ("1".equals(isEntrust) && "4".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "5");
					}
					// ***** S20160161 END    *******
					else {
						paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					}
					// ***** No：S20151312 END *******
					paramMap.put("ni_kind", kind);
					paramMap.put("ni_batch_no", batchNo);

					isStockSend = salaryServerDao.checkEmployee(paramMap);
				}

				// 年终奖
				Float bonus = new Float(0.0f);
				if (salaryMap.get("BONUS") != null) {
					bonus = Float.parseFloat(salaryMap.get("BONUS").toString());
				}
				if (bonus > 0) {
					kind = Constants.KIND_BONUS;

					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("si_company_no", companyNo);
					paramMap.put("si_emp_no", empNo);
					// ***** EPAY2.0 BEGIN *******
//					paramMap.put("si_acc_ym", accYm);
					// ***** EPAY2.0 END *******
					paramMap.put("si_real_acc_ym", realAccYm);
					// ***** No：S20151312 BEGIN *******
					// 外划工资实际发薪日推送当作预计发薪日推送处理
//					paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					if ("1".equals(isEntrust) && "3".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "2");
					}
					// ***** S20160161 BEGIN *******
					else if ("1".equals(isEntrust) && "4".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "5");
					}
					// ***** S20160161 END    *******
					else {
						paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					}
					// ***** No：S20151312 END *******
					paramMap.put("ni_kind", kind);
					paramMap.put("ni_batch_no", batchNo);

					isBonusSend = salaryServerDao.checkEmployee(paramMap);
				}

				// 经济补偿金
				Map<String, String> disSubsidyMap = salaryServerDao.getDisSubsidy(empNo, companyNo,
						realAccYm);
				String subsidyBefTax = disSubsidyMap.get("subsidyBefTax");// 税前应发经济补偿金
				String subsidyBefTaxDeduct = disSubsidyMap.get("subsidyBefTaxDeduct");// 税前应扣经济补偿金
				String subsidyRcvNoTax = disSubsidyMap.get("subsidyRcvNoTax");// 仅计税不发放经济补偿金
				String subsidyDeductTax = disSubsidyMap.get("subsidyDeductTax");// 仅抵扣计税项（经济补偿金）
				String subsidyTotal = disSubsidyMap.get("subsidyTotal");// 计税经济补偿金总额
				String subsidyTax = disSubsidyMap.get("subsidyTax");// 经济补偿金个人所得税
				String subsidyShouldPay = disSubsidyMap.get("subsidyShouldPay");// 税后应发经济补偿金
				String subsidyDeductPay = disSubsidyMap.get("subsidyDeductPay");// 税后应扣经济补偿金
				String subsidyRealPay = disSubsidyMap.get("subsidyRealPay");// 实发经济补偿金
				if (!("0.00".equals(subsidyBefTax) && "0.00".equals(subsidyBefTaxDeduct)
						&& "0.00".equals(subsidyRcvNoTax) && "0.00".equals(subsidyDeductTax)
						&& "0.00".equals(subsidyTotal) && "0.00".equals(subsidyTax)
						&& "0.00".equals(subsidyShouldPay) && "0.00".equals(subsidyDeductPay) && "0.00"
							.equals(subsidyRealPay))) {
					kind = Constants.KIND_SUBSIDY;

					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("si_company_no", companyNo);
					paramMap.put("si_emp_no", empNo);
					// ***** EPAY2.0 BEGIN *******
//					paramMap.put("si_acc_ym", accYm);
					// ***** EPAY2.0 END *******
					paramMap.put("si_real_acc_ym", realAccYm);
					// ***** No：S20151312 BEGIN *******
					// 外划工资实际发薪日推送当作预计发薪日推送处理
//					paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					if ("1".equals(isEntrust) && "3".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "2");
					}
					// ***** S20160161 BEGIN *******
					else if ("1".equals(isEntrust) && "4".equals(clientConfigInfo.getSendDateType())) {
						paramMap.put("si_send_date_type", "5");
					}
					// ***** S20160161 END    *******
					else {
						paramMap.put("si_send_date_type", clientConfigInfo.getSendDateType());
					}
					// ***** No：S20151312 END *******
					paramMap.put("ni_kind", kind);
					paramMap.put("ni_batch_no", batchNo);

					isSubsidySend = salaryServerDao.checkEmployee(paramMap);
				}

				if ("1".equals(isSalarySend) || "1".equals(isBonusSend) || "1".equals(isStockSend)
						|| "1".equals(isSubsidySend)) {
					// 经济补偿金
					if ("1".equals(isSubsidySend) && !empSubsidyList.contains(empNo)) {
						salaryMap.put("subsidyBefTax", subsidyBefTax);
						salaryMap.put("subsidyBefTaxDeduct", subsidyBefTaxDeduct);
						salaryMap.put("subsidyRcvNoTax", subsidyRcvNoTax);
						salaryMap.put("subsidyDeductTax", subsidyDeductTax);
						salaryMap.put("subsidyTotal", subsidyTotal);
						salaryMap.put("subsidyTax", subsidyTax);
						salaryMap.put("subsidyShouldPay", subsidyShouldPay);
						salaryMap.put("subsidyDeductPay", subsidyDeductPay);
						salaryMap.put("subsidyRealPay", subsidyRealPay);
						salaryMap.put("isSubsidySend", isSubsidySend);// 是否要发经济补偿金
						empSubsidyList.add(empNo);
					}

					// 调整薪酬数据
					adjustSalaryInfo(salaryMap, isSalarySend, isBonusSend, isStockSend);

					// 实际发薪日期
					Date salaryDate = null;
					Date bonusDate = null;
					Date stockDate = null;
					Date subsidyDate = null;

					if ("1".equals(isSalarySend)) {
						if (Constants.PAYROLL_TYPE_SALARY.equals(payrollType)) {
							kind = Constants.KIND_SALARY;
						}
						else if (Constants.PAYROLL_TYPE_REMUNERATION.equals(payrollType)) {
							kind = Constants.KIND_REMUNERATION;
						}
						salaryDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm,
								kind, batchNo);
					}
					if ("1".equals(isBonusSend)) {
						kind = Constants.KIND_BONUS;
						bonusDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm,
								kind, batchNo);
					}
					if ("1".equals(isStockSend)) {
						kind = Constants.KIND_STOCK;
						stockDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm,
								kind, batchNo);
					}
					if ("1".equals(isSubsidySend)) {
						kind = Constants.KIND_SUBSIDY;
						subsidyDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm,
								kind, batchNo);
					}

					salaryMap.put("REAL_SEND_DATE",
							getMaxRealPayDate(salaryDate, bonusDate, stockDate, subsidyDate));
					// 预计发薪日期
					// ***** EPAY2.0 BEGIN *******	
//					salaryMap.put("PRE_SEND_DATE", salaryServerDao.getPreSendDate(accYm, companyNo));
					salaryMap.put("PRE_SEND_DATE", salaryServerDao.getPreSendDate(realAccYm, companyNo));
					// ***** EPAY2.0 END *******	
					// ***** No：S20151312 BEGIN *******
					// 外划工资的预计发薪日当实际发薪日期处理
					if ("1".equals(isEntrust)) {
						DateFormat df = new SimpleDateFormat("yyyyMMdd");
						// ***** EPAY2.0 BEGIN *******	
//						salaryMap.put("REAL_SEND_DATE", 
//							   df.parse(salaryServerDao.getPreSendDate(accYm, companyNo)));
						salaryMap.put("REAL_SEND_DATE",
								df.parse(salaryServerDao.getPreSendDate(realAccYm, companyNo)));
						// ***** EPAY2.0 END *******	
					}
					// ***** No：S20151312 END *******
					// 商社名称
					salaryMap.put("COMPANY_NAME", clientInfo.getCompanyName());
					//商社中文全称
					salaryMap.put("NAME_CH", clientInfo.getCompanyNameCh());
					//商社英文全称
					salaryMap.put("NAME_EN", clientInfo.getCompanyNameEn());
//					// 销售组
//					salaryMap.put("SALE_GRP_CODE", clientInfo.getSaleGrpCode());
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
					// 发送日期类型
					salaryMap.put("SEND_DATE_TYPE", clientConfigInfo.getSendDateType());
					// 模板序号
					salaryMap.put("FORMAT_SNO", clientConfigInfo.getFormatSno());

					// 雇员所在部门名
					String departNo = salaryMap.get("DEPART_NO") != null ? salaryMap.get(
							"DEPART_NO").toString() : "";
					salaryMap.put("DEPART_NAME", salaryServerDao.getNameByDepartNo(departNo));

					// 雇员银行卡号
					salaryMap.put("BANK_ACCOUNT", salaryServerDao.getBankAccountByEmpNo(empNo));

					// 雇员成本中心
					salaryMap.put("COST_CENTER", salaryServerDao.getCostCenter(companyNo, empNo));

					// 查询取得雇员工资项目
					// ***** EPAY2.0 BEGIN *******
//					List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
//							accYm);
					List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
							realAccYm);
					// ***** EPAY2.0 END *******
					salaryMap.put("SALARY TITLE", salaryTitleList);

					// 税前应发合计
					salaryMap.put("PRETAX PAY COUNT",
							getSalaryCount(salaryMap, companyNo, "1", "1", "1"));

					// 税前应扣合计
					salaryMap.put("PRETAX DEDUCT COUNT",
							getSalaryCount(salaryMap, companyNo, "-1", "-1", "-1"));

					// 仅计税不发放工资合计
					salaryMap.put("ONLY ADD COUNT",
							String.valueOf(Double.valueOf(getSalaryCount(salaryMap, companyNo, "0", "1", "0"))
							+ Double.valueOf((salaryMap.get("CALC_FIELD3") == null ? "0.00" : salaryMap.get("CALC_FIELD3") + ""))));

					// 仅抵扣计税项合计
					salaryMap.put("ONLY DEDUCT COUNT",
							getSalaryCount(salaryMap, companyNo, "0", "-1", "0"));

					// 税后应发合计
					salaryMap.put("REAL PAY COUNT",
							getSalaryCount(salaryMap, companyNo, "1", "0", "1"));

					// 税后应扣合计
					salaryMap.put("REAL DEDUCT COUNT",
							getSalaryCount(salaryMap, companyNo, "-1", "0", "-1"));

					// ***** No：S20151358 BEGIN *******
					Map<String, Object> datePeriodMap = salaryServerDao.getSalaryPeriod(companyNo,
							realAccYm);

					if (datePeriodMap.get("BEGIN_DATE") != null) {
						String beginDate = datePeriodMap.get("BEGIN_DATE").toString();
						String endDate = datePeriodMap.get("END_DATE").toString();

						salaryMap.put("PAYROLL_PERIOD", beginDate + " - " + endDate);
					}
					else {
						salaryMap.put("PAYROLL_PERIOD", salaryMap.get("REAL_ACC_YM").toString());
					}
					// ***** No：S20151358 END *******

					// 雇员密码
					List<Map<String, Object>> empPwdList = salaryServerDao.getEmpPwd(empNo);
					if (empPwdList.size() > 0) {
						Map<String, Object> empPwdMap = empPwdList.get(0);
						String empPwd = Base64Util.getFromBase64(empPwdMap.get("EMP_PWD").toString());
						salaryMap.put("EMPLOYEE_PWD", empPwd);
					}
					else {
						salaryMap.put("EMPLOYEE_PWD", "");
					}

					selectedSalaryInfoList.add(salaryMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return selectedSalaryInfoList;
	}

	/**
	 * 取得工资项目的合计
	 * 
	 * @param salaryInfoList
	 * @param result
	 * @throws Exception
	 */
	// ***** EPAY2.0 BEGIN *******
	private String getSalaryCount(Map<String, Object> salaryMap, String companyNo,
			String addFlag, String taxFlag, String wfFlag) throws Exception {
//		List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
//				salaryMap.get("ACC_YM").toString(), addFlag, taxFlag, wfFlag);
		List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
				salaryMap.get("REAL_ACC_YM").toString(), addFlag, taxFlag, wfFlag);
		// ***** EPAY2.0 END *******
		
		float salaryCount = 0.00f;
		if (salaryTitleList != null) {
			for (SalaryTitleBean salaryTitleBean : salaryTitleList) {
				Object salaryObj = salaryMap.get(salaryTitleBean.getFieldname());
				if (salaryObj != null) {
					salaryCount += Float.parseFloat(salaryObj.toString());
				}
			}
		}

		return String.valueOf(salaryCount);
	}

	/**
	 * 追加邮件发送结果信息表中的记录
	 * 
	 * @param salaryInfoList
	 * @param result
	 */
	private void insertMailResultInfo(List<Map<String, Object>> salaryInfoList,String mailTitle) {

		for (Map<String, Object> salaryInfo : salaryInfoList) {

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
			if (bonus > 0) {
				if ("3".equals(salaryInfo.get("SEND_DATE_TYPE"))
						// ***** S20160161 BEGIN *******
						|| "4".equals(salaryInfo.get("SEND_DATE_TYPE"))) {
						// ***** S20160161 END    *******
					if (bonusCardSno > 0) {
						isYearBonusSend = Constants.SUCCESS;
					} else {
						isYearBonusSend = Constants.FAILURE;
					}
				} else {
					isYearBonusSend = Constants.SUCCESS;
				}
			} else {
				isYearBonusSend = Constants.FAILURE;
			}

			// 是否已发送股权激励
			String isStockSend = "";
			Float stock = new Float(0.0f);
			Integer stockCardSno = new Integer(0);
			if (salaryInfo.get("STOCK") != null) {
				stock = Float.parseFloat(salaryInfo.get("STOCK").toString());
			}
			if (salaryInfo.get("STOCK_CARD_SNO") != null) {
				stockCardSno = Integer.parseInt(salaryInfo.get("STOCK_CARD_SNO").toString());
			}
			if (stock > 0) {
				if ("3".equals(salaryInfo.get("SEND_DATE_TYPE"))
						// ***** S20160161 BEGIN *******
						|| "4".equals(salaryInfo.get("SEND_DATE_TYPE"))) {
						// ***** S20160161 END    *******
					if (stockCardSno > 0) {
						isStockSend = Constants.SUCCESS;
					} else {
						isStockSend = Constants.FAILURE;
					}
				} else {
					isStockSend = Constants.SUCCESS;
				}
			} else {
				isStockSend = Constants.FAILURE;
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
				if ("3".equals(salaryInfo.get("SEND_DATE_TYPE"))
						// ***** S20160161 BEGIN *******
						|| "4".equals(salaryInfo.get("SEND_DATE_TYPE"))) {
						// ***** S20160161 END    *******
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

			// 是否已发送经济补偿金
			String isRelasePaySend = salaryInfo.get("isSubsidySend") != null ? (String) salaryInfo
					.get("isSubsidySend") : "0";

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
		List<EmailModel> denyList = result.getDenyList();
		Long mailBatchNo = result.getBatchNo();
		String bizType = Constants.BIZ_TYPE;

		for (Map<String, Object> salaryInfo : salaryInfoList) {

			// 邮件发送状态
			String sendSuccess = Constants.MAIL_SEND_SUCCEED;

			// 失败原因
			String failReason = "";

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

				for (EmailModel denyEmp : denyList) {
					if (empNo.equals(denyEmp.getRcvId())) {
						sendSuccess = Constants.MAIL_BLACKLIST;
						failReason = denyEmp.getEmailStatus().getDenyRemarks();
						break;
					}
				}

				salaryServerDao.updateMailResultInfo(sendSuccess, empNo, failReason, bizType,
						mailBatchNo, filePath);
			}
		}
	}

	/**
	 * 发送提醒邮件给业务员
	 * 
	 * @param companyNo
	 * @param result
	 * @throws SQLException
	 */
	private void sendWarningMail(String companyNo, List<Map<String, Object>> salaryInfoList,
			EmailBatchStatus result) throws SQLException {

		List<Map<String, String>> failEmpList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> denyEmpList = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;

		// 发送失败或未发送人员录入人list
		List<String> inputPersonList = new ArrayList<String>();

		// 录入人所对应的发送失败或未发送人员list
		List<List<Map<String, String>>> empMapList = new ArrayList<List<Map<String, String>>>();

		for (Map<String, Object> salaryInfo : salaryInfoList) {
			String mail = salaryInfo.get("MAIL") != null ? salaryInfo.get("MAIL").toString() : "";
			String empNo = salaryInfo.get("EMP_NO").toString();
			// 邮件未发送（邮箱为空）时
			if (StringUtils.isEmpty(mail)) {
				map = new HashMap<String, String>();
				map.put("COMPANY_NO",  salaryInfo.get("COMPANY_NO").toString());
				map.put("COMPANY_NAME",  salaryInfo.get("COMPANY_NAME").toString());
				map.put("SALES",
						salaryInfo.get("USER_NAME") == null ? "" : salaryInfo.get("USER_NAME").toString());
				map.put("EMP_NO", salaryInfo.get("EMP_NO").toString());
				map.put("EMP_NAME", salaryInfo.get("Chinese Name").toString());
				map.put("REAL_ACC_YM", salaryInfo.get("REAL_ACC_YM").toString());
				map.put("MAIL", mail);
				map.put("STATUS", Constants.MAIL_NOT_SEND_NM);
				denyEmpList.add(map);

				String inputNewPerson = salaryInfo.get("INPUT_PERSON") == null?
						"" : salaryInfo.get("INPUT_PERSON").toString();
				if (!StringUtils.isEmpty(inputNewPerson)) {
					if (!inputPersonList.contains(inputNewPerson)) {
						inputPersonList.add(salaryInfo.get("INPUT_PERSON").toString());
						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						list.add(map);
						empMapList.add(list);
					}
					else {
						for (int i = 0; i<inputPersonList.size(); i++) {
							String inputPerson = inputPersonList.get(i).toString();
							if (inputPerson.equals(inputNewPerson)) {
								List<Map<String, String>> list = empMapList.get(i);
								list.add(map);
							}
						}
					}
				}
			} else {
				if (result != null) {
					List<EmailModel> failList = result.getFailList();

					// 邮件发送失败时
					for (EmailModel failEmp : failList) {
						if (empNo.equals(failEmp.getRcvId())) {
							map = new HashMap<String, String>();
							map.put("COMPANY_NO",  salaryInfo.get("COMPANY_NO").toString());
							map.put("COMPANY_NAME",  salaryInfo.get("COMPANY_NAME").toString());
							map.put("SALES",
									salaryInfo.get("USER_NAME") == null ? "" : salaryInfo.get("USER_NAME").toString());
							map.put("EMP_NO", empNo);
							map.put("EMP_NAME", salaryInfo.get("Chinese Name").toString());
							map.put("REAL_ACC_YM", salaryInfo.get("REAL_ACC_YM").toString());
							map.put("MAIL", mail);
							map.put("STATUS", Constants.MAIL_SEND_FAILURE_NM);
							failEmpList.add(map);
	
							String inputNewPerson = salaryInfo.get("INPUT_PERSON") == null?
									"" : salaryInfo.get("INPUT_PERSON").toString();
							if (!StringUtils.isEmpty(inputNewPerson)) {
								if (!inputPersonList.contains(inputNewPerson)) {
									inputPersonList.add(salaryInfo.get("INPUT_PERSON").toString());
									List<Map<String, String>> list = new ArrayList<Map<String, String>>();
									list.add(map);
									empMapList.add(list);
								}
								else {
									for (int i = 0; i<inputPersonList.size(); i++) {
										String inputPerson = inputPersonList.get(i).toString();
										if (inputPerson.equals(inputNewPerson)) {
											List<Map<String, String>> list = empMapList.get(i);
											list.add(map);
										}
									}
								}
							}
	
							break;
						}
					}
				}
			}
		}

		if (failEmpList.size() > 0 || denyEmpList.size() > 0) {
			// 取得负责该商社的业务员邮箱
			List<Map<String, Object>> saleList = salaryServerDao.getSaleInfo(companyNo);

			// 发送邮件给业务员
			SendEmail.sendEmailtoSale(saleList, failEmpList, denyEmpList);

			if (inputPersonList.size() > 0) {
				// 取得录入人邮箱
				List<List<Map<String, Object>>> mailList = new ArrayList<List<Map<String, Object>>>();
				for (String name : inputPersonList) {
					List<Map<String, Object>> list = salaryServerDao.getInputPersonMail(name);
					mailList.add(list);
				}
	
				// 发送邮件给录入人
				SendEmail.sendEmailtoInputPerson(saleList.get(0), inputPersonList, empMapList, mailList);
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
	 * 模板数据整理
	 */
	private List<SalaryTempletData> dealSalaryInfo(List<Map<String, Object>> salaryInfoList,
			FormatInfo formatInfo, ClientConfigInfo clientConfigInfo) {
		List<SalaryTempletData> SalaryTempletDataList = new ArrayList<SalaryTempletData>();
		for (int i = 0; i < salaryInfoList.size(); i++) {
			// 客户配置信息
			SalaryTempletData std = new SalaryTempletData();
			// 发送日期
			String sendDate = "";
			String preSendDate = "";
			String realSendDate = "";
			String preSendDateIndex = salaryInfoList.get(i).get("PRE_SEND_DATE") + "";
			Date realSendDateIndex = (Date) salaryInfoList.get(i).get("REAL_SEND_DATE");
			DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
			DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (!preSendDateIndex.equals("null")) {
					Date date1 = df1.parse(preSendDateIndex);
					preSendDate = df2.format(date1);
				}
				if (realSendDateIndex != null) {
					realSendDate = df2.format(realSendDateIndex);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if ("1".equals(clientConfigInfo.getSendDateType())
					|| "2".equals(clientConfigInfo.getSendDateType())
					// ***** S20160161 BEGIN *******
					|| "5".equals(clientConfigInfo.getSendDateType())) {
					// ***** S20160161 END   *******
				sendDate = preSendDate;
			} else if ("3".equals(clientConfigInfo.getSendDateType())
					// ***** S20160161 BEGIN *******
					|| "4".equals(clientConfigInfo.getSendDateType())) {
					// ***** S20160161 END    *******
				sendDate = realSendDate;
			}
			// 薪资期间
			// ***** No：S20151358 BEGIN *******
//			String datePeriod = (String) salaryInfoList.get(i).get("REAL_ACC_YM");
			String datePeriod = (String) salaryInfoList.get(i).get("PAYROLL_PERIOD");
			// ***** No：S20151358 END *******
			// 1、基本信息
			List<TempletBaseInfo> firstData = new ArrayList<TempletBaseInfo>();
			List<BaseItemConfig> bicList = salaryServerDao.getBaseItemConfig(clientConfigInfo
					.getFormatSno());
			// 职位显示字段
			Map<String, String> display = new HashMap<String, String>();
			display.put("显示字段1", salaryInfoList.get(i).get("DISPLAY1") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY1").toString());
			display.put("显示字段2", salaryInfoList.get(i).get("DISPLAY2") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY2").toString());
			display.put("显示字段3", salaryInfoList.get(i).get("DISPLAY3") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY3").toString());
			display.put("显示字段4", salaryInfoList.get(i).get("DISPLAY4") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY4").toString());
			display.put("显示字段5", salaryInfoList.get(i).get("DISPLAY5") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY5").toString());
			display.put("显示字段6", salaryInfoList.get(i).get("DISPLAY6") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY6").toString());
			display.put("显示字段7", salaryInfoList.get(i).get("DISPLAY7") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY7").toString());
			display.put("显示字段8", salaryInfoList.get(i).get("DISPLAY8") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY8").toString());
			display.put("显示字段9", salaryInfoList.get(i).get("DISPLAY9") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY9").toString());
			display.put("显示字段10", salaryInfoList.get(i).get("DISPLAY10") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY10").toString());
			display.put("显示字段11", salaryInfoList.get(i).get("DISPLAY11") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY11").toString());
			display.put("显示字段12", salaryInfoList.get(i).get("DISPLAY12") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY12").toString());
			display.put("显示字段13", salaryInfoList.get(i).get("DISPLAY13") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY13").toString());
			display.put("显示字段14", salaryInfoList.get(i).get("DISPLAY14") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY14").toString());
			display.put("显示字段15", salaryInfoList.get(i).get("DISPLAY15") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY15").toString());
			Map<String, Object> baseInfoMap = new HashMap<String, Object>();
			// 将显示字段display1-display25（701-725）放入基本信息
			baseInfoMap.put("60", salaryInfoList.get(i).get("DISPLAY1") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY1"));
			baseInfoMap.put("61", salaryInfoList.get(i).get("DISPLAY2") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY2"));
			baseInfoMap.put("62", salaryInfoList.get(i).get("DISPLAY3") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY3"));
			baseInfoMap.put("63", salaryInfoList.get(i).get("DISPLAY4") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY4"));
			baseInfoMap.put("64", salaryInfoList.get(i).get("DISPLAY5") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY5"));
			baseInfoMap.put("65", salaryInfoList.get(i).get("DISPLAY6") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY6"));
			baseInfoMap.put("66", salaryInfoList.get(i).get("DISPLAY7") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY7"));
			baseInfoMap.put("67", salaryInfoList.get(i).get("DISPLAY8") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY8"));
			baseInfoMap.put("68", salaryInfoList.get(i).get("DISPLAY9") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY9"));
			baseInfoMap.put("69", salaryInfoList.get(i).get("DISPLAY10") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY10"));
			baseInfoMap.put("101", salaryInfoList.get(i).get("DISPLAY11") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY11"));
			baseInfoMap.put("102", salaryInfoList.get(i).get("DISPLAY12") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY12"));
			baseInfoMap.put("103", salaryInfoList.get(i).get("DISPLAY13") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY13"));
			baseInfoMap.put("104", salaryInfoList.get(i).get("DISPLAY14") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY14"));
			baseInfoMap.put("105", salaryInfoList.get(i).get("DISPLAY15") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY15"));
			baseInfoMap.put("106", salaryInfoList.get(i).get("DISPLAY16") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY16"));
			baseInfoMap.put("107", salaryInfoList.get(i).get("DISPLAY17") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY17"));
			baseInfoMap.put("108", salaryInfoList.get(i).get("DISPLAY18") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY18"));
			baseInfoMap.put("109", salaryInfoList.get(i).get("DISPLAY19") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY19"));
			baseInfoMap.put("110", salaryInfoList.get(i).get("DISPLAY20") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY20"));
			baseInfoMap.put("131", salaryInfoList.get(i).get("DISPLAY21") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY21"));
			baseInfoMap.put("132", salaryInfoList.get(i).get("DISPLAY22") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY22"));
			baseInfoMap.put("133", salaryInfoList.get(i).get("DISPLAY23") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY23"));
			baseInfoMap.put("134", salaryInfoList.get(i).get("DISPLAY24") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY24"));
			baseInfoMap.put("135", salaryInfoList.get(i).get("DISPLAY25") == null ? ""
					: salaryInfoList.get(i).get("DISPLAY25"));
			baseInfoMap.put("92", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ANNUITY_COMP")));
			// 将调派四金放进基本信息（801-806）
			baseInfoMap.put("801", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("OEP_C")));// 养老
			baseInfoMap.put("802", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("MEDICAL_C")));// 医疗
			baseInfoMap.put("803", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("LOSE_JOB_C")));// 失业
			baseInfoMap.put("804", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("INJURY_INSU")));// 工伤
			baseInfoMap.put("805", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("CHILDBIRTH_INSU")));// 生育
			baseInfoMap.put("806", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ACCFUND_C")));// 单位公积金
			// 把基本信息itemNo提取数据放入map
			baseInfoMap.put("1901", salaryInfoList.get(i).get("NAME") == null ? "" : salaryInfoList
					.get(i).get("NAME"));
			baseInfoMap.put("1902", salaryInfoList.get(i).get("EMPLOYEE_CODE") == null ? ""
					: salaryInfoList.get(i).get("EMPLOYEE_CODE"));
			baseInfoMap.put("1903", salaryInfoList.get(i).get("EMP_NO") == null ? ""
					: salaryInfoList.get(i).get("EMP_NO"));
			String bankAccountIndex = salaryInfoList.get(i).get("BANK_ACCOUNT") + "";
			if (!"".equals(bankAccountIndex) && !"null".equals(bankAccountIndex) && bankAccountIndex.length() >= 4) {
				String bankAccountIndex2 = bankAccountIndex.substring(0,
						bankAccountIndex.length() - 4);
				String bankAccount = bankAccountIndex2 + "****";
				baseInfoMap.put("1904", bankAccount);
			} else {
				baseInfoMap.put("1904", "");
			}
			baseInfoMap.put("1905", salaryInfoList.get(i).get("DEPART_NAME") == null ? ""
					: salaryInfoList.get(i).get("DEPART_NAME"));
			baseInfoMap.put("1906", salaryInfoList.get(i).get("COST_CENTER") == null ? ""
					: salaryInfoList.get(i).get("COST_CENTER"));
			baseInfoMap.put("1907", salaryInfoList.get(i).get("WORK_PLACE") == null ? ""
					: salaryInfoList.get(i).get("WORK_PLACE"));
			if (!StringUtils.isEmpty(display.get(formatInfo.getRemarks()))
					&& !"null".equals(display.get(formatInfo.getRemarks()))) {
				baseInfoMap.put("1908", display.get(formatInfo.getRemarks()));// 职位
			} else {
				baseInfoMap.put("1908", "");
			}
			baseInfoMap.put("1909", salaryInfoList.get(i).get("ID") == null ? ""
					: salaryInfoList.get(i).get("ID"));
			//基本信息英文显示字段
			Map<String,String> baseInfoMapEn=new HashMap<String,String>();
			//基本信息
			baseInfoMapEn.put("1901", "Name");
			baseInfoMapEn.put("1902", "Company No");
			baseInfoMapEn.put("1903", "Employee No");
			baseInfoMapEn.put("1904", "Bank Account");
			baseInfoMapEn.put("1905", "Department Name");
			baseInfoMapEn.put("1906", "Cost Center");
			baseInfoMapEn.put("1907", "Work Place");
			baseInfoMapEn.put("1908", "Position");
			baseInfoMapEn.put("1909", "ID No.");
			//调派四金
			baseInfoMapEn.put("801", "Pension Insu-CO.");
			baseInfoMapEn.put("802", "Medical Insu-CO.");
			baseInfoMapEn.put("803", "Unemployment Insu-CO.");
			baseInfoMapEn.put("804", "Work Injury Insu-CO.");
			baseInfoMapEn.put("805", "Maternity Insu-CO.");
			baseInfoMapEn.put("806", "Housing Fund-CO.");
			baseInfoMapEn.put("807", "Tax Adjustment");
			//显示字段
			// ***** EPAY2.0 BEGIN *******
//			List<Map<String, Object>> baseInfoMapEnList = salaryServerDao.getbaseInfoNameEn(
//					clientConfigInfo.getCompanyNo(), salaryInfoList.get(i).get("ACC_YM") + "");
			List<Map<String, Object>> baseInfoMapEnList = salaryServerDao.getbaseInfoNameEn(
					clientConfigInfo.getCompanyNo(), salaryInfoList.get(i).get("REAL_ACC_YM") + "");
			// ***** EPAY2.0 END *******
			for (int b = 0; b < baseInfoMapEnList.size(); b++) {
				baseInfoMapEn.put(baseInfoMapEnList.get(b).get("ITEM_CODE") + "", baseInfoMapEnList
						.get(b).get("ITEM_NAME_EN") + "");
			}
			TempletBaseInfo tbi = null;
			for (int j = 0; j < bicList.size(); j++) {
				tbi = new TempletBaseInfo();
				tbi.setIsShow(Integer.valueOf(bicList.get(j).getIsNullDisplay()));
				tbi.setItemCode(bicList.get(j).getItemNo());
				if ("1".equals(clientConfigInfo.getItemNameType())) {
					tbi.setTitleValue(bicList.get(j).getDisplayName());
				}
                if ("2".equals(clientConfigInfo.getItemNameType())) {
					tbi.setTitleValue(baseInfoMapEn.get(bicList.get(j).getItemNo()));
				}
				tbi.setItemPosition(bicList.get(j).getItemPosition());
				tbi.setItemValue(baseInfoMap.get(bicList.get(j).getItemNo()) + "");
				firstData.add(tbi);
			}
			// 标准模板
			if ("1".equals(formatInfo.getFormatType())) {
				// 2、工资信息
				List<TempletSalaryInfo> secondData = new ArrayList<TempletSalaryInfo>();
				List<TempletSalaryItemInfo> secondItemData = new ArrayList<TempletSalaryItemInfo>();
				List<SalaryFieldConfig> sfcList = salaryServerDao.getSalaryFieldConfig(formatInfo
						.getFormatSno());
				List<SalaryItemConfig> sicList = salaryServerDao.getSalaryItemConfig(formatInfo
						.getFormatSno());

				// 把数据整理进去map
				Map<Integer, Object> salaryInfoMap = new HashMap<Integer, Object>();
				salaryInfoMap.put(1, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("PRETAX PAY COUNT")));// 税前应发
				salaryInfoMap.put(2, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("PRETAX DEDUCT COUNT")));// 税前应扣
				salaryInfoMap.put(3, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ONLY ADD COUNT")));// 仅计税不发放工资项目
				salaryInfoMap.put(4, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ONLY DEDUCT COUNT")));// 仅抵扣计税项目
				salaryInfoMap.put(5, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("TAX_SALARY")));// 计税总额
				salaryInfoMap.put(6,
						salaryInfoList.get(i).get("ADJUST_TAX") == null ? (ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("TAX")))
								: ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ADJUST_TAX")));// 个人所得税
				salaryInfoMap.put(7, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("REAL PAY COUNT")));// 税后应发
				salaryInfoMap.put(8, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("REAL DEDUCT COUNT")));// 税后应扣
				salaryInfoMap.put(9, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("REAL_PAY")));// 实发工资
				salaryInfoMap.put(10, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS")));// 税前应发年终奖
				salaryInfoMap.put(11, "0.00");// 税前应扣年终奖
				salaryInfoMap.put(12, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_NO_PAY")));// 仅计税不发放年终奖
				salaryInfoMap.put(13, "0.00");// 仅抵扣计税项
				salaryInfoMap
						.put(14,
								ObjectUtil.objectIsNull2(Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS")))
										+ Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_NO_PAY")))));// 计税年终奖总额
				salaryInfoMap.put(15,
						salaryInfoList.get(i).get("BONUS_ADJUST_TAX") == null ? (ObjectUtil.objectIsNull2(salaryInfoList
								.get(i).get("BONUS_TAX"))) : ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_ADJUST_TAX")));// 年终奖个人所得税
				/*salaryInfoMap
						.put(16,
								Double.valueOf(salaryInfoList.get(i).get("BONUS_REAL_PAY") == null ? "0.00"
										: salaryInfoList.get(i).get("BONUS_REAL_PAY") + "")
										+ Double.valueOf(salaryInfoList.get(i).get(
												"BONUS_FREE_TAX_DEDUCT") == null ? "0.00"
												: salaryInfoList.get(i)
														.get("BONUS_FREE_TAX_DEDUCT") + ""));// 税后应发年终奖*/
				salaryInfoMap.put(16, "0.00");//不是一个计算项目，取值为0.00
				salaryInfoMap.put(17, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_FREE_TAX_DEDUCT")));// 税后应扣年终奖
				salaryInfoMap.put(18, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_REAL_PAY")));// 实发年终奖
				salaryInfoMap.put(19, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK")));// 税前应发股权激励
				salaryInfoMap.put(20, "0.00");// 税前应扣股权激励
				salaryInfoMap.put(21, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_NO_PAY")));// 仅计税不发放股权激励
				salaryInfoMap.put(22, "0.00");// 仅抵扣计税项
				salaryInfoMap
						.put(23,
								ObjectUtil.objectIsNull2(Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK")))
										+ Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_NO_PAY")))));// 计税股权激励总额
				salaryInfoMap.put(24,
						salaryInfoList.get(i).get("STOCK_ADJUST_TAX") == null ? (ObjectUtil.objectIsNull2(salaryInfoList
								.get(i).get("STOCK_TAX"))) : ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_ADJUST_TAX")));// 股权激励个人所得税
				/*salaryInfoMap
						.put(25,
								Double.valueOf(salaryInfoList.get(i).get("STOCK_REAL_PAY") == null ? "0.00"
										: salaryInfoList.get(i).get("STOCK_REAL_PAY") + "")
										+ Double.valueOf(salaryInfoList.get(i).get(
												"STOCK_FREE_TAX_DEDUCT") == null ? "0.00"
												: salaryInfoList.get(i)
														.get("STOCK_FREE_TAX_DEDUCT") + ""));// 税后应发股权激励*/
				salaryInfoMap.put(25, "0.00");//不是一个计算项目，取值为0.00
				salaryInfoMap.put(26,
						ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_FREE_TAX_DEDUCT")));// 税后应扣股权激励
				salaryInfoMap.put(27, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_REAL_PAY")));// 实发股权激励
				salaryInfoMap.put(28, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyBefTax")));// 税前应发经济补偿金...
			    salaryInfoMap.put(29, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyBefTaxDeduct")));// 税前应扣经济补偿金...
			    salaryInfoMap.put(30, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyRcvNoTax")));// 仅计税不发放经济补偿金...
			    salaryInfoMap.put(31, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyDeductTax")));// 仅抵扣计税项...
			    salaryInfoMap.put(32, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyTotal")));// 计税经济补偿金总额（A-B+C-D）...
			    salaryInfoMap.put(33, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyTax")));// 经济补偿金个人所得税...
//			    salaryInfoMap.put(34, salaryInfoList.get(i).get("subsidyShouldPay")==null?"0.00":salaryInfoList.get(i).get("subsidyShouldPay").toString());// 税后应发经济补偿金...
			    salaryInfoMap.put(34, "0.00");//不是一个计算项目，取值为0.00
			    salaryInfoMap.put(35, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyDeductPay")));// 税后应扣经济补偿金...
			    salaryInfoMap.put(36, ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyRealPay")));// 实发经济补偿金（A-B-F+G-H）...
			    salaryInfoMap.put(37, "0.00");// other...
			    
			    List<Map<String, Object>> fieldEnList=salaryServerDao.getFieldNameEn();
			    Map<String,String> fieldEnMap=new HashMap<String,String>();
			    for(int b=0;b<fieldEnList.size();b++){
			    	fieldEnMap.put(fieldEnList.get(b).get("CODE")+"",fieldEnList.get(b).get("CODE_DESC_1_CONTENT")+"");
			    }
				TempletSalaryInfo tsi = null;
				for (int j = 0; j < sfcList.size(); j++) {
					if("CA0292".equals(clientConfigInfo.getCompanyNo())
							&& (sfcList.get(j).getFieldNo().intValue()==3||sfcList.get(j).getFieldNo().intValue()==4)){
					      continue;
					}
					tsi = new TempletSalaryInfo();
					tsi.setBigTitlePosition(sfcList.get(j).getFieldNo());
					//中文大标题
				     if ("1".equals(clientConfigInfo.getItemNameType())) {
					     if("CA0292".equals(clientConfigInfo.getCompanyNo())&&(sfcList.get(j).getFieldNo().intValue()==5)){
					    	tsi.setBigTitle(sfcList.get(j).getDisplayName()==null?"":sfcList.get(j).getDisplayName().replace("(A-B+C-D)", ""));
					     }else{
					     	tsi.setBigTitle(sfcList.get(j).getDisplayName());
					     }
				     }
				     //英文大标题
				     if ("2".equals(clientConfigInfo.getItemNameType())) {
					     if("CA0292".equals(clientConfigInfo.getCompanyNo())&&(sfcList.get(j).getFieldNo().intValue()==5)){
					     	tsi.setBigTitle(fieldEnMap.get(sfcList.get(j).getFieldNo().toString())==null?"":fieldEnMap.get(sfcList.get(j).getFieldNo().toString()).replace("(A-B+C-D)", ""));
					     }else{
					     	tsi.setBigTitle(fieldEnMap.get(sfcList.get(j).getFieldNo().toString()));
					     }
				     }
					tsi.setBigRemarks(sfcList.get(j).getRemarks());
					tsi.setBigSum(ObjectUtil.objectIsNull2(salaryInfoMap.get(sfcList.get(j).getFieldNo())));
					tsi.setIsCount(sfcList.get(j).getIsCount());
					secondData.add(tsi);
				}
				// 处理数据
				List<SalaryTitleBean> salaryTitleList = (List<SalaryTitleBean>) salaryInfoList.get(
						i).get("SALARY TITLE");
				Map<String, String> itemTitle = new HashMap<String, String>();
				Map<String, String> itemValue = new HashMap<String, String>();

				// 工资项目数据
				String strIndex = "";
				for (int k = 0; k < salaryTitleList.size(); k++) {
					if ("1".equals(clientConfigInfo.getItemNameType())) {
						itemTitle.put(salaryTitleList.get(k).getItemCode(), salaryTitleList.get(k)
								.getItemName());
					}
					if ("2".equals(clientConfigInfo.getItemNameType())) {
						itemTitle.put(salaryTitleList.get(k).getItemCode(), salaryTitleList.get(k)
								.getItemNameEn());
					}
					if (Pattern.compile("^DISPLAY.*")
							.matcher(salaryTitleList.get(k).getFieldname()).matches() == true) {
						strIndex = (salaryInfoList.get(i).get(salaryTitleList.get(k).getFieldname()
								+ "")) == null ? "" : salaryInfoList.get(i).get(
								salaryTitleList.get(k).getFieldname() + "")
								+ "";
					} else {
						strIndex = ObjectUtil.objectIsNull2(salaryInfoList.get(i).get(salaryTitleList.get(k).getFieldname()
								+ ""));
					}
//					if (Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")
//							.matcher(strIndex).matches() == true
//							&& salaryTitleList.get(k).getAddFlag() != 0
//							&& Double.valueOf(strIndex) != 0) {
//						itemValue.put(salaryTitleList.get(k).getItemCode(),
//								(Double.valueOf(strIndex) * salaryTitleList.get(k).getAddFlag())
//										+ "");
//					} else {
						itemValue.put(salaryTitleList.get(k).getItemCode(), strIndex);
//					}

				}
				/*
				 * 其他信息
				 */
				// 个人基本信息
				if ("1".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("1901", "姓名");
					itemTitle.put("1902", "公司工号");
					itemTitle.put("1903", "外服工号");
					itemTitle.put("1904", "银行卡号");
					itemTitle.put("1905", "部门名");
					itemTitle.put("1906", "成本中心");
					itemTitle.put("1907", "工作地");
					itemTitle.put("1908", "职位");
					itemTitle.put("1909", "证件号");
				}
				if ("2".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("1901", "Name");
					itemTitle.put("1902", "Company No");
					itemTitle.put("1903", "Employee No");
					itemTitle.put("1904", "Bank Account");
					itemTitle.put("1905", "Department Name");
					itemTitle.put("1906", "Cost Center");
					itemTitle.put("1907", "Work Place");
					itemTitle.put("1908", "Position");
					itemTitle.put("1909", "ID No.");
				}

				itemValue.put("1901", baseInfoMap.get("1901") + "");
				itemValue.put("1902", baseInfoMap.get("1902") + "");
				itemValue.put("1903", baseInfoMap.get("1903") + "");
				itemValue.put("1904", baseInfoMap.get("1904") + "");
				itemValue.put("1905", baseInfoMap.get("1905") + "");
				itemValue.put("1906", baseInfoMap.get("1906") + "");
				itemValue.put("1907", baseInfoMap.get("1907") + "");
				itemValue.put("1908", baseInfoMap.get("1908") + "");
				itemValue.put("1909", baseInfoMap.get("1909") + "");

				// 调派四金
				if ("1".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("801", "单位部分养老");
					itemTitle.put("802", "单位部分医疗");
					itemTitle.put("803", "单位部分失业");
					itemTitle.put("804", "单位工伤保险");
					itemTitle.put("805", "单位生育保险");
					itemTitle.put("806", "单位公积金");
					itemTitle.put("807", "计税调整项");
				}
				if ("2".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("801", "Pension Insu-CO.");
					itemTitle.put("802", "Medical Insu-CO.");
					itemTitle.put("803", "Unemployment Insu-CO.");
					itemTitle.put("804", "Work Injury Insu-CO.");
					itemTitle.put("805", "Maternity Insu-CO.");
					itemTitle.put("806", "Housing Fund-CO.");
					itemTitle.put("807", "Tax Adjustment");
				}
				itemValue.put("801", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("OEP_C")));// 养老
				itemValue.put("802", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("MEDICAL_C")));// 医疗
				itemValue.put("803", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("LOSE_JOB_C")));// 失业
				itemValue.put("804", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("INJURY_INSU")));// 工伤
				itemValue.put("805", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("CHILDBIRTH_INSU")));// 生育
				itemValue.put("806", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ACCFUND_C")));// 单位公积金
				itemValue.put("807", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("CALC_FIELD3")));// 计税调整项

				TempletSalaryItemInfo tsii = null;
				for (int j = 0; j < sicList.size(); j++) {
					tsii = new TempletSalaryItemInfo();
					tsii.setItemBelongPosition(sicList.get(j).getfieldNo());
					tsii.setItemCode(sicList.get(j).getItemCode().toString());
					tsii.setItemIsShow(Integer.valueOf(sicList.get(j).getIsZeroDisplay()));
					tsii.setItemPosition(sicList.get(j).getItemPosition());
					tsii.setItemTitle(itemTitle.get(sicList.get(j).getItemCode().toString()));
					tsii.setItemValue(itemValue.get(sicList.get(j).getItemCode().toString()));
					secondItemData.add(tsii);
				}
				// 个人备注
				String remarks = salaryInfoList.get(i).get("REMARKS") == null ? "" : salaryInfoList
						.get(i).get("REMARKS").toString();
				//模板显示中文名字
				String companyNameCh=ObjectUtil.ObjectIsNull(salaryInfoList.get(i).get("NAME_CH"));
				//模板显示英文名字
				String companyNameEn=ObjectUtil.ObjectIsNull(salaryInfoList.get(i).get("NAME_EN"));
				//logo下是否显示公司全称
				String isCompnameDisplay=ObjectUtil.ObjectIsNull(clientConfigInfo.getIsCompnameDisplay());
				// 添加到总实体
				std.setFormatInfo(formatInfo);
				std.setSendDate(sendDate);
				std.setDatePeriod(datePeriod);
				std.setRemarks(remarks);
				std.setfirstData(firstData);
				std.setSecondData(secondData);
				std.setSecondItemData(secondItemData);
				std.setItemNameType(clientConfigInfo.getItemNameType());
				std.setCompanyNameCh(companyNameCh);
				std.setCompanyNameEn(companyNameEn);
				std.setIsCompnameDisplay(isCompnameDisplay);
				SalaryTempletDataList.add(std);
				// 自定义模板
			} else if ("2".equals(formatInfo.getFormatType())) {
				// 2、工资信息
				List<TempletSalaryInfo> secondData = new ArrayList<TempletSalaryInfo>();
				List<TempletSalaryItemInfo> secondItemData = new ArrayList<TempletSalaryItemInfo>();
				List<SalaryFieldConfig> sfcList = salaryServerDao.getSalaryFieldConfig(Integer
						.valueOf(salaryInfoList.get(i).get("FORMAT_SNO") + ""));
				List<SalaryItemConfig> sicList = salaryServerDao.getSalaryItemConfig(Integer
						.valueOf(salaryInfoList.get(i).get("FORMAT_SNO") + ""));

				// 处理工资项目数据
				List<SalaryTitleBean> salaryTitleList = (List<SalaryTitleBean>) salaryInfoList.get(
						i).get("SALARY TITLE");
				Map<String, String> itemTitle = new HashMap<String, String>();
				Map<String, String> itemValue = new HashMap<String, String>();

				// 工资项目数据
				String strIndex = "";
				for (int k = 0; k < salaryTitleList.size(); k++) {
					if ("1".equals(clientConfigInfo.getItemNameType())) {
						itemTitle.put(salaryTitleList.get(k).getItemCode(), salaryTitleList.get(k)
								.getItemName());
					}
					if ("2".equals(clientConfigInfo.getItemNameType())) {
						itemTitle.put(salaryTitleList.get(k).getItemCode(), salaryTitleList.get(k)
								.getItemNameEn());
					}
					if (Pattern.compile("^DISPLAY.*")
							.matcher(salaryTitleList.get(k).getFieldname()).matches() == true) {
						strIndex = (salaryInfoList.get(i).get(salaryTitleList.get(k).getFieldname()
								+ "")) == null ? "" : salaryInfoList.get(i).get(
								salaryTitleList.get(k).getFieldname() + "")
								+ "";
					} else {
						strIndex = ObjectUtil.objectIsNull2((salaryInfoList.get(i).get(salaryTitleList.get(k).getFieldname()+ "")));
					}
//					if (Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")
//							.matcher(strIndex).matches() == true
//							&& salaryTitleList.get(k).getAddFlag() != 0
//							&& Double.valueOf(strIndex) != 0) {
//						itemValue.put(salaryTitleList.get(k).getItemCode(),
//								(Double.valueOf(strIndex) * salaryTitleList.get(k).getAddFlag())
//										+ "");
//					} else {
						itemValue.put(salaryTitleList.get(k).getItemCode(), strIndex);
//					}
				}
				// 个人基本信息
				if ("1".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("1901", "姓名");
					itemTitle.put("1902", "公司工号");
					itemTitle.put("1903", "外服工号");
					itemTitle.put("1904", "银行卡号");
					itemTitle.put("1905", "部门名");
					itemTitle.put("1906", "成本中心");
					itemTitle.put("1907", "工作地");
					itemTitle.put("1908", "职位");
					itemTitle.put("1909", "证件号");
				}
				if ("2".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("1901", "Name");
					itemTitle.put("1902", "Company No");
					itemTitle.put("1903", "Employee No");
					itemTitle.put("1904", "Bank Account");
					itemTitle.put("1905", "Department Name");
					itemTitle.put("1906", "Cost Center");
					itemTitle.put("1907", "Work Place");
					itemTitle.put("1908", "Position");
					itemTitle.put("1909", "ID No.");
				}
				itemValue.put("1901", baseInfoMap.get("1901") + "");
				itemValue.put("1902", baseInfoMap.get("1902") + "");
				itemValue.put("1903", baseInfoMap.get("1903") + "");
				itemValue.put("1904", baseInfoMap.get("1904") + "");
				itemValue.put("1905", baseInfoMap.get("1905") + "");
				itemValue.put("1906", baseInfoMap.get("1906") + "");
				itemValue.put("1907", baseInfoMap.get("1907") + "");
				itemValue.put("1908", baseInfoMap.get("1908") + "");
				itemValue.put("1909", baseInfoMap.get("1909") + "");
				// 调派四金
				if ("1".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("801", "单位部分养老");
					itemTitle.put("802", "单位部分医疗");
					itemTitle.put("803", "单位部分失业");
					itemTitle.put("804", "单位工伤保险");
					itemTitle.put("805", "单位生育保险");
					itemTitle.put("806", "单位公积金");
					itemTitle.put("807", "计税调整项");
				}
				if ("2".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("801", "Pension Insu-CO.");
					itemTitle.put("802", "Medical Insu-CO.");
					itemTitle.put("803", "Unemployment Insu-CO.");
					itemTitle.put("804", "Work Injury Insu-CO.");
					itemTitle.put("805", "Maternity Insu-CO.");
					itemTitle.put("806", "Housing Fund-CO.");
					itemTitle.put("807", "Tax Adjustment");
				}
				itemValue.put("801", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("OEP_C")));// 养老
				itemValue.put("802", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("MEDICAL_C")));// 医疗
				itemValue.put("803", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("LOSE_JOB_C")));// 失业
				itemValue.put("804", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("INJURY_INSU")));// 工伤
				itemValue.put("805", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("CHILDBIRTH_INSU")));// 生育
				itemValue.put("806", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ACCFUND_C")));// 单位公积金
				itemValue.put("807", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("CALC_FIELD3")));// 计税调整项
				// 添加部分工资、年终奖、股权激励、经济补偿金
				if ("1".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("811", "计税总额");
					itemTitle.put("812", "个人所得税");
					itemTitle.put("813", "实发工资");
					itemTitle.put("821", "税前应发年终奖");
					itemTitle.put("822", "税前应扣年终奖");
					itemTitle.put("823", "仅计税不发放年终奖");
					itemTitle.put("824", "仅抵扣计税项(年终奖)");
					itemTitle.put("825", "计税年终奖总额");
					itemTitle.put("826", "年终奖个人所得税");
					itemTitle.put("827", "税后应发年终奖");
					itemTitle.put("828", "税后应扣年终奖");
					itemTitle.put("829", "实发年终奖");
					itemTitle.put("831", "税前应发股权激励");
					itemTitle.put("832", "税前应扣股权激励");
					itemTitle.put("833", "仅计税不发放股权激励");
					itemTitle.put("834", "仅抵扣计税项（股权激励）");
					itemTitle.put("835", "计税股权激励总额");
					itemTitle.put("836", "股权激励个人所得税");
					itemTitle.put("837", "税后应发股权激励");
					itemTitle.put("838", "税后应扣股权激励");
					itemTitle.put("839", "实发股权激励");
					itemTitle.put("841", "税前应发经济补偿金");
					itemTitle.put("842", "税前应扣经济补偿金");
					itemTitle.put("843", "仅计税不发放经济补偿金");
					itemTitle.put("844", "仅抵扣计税项（经济补偿金）");
					itemTitle.put("845", "计税经济补偿金总额");
					itemTitle.put("846", "经济补偿金个人所得税");
					itemTitle.put("847", "税后应发经济补偿金");
					itemTitle.put("848", "税后应扣经济补偿金");
					itemTitle.put("849", "实发经济补偿金");
				}
				if ("2".equals(clientConfigInfo.getItemNameType())) {
					itemTitle.put("811", "Total Taxable Salary Income");
					itemTitle.put("812", "Salary Income Tax");
					itemTitle.put("813", "Total Net Payment");
					itemTitle.put("821", "Annual Bonus before Tax");
					itemTitle.put("822", "Annual Bonus Deduction before Tax");
					itemTitle.put("823", "Other Taxable Annual Bonus");
					itemTitle.put("824", "Other Tax-deductible Item");
					itemTitle.put("825", "Total Taxable Annual Bonus");
					itemTitle.put("826", "Annual Bonus Tax");
					itemTitle.put("827", "Non-tax Annual Bonus");
					itemTitle.put("828", "Annual Bonus Deduction after Tax");
					itemTitle.put("829", "Total Net Annual Bonus");
					itemTitle.put("831", "Stock Option before Tax");
					itemTitle.put("832", "Stock Option Deduction before Tax");
					itemTitle.put("833", "Other Taxable Stock Option");
					itemTitle.put("834", "Other Tax-deductible Item");
					itemTitle.put("835", "Total Taxable Stock Option");
					itemTitle.put("836", "Stock Option Tax");
					itemTitle.put("837", "Non-tax Stock Option");
					itemTitle.put("838", "Stock Option Deduction after Tax");
					itemTitle.put("839", "Total Net Stock Option");
					itemTitle.put("841", "Severance Compensation before Tax");
					itemTitle.put("842", "Severance Compensation Deduction before Tax");
					itemTitle.put("843", "Other Taxable Severance Compensation");
					itemTitle.put("844", "Other Tax- deductible Item");
					itemTitle.put("845", "Total Taxable Severance Compensation");
					itemTitle.put("846", "Severance Compensation Tax");
					itemTitle.put("847", "Non-tax Severance Compensation");
					itemTitle.put("848", "Severance Compensation Deduction after Tax");
					itemTitle.put("849", "Total Net Severance Compensation");
				}

				itemValue.put("811", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("TAX_SALARY")));// 计税总额
				itemValue.put("812",
						salaryInfoList.get(i).get("ADJUST_TAX") == null ? (ObjectUtil.objectIsNull2(salaryInfoList.get(i)
								.get("TAX"))) : ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("ADJUST_TAX").toString()));// 个人所得税
				itemValue.put("813", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("REAL_PAY")));// 实发工资
				itemValue.put("821", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS")));// 税前应发年终奖
				itemValue.put("822", "0.00");// 税前应扣年终奖
				itemValue.put("823", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_NO_PAY")));// 仅计税不发放年终奖
				itemValue.put("824", "0.00");// 仅抵扣计税项
				itemValue
						.put("825",
								ObjectUtil.objectIsNull2(Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS")))
										+ Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_NO_PAY")))));// 计税年终奖总额
				itemValue.put(
						"826",
						salaryInfoList.get(i).get("BONUS_ADJUST_TAX") == null ? (ObjectUtil.objectIsNull2(salaryInfoList
								.get(i).get("BONUS_TAX"))) : ObjectUtil.objectIsNull2(salaryInfoList.get(i)
								.get("BONUS_ADJUST_TAX")));// 年终奖个人所得税
				itemValue
						.put("827","0.00");// 税后应发年终奖（不是一个计算项目，取值为0.00）
				itemValue.put("828",
						ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_FREE_TAX_DEDUCT")));// 税后应扣年终奖
				itemValue.put("829", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("BONUS_REAL_PAY")));// 实发年终奖
				itemValue.put("831", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK")));// 税前应发股权激励
				itemValue.put("832", "0.00");// 税前应扣股权激励
				itemValue.put("833", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_NO_PAY")));// 仅计税不发放股权激励
				itemValue.put("834", "0.00");// 仅抵扣计税项
				itemValue
						.put("835",
								ObjectUtil.objectIsNull2(Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK")))
										+ Double.valueOf(ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_NO_PAY")))));// 计税股权激励总额
				itemValue.put(
						"836",
						salaryInfoList.get(i).get("STOCK_ADJUST_TAX") == null ? (ObjectUtil.objectIsNull2(salaryInfoList
								.get(i).get("STOCK_TAX"))) : ObjectUtil.objectIsNull2(salaryInfoList.get(i)
								.get("STOCK_ADJUST_TAX")));// 股权激励个人所得税
				itemValue
						.put("837","0.00");// 税后应发股权激励(不是一个计算项目，取值为0.00)
				itemValue.put("838",
						ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_FREE_TAX_DEDUCT")));// 税后应扣股权激励
				itemValue.put("839", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("STOCK_REAL_PAY")));// 实发股权激励
				itemValue.put("841", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyBefTax")));// 税前应发经济补偿金...
				itemValue.put("842",
						ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyBefTaxDeduct")));// 税前应扣经济补偿金...
				itemValue.put("843", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyRcvNoTax")));// 仅计税不发放经济补偿金...
				itemValue.put("844", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyDeductTax")));// 仅抵扣计税项...
				itemValue.put("845", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyTotal")));// 计税经济补偿金总额（A-B+C-D）...
				itemValue.put("846", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyTax")));// 经济补偿金个人所得税...
				itemValue.put("847", "0.00");// 税后应发经济补偿金...(不是一个计算项目，取值为0.00)
				itemValue.put("848", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyDeductPay")));// 税后应扣经济补偿金...
				itemValue.put("849", ObjectUtil.objectIsNull2(salaryInfoList.get(i).get("subsidyRealPay")));// 实发经济补偿金（A-B-F+G-H）...
				TempletSalaryItemInfo tsii = null;
				for (int j = 0; j < sicList.size(); j++) {
					tsii = new TempletSalaryItemInfo();
					tsii.setItemBelongPosition(sicList.get(j).getfieldNo());
					tsii.setItemCode(sicList.get(j).getItemCode().toString());
					tsii.setItemIsShow(Integer.valueOf(sicList.get(j).getIsZeroDisplay()));
					tsii.setItemPosition(sicList.get(j).getItemPosition());
					tsii.setItemTitle(itemTitle.get(sicList.get(j).getItemCode().toString()));
					tsii.setItemValue(itemValue.get(sicList.get(j).getItemCode().toString()));
					secondItemData.add(tsii);
				}
				// 处理大标题数据
				Map<String, Object> salaryInfoMap = new HashMap<String, Object>();
				for (int n = 0; n < sfcList.size(); n++) {
					double sumValue = 0.00;
					for (int m = 0; m < sicList.size(); m++) {
						if (sicList.get(m).getfieldNo().intValue() == sfcList.get(n).getFieldNo().intValue()) {
							if (itemValue.get(sicList.get(m).getItemCode() + "") != null
									&& Pattern
											.compile(
													"^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")
											.matcher(
													itemValue
															.get(sicList.get(m).getItemCode() + ""))
											.matches() == true&&!"1903".equals(sicList.get(m).getItemCode())) {

								sumValue = sumValue
										+ Double.valueOf(StringUtils.isEmpty(itemValue.get(sicList
												.get(m).getItemCode() + "")) ? "0.00" : (itemValue
												.get(sicList.get(m).getItemCode() + "").toString()));// 计算大标题的合计值
							}
						}
					}
					BigDecimal bd = new BigDecimal(sumValue);
					sumValue = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					salaryInfoMap.put(sfcList.get(n).getFieldNo()+"", sumValue == 0 ? 0 : sumValue);
				}

				TempletSalaryInfo tsi = null;
				for (int j = 0; j < sfcList.size(); j++) {
					tsi = new TempletSalaryInfo();
					tsi.setBigTitlePosition(sfcList.get(j).getFieldNo());
					tsi.setBigTitle(sfcList.get(j).getDisplayName());
					tsi.setBigRemarks(sfcList.get(j).getRemarks());
					tsi.setBigSum(ObjectUtil.objectIsNull2(salaryInfoMap.get(sfcList.get(j).getFieldNo()
							.toString())));
					tsi.setIsCount(sfcList.get(j).getIsCount());
					secondData.add(tsi);
				}
				// 个人备注
				String remarks = salaryInfoList.get(i).get("REMARKS") == null ? "" : salaryInfoList
						.get(i).get("REMARKS").toString();
				//模板显示中文名字
				String companyNameCh=ObjectUtil.ObjectIsNull(salaryInfoList.get(i).get("NAME_CH"));
				//模板显示英文名字
				String companyNameEn=ObjectUtil.ObjectIsNull(salaryInfoList.get(i).get("NAME_EN"));
				//logo下是否显示公司全称
				String isCompnameDisplay=ObjectUtil.ObjectIsNull(clientConfigInfo.getIsCompnameDisplay());
				// 添加到总实体
				std.setFormatInfo(formatInfo);
				std.setSendDate(sendDate);
				std.setDatePeriod(datePeriod);
				std.setRemarks(remarks);
				std.setfirstData(firstData);
				std.setSecondData(secondData);
				std.setSecondItemData(secondItemData);
				std.setItemNameType(clientConfigInfo.getItemNameType());
				std.setCompanyNameCh(companyNameCh);
				std.setCompanyNameEn(companyNameEn);
				std.setIsCompnameDisplay(isCompnameDisplay);
				SalaryTempletDataList.add(std);
			} else {
				log.info("********** Not Exist This Type **********");
			}
		}
		return SalaryTempletDataList;
	}

	/**
	 * 工资单文件预览
	 * 
	 * @throws Exception
	 */
	@Override
	public String salaryPreview(String companyNo, String empNo, String batchNo) throws Exception {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");

		String path = System.getProperty("user.dir");

		// 获得客户配置信息
		ClientConfigInfo clientConfigInfo = salaryServerDao
				.getClientConfigInfoByCompanyNo(companyNo);

		// 获取当天需要发送的雇员信息
		List<Map<String, Object>> salaryInfoList = new ArrayList<Map<String, Object>>();
		FormatInfo formatInfo = salaryServerDao.getFormatInfo(clientConfigInfo.getFormatSno());
		salaryInfoList.add(getEmployeeSalaryBaseMonth(companyNo, empNo, clientConfigInfo,
				formatInfo.getBaseAccYm(),batchNo));

		String sendFilePath = "";

		// 数据整理
		List<SalaryTempletData> dealSalaryInfo = dealSalaryInfo(salaryInfoList, formatInfo,
				clientConfigInfo);

		// 下载公司logo图片
		String downloadFile = clientConfigInfo.getLogoPath();
		String imagName = clientConfigInfo.getCompanyNo() + "_companyLogo";
		String imagePath = "";
		if (!StringUtils.isEmpty(downloadFile)) {
			imagePath = path + "\\" + imagName + ".jpg";
			downloadFile(downloadFile, imagePath);
		}

		Map<String, Object> salaryInfo = salaryInfoList.get(0);

		String FileName = salaryInfo.get("REAL_ACC_YM") + Constants.SALARY_FILE_NAME + "_"
				+ salaryInfo.get("EMP_NO") + "_"
				+ salaryInfo.get("SALARY_SNO") + df.format(date);

		if ("1".equals(formatInfo.getFormatType())) {// 标准模板工资单文件
			// PDF
			if ("1".equals(clientConfigInfo.getFileType())) {
				String xlsFilePathS = path + "\\" + FileName + ".xlsx";
				String pdfFilePathS = path + "\\" + FileName + ".pdf";
				Excell07StandardTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathS, imagePath, 0);
				ExcellToPdf.saveAsPDF(xlsFilePathS, pdfFilePathS);

				// 上传文件
				// ***** EPAY2.0 BEGIN *******	
//				sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), pdfFilePathS);
				sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), pdfFilePathS);
				// ***** EPAY2.0 END *******	
				salaryInfo.put("FILE_PATH", sendFilePath);

				new File(xlsFilePathS).delete();
				new File(pdfFilePathS).delete();

				log.info("********** " + companyNo + " , " + empNo
						+ " : Standard Pdf Finished **********");
			} else if ("2".equals(clientConfigInfo.getFileType())) {
				// Word
				String wordFilePathS = path + "\\" + FileName + ".doc";
				WordStandardTemplet.writeWordFile(
						dealSalaryInfo,
						wordFilePathS,
						downloadFile == null ? ""
								: (downloadFile.replace(Constants.PIC_REFELCT_DIRECTORY, "http://"
										+ Constants.SERVER_ADDR)), 0);
				// 上传文件
				// ***** EPAY2.0 BEGIN *******	
//				sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), wordFilePathS);
				sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), wordFilePathS);
				// ***** EPAY2.0 END *******	
				salaryInfo.put("FILE_PATH", sendFilePath);

				new File(wordFilePathS).delete();

				log.info("********** " + companyNo + " , " + empNo
						+ " : Standard Word Finished **********");
			} else if ("3".equals(clientConfigInfo.getFileType())) {
				// Excel
				String xlsFilePathS = path + "\\" + FileName + ".xlsx";
				Excell07StandardTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathS, imagePath, 0);

				// 上传文件
				// ***** EPAY2.0 BEGIN *******	
//				sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), xlsFilePathS);
				sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), xlsFilePathS);
				// ***** EPAY2.0 END *******	
				salaryInfo.put("FILE_PATH", sendFilePath);

				new File(xlsFilePathS).delete();

				log.info("********** " + companyNo + " , " + empNo
						+ " : Standard Excel Finished **********");
			} else {
				log.info("********** Not Exist This Type **********");
			}
		} else if ("2".equals(formatInfo.getFormatType())) {// 自定义模板工资单文件
			if ("1".equals(clientConfigInfo.getFileType())) {
				// PDF
				String xlsFilePathF = path + "\\" + FileName + ".xlsx";
				String pdfFilePathF = path + "\\" + FileName + ".pdf";
				ExcellToPdf
						.saveAsPDF(Excell07FreeTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathF,
								imagePath, 0), pdfFilePathF);

				// 上传文件
				// ***** EPAY2.0 BEGIN *******	
//				sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), pdfFilePathF);
				sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), pdfFilePathF);
				// ***** EPAY2.0 END *******	
				salaryInfo.put("FILE_PATH", sendFilePath);

				new File(xlsFilePathF).delete();
				new File(pdfFilePathF).delete();

				log.info("********** " + companyNo + " , " + empNo
						+ " : Free Pdf Finished **********");
			} else if ("2".equals(clientConfigInfo.getFileType())) {
				// Word
				String wordFilePathF = path + "\\" + FileName + ".doc";

				WordFreeTemplet.writeWordFile(
						dealSalaryInfo,
						wordFilePathF,
						downloadFile == null ? ""
								: (downloadFile.replace(Constants.PIC_REFELCT_DIRECTORY, "http://"
										+ Constants.SERVER_ADDR)), 0);
				// 上传文件
				// ***** EPAY2.0 BEGIN *******	
//				sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), wordFilePathF);
				sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), wordFilePathF);
				// ***** EPAY2.0 END *******	
				salaryInfo.put("FILE_PATH", sendFilePath);

				new File(wordFilePathF).delete();

				log.info("********** " + companyNo + " , " + empNo
						+ " : Free Word Finished **********");
			} else if ("3".equals(clientConfigInfo.getFileType())) {
				// Excel
				String xlsFilePathF = path + "\\" + FileName + ".xlsx";
				Excell07FreeTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathF, imagePath, 0);

				// 上传文件
				// ***** EPAY2.0 BEGIN *******	
//				sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), xlsFilePathF);
				sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), xlsFilePathF);
				// ***** EPAY2.0 END *******	
				salaryInfo.put("FILE_PATH", sendFilePath);

				new File(xlsFilePathF).delete();

				log.info("********** " + companyNo + " , " + empNo
						+ " : Free Excel Finished **********");
			} else {
				log.info("********** Not Exist This Type **********");
			}
		} else {
			log.info("********** Not Exist This Type **********");
		}
		// 让线程阻塞10秒，保证上传文件成功！
		Thread.sleep(10000);

		// 删除本地公司logo图片
		// File file = new File(imagePath);
		// file.delete();

		return sendFilePath;
	}

	/**
	 * 获取薪酬数据
	 * 
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	private Map<String, Object> getEmployeeSalaryBaseMonth(String companyNo, String empNo,
			ClientConfigInfo clientConfigInfo, String baseAccYm,String searchBatchNo) throws SQLException {

		Map<String, Object> salaryMap = null;

		try {
			// 获得商社的基本信息
			ClientInfo clientInfo = salaryServerDao.getClientInfoByCompanyNo(companyNo);

			// 查询取得雇员工资表中的雇员工资数据
			List<Map<String, Object>> salaryList = salaryServerDao.getSalaryInfoBaseMonth(
					companyNo, empNo, baseAccYm, searchBatchNo);

			if (salaryList.size() > 0) {
				salaryMap = salaryList.get(0);
				// ***** EPAY2.0 BEGIN *******
//				String accYm = salaryMap.get("ACC_YM").toString();
				// ***** EPAY2.0 END *******
				String realAccYm = salaryMap.get("REAL_ACC_YM").toString();
				Integer batchNo = Integer.parseInt(salaryMap.get("BATCH_NO").toString());
				String payrollType = salaryMap.get("PAYROLL_TYPE").toString();

				// 经济补偿金
				Map<String, String> disSubsidyMap = salaryServerDao.getDisSubsidy(empNo, companyNo,
						realAccYm);
				String subsidyBefTax = disSubsidyMap.get("subsidyBefTax");
				String subsidyBefTaxDeduct = disSubsidyMap.get("subsidyBefTaxDeduct");
				String subsidyRcvNoTax = disSubsidyMap.get("subsidyRcvNoTax");
				String subsidyDeductTax = disSubsidyMap.get("subsidyDeductTax");
				String subsidyTotal = disSubsidyMap.get("subsidyTotal");
				String subsidyTax = disSubsidyMap.get("subsidyTax");
				String subsidyShouldPay = disSubsidyMap.get("subsidyShouldPay");
				String subsidyDeductPay = disSubsidyMap.get("subsidyDeductPay");
				String subsidyRealPay = disSubsidyMap.get("subsidyRealPay");

				salaryMap.put("subsidyBefTax", subsidyBefTax);
				salaryMap.put("subsidyBefTaxDeduct", subsidyBefTaxDeduct);
				salaryMap.put("subsidyRcvNoTax", subsidyRcvNoTax);
				salaryMap.put("subsidyDeductTax", subsidyDeductTax);
				salaryMap.put("subsidyTotal", subsidyTotal);
				salaryMap.put("subsidyTax", subsidyTax);
				salaryMap.put("subsidyShouldPay", subsidyShouldPay);
				salaryMap.put("subsidyDeductPay", subsidyDeductPay);
				salaryMap.put("subsidyRealPay", subsidyRealPay);

				// 实际发薪日期
				Date bonusDate = salaryServerDao.getRealSendDate(empNo, companyNo,
						realAccYm, Constants.KIND_BONUS, batchNo);
				Date stockDate = salaryServerDao.getRealSendDate(empNo, companyNo,
						realAccYm, Constants.KIND_STOCK, batchNo);
				Date subsidyDate = salaryServerDao.getRealSendDate(empNo, companyNo,
						realAccYm, Constants.KIND_SUBSIDY, batchNo);

				Date salaryDate = null;
				if (Constants.PAYROLL_TYPE_SALARY.equals(payrollType)) {
					salaryDate = salaryServerDao.getRealSendDate(empNo, companyNo,
							realAccYm, Constants.KIND_SALARY, batchNo);
				}
				else if (Constants.PAYROLL_TYPE_REMUNERATION.equals(payrollType)) {
					salaryDate = salaryServerDao.getRealSendDate(empNo, companyNo,
							realAccYm, Constants.KIND_REMUNERATION, batchNo);
				}
				salaryMap.put("REAL_SEND_DATE", getMaxRealPayDate(salaryDate, bonusDate, stockDate, subsidyDate));
				// 预计发薪日期
				// ***** EPAY2.0 BEGIN *******	
//				salaryMap.put("PRE_SEND_DATE", salaryServerDao.getPreSendDate(accYm, companyNo));
				salaryMap.put("PRE_SEND_DATE", salaryServerDao.getPreSendDate(realAccYm, companyNo));
				// ***** EPAY2.0 END *******
				// 商社名称
				salaryMap.put("COMPANY_NAME", clientInfo.getCompanyName());
				//商社中文全称
				salaryMap.put("NAME_CH", clientInfo.getCompanyNameCh());
				//商社英文全称
				salaryMap.put("NAME_EN", clientInfo.getCompanyNameEn());
//				// 销售组
//				salaryMap.put("SALE_GRP_CODE", clientInfo.getSaleGrpCode());
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
				// 发送日期类型
				salaryMap.put("SEND_DATE_TYPE", clientConfigInfo.getSendDateType());
				// 模板序号
				salaryMap.put("FORMAT_SNO", clientConfigInfo.getFormatSno());

				// 雇员所在部门名
				String departNo = salaryMap.get("DEPART_NO") != null ? salaryMap.get("DEPART_NO")
						.toString() : "";
				salaryMap.put("DEPART_NAME", salaryServerDao.getNameByDepartNo(departNo));

				// 雇员银行卡号
				salaryMap.put("BANK_ACCOUNT", salaryServerDao.getBankAccountByEmpNo(empNo));

				// 雇员成本中心
				salaryMap.put("COST_CENTER", salaryServerDao.getCostCenter(companyNo, empNo));

				// 查询取得雇员工资项目
				// ***** EPAY2.0 BEGIN *******
//				List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
//						accYm);
				List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
						realAccYm);
				// ***** EPAY2.0 END *******
				salaryMap.put("SALARY TITLE", salaryTitleList);

				// 税前应发合计
				salaryMap.put("PRETAX PAY COUNT",
						getSalaryCount(salaryMap, companyNo, "1", "1", "1"));

				// 税前应扣合计
				salaryMap.put("PRETAX DEDUCT COUNT",
						getSalaryCount(salaryMap, companyNo, "-1", "-1", "-1"));

				// 仅计税不发放工资合计
				salaryMap.put("ONLY ADD COUNT", String.valueOf(Double.valueOf(getSalaryCount(
						salaryMap, companyNo, "0", "1", "0"))
						+ Double.valueOf((salaryMap.get("CALC_FIELD3") == null ? "0.00" : salaryMap
								.get("CALC_FIELD3") + ""))));

				// 仅抵扣计税项合计
				salaryMap.put("ONLY DEDUCT COUNT",
						getSalaryCount(salaryMap, companyNo, "0", "-1", "0"));

				// 税后应发合计
				salaryMap.put("REAL PAY COUNT",
						getSalaryCount(salaryMap, companyNo, "1", "0", "1"));

				// 税后应扣合计
				salaryMap.put("REAL DEDUCT COUNT",
						getSalaryCount(salaryMap, companyNo, "-1", "0", "-1"));

				// ***** No：S20151358 BEGIN *******
				Map<String, Object> datePeriodMap = salaryServerDao.getSalaryPeriod(companyNo,
						realAccYm);

				String beginDate = datePeriodMap.get("BEGIN_DATE").toString();
				String endDate = datePeriodMap.get("END_DATE").toString();

				salaryMap.put("PAYROLL_PERIOD", beginDate + " - " + endDate);
				// ***** No：S20151358 END *******
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salaryMap;
	}

	/**
	 * 重发加密工资单邮件
	 * 本月工资单生成工资单文件后推送
	 * 历月工资单从文件服务器下载后推送
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean salaryMailResend(String companyNo, String empNo, String batchNo, String accYm,
			String realAccYm, String userId, Long resultSno, String filePath) throws Exception {

		// 黑名单中的雇员不重发加密工资单
		List<EmailBlackList> list = SendEmail.getBlackCode();
		for (EmailBlackList entity : list) {
			if (Constants.BLACKCODE_RCV.equals(entity.getFilter_type())) {
				if (empNo.equals(entity.getBlack_code())) {
					String failReason = "接收者 雇员" + empNo + "在发送黑名单中";
					salaryServerDao.updateRcvBlackCode(resultSno, failReason);
					return false;
				}
			}
		}
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		DateFormat df2 = new SimpleDateFormat("yyyyMM");
		String currentDate = df2.format(date);

		String path = System.getProperty("user.dir");

		// 获得客户配置信息
		ClientConfigInfo clientConfigInfo = salaryServerDao
				.getClientConfigInfoByCompanyNo(companyNo);

		// 获取当天需要发送的雇员信息
		List<Map<String, Object>> salaryInfoList = new ArrayList<Map<String, Object>>();
		salaryInfoList.add(getEmployeeSalaryForResend(resultSno, companyNo, empNo, batchNo, accYm, realAccYm,
				clientConfigInfo));

		Map<String, Object> salaryInfo = salaryInfoList.get(0);
		// 工资单文件生成
		String sendFilePath = "";
		if (!currentDate.equals(realAccYm)) {
			salaryInfo.put("FILE_PATH", filePath);
			sendFilePath = filePath;
		}
		else {
			FormatInfo formatInfo = salaryServerDao.getFormatInfo(clientConfigInfo.getFormatSno());
	
			// 数据整理
			List<SalaryTempletData> dealSalaryInfo = dealSalaryInfo(salaryInfoList, formatInfo,
					clientConfigInfo);

			// 下载公司logo图片
			String downloadFile = clientConfigInfo.getLogoPath();
			String imagName = companyNo + "_companyLogo";
			String imagePath = "";
			if (!StringUtils.isEmpty(downloadFile)) {
				imagePath = path + "\\" + imagName + ".jpg";
				downloadFile(downloadFile, imagePath);
			}
	
			String FileName = salaryInfo.get("REAL_ACC_YM") + Constants.SALARY_FILE_NAME + "_"
					+ salaryInfo.get("EMP_NO") + "_"
					+ salaryInfo.get("SALARY_SNO") + df.format(date);
	
			if ("1".equals(formatInfo.getFormatType())) {// 标准模板工资单文件
				// PDF
				if ("1".equals(clientConfigInfo.getFileType())) {
					String xlsFilePathS = path + "\\" + FileName + ".xlsx";
					String pdfFilePathS = path + "\\" + FileName + ".pdf";
					Excell07StandardTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathS, imagePath, 0);
					ExcellToPdf.saveAsPDF(xlsFilePathS, pdfFilePathS);
	
					// 上传文件
					// ***** EPAY2.0 BEGIN *******	
//					sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), pdfFilePathS);
					sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), pdfFilePathS);
					// ***** EPAY2.0 END *******	
					salaryInfo.put("FILE_PATH", sendFilePath);
	
					new File(xlsFilePathS).delete();
					new File(pdfFilePathS).delete();
	
					log.info("********** " + companyNo + " , " + empNo
							+ " : Standard Pdf Finished **********");
				} else if ("2".equals(clientConfigInfo.getFileType())) {
					// Word
					String wordFilePathS = path + "\\" + FileName + ".doc";
					WordStandardTemplet.writeWordFile(
							dealSalaryInfo,
							wordFilePathS,
							downloadFile == null ? ""
									: (downloadFile.replace(Constants.PIC_REFELCT_DIRECTORY, "http://"
											+ Constants.SERVER_ADDR)), 0);
					// 上传文件
					// ***** EPAY2.0 BEGIN *******	
//					sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), wordFilePathS);
					sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), wordFilePathS);
					// ***** EPAY2.0 END *******	
					salaryInfo.put("FILE_PATH", sendFilePath);
	
					new File(wordFilePathS).delete();
	
					log.info("********** " + companyNo + " , " + empNo
							+ " : Standard Word Finished **********");
				} else if ("3".equals(clientConfigInfo.getFileType())) {
					// Excel
					String xlsFilePathS = path + "\\" + FileName + ".xlsx";
					Excell07StandardTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathS, imagePath, 0);
	
					// 上传文件
					// ***** EPAY2.0 BEGIN *******	
//					sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), xlsFilePathS);
					sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), xlsFilePathS);
					// ***** EPAY2.0 END *******	
					salaryInfo.put("FILE_PATH", sendFilePath);
	
					new File(xlsFilePathS).delete();
	
					log.info("********** " + companyNo + " , " + empNo
							+ " : Standard Excel Finished **********");
				} else {
					log.info("********** Not Exist This Type **********");
				}
			} else if ("2".equals(formatInfo.getFormatType())) {// 自定义模板工资单文件
				if ("1".equals(clientConfigInfo.getFileType())) {
					// PDF
					String xlsFilePathF = path + "\\" + FileName + ".xlsx";
					String pdfFilePathF = path + "\\" + FileName + ".pdf";
					ExcellToPdf
							.saveAsPDF(Excell07FreeTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathF,
									imagePath, 0), pdfFilePathF);
	
					// 上传文件
					// ***** EPAY2.0 BEGIN *******	
//					sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), pdfFilePathF);
					sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), pdfFilePathF);
					// ***** EPAY2.0 END *******	
					salaryInfo.put("FILE_PATH", sendFilePath);
	
					new File(xlsFilePathF).delete();
					new File(pdfFilePathF).delete();
	
					log.info("********** " + companyNo + " , " + empNo
							+ " : Free Pdf Finished **********");
				} else if ("2".equals(clientConfigInfo.getFileType())) {
					// Word
					String wordFilePathF = path + "\\" + FileName + ".doc";
					WordFreeTemplet.writeWordFile(
							dealSalaryInfo,
							wordFilePathF,
							downloadFile == null ? ""
									: (downloadFile.replace(Constants.PIC_REFELCT_DIRECTORY, "http://"
											+ Constants.SERVER_ADDR)), 0);
					// 上传文件
					// ***** EPAY2.0 BEGIN *******	
//					sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), wordFilePathF);
					sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), wordFilePathF);
					// ***** EPAY2.0 END *******	
					salaryInfo.put("FILE_PATH", sendFilePath);
	
					new File(wordFilePathF).delete();
	
					log.info("********** " + companyNo + " , " + empNo
							+ " : Free Word Finished **********");
				} else if ("3".equals(clientConfigInfo.getFileType())) {
					// Excel
					String xlsFilePathF = path + "\\" + FileName + ".xlsx";
					Excell07FreeTemplet.sfscExcellPay(dealSalaryInfo, xlsFilePathF, imagePath, 0);
	
					// 上传文件
					// ***** EPAY2.0 BEGIN *******	
//					sendFilePath = uploadFile(salaryInfo.get("ACC_YM").toString(), xlsFilePathF);
					sendFilePath = uploadFile(salaryInfo.get("REAL_ACC_YM").toString(), xlsFilePathF);
					// ***** EPAY2.0 END *******	
					salaryInfo.put("FILE_PATH", sendFilePath);
	
					new File(xlsFilePathF).delete();
	
					log.info("********** " + companyNo + " , " + empNo	+ " : Free Excel Finished **********");
				} else {
					log.info("********** Not Exist This Type **********");
				}
			} else {
				log.info("********** Not Exist This Type **********");
			}
	
			// 删除本地公司logo图片
			File file = new File(imagePath);
			file.delete();
	
			// 线程阻塞，保证上传文件成功！
			Thread.sleep(3000);
		}
		// 发送邮件
		EmailBatchStatus result = SendEmail.sendSalaryEmail(salaryInfoList, clientConfigInfo);

		if (result != null) {
			// 更新文件上传信息表中的记录
			updateMailResultInfoForResend(empNo, sendFilePath, salaryInfo, result, userId,
					resultSno);
		} else {
			log.info("************* Mail Empty *************");
			String mail = salaryInfo.get("MAIL") != null ? salaryInfo.get("MAIL").toString() : "";
			if (StringUtils.isEmpty(mail)) {
				salaryServerDao.updateMailResultInfoForResend(Constants.MAIL_NOT_SEND, userId, mail, "",
						sendFilePath, resultSno);
			}
			return false;
		}

		return true;
	}

	/**
	 * 获取薪酬数据
	 * 
	 * @param resultSno
	 * @param companyNo
	 * @param empNo
	 * @param batchNo
	 * @param accYm
	 * @param realAccYm
	 * 	@param clientConfigInfo
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	private Map<String, Object> getEmployeeSalaryForResend(Long resultSno, String companyNo,
			String empNo, String batchNo, String accYm, String realAccYm,
			ClientConfigInfo clientConfigInfo) throws SQLException {

		Map<String, Object> salaryMap = new HashMap<String, Object>();

		try {
			// 获得商社的基本信息
			ClientInfo clientInfo = salaryServerDao.getClientInfoByCompanyNo(companyNo);

			// 查询取得雇员工资表中的雇员工资数据
			salaryMap = salaryServerDao.getSalaryInfoForResend(companyNo, empNo, batchNo, accYm,
					realAccYm);

			SalaryExecuteInfo salaryExecuteInfo = salaryServerDao.getMailResultInfo(resultSno);

			Integer kind = null;
			String isSalarySend = salaryExecuteInfo.getIsSalarySend();// 是否要发工资薪酬
			String isBonusSend = salaryExecuteInfo.getIsYearBonusSend();// 是否要发年终奖
			String isStockSend = salaryExecuteInfo.getIsStockSend();// 是否要发股权激励
			String isSubsidySend = salaryExecuteInfo.getIsReleasePaySend();// 是否要发经济补偿金

			// 经济补偿金
			Map<String, String> disSubsidyMap = salaryServerDao.getDisSubsidy(empNo, companyNo,
					realAccYm);
			String subsidyBefTax = disSubsidyMap.get("subsidyBefTax");
			String subsidyBefTaxDeduct = disSubsidyMap.get("subsidyBefTaxDeduct");
			String subsidyRcvNoTax = disSubsidyMap.get("subsidyRcvNoTax");
			String subsidyDeductTax = disSubsidyMap.get("subsidyDeductTax");
			String subsidyTotal = disSubsidyMap.get("subsidyTotal");
			String subsidyTax = disSubsidyMap.get("subsidyTax");
			String subsidyShouldPay = disSubsidyMap.get("subsidyShouldPay");
			String subsidyDeductPay = disSubsidyMap.get("subsidyDeductPay");
			String subsidyRealPay = disSubsidyMap.get("subsidyRealPay");

			// 经济补偿金
			if ("1".equals(isSubsidySend)) {
				salaryMap.put("subsidyBefTax", subsidyBefTax);
				salaryMap.put("subsidyBefTaxDeduct", subsidyBefTaxDeduct);
				salaryMap.put("subsidyRcvNoTax", subsidyRcvNoTax);
				salaryMap.put("subsidyDeductTax", subsidyDeductTax);
				salaryMap.put("subsidyTotal", subsidyTotal);
				salaryMap.put("subsidyTax", subsidyTax);
				salaryMap.put("subsidyShouldPay", subsidyShouldPay);
				salaryMap.put("subsidyDeductPay", subsidyDeductPay);
				salaryMap.put("subsidyRealPay", subsidyRealPay);
				salaryMap.put("isSubsidySend", isSubsidySend);
			}

			// 调整薪酬数据
			adjustSalaryInfo(salaryMap, isSalarySend, isBonusSend, isStockSend);

			// 实际发薪日期
			Date salaryDate = null;
			Date bonusDate = null;
			Date stockDate = null;
			Date subsidyDate = null;

			if ("1".equals(isSalarySend)) {
				kind = Constants.KIND_SALARY;
				salaryDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm, kind,
						Integer.parseInt(batchNo));
			}
			if ("1".equals(isBonusSend)) {
				kind = Constants.KIND_BONUS;
				bonusDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm, kind,
						Integer.parseInt(batchNo));
			}
			if ("1".equals(isStockSend)) {
				kind = Constants.KIND_STOCK;
				stockDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm, kind,
						Integer.parseInt(batchNo));
			}
			if ("1".equals(isSubsidySend)) {
				kind = Constants.KIND_SUBSIDY;
				subsidyDate = salaryServerDao.getRealSendDate(empNo, companyNo, realAccYm, kind,
						Integer.parseInt(batchNo));
			}

			salaryMap.put("REAL_SEND_DATE",
					getMaxRealPayDate(salaryDate, bonusDate, stockDate, subsidyDate));
			// 预计发送日期
			// ***** EPAY2.0 BEGIN *******	
//			salaryMap.put("PRE_SEND_DATE", salaryServerDao.getPreSendDate(accYm, companyNo));
			salaryMap.put("PRE_SEND_DATE", salaryServerDao.getPreSendDate(realAccYm, companyNo));
			// ***** EPAY2.0 END *******	
			// 商社名称
			salaryMap.put("COMPANY_NAME", clientInfo.getCompanyName());
			//商社中文全称
			salaryMap.put("NAME_CH", clientInfo.getCompanyNameCh());
			//商社英文全称
			salaryMap.put("NAME_EN", clientInfo.getCompanyNameEn());
//			// 销售组
//			salaryMap.put("SALE_GRP_CODE", clientInfo.getSaleGrpCode());
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
			// 发送日期类型
			salaryMap.put("SEND_DATE_TYPE", clientConfigInfo.getSendDateType());
			// 模板序号
			salaryMap.put("FORMAT_SNO", clientConfigInfo.getFormatSno());

			// 雇员所在部门名
			String departNo = salaryMap.get("DEPART_NO") != null ? salaryMap.get("DEPART_NO")
					.toString() : "";
			salaryMap.put("DEPART_NAME", salaryServerDao.getNameByDepartNo(departNo));

			// 雇员银行卡号
			salaryMap.put("BANK_ACCOUNT", salaryServerDao.getBankAccountByEmpNo(empNo));

			// 雇员成本中心
			salaryMap.put("COST_CENTER", salaryServerDao.getCostCenter(companyNo, empNo));

			// 查询取得雇员工资项目
			// ***** EPAY2.0 BEGIN *******
//			List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
//					accYm);
			List<SalaryTitleBean> salaryTitleList = salaryServerDao.findSalaryTitle(companyNo,
					realAccYm);
			// ***** EPAY2.0 END *******
			salaryMap.put("SALARY TITLE", salaryTitleList);

			// 税前应发合计
			salaryMap.put("PRETAX PAY COUNT",
					getSalaryCount(salaryMap, companyNo, "1", "1", "1"));

			// 税前应扣合计
			salaryMap.put("PRETAX DEDUCT COUNT",
					getSalaryCount(salaryMap, companyNo, "-1", "-1", "-1"));

			// 仅计税不发放工资合计
			salaryMap.put("ONLY ADD COUNT",
					String.valueOf(Double.valueOf(getSalaryCount(salaryMap, companyNo, "0", "1", "0"))
					+ Double.valueOf((salaryMap.get("CALC_FIELD3") == null ? "0.00" : salaryMap.get("CALC_FIELD3") + ""))));

			// 仅抵扣计税项合计
			salaryMap.put("ONLY DEDUCT COUNT",
					getSalaryCount(salaryMap, companyNo, "0", "-1", "0"));

			// 税后应发合计
			salaryMap.put("REAL PAY COUNT",
					getSalaryCount(salaryMap, companyNo, "1", "0", "1"));

			// 税后应扣合计
			salaryMap.put("REAL DEDUCT COUNT",
					getSalaryCount(salaryMap, companyNo, "-1", "0", "-1"));

			// ***** No：S20151358 BEGIN *******
			Map<String, Object> datePeriodMap = salaryServerDao.getSalaryPeriod(companyNo,
					realAccYm);

			String beginDate = datePeriodMap.get("BEGIN_DATE").toString();
			String endDate = datePeriodMap.get("END_DATE").toString();

			salaryMap.put("PAYROLL_PERIOD", beginDate + " - " + endDate);
			// ***** No：S20151358 END *******

			// 雇员密码
			List<Map<String, Object>> empPwdList = salaryServerDao.getEmpPwd(empNo);
			if (empPwdList.size() > 0) {
				Map<String, Object> empPwdMap = empPwdList.get(0);
				String empPwd = Base64Util.getFromBase64(empPwdMap.get("EMP_PWD").toString());
				salaryMap.put("EMPLOYEE_PWD", empPwd);
			}
			else {
				salaryMap.put("EMPLOYEE_PWD", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salaryMap;
	}

	/**
	 * 更新邮件发送结果信息表中的发送状态
	 * 
	 * @param empNo
	 * @param sendFilePath
	 * @param salaryInfo
	 * 	@param result
	 * @param userId
	 * @param resultSno
	 */
	private void updateMailResultInfoForResend(String empNo, String sendFilePath,
			Map<String, Object> salaryInfo, EmailBatchStatus result, String userId, Long resultSno) {
		List<EmailModel> failList = result.getFailList();
		List<EmailModel> denyList = result.getDenyList();
		String failReason = "";

		// 邮件发送状态
		String sendSuccess = Constants.MAIL_SEND_SUCCEED;

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
		}

		for (EmailModel denyEmp : denyList) {
			 if (empNo.equals(denyEmp.getRcvId())) {
				 sendSuccess = Constants.MAIL_BLACKLIST;
				 failReason = denyEmp.getEmailStatus().getDenyRemarks();
				 break;
			 }
		 }

		salaryServerDao.updateMailResultInfoForResend(sendSuccess, userId, mail, failReason,
				sendFilePath, resultSno);
	}

	/**
	 * 调整薪酬数据
	 * 
	 * @param salaryInfo
	 * @param isSalarySend
	 * @param isBonusSend
	 * @param isStockSend
	 */
	private void adjustSalaryInfo(Map<String, Object> salaryInfo, String isSalarySend,
			String isBonusSend, String isStockSend) {
		if ("0".equals(isSalarySend)) {
			// 清除工资薪酬数据
			// A.税前应发
			salaryInfo.put("BASIC_SALARY", null);
			salaryInfo.put("MONTH_BONUS", null);
			salaryInfo.put("OVERTIME_SALARY", null);
			salaryInfo.put("TRAFFIC_ALLOWANCE", null);
			salaryInfo.put("LUNCH_ALLOWANCE", null);
			for (int i = 1; i <= 25; i++) {
				salaryInfo.put("ALLOWANCE" + String.valueOf(i), null);
			}
			// B.税前应扣
			salaryInfo.put("OEP_PER", null);
			salaryInfo.put("MEDICAL_INSU", null);
			salaryInfo.put("LOSE_JOB_INSU", null);
			salaryInfo.put("ACCFUND_INSU", null);
			salaryInfo.put("APPEND_ACCFUND", null);
			for (int i = 1; i <= 10; i++) {
				salaryInfo.put("FREE_TAX_DEDUCT" + String.valueOf(i), null);
			}
			salaryInfo.put("SUPPLY_SOCIAL_INSU", null);
			salaryInfo.put("INJURY_PER", null);
			salaryInfo.put("CHILDBIRTH_PER", null);
			salaryInfo.put("ANNUITY_PER", null);
			// C.仅计税不发放工资
			salaryInfo.put("FREERCV_ALLOWANCE1", null);
			salaryInfo.put("FREERCV_ALLOWANCE2", null);
			salaryInfo.put("ALLOWANCE_NO_PAY3", null);
			salaryInfo.put("ALLOWANCE_NO_PAY4", null);
			salaryInfo.put("ALLOWANCE_NO_PAY5", null);
			salaryInfo.put("ANNUITY_EXCEED", null);
			salaryInfo.put("CALC_FIELD3", null);
			// D.仅抵扣计税项
			salaryInfo.put("FOREIGN_DEDUCT_BEFORE_TAX", null);
			salaryInfo.put("DEDUCT_BEFORE_TAX", null);
			salaryInfo.put("DONATION", null);
			// E.计税总额
			salaryInfo.put("TAX_SALARY", null);
			// F.个人所得
			salaryInfo.put("ADJUST_TAX", null);
			salaryInfo.put("TAX", null);
			// G.税后应发
			for (int i = 1; i <= 10; i++) {
				salaryInfo.put("FREE_TAX_ALLOWANCE" + String.valueOf(i), null);
			}
			salaryInfo.put("FOREIGN_HOUSING_SUBSIDY", null);
			salaryInfo.put("FOREIGN_MEAL_ALLOWANCE", null);
			salaryInfo.put("FOREIGN_MOVING_FEE", null);
			salaryInfo.put("FOREIGN_LAUNDRY_FEE", null);
			salaryInfo.put("FOREIGN_FAMILY_ALLOWANCE", null);
			salaryInfo.put("FOREIGN_TRAINING_FEE", null);
			salaryInfo.put("FOREIGN_ALLOWANCE", null);
			salaryInfo.put("FOREIGN_EDUCATION_EXPENSE", null);
			salaryInfo.put("FOREIGN_TRAVEL_ALLOWANCE", null);
			// H.税后应扣
			for (int i = 1; i <= 10; i++) {
				salaryInfo.put("DEDUCT" + String.valueOf(i), null);
			}
			// I.实发工资
			salaryInfo.put("REAL_PAY", null);
		}
		if ("0".equals(isBonusSend)) {
			// 清除年终奖数据
			salaryInfo.put("BONUS", null);
			salaryInfo.put("BONUS_NO_PAY", null);
			salaryInfo.put("BONUS_ADJUST_TAX", null);
			salaryInfo.put("BONUS_TAX", null);
			salaryInfo.put("BONUS_REAL_PAY", null);
			salaryInfo.put("BONUS_FREE_TAX_DEDUCT", null);

		}
		if ("0".equals(isStockSend)) {
			// 清除股权激励数据
			salaryInfo.put("STOCK", null);
			salaryInfo.put("STOCK_NO_PAY", null);
			salaryInfo.put("STOCK_ADJUST_TAX", null);
			salaryInfo.put("STOCK_TAX", null);
			salaryInfo.put("STOCK_REAL_PAY", null);
			salaryInfo.put("STOCK_FREE_TAX_DEDUCT", null);
		}
	}

	/**
	 * 取得工资、年终奖、股权激励、经济补偿金的最大实发日期
	 * 
	 * 
	 * @param salaryDate
	 * @param bonusDate
	 * @param stockDate
	 * @param subsidyDate
	 * @return Date
	 * @throws ParseException
	 */
	private Date getMaxRealPayDate(Date salaryDate, Date bonusDate, Date stockDate, Date subsidyDate)
			throws ParseException {

		Date maxDate = null;
		String firstDate = "1001-01-01";// 预设最小年月初值
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if (salaryDate == null && bonusDate == null
				&& stockDate == null && subsidyDate == null) {
		}
		else {
			if (salaryDate == null) {
				salaryDate = df.parse(firstDate);
			}
			if (bonusDate == null) {
				bonusDate = df.parse(firstDate);
			}
			if (stockDate == null) {
				stockDate = df.parse(firstDate);
			}
			if (subsidyDate == null) {
				subsidyDate = df.parse(firstDate);
			}
	
			if (salaryDate.before(bonusDate)) {
				maxDate = bonusDate;
			} else {
				maxDate = salaryDate;
			}
	
			if (maxDate.before(stockDate)) {
				maxDate = stockDate;
			}
	
			if (maxDate.before(subsidyDate)) {
				maxDate = subsidyDate;
			}
		}
		return maxDate;
	}
}
