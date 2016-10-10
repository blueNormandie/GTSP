package com.salaryMail.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.salaryMail.dao.SalaryServerDao;
import com.salaryMail.dao.impl.SalaryServerDaoImpl;
import com.salaryMail.entity.ClientConfigInfo;
import com.salaryMail.service.OnlinePayrollPreviewService;
import com.salaryMail.service.SalaryServerService;
import com.salaryMail.util.SpringUtil;

public class SalaryWebService {

	private SalaryServerService salaryServerService = (SalaryServerService) SpringUtil
			.getApplicationContext().getBean("salaryServerService");
	private OnlinePayrollPreviewService onlinePayrollPreviewService = (OnlinePayrollPreviewService) SpringUtil
			.getApplicationContext().getBean("OnlinePayrollPreviewService");

	/**
	 * 工资单文件预览
	 * 
	 * @param json
	 * @return JSONArray
	 * @throws Exception
	 */
	public String salaryPreview(String json) {

		// 取得电脑号、商社代码
		JSONObject jsonObject = JSONObject.fromObject(json);
		String empNo = (String) jsonObject.get("EmpNo");
		String companyNo = (String) jsonObject.get("CompanyNo");
		String batchNo = (String) jsonObject.get("BatchNo");
		if (StringUtils.isEmpty(batchNo)) {
			batchNo = "1";
		}

		Map<String, String> map = new HashMap<String, String>();
		try {
			String filePath = salaryServerService.salaryPreview(companyNo, empNo, batchNo);

			map.put("FilePath", filePath);

			JSONObject retObject = JSONObject.fromObject(map);
			return retObject.toString();
		} catch (Exception e) {
			e.printStackTrace();

			map.put("FilePath", "");
			JSONObject retObject = JSONObject.fromObject(map);
			return retObject.toString();
		}
	}

	/**
	 * 工资单文件重发
	 * 
	 * @param json
	 * @return JSONArray
	 * @throws Exception
	 */
	public String salaryMailResend(String json) {

		// 取得电脑号、商社代码
		// JSONArray jsonArray = JSONArray.fromObject(json);
		// Map<String, Boolean> map = null;
		// JSONArray jsonRet = new JSONArray();
		//
		// String empNo = "";
		// String companyNo = "";
		// String batchNo = "";
		// String accYm = "";
		// String realAccYm = "";
		// String resultSno = "";
		// String userId = "";

		// 取得电脑号、商社代码
		JSONObject jsonObject = JSONObject.fromObject(json);
		String empNo = (String) jsonObject.get("EmpNo");
		String companyNo = (String) jsonObject.get("CompanyNo");
		String batchNo = (String) jsonObject.get("BatchNo");
		// ***** EPAY2.0 BEGIN *******
		// String accYm = (String) jsonObject.get("AccYm");
		String accYm = (String) jsonObject.get("RealAccYm");
		// ***** EPAY2.0 END *******
		String realAccYm = (String) jsonObject.get("RealAccYm");
		String resultSno = (String) jsonObject.get("ResultSno");
		String userId = (String) jsonObject.get("UserId");
		String filePath = (String) jsonObject.get("FilePath");
		Map<String, String> map = new HashMap<String, String>();

		try {
			boolean result = salaryServerService.salaryMailResend(companyNo, empNo, batchNo, accYm,
					realAccYm, userId, new Long(resultSno), filePath);

			map.put("SendResult", String.valueOf(result));
			JSONObject retObject = JSONObject.fromObject(map);
			return retObject.toString();
		} catch (Exception e) {
			e.printStackTrace();

			map.put("SendResult", String.valueOf(false));
			JSONObject retObject = JSONObject.fromObject(map);
			return retObject.toString();
		}
	}

	/**
	 * 网上工资单预览
	 * 
	 * @param json
	 * @return JSONArray
	 * @throws Exception
	 */
	public String OnlinePayrollPreview(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String companyNo = jsonObject.getString("CompanyNo");
		String empNo = jsonObject.getString("EmpNo");
		String batchNo = jsonObject.getString("BatchNo");
		if (StringUtils.isEmpty(batchNo)) {
			batchNo = "1";
		}

		Map<String, String> map = new HashMap<String, String>();
		try {
			String HTMLFileName = onlinePayrollPreviewService.OnlineSalaryPreview(companyNo, empNo,
					batchNo);
			map.put("result", HTMLFileName);
			JSONObject retObject = JSONObject.fromObject(map);
			return retObject.toString();
		} catch (Exception e) {
			e.printStackTrace();

			map.put("result", "");
			JSONObject retObject = JSONObject.fromObject(map);
			return retObject.toString();
		}

	}

}
