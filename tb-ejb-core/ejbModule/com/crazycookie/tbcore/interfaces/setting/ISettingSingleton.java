package com.crazycookie.tbcore.interfaces.setting;

import java.util.List;

import javax.ejb.Remote;

import com.crazycookie.tbcore.setting.entity.UserSettingType;

@Remote
public interface ISettingSingleton {
	
	public List<UserSettingType> retrieveUserSettingTypeList();
	
}
