package com.crazycookie.tbcore.system.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.crazycookie.tbcore.system.qualifier.TBCoreDataSource;

@Dependent
public class DatabaseHelper {
	
	@Inject @TBCoreDataSource DataSource ds;
	
	private enum OpMode {
		RETURN_EFFECT_ROW, RETURN_GEN_KEY,
	}
	
	public int doDMLOperationWithGeneratedKeyReturn(String sql, Object[] params){
		return doDML(sql, params, OpMode.RETURN_GEN_KEY);
	}
	
	public int doDMLOperation(String sql, Object[] params) {
		return doDML(sql, params, OpMode.RETURN_EFFECT_ROW);
	}
	
	private int doDML(String sql, Object[] params, OpMode flag){
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ds.getConnection();
			switch (flag) {
			case RETURN_EFFECT_ROW:
				stmt = conn.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
				break;
			case RETURN_GEN_KEY:
				stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				break;
			default:
				break;
			}
			
			if (params != null){
				for (int i = 0; i < params.length; i++) {
					if (params[i] instanceof String) {
						stmt.setString(i + 1, (String) params[i]);
					} else if (params[i] instanceof Integer) {
						stmt.setInt(i + 1, (Integer) params[i]);
					} else if (params[i] instanceof java.util.Date){
						stmt.setDate(i + 1, new java.sql.Date(((java.util.Date) params[i]).getTime()));
					} else {
						stmt.setObject(i + 1, params[i]);
					}
				}
			}
			
			switch (flag) {
			case RETURN_EFFECT_ROW:
				int effectRow = stmt.executeUpdate();
				return effectRow;
			case RETURN_GEN_KEY:
				stmt.executeUpdate();
				ResultSet gk = stmt.getGeneratedKeys();
				if (gk.next()){
					int generatedKey = gk.getInt(1);
					return generatedKey;
				}
			default:
				break;
			}
			
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlex) {
					// ignore, as we can't do anything about it here
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqlex) {
					// ignore, as we can't do anything about it here
				}
				conn = null;
			}
		}
	}
	
	public List<Object[]> doQuery(String sql, Object[] params) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ds.getConnection();
			stmt = conn.prepareStatement(sql);
			if (params != null){
				for (int i = 0; i < params.length; i++) {
					if (params[i] instanceof String) {
						stmt.setString(i + 1, (String) params[i]);
					} else if (params[i] instanceof Integer) {
						stmt.setInt(i + 1, (Integer) params[i]);
					} else if (params[i] instanceof java.util.Date){
						stmt.setDate(i + 1, new java.sql.Date(((java.util.Date) params[i]).getTime()));
					} else {
						stmt.setObject(i + 1, params[i]);
					}
				}
			}
			ResultSet rs = stmt.executeQuery();
			int colNumber = rs.getMetaData().getColumnCount();
			List<Object[]> listResult = new ArrayList<Object[]>();
			while (rs.next()){
				Object[] data = new Object[colNumber];
				for (int i = 0; i < colNumber; i ++){
					data[i] = rs.getObject(i + 1);
				}
				listResult.add(data);
			}
			rs.close();
			return listResult;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlex) {
					// ignore, as we can't do anything about it here
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqlex) {
					// ignore, as we can't do anything about it here
				}
				conn = null;
			}
		}
	}
}
