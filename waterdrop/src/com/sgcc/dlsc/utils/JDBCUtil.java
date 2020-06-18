package com.sgcc.dlsc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sgcc.dlsc.entity.DBEntity;
import com.sgcc.dlsc.listener.ResultSetPrcessListener;

/**
 * 
 * @title JDBCUtil
 * @description 数据库操作的工具类
 * @author niemingming
 */
public class JDBCUtil {
	/**构建一个小的数据库连接池，供一次计算使用*/
	private  List<Connection> connPool = new ArrayList<Connection>();
	/**构建单例对象*/
	private static JDBCUtil single;
	/**数据库配置对象*/
	private static DBEntity db;
	
	private static String lock = "jdbcUtilLock";
	
	public static boolean DebugMode = false;
	
	private JDBCUtil(){}
	
	public static JDBCUtil getInstance(){
		synchronized (lock) {
			if(single == null) {
				db = ReadFileUtil.getInstance().getDBConfigEntity();
				single = new JDBCUtil();
			}
		}
		return single;
	}
	/**
	 * 
	 * @description 获取连接
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public Connection getConnection(){
		if(connPool.isEmpty()) {
			connPool.add(createConn());
		}
		return connPool.remove(0);
	}
	/**
	 * 
	 * @description 关闭连接提交事务
	 * @param rs
	 * @param ps
	 * @param conn
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public void closeConnection(ResultSet rs, PreparedStatement ps,Connection conn){
		closeConnection(rs, ps, conn,true);
	}
	/**
	 * 
	 * @description 关闭连接根据需要提交事务
	 * @param rs
	 * @param ps
	 * @param conn
	 * @param shouldCommit
	 * @author niemingming
	 * @date 2014-2-8
	 */
	private void closeConnection(ResultSet rs, PreparedStatement ps,
			Connection conn, boolean shouldCommit) {
		try {
			if(rs!= null) {
				rs.close();
			}
			rs = null;
		} catch (Exception e) {
			if(DebugMode){
				e.printStackTrace();
			}
			rs = null;
		}finally{
			try {
				if(ps != null) {
					ps.close();
					ps = null;
				}
			} catch (Exception e2) {
				ps = null;
			}finally {
				try {
					if(conn != null && !conn.isClosed()) {
						if(shouldCommit) {
							conn.commit();
						}
						connPool.add(conn);
					}
				} catch (SQLException e) {
					if(DebugMode){
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @description 结束算法提交所有事务并关闭所有连接
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public void endAndCloseConnection(){
		while(connPool.size() > 0){
			try {
				Connection conn = connPool.remove(0);
				conn.commit();
				conn.close();
			} catch (Exception e) {
				if(DebugMode){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @description 创建连接
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	private static Connection createConn() {
		try {
			System.out.println("创建连接！");
			System.out.println(db.getDriverClassName());
			System.out.println(db.getUrl());
			System.out.println(db.getUsername());
			System.out.println(db.getPassword());
			Class.forName(db.getDriverClassName());
			return DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		} catch (Exception e) {
			if(DebugMode){
				e.printStackTrace();
			}
			//获取数据库连接失败
			System.exit(98);
		}
		return null;
	}
	/**
	 * 
	 * @description 执行查询语句返回查询结果
	 * @param sql
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	@SuppressWarnings("rawtypes")
	public List executeQuery(String sql){
		return executeQuery(sql,null,2);
	}
	/**
	 * 
	 * @description 执行查询方法返回查询结果
	 * @param sql
	 * @param params
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List executeQuery(String sql, Object[] params,int code) {
		System.out.println("调用");
		System.out.println(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List res = new ArrayList();
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			if(params != null) {
				for(int i = 0; i < params.length; i++ ) {
					ps.setObject(i + 1, params[i]);
				}
			}
			rs = ps.executeQuery();
			ResultSetMetaData rmd = rs.getMetaData();
			int count = rmd.getColumnCount();
			while (rs.next()) {
				if(count == 1) {
					res.add(rs.getObject(1));
				}else {
					Object[] resArr = new Object[count];
					for(int i = 0; i < resArr.length; i++ ) {
						resArr[i] = rs.getObject(i+1);
					}
					res.add(resArr);
				}
			}
		} catch (Exception e) {
			if(DebugMode){
				e.printStackTrace();
			}
			System.exit(code);
		}finally {
			closeConnection(rs, ps, conn);
		}
		return res;
	}
	/**
	 * 
	 * @description 执行更新操作，包括删除
	 * @param sql
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public int executeUpdate(String sql,int code) {
		return executeUpdate(sql,true,code);
	}
	/**
	 * 
	 * @description 执行更新操作，根据需要判断是否关闭事务
	 * @param sql
	 * @param shouldCommit
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public int executeUpdate(String sql, boolean shouldCommit,int code) {
		return executeUpdate(sql,null,shouldCommit,code);
	}
	/**
	 * 
	 * @description 执行更新操作，根据需要判断是否关闭事务
	 * @param sql
	 * @param object
	 * @param shouldCommit
	 * @return
	 * @author niemingming
	 * @date 2014-2-8
	 */
	public int executeUpdate(String sql, Object[] params, boolean shouldCommit,int code) {
		System.out.println("调用更新");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			if(params != null) {
				for(int i = 0; i < params.length; i++ ) {
					ps.setObject(i + 1, params[i]);
				}
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			if(DebugMode){
				e.printStackTrace();
			}
			System.exit(code);
		}finally {
			closeConnection(null, ps, conn,shouldCommit);
		}
		return 0;
	}
	/**
	 * 
	 * @description 根据监听处理查询时间
	 * @param lisnter
	 * @author niemingming
	 * @date 2014-2-12
	 */
	public void executUpdateByListener(String sql,ResultSetPrcessListener listener ,int code) {
		System.out.println("调用接口更新");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			listener.processResult(conn, ps, rs);
		} catch (Exception e) {
			if(DebugMode){
				e.printStackTrace();
			}
			System.exit(code);
		}finally {
			closeConnection(null, ps, conn,false);
		}
	}
}
