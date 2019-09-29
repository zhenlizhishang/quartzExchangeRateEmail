package com.ljy.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class DBUtils {

	private static Connection getConn() {
	    Connection conn = null;
	    try {
	        Class.forName(XMLData.JDBCDRIVER); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(XMLData.JDBCURL, XMLData.JDBCUSERNAME, XMLData.JDBCPASSWORD);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	/**
	 * 新增
	 * @param sql
	 * @return
	 */
	public static int insertSql(String sql) {
	    Connection conn = getConn();
	    int i = 0;
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	
	/**
	 * 查询
	 * @param sql
	 * @return
	 */
	public static ResultSet getAll(String sql) {
	    Connection conn = getConn();
	    PreparedStatement pstmt;
	    ResultSet rs = null;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rs;
	}
}