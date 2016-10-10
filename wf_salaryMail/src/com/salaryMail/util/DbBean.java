package com.salaryMail.util;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DbBean {

	public ArrayList getArrayListFromDb(String sql, Class c, Connection con) throws Exception {
		ArrayList list = new ArrayList();
		String[] EachItem = null;
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		java.sql.ResultSetMetaData metaData = rs.getMetaData();
		while (rs.next()) {
			int column = metaData.getColumnCount();
			Object bean = c.newInstance();
			for (int i = 0; i < column; i++) {
				int colType = metaData.getColumnType(i + 1);
				String propertyName = changeToMdlStr(metaData.getColumnName(i + 1));
				String getName = "get" + ("" + propertyName.charAt(0)).toUpperCase() + propertyName.substring(1);
				String setName = "set" + ("" + propertyName.charAt(0)).toUpperCase() + propertyName.substring(1);
				Method getMethod = bean.getClass().getMethod(getName, new Class[] {});
				Method setMethod = bean.getClass().getMethod(setName, new Class[] { getMethod.getReturnType() });
				if (getMethod.getReturnType().getName().equals("java.lang.Integer")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { new Integer(rs.getInt(i + 1)) });
				} else if (getMethod.getReturnType().getName().equals("java.lang.Double")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { new Double(rs.getDouble(i + 1)) });
				} else if (getMethod.getReturnType().getName().equals("java.lang.String")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { rs.getString(i + 1) });
				} else if (getMethod.getReturnType().getName().equals("java.util.Date")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { rs.getTimestamp(i + 1) });
				} else if (getMethod.getReturnType().getName().equals("java.lang.Long")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { new Long(rs.getLong(i + 1)) });
				}
			}
			list.add(bean);
		}
		rs.close();
		st.close();
		return list;
	}

	public Object getObjectFromDb(String sql, Class c, Connection con) throws Exception {
		String[] EachItem = null;
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		java.sql.ResultSetMetaData metaData = rs.getMetaData();
		Object bean = c.newInstance();
		if (rs.next()) {
			int column = metaData.getColumnCount();
			for (int i = 0; i < column; i++) {
				int colType = metaData.getColumnType(i + 1);
				String propertyName = changeToMdlStr(metaData.getColumnName(i + 1));
				String getName = "get" + ("" + propertyName.charAt(0)).toUpperCase() + propertyName.substring(1);
				String setName = "set" + ("" + propertyName.charAt(0)).toUpperCase() + propertyName.substring(1);
				Method getMethod = bean.getClass().getMethod(getName, new Class[] {});
				Method setMethod = bean.getClass().getMethod(setName, new Class[] { getMethod.getReturnType() });
				if (getMethod.getReturnType().getName().equals("java.lang.Integer")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { new Integer(rs.getInt(i + 1)) });
				} else if (getMethod.getReturnType().getName().equals("java.lang.Double")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { new Double(rs.getDouble(i + 1)) });
				} else if (getMethod.getReturnType().getName().equals("java.lang.String")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { rs.getString(i + 1) });
				} else if (getMethod.getReturnType().getName().equals("java.util.Date")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { rs.getTimestamp(i + 1) });
				} else if (getMethod.getReturnType().getName().equals("java.lang.Long")) {
					if (rs.getString(i + 1) != null)
						setMethod.invoke(bean, new Object[] { new Long(rs.getLong(i + 1)) });
				}
			}
		}
		rs.close();
		st.close();
		return bean;
	}

	public Object getObjectFromRs(ResultSet rs, Class c) throws Exception {
		String[] EachItem = null;
		java.sql.ResultSetMetaData metaData = rs.getMetaData();
		Object bean = c.newInstance();
		int column = metaData.getColumnCount();

		for (int i = 0; i < column; i++) {
			int colType = metaData.getColumnType(i + 1);
			String propertyName = changeToMdlStr(metaData.getColumnName(i + 1));
			String getName = "get" + ("" + propertyName.charAt(0)).toUpperCase() + propertyName.substring(1);
			String setName = "set" + ("" + propertyName.charAt(0)).toUpperCase() + propertyName.substring(1);
			Method getMethod = bean.getClass().getMethod(getName, new Class[] {});
			Method setMethod = bean.getClass().getMethod(setName, new Class[] { getMethod.getReturnType() });

			if (getMethod.getReturnType().getName().equals("java.lang.Integer")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { new Integer(rs.getInt(i + 1)) });
			} else if (getMethod.getReturnType().getName().equals("java.lang.Double")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { new Double(rs.getDouble(i + 1)) });
			} else if (getMethod.getReturnType().getName().equals("java.lang.String")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { rs.getString(i + 1) });
			} else if (getMethod.getReturnType().getName().equals("java.util.Date")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { rs.getTimestamp(i + 1) });
			} else if (getMethod.getReturnType().getName().equals("java.lang.Long")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { new Long(rs.getLong(i + 1)) });
			} else if (getMethod.getReturnType().getName().equals("int")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { new Integer(rs.getInt(i + 1)) });
			} else if (getMethod.getReturnType().getName().equals("long")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { new Long(rs.getLong(i + 1)) });
			} else if (getMethod.getReturnType().getName().equals("double")) {
				if (rs.getString(i + 1) != null)
					setMethod.invoke(bean, new Object[] { new Double(rs.getDouble(i + 1)) });
			}
		}
		return bean;
	}

	public String changeToMdlStr(String str) {
		str = str.toLowerCase();
		String tmpStr = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '_') {
				i++;
				if (i < str.length())
					tmpStr += (str.charAt(i) + "").toUpperCase();
			} else {
				tmpStr += str.charAt(i);
			}
		}
		return tmpStr;
	}

}
