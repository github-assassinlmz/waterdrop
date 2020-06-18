package com.sgcc.dlsc.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @title ResultSetPrcessListener
 * @description sql执行的监听
 * @author niemingming
 */
public interface ResultSetPrcessListener {

	public void processResult(Connection conn,PreparedStatement ps,ResultSet rs) throws SQLException;
	
}
