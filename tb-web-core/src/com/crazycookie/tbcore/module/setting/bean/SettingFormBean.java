package com.crazycookie.tbcore.module.setting.bean;

import javax.enterprise.inject.Model;

@Model
public class SettingFormBean {
	
	private String pwd;
	private String retryPwd;
	
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getRetryPwd() {
		return retryPwd;
	}
	public void setRetryPwd(String retryPwd) {
		this.retryPwd = retryPwd;
	}
	
	
}
