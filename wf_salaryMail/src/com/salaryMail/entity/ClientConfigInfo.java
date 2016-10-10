package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class ClientConfigInfo implements RowMapper {

	private String companyNo;
	private String sales;
	private String belongToDept;
	private Integer formatSno;
	private String isMailSent;
	private String fileType;
	private String encryptMethod;
	private String isSiteDisplay;
	private String isAppDisplay;
	private String isWechatDisplay;
	private String mailSendFileType;
	private String mailTitle;
	private String mailContent;
	private String mailSign;
	private String sendDateType;
	private String sendBeginYm;
	private String itemNameType;
	private String isValid;
	private String creator;
	private Date createDate;
	private String modifier;
	private Date modifyDate;
	private String logoPath;
	private String isCompnameDisplay;
	
	// ***** No：S20160136 BEGIN *******
	private String isTxtMailContent;

	public String getIsTxtMailContent() {
		return isTxtMailContent;
	}

	public void setIsTxtMailContent(String isTxtMailContent) {
		this.isTxtMailContent = isTxtMailContent;
	}
	// ***** No：S20160136 END    *******
	
	public String getIsCompnameDisplay() {
		return isCompnameDisplay;
	}

	public void setIsCompnameDisplay(String isCompnameDisplay) {
		this.isCompnameDisplay = isCompnameDisplay;
	}

	/**
	 * @return the sales
	 */
	public String getSales() {
		return sales;
	}

	/**
	 * @param sales the sales to set
	 */
	public void setSales(String sales) {
		this.sales = sales;
	}

	/**
	 * @return the companyNo
	 */
	public String getCompanyNo() {
		return companyNo;
	}

	/**
	 * @param companyNo the companyNo to set
	 */
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	/**
	 * @return the formatNo
	 */
	public Integer getFormatSno() {
		return formatSno;
	}

	/**
	 * @param formatNo the formatNo to set
	 */
	public void setFormatNo(Integer formatSno) {
		this.formatSno = formatSno;
	}

	/**
	 * @return the isMailSent
	 */
	public String getIsMailSent() {
		return isMailSent;
	}

	/**
	 * @param isMailSent the isMailSent to set
	 */
	public void setIsMailSent(String isMailSent) {
		this.isMailSent = isMailSent;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the encryptMethod
	 */
	public String getEncryptMethod() {
		return encryptMethod;
	}

	/**
	 * @param encryptMethod the encryptMethod to set
	 */
	public void setEncryptMethod(String encryptMethod) {
		this.encryptMethod = encryptMethod;
	}

	/**
	 * @return the isSiteDisplay
	 */
	public String getIsSiteDisplay() {
		return isSiteDisplay;
	}

	/**
	 * @param isSiteDisplay the isSiteDisplay to set
	 */
	public void setIsSiteDisplay(String isSiteDisplay) {
		this.isSiteDisplay = isSiteDisplay;
	}

	/**
	 * @return the isAppDisplay
	 */
	public String getIsAppDisplay() {
		return isAppDisplay;
	}

	/**
	 * @param isAppDisplay the isAppDisplay to set
	 */
	public void setIsAppDisplay(String isAppDisplay) {
		this.isAppDisplay = isAppDisplay;
	}

	/**
	 * @return the isWechatDisplay
	 */
	public String getIsWechatDisplay() {
		return isWechatDisplay;
	}

	/**
	 * @param isWechatDisplay the isWechatDisplay to set
	 */
	public void setIsWechatDisplay(String isWechatDisplay) {
		this.isWechatDisplay = isWechatDisplay;
	}

	/**
	 * @return the mailSendFileType
	 */
	public String getMailSendFileType() {
		return mailSendFileType;
	}

	/**
	 * @param mailSendFileType the mailSendFileType to set
	 */
	public void setMailSendFileType(String mailSendFileType) {
		this.mailSendFileType = mailSendFileType;
	}

	/**
	 * @return the mailTitle
	 */
	public String getMailTitle() {
		return mailTitle;
	}

	/**
	 * @param mailTitle the mailTitle to set
	 */
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	/**
	 * @return the mailContent
	 */
	public String getMailContent() {
		return mailContent;
	}

	/**
	 * @param mailContent the mailContent to set
	 */
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	/**
	 * @return the mailSign
	 */
	public String getMailSign() {
		return mailSign;
	}

	/**
	 * @param mailSign the mailSign to set
	 */
	public void setMailSign(String mailSign) {
		this.mailSign = mailSign;
	}

	/**
	 * @return the sendDateType
	 */
	public String getSendDateType() {
		return sendDateType;
	}

	/**
	 * @param sendDateType the sendDateType to set
	 */
	public void setSendDateType(String sendDateType) {
		this.sendDateType = sendDateType;
	}

	/**
	 * @return the sendBeginYm
	 */
	public String getSendBeginYm() {
		return sendBeginYm;
	}

	/**
	 * @param sendBeginYm the sendBeginYm to set
	 */
	public void setSendBeginYm(String sendBeginYm) {
		this.sendBeginYm = sendBeginYm;
	}

	/**
	 * @return the itemNameType
	 */
	public String getItemNameType() {
		return itemNameType;
	}

	/**
	 * @param itemNameType the itemNameType to set
	 */
	public void setItemNameType(String itemNameType) {
		this.itemNameType = itemNameType;
	}


	/**
	 * @return the isValid
	 */
	public String getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	/**
	 * @return the logoPath
	 */
	public String getLogoPath() {
		return logoPath;
	}

	/**
	 * @param logoPath the logoPath to set
	 */
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	/**
	 * @param formatSno the formatSno to set
	 */
	public void setFormatSno(Integer formatSno) {
		this.formatSno = formatSno;
	}

	/**
	 * @return the belongToDept
	 */
	public String getBelongToDept() {
		return belongToDept;
	}

	/**
	 * @param belongToDept the belongToDept to set
	 */
	public void setBelongToDept(String belongToDept) {
		this.belongToDept = belongToDept;
	}
	
	

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClientConfigInfo clientConfigInfo=new ClientConfigInfo();
		
		clientConfigInfo.setCompanyNo(rs.getString("COMPANY_NO"));
		clientConfigInfo.setEncryptMethod(rs.getString("ENCRYPT_METHOD"));
		clientConfigInfo.setFileType(rs.getString("FILE_TYPE"));
		clientConfigInfo.setFormatNo(rs.getInt("FORMAT_SNO"));
		clientConfigInfo.setIsAppDisplay(rs.getString("IS_APP_DISPLAY"));
		clientConfigInfo.setIsMailSent(rs.getString("IS_MAIL_SENT"));
		clientConfigInfo.setIsSiteDisplay(rs.getString("IS_SITE_DISPLAY"));
		clientConfigInfo.setIsValid(rs.getString("IS_VALID"));
		clientConfigInfo.setIsWechatDisplay(rs.getString("IS_WECHAT_DISPLAY"));
		clientConfigInfo.setItemNameType(rs.getString("ITEM_NAME_TYPE"));
		clientConfigInfo.setMailContent(rs.getString("MAIL_CONTENT"));
		clientConfigInfo.setMailSendFileType(rs.getString("MAIL_SEND_FILE_TYPE"));
		clientConfigInfo.setMailSign(rs.getString("MAIL_SIGN"));
		clientConfigInfo.setMailTitle(rs.getString("MAIL_TITLE"));
		clientConfigInfo.setSendBeginYm(rs.getString("SEND_BEGIN_YM"));
		clientConfigInfo.setSendDateType(rs.getString("SEND_DATE_TYPE"));
		clientConfigInfo.setCreator(rs.getString("CREATOR"));
		clientConfigInfo.setCreateDate(rs.getDate("CREATE_DATE"));
		clientConfigInfo.setModifier(rs.getString("MODIFIER"));
		clientConfigInfo.setModifyDate(rs.getDate("MODIFY_DATE"));
		clientConfigInfo.setLogoPath(rs.getString("LOGO_PATH"));
		clientConfigInfo.setSales(rs.getString("SALES"));
		clientConfigInfo.setBelongToDept(rs.getString("BELONG_TO_DEPT"));
		clientConfigInfo.setIsCompnameDisplay(rs.getString("IS_COMPNAME_DISPLAY"));
		// ***** No：S20160136 BEGIN *******
		clientConfigInfo.setIsTxtMailContent(rs.getString("is_txt_mail_content"));
		// ***** No：S20160136 END    *******
		return clientConfigInfo;
	}

}
