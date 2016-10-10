package com.salaryMail.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class TestArchives {

	public static void main(String[] args) throws Exception {
		testOnlinePayrollPreview();
//		testArchivesWebService();
//		testArchivesWebService2();
//		testTime();
	}

	public static void testArchivesWebService() throws Exception {
//		String endpoint = "http://172.16.215.130:8083/wf_salaryMail/services/salaryWebService?wsdl";
		String endpoint = "http://172.16.100.12:8081/wf_salaryMail/services/salaryWebService?wsdl";

		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		call.setOperationName("salaryMailResend");

		Map<String, String> map = new HashMap<String, String>();
		map.put("EmpNo", "1668924");
		map.put("CompanyNo", "HK3791");
		map.put("BatchNo", "1");
		map.put("AccYm", "201511");
		map.put("RealAccYm", "201511");
		map.put("ResultSno", "20524");
		map.put("UserId", "0116");
		map.put("FilePath", "/home/httpd/imgehr.efesco.com/wf_salaryMail/SalaryFile/201511/201511Payslip_1668924_2081651320151130191000.pdf");

		JSONObject object = JSONObject.fromObject(map);

		String json = (String) call.invoke(new Object[] { object.toString() });
		JSONObject jsonObject = JSONObject.fromObject(json);
		String SendResult = (String) jsonObject.get("SendResult");
		System.out.println(SendResult);

	}

	public static void testArchivesWebService2() throws Exception {
//		String endpoint = "http://172.16.215.130:8083/wf_salaryMail_NA/services/salaryWebService?wsdl";
		String endpoint = "http://172.16.100.12:8081/wf_salaryMail/services/salaryWebService?wsdl";

		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		call.setOperationName("salaryPreview");

		Map map = new HashMap();
		map.put("EmpNo", "1709043");
		map.put("CompanyNo", "CA0292");
//		map.put("SfscCode", "793861520");

		JSONObject object = JSONObject.fromObject(map);

		String json = (String) call.invoke(new Object[] { object.toString() });
		JSONObject jsonObject = JSONObject.fromObject(json);
		String filePath = (String) jsonObject.get("FilePath");
		System.out.println(filePath);
	}
	
	public static void testOnlinePayrollPreview() throws Exception {
		  String endpoint = "http://172.16.13.27:8083/wf_salaryMail/services/salaryWebService?wsdl";
		  
		  Service service = new Service();
		  Call call = (Call) service.createCall();
		  call.setTargetEndpointAddress(new java.net.URL(endpoint));
		  call.setOperationName("OnlinePayrollPreview");
		  
		  Map map = new HashMap();
		  map.put("CompanyNo", "US0586");
		  map.put("EmpNo", "718836");
		  map.put("BatchNo", "1");
		  
		  JSONObject object = JSONObject.fromObject(map);
		  
		  String json = (String) call.invoke(new Object[] { object.toString() });
		  JSONObject jsonObject = JSONObject.fromObject(json);
		  String fileName = (String) jsonObject.get("result");
		  System.out.println("返回的超链接："+fileName);
	 }

	public static void testTime() throws Exception {
	    Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(df.format(date));
	}
}
