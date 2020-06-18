package com.sgcc.dlsc.entity;

/**
 * 
 * @title DBEntity
 * @description 数据库配置实体对象
 * @author niemingming
 */
public class DBEntity {
	/**数据库驱动*/
	private String driverClassName;
	/**数据库连接地址*/
	private String url;
	/**数据库用户名*/
	private String username;
	/**数据库密码*/
	private String password;
	
	public DBEntity() {
	}
	public DBEntity(String driverClassName, String url, String username,
			String password) {
		super();
		this.driverClassName = driverClassName;
		this.url = url;
		this.username = username;
		this.password = password;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
