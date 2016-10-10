package com.salaryMail.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import com.efesco.mailsvc.model.EmailBatchModel;
import com.efesco.mailsvc.model.EmailBatchStatus;
import com.efesco.mailsvc.model.EmailBlackList;
import com.efesco.mailsvc.model.EmailModel;
import com.salaryMail.common.Constants;
import com.salaryMail.entity.ClientConfigInfo;
import com.salaryMail.service.MailService;

public class SendEmail {
	private static final Logger log = Logger.getLogger(SendEmail.class);
	static final String PASSWORD = "wfEmailSvcPswd";
	static final String SENDER = "上海外服薪酬服务中心";
	static final String MAIL = "sfscpay@efesco.com";
	// ***** No：S20151410 BEGIN *******
	static final String CH12148_SENDER = "安吉星厦门";
	static final String CH12148_MAIL = "XM.HR@ShanghaiOnStar.com";
	// ***** No：S20151410 END *******
	static final String MAIL_TITLE = "蔻驰工资单";
	static final String MAIL_CONTENT = "您好，请查收！";
	static final String MAIL_SIGN = "上海外服薪酬服务中心";
//	static final String COACH_MAIL= "tangjw@efesco.com";
	static final String COACH_MAIL= "zhoushaohui@sfsctech.com";
	static final String MAIL_NAME = "唐佳文";

	/**
	 * 艾睿电子发送工资单文件
	 * 
	 * @param salaryInfoList
	 * @return EmailBatchStatus
	 */
	public static EmailBatchStatus sendEmailForArrow(List<Map<String, Object>> salaryInfoList) {

		EmailBatchStatus result = null;
//		String mailServiceUrl = "http://172.16.96.223/WfEmailService/services/EmailService";
		String mailServiceUrl = "http://172.16.96.120/WfEmailService/services/EmailService";
		MailService mailService = null;
		Service service = new ObjectServiceFactory().create(MailService.class);
		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);

		try {
			// 创建客户端代理
			mailService = (MailService) factory.create(service, mailServiceUrl);

			// 初始化参数
			List<EmailModel> emaillist = new ArrayList<EmailModel>();// 邮件列表

			Map<String, Object> firstSalaryInfo = salaryInfoList.get(0);

			EmailBatchModel ebm = new EmailBatchModel();
			ebm.setCaller("123");// 来源系统
			ebm.setBatchNo(500L);// 批次号

			Date date = new Date();
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.MINUTE, 30);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdfEng = new SimpleDateFormat("MMM yyyy", Locale.US);
			SimpleDateFormat sdfEng2 = new SimpleDateFormat("MMM.yyyy", Locale.US);
			String beginSendDate1 = sdf.format(date);
			String endSendDate1 = sdf.format(nowTime.getTime());

			ebm.setYdBingSendDate(beginSendDate1);// 启动发送时间
			ebm.setYdEndSendDate(endSendDate1);// 最晚发送时间

			ebm.setAllowRetryCnt("0");// 失败重发次数
			ebm.setIsMail("0");// 是否邮件反馈结果，1是，0否
			ebm.setBizType(Constants.BIZ_TYPE);// 来源业务类型
			ebm.setHtmlTemptype("0");

			ebm.setSendDepts(firstSalaryInfo.get("BELONG_TO_DEPT").toString());// 发送部门
			ebm.setSendDeptsName(firstSalaryInfo.get("DEPT_NAME").toString());// 发送部门名称
			ebm.setSendUser(firstSalaryInfo.get("USER_ID").toString());// 发送人
			ebm.setSendUserName(SENDER);// 发送人姓名
			ebm.setSendUserEmail(MAIL);// 发送人邮箱地址
			ebm.setIsUrgent("0");// 紧急程度（普通、紧急）
			ebm.setFremarks(new Date().toString());

