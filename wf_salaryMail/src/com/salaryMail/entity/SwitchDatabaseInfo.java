package com.salaryMail.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class SwitchDatabaseInfo implements RowMapper{
	
	private Integer dbSno;
	private String dbName;
	private String dbIp;
	private String IpPort;
	private String dbSid;
	private String userName;
	private String passwd;
	private String fremark;
	private Date createDate;
	
	public Integer getDbSno() {
		return dbSno;
	}
	public void setDbSno(Integer dbSno) {
		this.dbSno = dbSno;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbIp() {
		return dbIp;
	}
	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}
	public String getIpPort() {
		return IpPort;
	}
	public void setIpPort(String ipPort) {
		IpPort = ipPort;
	}
	public String getDbSid() {
		return dbSid;
	}
	public void setDbSid(String dbSid) {
		this.dbSid = dbSid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SwitchDatabaseInfo sd = new SwitchDatabaseInfo();
		sd.setDbSno(rs.getInt("db_sno"));
		sd.setDbName(rs.getString("db_Name"));
		sd.setDbIp(rs.getString("db_Ip"));
		sd.setIpPort(rs.getString("Ip_Port"));
		sd.setDbSid(rs.getString("db_Sid"));
		sd.setUserName(rs.getString("user_Name"));
		sd.setPasswd(rs.getString("passwd"));
		sd.setFremark(rs.getString("fremark"));
		sd.setCreateDate(rs.getDate("create_Date"));
		return sd;
	}
	
	
}
