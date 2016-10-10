package com.salaryMail.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class CodeDetail implements RowMapper{
	
	private String code;
	private String codeDesc1Content;
	private String codeDesc2Content;
	private String codeDesc3Content;
	private String isValid;
	private String creator;
	private Date createDate;
	private String modifier;
	private Date modifyDate;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeDesc1Content() {
		return codeDesc1Content;
	}
	public void setCodeDesc1Content(String codeDesc1Content) {
		this.codeDesc1Content = codeDesc1Content;
	}
	public String getCodeDesc2Content() {
		return codeDesc2Content;
	}
	public void setCodeDesc2Content(String codeDesc2Content) {
		this.codeDesc2Content = codeDesc2Content;
	}
	public String getCodeDesc3Content() {
		return codeDesc3Content;
	}
	public void setCodeDesc3Content(String codeDesc3Content) {
		this.codeDesc3Content = codeDesc3Content;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		CodeDetail codeDetail = new CodeDetail();
		codeDetail.setCode(rs.getString("CODE"));
		codeDetail.setCodeDesc1Content(rs.getString("CODE_DESC_1_CONTENT"));
		codeDetail.setCodeDesc2Content(rs.getString("CODE_DESC_2_CONTENT"));
		codeDetail.setCodeDesc3Content(rs.getString("CODE_DESC_3_CONTENT"));
		codeDetail.setIsValid(rs.getString("IS_VALID"));
		codeDetail.setCreator(rs.getString("CREATOR"));
		codeDetail.setCreateDate(rs.getDate("CREATE_DATE"));
		codeDetail.setModifier(rs.getString("MODIFIER"));
		codeDetail.setModifyDate(rs.getDate("MODIFY_DATE"));
		return codeDetail;
	}
	
}
