package com.qys.training.biz.auth.entity;

import com.qys.training.base.entity.BaseEntity;

public class User extends BaseEntity {

	private static final long serialVersionUID = -4208203204393914207L;
	
	private String username;
	private String password;
	
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
