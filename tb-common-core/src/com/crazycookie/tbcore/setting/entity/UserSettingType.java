package com.crazycookie.tbcore.setting.entity;

import java.io.Serializable;

import javax.enterprise.inject.Model;

@Model
public class UserSettingType implements Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -141627913014118L;
	
	private int id = -1;
	private String label;
	
	public void clear(){
		id = -1;
		label = "";
	}
	
	public void setUserSettingType(UserSettingType userSettingType){
		id = userSettingType.id;
		label = userSettingType.label;
	}
	
	public UserSettingType clone(){
		UserSettingType userSettingType = null;
		try{
			userSettingType = (UserSettingType)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return userSettingType;	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
