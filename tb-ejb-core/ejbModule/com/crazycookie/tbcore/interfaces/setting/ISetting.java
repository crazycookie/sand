package com.crazycookie.tbcore.interfaces.setting;

import javax.ejb.Remote;

import com.crazycookie.tbcore.setting.entity.UserSettingBean;
import com.crazycookie.tbcore.setting.helper.UserSettingBeanCondition;
import com.crazycookie.tbcore.system.bean.PageObject;
import com.crazycookie.tbcore.system.interfaces.IQueryResult;

@Remote
public interface ISetting {
	
	public boolean createUserSettingBean(UserSettingBean userSettingBean);
	
	public int deleteUserSettingBean(UserSettingBean userSettingBean);
	
	public int updateUserSettingBean(UserSettingBean userSettingBean);

	public IQueryResult<UserSettingBean> queryUserSettingBean(UserSettingBeanCondition condition, PageObject pageObject);
	
	public UserSettingBean loadUserSettingBean(int userSettingBeanId);

}