			Date realAccYm = null;
			String dateEng = "";
			String dateEng2 = "";
			for (Map<String, Object> salaryInfo : salaryInfoList) {
				String mail = salaryInfo.get("MAIL") != null ? salaryInfo.get("MAIL").toString()
						: "";
				if (!StringUtils.isEmpty(mail)) {
					EmailModel emailModel = new EmailModel();
					realAccYm = sdf2.parse(salaryInfo.get("REAL_ACC_YM").toString() + "01");
					dateEng = sdfEng.format(realAccYm);
					dateEng2 = sdfEng2.format(realAccYm);

					emailModel.setSubject("Payslip of " + dateEng);// 邮件主题

					String mailContent = ""
							+ "Dear Employee,<br/><br/>Attached please find the payslip of "
							+ dateEng2
							+ ".<br/><br/>"
							+ "The password for opening is the last six numbers of your China ID card.<br/><br/>"
							+ "Please be noted payslip is highly confidential, any questions please contact with SFSC（上海外服） or Arrow local HR.<br/><br/>"
							+ "<div>SFSC（上海外服）联系人：</div>"
							+ "<table border='1' cellpadding='0' cellspacing='0' style='text-align:left; font-size:12px'>"
							+ "<tr style='background-color:#99FFFF'>"
							+ "<td>姓名</td>"
							+ "<td>职位</td>"
							+ "<td>负责内容</td>"
							+ "<td>联系方式</td>"
							+ "<td>电子邮箱</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>许雯</td>"
							+ "<td>服务专员</td>"
							+ "<td>工资计算、社保、公积金等的办理</td>"
							+ "<td>021-68410500*192</td>"
							+ "<td style='color:#9933CC'>xuwen@efesco.com</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>顾林俊</td>"
							+ "<td>客户经理</td>"
							+ "<td>工资计算，服务协调与处理</td>"
							+ "<td>021-68410500*176</td>"
							+ "<td style='color:#9933CC'>gulj@efesco.com</td>"
							+ "</tr>"
							+ "</table><br/>"
							+ "<div>Arrow HR联系人：</div>"
							+ "<table border='1' cellpadding='0' cellspacing='0' style='text-align:center; font-size:12px'>"
							+ "<tr style='background-color:#99FFFF'>"
							+ "<td>区域</td>"
							+ "<td>HR</td>"
							+ "<td>联系电话</td>"
							+ "<td>邮件</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>中区</td>"
							+ "<td>Louisa Xu</td>"
							+ "<td>021-2215 2266</td>"
							+ "<td style='color:#9933CC'>LOUISA.XU@arrowasia.com</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>北区</td>"
							+ "<td>Alina Zhang</td>"
							+ "<td>010-5606 4016</td>"
							+ "<td style='color:#9933CC'>ALINA.ZHANG@arrowasia.com</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>南区</td>"
							+ "<td>Wing Xian</td>"
							+ "<td>0755-8836 9125</td>"
							+ "<td style='color:#9933CC'>WING.XIAN@arrowasia.com</td>"
							+ "</tr>"
							+ "</table><br/>"
							+ "Thanks.<br/><br/>"
							+ "SFSC（上海外服）-Arrow Payroll Center";

					emailModel.setContent(mailContent + new Date());// 邮件正文

					emailModel.setAttachAddr("sftp://httpd:61.129.112.8@HTTPD@172.16.221.143:22"
							+ salaryInfo.get("FILE_PATH").toString());// 附件地址
					emailModel.setIsEncryptAttach("0");// 附件是否加密
					emailModel.setEncryptKey("");// 附件加密密钥

					// 邮件接收者信息
					emailModel.setDeptNo(salaryInfo.get("BELONG_TO_DEPT").toString());// 业务部
					emailModel.setCompGrpCode(salaryInfo.get("COMP_GRP_CODE").toString());// 客户组
					emailModel.setCompanyNo(salaryInfo.get("COMPANY_NO").toString());// 商社代码
					emailModel.setRcvType("EMP");// 接收者类别
					emailModel.setRcvId(salaryInfo.get("EMP_NO").toString());// 接收者ID
					emailModel.setRcvName(salaryInfo.get("Chinese Name").toString());// 接收者名称
					emailModel.setRcvMailAddr(mail);// 接收者邮箱

					emaillist.add(emailModel);
				}
			}
			ebm.setEmaillist(emaillist);

