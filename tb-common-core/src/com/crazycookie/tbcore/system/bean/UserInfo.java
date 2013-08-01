package com.crazycookie.tbcore.system.bean;


import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named
public class UserInfo implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -140357036673267L;
	
	private int userId; // id
	private String tbUserNick; // taobao_user_nick
	private String tbUserId; // taobao_user_id
	private String password;
	private String passwordRetry;
	private String accessToken;
	private String refreshToken;
	
	public UserInfo clone(){
		UserInfo ui = null;
		try{
			ui = (UserInfo)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return ui;	
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTbUserNick() {
		return tbUserNick;
	}

	public void setTbUserNick(String tbUserNick) {
		this.tbUserNick = tbUserNick;
	}

	public String getTbUserId() {
		return tbUserId;
	}

	public void setTbUserId(String tbUserId) {
		this.tbUserId = tbUserId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRetry() {
		return passwordRetry;
	}

	public void setPasswordRetry(String passwordRetry) {
		this.passwordRetry = passwordRetry;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
