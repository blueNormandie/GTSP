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

import com.jcraft.jsch.JSchException;
import com.salaryMail.common.Constants;
import com.salaryMail.dao.SalaryServerDao;
import com.salaryMail.entity.BaseItemConfig;
import com.salaryMail.entity.ClientConfigInfo;
import com.salaryMail.entity.ClientInfo;
import com.salaryMail.entity.FormatInfo;
import com.salaryMail.entity.SalaryFieldConfig;
import com.salaryMail.entity.SalaryItemConfig;
import com.salaryMail.entity.SalaryTempletData;
import com.salaryMail.entity.SalaryTitleBean;
import com.salaryMail.entity.TempletBaseInfo;
import com.salaryMail.entity.TempletSalaryInfo;
import com.salaryMail.entity.TempletSalaryItemInfo;
import com.salaryMail.service.OnlinePayrollPreviewService;
import com.salaryMail.util.HtmlSalaryPreview;
import com.salaryMail.util.JSchChannel;
import com.salaryMail.util.ObjectUtil;

@Service("OnlinePayrollPreviewService")
public class OnlinePayrollPreviewServiceImpl implements OnlinePayrollPreviewService {

	
	@Resource
	private SalaryServerDao salaryServerDao;

	public SalaryServerDao getSalaryServerDao() {
		return salaryServerDao;
	}

	public void setSalaryServerDao(SalaryServerDao salaryServerDao) {
		this.salaryServerDao = salaryServerDao;
	}

	private final Logger log = Logger.getLogger(OnlinePayrollPreviewServiceImpl.class);
	