			if (emaillist.size() != 0) {
				result = mailService.sendSalaryEmailBatch(PASSWORD, ebm);
				log.info("********** Send Status : " + result.getStatus().getNameCh() + " **********");
				log.info("********** End Time : " + result.getFinishedDate() + " **********");
				log.info("********** Success Count : " + result.getSuccessCnt() + " **********");
				log.info("********** Fail Count : " + result.getFailCnt() + " **********");
				log.info("********** Deny Count : " + result.getDenyCnt() + " **********");
			} else {
				log.info("********** Mail Empty **********");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送工资单文件
	 * 
	 * @param salaryInfoList
	 * @param clientConfigInfo
	 * @return EmailBatchStatus
	 */
	public static EmailBatchStatus sendSalaryEmail(List<Map<String, Object>> salaryInfoList,
			ClientConfigInfo clientConfigInfo) {

		EmailBatchStatus result = null;
		String mailServiceUrl = "http://172.16.96.223/WfEmailService/services/EmailService";
//		String mailServiceUrl = "http://172.16.96.120/WfEmailService/services/EmailService";
		MailService mailService = null;
		Service service = new ObjectServiceFactory().create(MailService.class);
		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);

		try {
			// 创建客户端代理
			mailService = (MailService) factory.create(service, mailServiceUrl);

			// 初始化参数
			List<EmailModel> emaillist = new ArrayList<EmailModel>();// 邮件列表

			Map<String, Object> firstSalaryInfo = salaryInfoList.get(0);

			EmailBatchModel ebm = new EmailBatchModel();
			ebm.setCaller("erp");// 来源系统
			ebm.setBatchNo(500L);// 批次号

			Date date = new Date();
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.MINUTE, 30);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String beginSendDate1 = sdf.format(date);
			String endSendDate1 = sdf.format(nowTime.getTime());
			ebm.setYdBingSendDate(beginSendDate1);
			ebm.setYdEndSendDate(endSendDate1);

			ebm.setAllowRetryCnt("0");// 失败重发次数
			ebm.setIsMail("0");// 是否邮件反馈结果，1是，0否
			ebm.setBizType(Constants.BIZ_TYPE);// 来源业务类型
			// ***** No：S20160136 BEGIN *******
			if ("1".equals(clientConfigInfo.getIsTxtMailContent())) {
				ebm.setHtmlTemptype("1");
				ebm.setHtmlTempAddr(null);
			}
			else {
				ebm.setHtmlTemptype("0");
			}
			// ***** No：S20160136 END    *******

			ebm.setSendDepts(firstSalaryInfo.get("BELONG_TO_DEPT").toString());// 发送部门
			ebm.setSendDeptsName(firstSalaryInfo.get("DEPT_NAME").toString());// 发送部门名称
			ebm.setSendUser(firstSalaryInfo.get("USER_ID").toString());// 发送人
			// ***** No：S20151410 BEGIN *******
//			ebm.setSendUserName(SENDER);// 发送人姓名
//			ebm.setSendUserEmail(MAIL);// 发送人邮箱地址
			if ("CH12148".equals(firstSalaryInfo.get("COMPANY_NO").toString())) {
				ebm.setSendUserName(CH12148_SENDER);// 发送人姓名
				ebm.setSendUserEmail(CH12148_MAIL);// 发送人邮箱地址
			} else {
				ebm.setSendUserName(SENDER);// 发送人姓名
				ebm.setSendUserEmail(MAIL);// 发送人邮箱地址
			}
			// ***** No：S20151410 END *******
			ebm.setIsUrgent("0");// 紧急程度（普通、紧急）
			ebm.setFremarks(new Date().toString());

			ebm.setFileFormatType(clientConfigInfo.getFileType());// 工资单生成文件格式
			if (Constants.ENCRYPT_METHOD_FILE.equals(clientConfigInfo.getEncryptMethod())
					&& Constants.MAIL_SEND_FILE_ZIP.equals(clientConfigInfo.getMailSendFileType())) {
				ebm.setFileType(Constants.FILE_ENCRYPT_THEN_ZIP);
			} else {
				ebm.setFileType(clientConfigInfo.getEncryptMethod());// 工资单文件邮件发送附件类型
			}

			List<Map<String, Object>> sendSalaryInfoList = new ArrayList<Map<String, Object>>(salaryInfoList.size());
			for (Map<String, Object> salaryInfo : salaryInfoList) {
				Map<String, Object> newSalaryInfo = new HashMap<String, Object>();
				newSalaryInfo.putAll(salaryInfo);
				sendSalaryInfoList.add(newSalaryInfo);
			}
			// 相同雇员一天只发送一封邮件（包含多封附件）
			for (int i = 0; i < sendSalaryInfoList.size(); i++) {
				String path = "sftp://httpd:61.129.112.8@HTTPD@172.16.221.143:22"
						+ sendSalaryInfoList.get(i).get("FILE_PATH").toString();
				// 处理只有一条数据的情况
				if (sendSalaryInfoList.size() == 1) {
					sendSalaryInfoList.get(0).put("FILE_PATH", path);
				}
				// 过滤掉之后有相同电脑号的数据
				for (int j = i + 1; j < sendSalaryInfoList.size(); j++) {
					if (sendSalaryInfoList.get(i).get("EMP_NO")
							.equals(sendSalaryInfoList.get(j).get("EMP_NO"))) {
						path += ";sftp://httpd:61.129.112.8@HTTPD@172.16.221.143:22"
								+ sendSalaryInfoList.get(j).get("FILE_PATH").toString();
						sendSalaryInfoList.get(i).put("FILE_PATH", path);
						sendSalaryInfoList.remove(j);
						j--;
					} else {
						sendSalaryInfoList.get(i).put("FILE_PATH", path);
					}
				}
				// 最后条数据
				if (i == sendSalaryInfoList.size() - 1) {
					sendSalaryInfoList.get(i).put("FILE_PATH", path);
				}
			}

			String mailTitle = clientConfigInfo.getMailTitle();
			String mailContent = clientConfigInfo.getMailContent();
			String mailSign = clientConfigInfo.getMailSign();
			for (Map<String, Object> sendSalaryInfo : sendSalaryInfoList) {
				String mail = sendSalaryInfo.get("MAIL") != null ? sendSalaryInfo.get("MAIL")
						.toString() : "";
				if (!StringUtils.isEmpty(mail)) {
					EmailModel emailModel = new EmailModel();

					String password = sendSalaryInfo.get("EMPLOYEE_PWD") == null?
							"" : sendSalaryInfo.get("EMPLOYEE_PWD") .toString();
					if (StringUtils.isEmpty(password)) {
						String id = sendSalaryInfo.get("ID").toString();
						if (id.length() > 6) {
							password = id.substring(id.length() - 6, id.length());
						} else {
							password = id;
						}
					}

					String realAccYm = sendSalaryInfo.get("REAL_ACC_YM").toString();
					emailModel.setSubject(replaceRealAccYmInMail(realAccYm, mailTitle));// 邮件主题
					emailModel.setContent(replaceRealAccYmInMail(realAccYm, mailContent) + "<br/>"
							+ replaceRealAccYmInMail(realAccYm, mailSign));// 邮件正文

					emailModel.setAttachAddr(sendSalaryInfo.get("FILE_PATH").toString());// 附件地址
					emailModel.setIsEncryptAttach("1");// 附件是否加密
					emailModel.setEncryptKey(password);// 附件加密密钥

					// 邮件接收者信息
					emailModel.setDeptNo(sendSalaryInfo.get("BELONG_TO_DEPT").toString());// 业务部
					emailModel.setCompGrpCode(sendSalaryInfo.get("COMP_GRP_CODE") == null ? "" : sendSalaryInfo.get("COMP_GRP_CODE").toString());// 客户组
					emailModel.setCompanyNo(sendSalaryInfo.get("COMPANY_NO").toString());// 商社代码
					emailModel.setRcvType("EMP");// 接收者类别
					emailModel.setRcvId(sendSalaryInfo.get("EMP_NO").toString());// 接收者ID
					emailModel.setRcvName(sendSalaryInfo.get("Chinese Name").toString());// 接收者名称
					emailModel.setRcvMailAddr(mail);// 接收者邮箱

					emaillist.add(emailModel);
				}
			}
			ebm.setEmaillist(emaillist);

			if (emaillist.size() != 0) {
				result = mailService.queryBatchStatus(PASSWORD,
						mailService.sendSalaryEmailBatch(PASSWORD, ebm).getBatchNo());
				log.info("********** Send Status : " + result.getStatus().getNameCh() + " **********");
				log.info("********** End Time : " + result.getFinishedDate() + " **********");
				log.info("********** Success Count : " + result.getSuccessCnt() + " **********");
				log.info("********** Fail Count : " + result.getFailCnt() + " **********");
				log.info("********** Deny Count : " + result.getDenyCnt() + " **********");
			} else {
				log.info("********** Mail Empty **********");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送提醒邮件给业务员
	 * 
	 * @param salaryInfoList
	 * @param failEmpList
	 * @return EmailBatchStatus
	 */
	public static void sendEmailtoSale(List<Map<String, Object>> saleList,
			List<Map<String, String>> failEmpList, List<Map<String, String>> denyEmpList) {

//		String mailServiceUrl = "http://172.16.96.223/WfEmailService/services/EmailService";
		String mailServiceUrl = "http://172.16.96.120/WfEmailService/services/EmailService";
		MailService mailService = null;
		Service service = new ObjectServiceFactory().create(MailService.class);
		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);

		try {
			// 创建客户端代理
			mailService = (MailService) factory.create(service, mailServiceUrl);

			// 初始化参数
			List<EmailModel> emaillist = new ArrayList<EmailModel>();// 邮件列表

			Map<String, Object> firstSaleInfo = saleList.get(0);

			EmailBatchModel ebm = new EmailBatchModel();
			ebm.setCaller("erp");// 来源系统
			ebm.setBatchNo(500L);// 批次号

			Date date = new Date();
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.MINUTE, 30);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String beginSendDate1 = sdf.format(date);
			String endSendDate1 = sdf.format(nowTime.getTime());

			ebm.setYdBingSendDate(beginSendDate1);
			ebm.setYdEndSendDate(endSendDate1);

			ebm.setAllowRetryCnt("0");// 失败重发次数
			ebm.setIsMail("0");// 是否邮件反馈结果，1是，0否
			ebm.setBizType(Constants.BIZ_TYPE);// 来源业务类型
			ebm.setHtmlTemptype("0");

			ebm.setSendDepts(firstSaleInfo.get("GRP_ID").toString());// 发送部门
			ebm.setSendDeptsName(firstSaleInfo.get("GRP_NAME").toString());// 发送部门名称
			ebm.setSendUser(firstSaleInfo.get("USER_ID").toString());// 发送人
			ebm.setSendUserName(SENDER);// 发送人姓名
			ebm.setSendUserEmail(MAIL);// 发送人邮箱地址
			ebm.setIsUrgent("0");// 紧急程度（普通、紧急）
			ebm.setFremarks(new Date().toString());

			EmailModel emailModel = null;
			StringBuffer mailContent = new StringBuffer();
			mailContent.append(sdf2.format(date) + " 加密工资单邮件推送，以下雇员存在异常情况</br>"
				+ "<table border='1' cellpadding='0' cellspacing='0' style='text-align:left; font-size:15px'>"
				+ "<tr style='background-color:#99FFFF'>"
				+ "<td>客户编号</td>"
				+ "<td>客户简称</td>"
				+ "<td>电脑号</td>"
				+ "<td>姓名</td>"
				+ "<td>业务年月</td>"
				+ "<td>电子邮箱</td>"
				+ "<td>邮件发送状态</td>"
				+ "<td>业务员</td>"
				+ "</tr>");

			if (failEmpList.size() > 0) {
			for (Map<String, String> map : failEmpList) {
					mailContent = mailContent.append(""
							+ "<tr style='background-color:#99FFFF'>"
							+ "<td>"+map.get("COMPANY_NO")+"</td>"
							+ "<td>"+map.get("COMPANY_NAME")+"</td>"
							+ "<td>"+map.get("EMP_NO")+"</td>"
							+ "<td>"+map.get("EMP_NAME")+"</td>"
							+ "<td>"+map.get("REAL_ACC_YM")+"</td>"
							+ "<td>"+map.get("MAIL")+"</td>"
							+ "<td>发送失败</td>"
							+ "<td>"+map.get("SALES")+"</td>"
							+ "</tr>");
				}
			}

			if (denyEmpList.size() > 0) {
				for (Map<String, String> map : denyEmpList) {
					mailContent = mailContent.append(""
							+ "<tr style='background-color:#99FFFF'>"
							+ "<td>"+map.get("COMPANY_NO")+"</td>"
							+ "<td>"+map.get("COMPANY_NAME")+"</td>"
							+ "<td>"+map.get("EMP_NO")+"</td>"
							+ "<td>"+map.get("EMP_NAME")+"</td>"
							+ "<td>"+map.get("REAL_ACC_YM")+"</td>"
							+ "<td>"+map.get("MAIL")+"</td>"
							+ "<td>未发送</td>"
							+ "<td>"+map.get("SALES")+"</td>"
							+ "</tr>");
				}
			}

			mailContent.append("</table>");

			if (saleList.size()>0) {
				Map<String, Object> saleInfo = saleList.get(0);
				String mail = saleInfo.get("EMAIL") != null ? saleInfo.get("EMAIL").toString() : "";

				if (!StringUtils.isEmpty(mail)) {
					emailModel = new EmailModel();
					emailModel.setSubject("加密工资单邮件推送失败(" + sdf2.format(date) + ")");// 邮件主题
					emailModel.setContent(mailContent.toString());// 邮件正文
					emailModel.setIsEncryptAttach("0");// 附件是否加密
					emailModel.setEncryptKey("");// 附件加密密钥

					// 邮件接收者信息
					emailModel.setDeptNo(saleInfo.get("GRP_ID").toString());// 业务部
					emailModel.setCompGrpCode(saleInfo.get("COMP_GRP_CODE") == null ? "" : saleInfo.get("COMP_GRP_CODE").toString());// 客户组
					emailModel.setCompanyNo(saleInfo.get("COMPANY_NO").toString());// 商社代码
					emailModel.setRcvType("COMM");// 接收者类别
					emailModel.setRcvId(saleInfo.get("USER_ID").toString());// 接收者ID
					emailModel.setRcvName(saleInfo.get("USER_NAME").toString());// 接收者名称
					emailModel.setRcvMailAddr(mail);// 接收者邮箱

					emaillist.add(emailModel);
				}
			}

			ebm.setEmaillist(emaillist);

			if (emaillist.size() != 0) {
				mailService.sendSalaryEmailBatch(PASSWORD, ebm);
			} else {
				log.info("********** Sales Mail Empty **********");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送提醒邮件给录入人
	 * 
	 * @param salaryInfoList
	 * @param failEmpList
	 * @return EmailBatchStatus
	 */
	public static void sendEmailtoInputPerson(Map<String, Object> saleInfo,
			List<String> inputPersonList,
			List<List<Map<String, String>>> empMapList,
			List<List<Map<String, Object>>> inputPersonMailList) {

//		String mailServiceUrl = "http://172.16.96.223/WfEmailService/services/EmailService";
		String mailServiceUrl = "http://172.16.96.120/WfEmailService/services/EmailService";
		MailService mailService = null;
		Service service = new ObjectServiceFactory().create(MailService.class);
		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);

		try {
			// 创建客户端代理
			mailService = (MailService) factory.create(service, mailServiceUrl);

			// 初始化参数
			List<EmailModel> emaillist = new ArrayList<EmailModel>();// 邮件列表

			EmailBatchModel ebm = new EmailBatchModel();
			ebm.setCaller("erp");// 来源系统
			ebm.setBatchNo(500L);// 批次号

			Date date = new Date();
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.MINUTE, 30);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String beginSendDate1 = sdf.format(date);
			String endSendDate1 = sdf.format(nowTime.getTime());

			ebm.setYdBingSendDate(beginSendDate1);
			ebm.setYdEndSendDate(endSendDate1);

			ebm.setAllowRetryCnt("0");// 失败重发次数
			ebm.setIsMail("0");// 是否邮件反馈结果，1是，0否
			ebm.setBizType(Constants.BIZ_TYPE);// 来源业务类型
			ebm.setHtmlTemptype("0");

			ebm.setSendDepts(saleInfo.get("GRP_ID").toString());// 发送部门
			ebm.setSendDeptsName(saleInfo.get("GRP_NAME").toString());// 发送部门名称
			ebm.setSendUser(saleInfo.get("USER_ID").toString());// 发送人
			ebm.setSendUserName(SENDER);// 发送人姓名
			ebm.setSendUserEmail(MAIL);// 发送人邮箱地址
			ebm.setIsUrgent("0");// 紧急程度（普通、紧急）
			ebm.setFremarks(new Date().toString());

			EmailModel emailModel = null;

			for (int i = 0; i < inputPersonList.size(); i++) {
				String inputPersonName = inputPersonList.get(i);
				// 录入人不是业务员时才发送邮件
				if (!inputPersonName.equals(saleInfo.get("NAME").toString())) {
					StringBuffer mailContent = new StringBuffer();
					mailContent.append(sdf2.format(date) + " 加密工资单邮件推送，以下雇员存在异常情况"
						+ "<table border='1' cellpadding='0' cellspacing='0' style='text-align:left; font-size:15px'>"
						+ "<tr style='background-color:#99FFFF'>"
						+ "<td>客户编号</td>"
						+ "<td>客户简称</td>"
						+ "<td>电脑号</td>"
						+ "<td>姓名</td>"
						+ "<td>业务年月</td>"
						+ "<td>电子邮箱</td>"
						+ "<td>邮件发送状态</td>"
						+ "<td>业务员</td>"
						+ "</tr>");

					List<Map<String, String>> empList = empMapList.get(i);
					for (Map<String, String> map : empList) {
						mailContent = mailContent.append(""
								+ "<tr style='background-color:#99FFFF'>"
								+ "<td>"+map.get("COMPANY_NO")+"</td>"
								+ "<td>"+map.get("COMPANY_NAME")+"</td>"
								+ "<td>"+map.get("EMP_NO")+"</td>"
								+ "<td>"+map.get("EMP_NAME")+"</td>"
								+ "<td>"+map.get("REAL_ACC_YM")+"</td>"
								+ "<td>"+map.get("MAIL")+"</td>"
								+ "<td>"+map.get("STATUS")+"</td>"
								+ "<td>"+map.get("SALES")+"</td>"
								+ "</tr>");
					}
	
					mailContent.append("</table>");
	
					List<Map<String, Object>> mailList = inputPersonMailList.get(i);
					for (Map<String, Object> mailMap : mailList) {
						String mail = mailMap.get("EMAIL") != null ? mailMap.get("EMAIL").toString() : "";
		
						if (!StringUtils.isEmpty(mail)) {
							emailModel = new EmailModel();
							emailModel.setSubject("加密工资单邮件推送失败(" + sdf2.format(date) + ")");// 邮件主题
							emailModel.setContent(mailContent.toString());// 邮件正文
							emailModel.setIsEncryptAttach("0");// 附件是否加密
							emailModel.setEncryptKey("");// 附件加密密钥
		
							// 邮件接收者信息
							emailModel.setDeptNo(saleInfo.get("GRP_ID").toString());// 业务部
							emailModel.setCompGrpCode(saleInfo.get("COMP_GRP_CODE").toString());// 客户组
							emailModel.setCompanyNo(saleInfo.get("COMPANY_NO").toString());// 商社代码
							emailModel.setRcvType("COMM");// 接收者类别
							emailModel.setRcvId(saleInfo.get("USER_ID").toString());// 接收者ID
							emailModel.setRcvName(saleInfo.get("USER_NAME").toString());// 接收者名称
							emailModel.setRcvMailAddr(mail);// 接收者邮箱
		
							emaillist.add(emailModel);
						}
					}
				}
			}

			ebm.setEmaillist(emaillist);

			if (emaillist.size() != 0) {
				mailService.sendSalaryEmailBatch(PASSWORD, ebm);
			} else {
				log.info("********** Sales Mail Empty **********");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取邮件平台黑名单的数据
	 * 
	 * @param salaryInfoList
	 * @param failEmpList
	 * @return EmailBatchStatus
	 */
	public static List<EmailBlackList> getBlackCode() {

		List<EmailBlackList> list = new ArrayList<EmailBlackList>();
        String mailServiceUrl = "http://172.16.96.223/WfEmailService/services/EmailService";
//		String mailServiceUrl = "http://172.16.96.120/WfEmailService/services/EmailService";
		MailService mailService = null;
		Service service = new ObjectServiceFactory().create(MailService.class);
		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);

		try {
			// 创建客户端代理
			mailService = (MailService) factory.create(service, mailServiceUrl);

			list = mailService.getBlackMaps(Constants.BIZ_TYPE, PASSWORD);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 解析邮件标题、邮件正文、邮件落款中具有时间含义的特殊字符短语
	 * 
	 * @param date
	 * @param sdf
	 * @param content
	 * @throws ParseException 
	 * @throws Exception
	 */
	private static String replaceRealAccYmInMail(String realAccYm, String content) throws ParseException {
		String strRealDate = realAccYm + "01";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 =  new SimpleDateFormat("yyyyMM");
		Date dteRealDate = sdf.parse(strRealDate);
		
		// 取得业务年月的上月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dteRealDate);
		calendar.add(Calendar.MONTH, -1);
		String lastMonth = sdf2.format(calendar.getTime());
		
		// 取得业务年月的上上月
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dteRealDate);
		calendar2.add(Calendar.MONTH, -2);
		String last2Month = sdf2.format(calendar2.getTime());

		// 替换邮件标题、邮件标题、邮件标题
		content = content.replaceAll("<CurrentMonth>", realAccYm);
		content = content.replaceAll("<LastMonth>", lastMonth);
		content = content.replaceAll("<Last2Month>", last2Month);

		return content;
	}
	
	/**
	 * 蔻驰发送工资单文件
	 * @author zhoushaohui
	 * @param salaryInfoList
	 * @return EmailBatchStatus
	 */
	public static EmailBatchStatus sendEmailForCoach(List<Map<String, Object>> salaryInfoList) {
		EmailBatchStatus result = null;
//		String mailServiceUrl = "http://172.16.96.223/WfEmailService/services/EmailService";
		String mailServiceUrl = "http://172.16.96.120/WfEmailService/services/EmailService";
		MailService mailService = null;
		Service service = new ObjectServiceFactory().create(MailService.class);
		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);
		try{
			// 创建客户端代理
			mailService = (MailService) factory.create(service, mailServiceUrl);

			// 初始化参数
			List<EmailModel> emaillist = new ArrayList<EmailModel>();// 邮件列表

			Map<String, Object> firstSalaryInfo = salaryInfoList.get(0);

			EmailBatchModel ebm = new EmailBatchModel();
			ebm.setCaller("erp");// 来源系统
			ebm.setBatchNo(500L);// 批次号

			Date date = new Date();
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.MINUTE, 30);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String beginSendDate1 = sdf.format(date);
			String endSendDate1 = sdf.format(nowTime.getTime());
			ebm.setYdBingSendDate(beginSendDate1);
			ebm.setYdEndSendDate(endSendDate1);

			ebm.setAllowRetryCnt("0");// 失败重发次数
			ebm.setIsMail("0");// 是否邮件反馈结果，1是，0否
			ebm.setBizType(Constants.BIZ_TYPE);// 来源业务类型
			ebm.setHtmlTemptype("0");

			ebm.setSendDepts(firstSalaryInfo.get("BELONG_TO_DEPT").toString());// 发送部门
			ebm.setSendDeptsName(firstSalaryInfo.get("DEPT_NAME").toString());// 发送部门名称
			ebm.setSendUser(firstSalaryInfo.get("USER_ID").toString());// 发送人
			ebm.setSendUserName(SENDER);// 发送人姓名
			ebm.setSendUserEmail(MAIL);// 发送人邮箱地址
			ebm.setIsUrgent("0");// 紧急程度（普通、紧急）
			ebm.setFremarks(new Date().toString());

			ebm.setFileFormatType(Constants.FILE_TYPE_WORD);// 工资单生成文件格式
			ebm.setFileType(Constants.MAIL_SEND_FILE_ZIP);//工资单生成附件类型

			String mailTitle = MAIL_TITLE;
			String mailContent = MAIL_CONTENT;
			String mailSign = MAIL_SIGN;
			for (int i=0;i<salaryInfoList.size();i++) {
				String mail = COACH_MAIL;
				if (!StringUtils.isEmpty(mail)) {
					if(i==salaryInfoList.size()-1||(i+1)%30==0){
						
						EmailModel emailModel = new EmailModel();
						
						String realAccYm = salaryInfoList.get(i).get("REAL_ACC_YM").toString();
						emailModel.setSubject(mailTitle);// 邮件主题
						emailModel.setContent(mailContent + "<br/>" +"月份："+realAccYm+"<br/>"
								+ mailSign);// 邮件正文
						
						emailModel.setAttachAddr("sftp://httpd:61.129.112.8@HTTPD@172.16.221.143:22"+salaryInfoList.get(i).get("FILE_PATH").toString());// 附件地址
						emailModel.setIsEncryptAttach("0");// 附件是否加密
						
						// 邮件接收者信息
						emailModel.setDeptNo(salaryInfoList.get(i).get("BELONG_TO_DEPT").toString());// 业务部
						emailModel.setCompGrpCode(salaryInfoList.get(i).get("COMP_GRP_CODE") == null ? "" : salaryInfoList.get(i).get("COMP_GRP_CODE").toString());// 客户组
						emailModel.setCompanyNo(salaryInfoList.get(i).get("COMPANY_NO").toString());// 商社代码
						emailModel.setRcvType("EMP");// 接收者类别
						emailModel.setRcvId(salaryInfoList.get(i).get("EMP_NO").toString());// 接收者ID
						emailModel.setRcvName(MAIL_NAME);// 接收者名称
						emailModel.setRcvMailAddr(mail);// 接收者邮箱
						
						emaillist.add(emailModel);
					}
				}
			}
			ebm.setEmaillist(emaillist);

			if (emaillist.size() != 0) {
				result = mailService.queryBatchStatus(PASSWORD,
						mailService.sendSalaryEmailBatch(PASSWORD, ebm).getBatchNo());
				log.info("********** Send Status : " + result.getStatus().getNameCh() + " **********");
				log.info("********** End Time : " + result.getFinishedDate() + " **********");
				log.info("********** Success Count : " + result.getSuccessCnt() + " **********");
				log.info("********** Fail Count : " + result.getFailCnt() + " **********");
				log.info("********** Deny Count : " + result.getDenyCnt() + " **********");
			} else {
				log.info("********** Mail Empty **********");
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
