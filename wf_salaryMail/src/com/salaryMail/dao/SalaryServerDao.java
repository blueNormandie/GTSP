package com.salaryMail.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

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

public interface SalaryServerDao {

	// ***** EPAY2.0 BEGIN *******
//	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String accym) throws Exception;
	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String realAccYm) throws Exception;
	// ***** EPAY2.0 END *******
	
	public List<SalaryTitleBean> findSalaryTitleForCoach(String companyNo, String realAccYm) throws Exception;

	// ***** EPAY2.0 BEGIN *******
//	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String accym, String addFlag,
//			String taxFlag, String wfFlag) throws Exception;
	public List<SalaryTitleBean> findSalaryTitle(String companyNo, String realAccYm, String addFlag,
			String taxFlag, String wfFlag) throws Exception;
	// ***** EPAY2.0 END *******

	public List<ClientConfigInfo> getClientConfigInfo();

	public ClientConfigInfo getClientConfigInfoByCompanyNo(String companyNo);

	public ClientInfo getClientInfoByCompanyNo(String companyNo) throws Exception;

	public List<Map<String, Object>> getSalaryInfoForArrow(String companyNo, String sendBeginYm,
			String sendDateType) throws SQLException;

	public List<Map<String, Object>> getSalaryInfo(String companyNo, String sendBeginYm,
			String sendDateType) throws SQLException;

	public List<Map<String, Object>> getSalaryInfoBaseMonth(String companyNo, String empNo,
			String baseMonth,String batchNo) throws SQLException;

	public Map<String, Object> getSalaryInfoForResend(String companyNo, String empNo,
			String batchNo, String accYm, String realAccYm) throws SQLException;

	public String checkEmployee(Map<String, Object> paramMap) throws SQLException;

	public CodeDetail getCodeDetail(String codeClass, String code);

	public String insertMailResultInfo(Map<String, Object> salaryInfo, String isYearBonusSend,
			String isSalarySend, String isStockSend, String isReleasePaySend, String mailTitle, String sendSuccess,
			String isSendAgain, String sender);

	public int updateMailResultInfo(String sendSuccess, String empNo, String failReason,
			String bizType, Long mailBatchNo, String filePath);

	public int updateMailResultInfoForResend(String sendSuccess, String userId, String mail,
			String failReason, String sendFilePath, Long resultSno);

	public String getNameByDepartNo(String departNo) throws SQLException;

	public String getBankAccountByEmpNo(String empNo) throws SQLException;

	public String getCostCenter(String companyNo, String empNo) throws SQLException;

	public List<Map<String, Object>> getSaleInfo(String companyNo) throws SQLException;

	public List<BaseItemConfig> getBaseItemConfig(int formatSno);

	public List<SalaryFieldConfig> getSalaryFieldConfig(int formatSno);

	public List<SalaryItemConfig> getSalaryItemConfig(int formatSno);

	public FormatInfo getFormatInfo(int formatSno);

	// ***** No：S20151358 BEGIN *******
//	public String getPreSendDate(String accYm, String companyNo);
	public String getPreSendDate(String realAccYm, String companyNo);
	// ***** No：S20151358 END *******

	public Date getRealSendDate(String empNo, String companyNo, String realAccYm, Integer kind,
			Integer batchNo);

	public Map<String, String> getDisSubsidy(String empNo, String companyNo, String realAccYm)
			throws SQLException, DataAccessException;
	
	public SalaryExecuteInfo getMailResultInfo(Long resultSno);

	public List<Map<String, Object>> getEmpPwd(String empNo);
	
	public List<Map<String, Object>> getInputPersonMail(String name);
	
	public List<Map<String, Object>> getFieldNameEn();
	
	// ***** No：S20151358 BEGIN *******
//	public List<Map<String, Object>> getbaseInfoNameEn(String companyNo,String accYm);
	public List<Map<String, Object>> getbaseInfoNameEn(String companyNo, String realAccYm);
	// ***** No：S20151358 END *******
	
	public int updateRcvBlackCode(String failReason, String empNo, String fileName);

	public int updateRcvBlackCode(Long resultSno, String failReason); 

	public MailSendInfo getMailSendInfo(Long resultSno);
	
	public String getCompanyJoinYear(String regNo, String companyNo);
	
	public int insertSalaryExecuteInfo(Map<String, Object> salaryInfo, String isYearBonusSend,
			String isSalarySend, String isStockSend, String isReleasePaySend);

	// ***** No：S20151358 BEGIN *******
	public Map<String, Object> getSalaryPeriod(String CompanyNo,String realAccYm);
	// ***** No：S20151358 END *******
}