	/*
	 * 网上工资单预览 
	 * @see com.salaryMail.service.OnlinePayrollPreviewService#OnlineSalaryPreview(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String OnlineSalaryPreview(String companyNo, String empNo, String batchNo)
			throws Exception {
		//获取客户配置信息
		ClientConfigInfo clientConfigInfo = salaryServerDao.getClientConfigInfoByCompanyNo(companyNo);
		//获取模板信息
		FormatInfo formatInfo = salaryServerDao.getFormatInfo(clientConfigInfo.getFormatSno());
		//获取要预览的员工工资信息
		List<Map<String, Object>> salaryInfoList = new ArrayList<Map<String, Object>>();
		Map<String,Object> employeeSalary = getEmployeeSalaryBaseMonth(companyNo, empNo, clientConfigInfo,formatInfo.getBaseAccYm(),batchNo);
		List<SalaryTempletData> dealSalaryInfo=null;
		String HTMLFileName="no_data";
		if(employeeSalary!=null){
			salaryInfoList.add(employeeSalary);
			//为模板工资单整理包装数据
			dealSalaryInfo = dealSalaryInfo(salaryInfoList, formatInfo,clientConfigInfo);
			log.info("**********数据处理完毕!**********");
			//生成HTML文件
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			HTMLFileName = employeeSalary.get("REAL_ACC_YM") + Constants.SALARY_FILE_NAME + "_"
					+ employeeSalary.get("EMP_NO");
		}
		//获取html文件的根目录
		String classPath = HtmlSalaryPreview.class.getClassLoader().getResource("/").getPath();
		
		String rootPath = "";
		//windows下
		if("\\".equals(File.separator)){
		rootPath = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
		rootPath = rootPath.replace("/", "\\");
		}
		//linux下
		if("/".equals(File.separator)){
		rootPath = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
		rootPath = rootPath.replace("\\", "/");
		}
		log.info("**********HTML文件根目录："+rootPath+"**********");
		//获得生成文件的绝对路径
		HTMLFileName=HtmlSalaryPreview.generateHtmlSalaryFile(dealSalaryInfo, rootPath+"\\html\\"+HTMLFileName+".html");
		if(HTMLFileName.contains("wf_salaryMail.war")){
			HTMLFileName=HTMLFileName.replace("wf_salaryMail.war", "wf_salaryMail");
		}
		log.info("**********HTML文件生成完毕："+HTMLFileName+"**********");
		//生成超链接
		String salaryLink=Constants.SALARY_DOMAIN+HTMLFileName.substring(HTMLFileName.indexOf("wf_salaryMail"));
		salaryLink=salaryLink.replace("\\", "/");
		return salaryLink;
	}
	
	/*
	 * 每天定时清除预览生成的html文件
	 */
	public void clearHtml(){
		//获取html文件的根目录
		String classPath = HtmlSalaryPreview.class.getClassLoader().getResource("/").getPath();
		String rootPath = "";
		//windows下
		if("\\".equals(File.separator)){
			rootPath = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
			rootPath = rootPath.replace("/", "\\");
		}
		//linux下
		if("/".equals(File.separator)){
			rootPath = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
			rootPath = rootPath.replace("\\", "/");
		}
		File deleteRoot=new File(rootPath+"\\html");
		File[] fs=deleteRoot.listFiles();
		for (File file : fs) {
			if("html".equals(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1))){
				file.delete();
			}
		}
	}
	/**
	 * 模板数据整理
	 */
	private List<SalaryTempletData> dealSalaryInfo(List<Map<String, Object>> salaryInfoList,
			FormatInfo formatInfo, ClientConfigInfo clientConfigInfo) {
		List<SalaryTempletData> SalaryTempletDataList = new ArrayList<SalaryTempletData>();
		//网上工资单的预览不支持中英文混排
		if ("3".equals(clientConfigInfo.getItemNameType())) {
			return null;
		}
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
				StringBuffer hide = new StringBuffer();
				for (int m = 0; m < bankAccountIndex.length() - 4; m++) {
					hide.append("*");
				}
				String bankAccountIndex2 = bankAccountIndex.substring(
						bankAccountIndex.length() - 4,
						bankAccountIndex.length());
				String bankAccount = hide.append(bankAccountIndex2).toString();
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
				String bigTitleRemarksIsDisplay="0";
				for (int j = 0; j < sfcList.size(); j++) {
					if(sfcList.get(j).getRemarks()!=null){
						bigTitleRemarksIsDisplay="1";
					}
					if("CA0292".equals(clientConfigInfo.getCompanyNo())
							&& (sfcList.get(j).getFieldNo().intValue()==3||sfcList.get(j).getFieldNo().intValue()==4)){
					      continue;
					}
					tsi = new TempletSalaryInfo();
					tsi.setBigTitlePosition(sfcList.get(j).getFieldNo());
					//中文大标题
				     if ("1".equals(clientConfigInfo.getItemNameType())) {
					     if("CA0292".equals(clientConfigInfo.getCompanyNo())&&(sfcList.get(j).getFieldNo().intValue()==5)){
					    	tsi.setBigTitle(sfcList.get(j).getDisplayName()==null?null:sfcList.get(j).getDisplayName().replace("(A-B+C-D)", ""));
					     }else{
					     	tsi.setBigTitle(sfcList.get(j).getDisplayName());
					     }
				     }
				     //英文大标题
				     if ("2".equals(clientConfigInfo.getItemNameType())) {
					     if("CA0292".equals(clientConfigInfo.getCompanyNo())&&(sfcList.get(j).getFieldNo().intValue()==5)){
					     	tsi.setBigTitle(sfcList.get(j).getDisplayNameEn()==null?null:sfcList.get(j).getDisplayNameEn().replace("(A-B+C-D)", ""));
					     }else{
					     	tsi.setBigTitle(sfcList.get(j).getDisplayNameEn());
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
				std.setLogoPath(clientConfigInfo.getLogoPath());
				std.setBigTitleRemarksIsDisplay(bigTitleRemarksIsDisplay);
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
				String bigTitleRemarksIsDisplay="0";
				for (int j = 0; j < sfcList.size(); j++) {
					if(sfcList.get(j).getRemarks()!=null){
						bigTitleRemarksIsDisplay="1";
					}
					tsi = new TempletSalaryInfo();
					tsi.setBigTitlePosition(sfcList.get(j).getFieldNo());
					if ("1".equals(clientConfigInfo.getItemNameType())) {
						tsi.setBigTitle(sfcList.get(j).getDisplayName());
					}else if("2".equals(clientConfigInfo.getItemNameType())){
						tsi.setBigTitle(sfcList.get(j).getDisplayNameEn()==null?sfcList.get(j).getDisplayName():sfcList.get(j).getDisplayNameEn());
					}
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
				std.setLogoPath(clientConfigInfo.getLogoPath());
				std.setBigTitleRemarksIsDisplay(bigTitleRemarksIsDisplay);
				SalaryTempletDataList.add(std);
			} else {
				log.info("********** Not Exist This Type **********");
			}
		}
		return SalaryTempletDataList;
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
			}else{
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salaryMap;
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
	
}
