package com.crazycookie.tbcore.setting.entity;

import java.io.Serializable;

import javax.enterprise.inject.Model;

import com.crazycookie.tbcore.setting.qualifier.UserSettingBeanQualifier;

@Model
@UserSettingBeanQualifier
public class UserSettingBean implements Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -20814329426296L;
	
	private int id;
	private String password;
	
	public void clear(){
		id = 0;
		password = null;
	}
	
	public void setUserSettingBean(UserSettingBean userSettingBean){
		id = userSettingBean.id;
		password = userSettingBean.password;
	}
	
	public UserSettingBean clone(){
		UserSettingBean userSettingBean = null;
		try{
			userSettingBean = (UserSettingBean)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return userSettingBean;	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
