package com.dianping.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.dianping.dao.Dao;

public class BaseDao {
	private static String url = null;
	private static String user = null;
	private static String password = null;
	static {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("config");
			String driver = bundle.getString("driver");
			url = bundle.getString("url");
			user = bundle.getString("user");
			password = bundle.getString("password");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Connection getConn() throws Exception {
		return new Inner().getConn();
	}

	protected void close(ResultSet res, Statement stm, Connection conn) {
		new Inner().close(res, stm, conn);
	}

	private class Inner implements Dao {
		public Connection getConn() throws Exception {
			return DriverManager.getConnection(url, user, password);
		}
		public void close(ResultSet res, Statement stm, Connection conn) {
			if (res != null) {
				try {
					res.close();
					res = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stm != null) {
				try {
					stm.close();
					stm = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}