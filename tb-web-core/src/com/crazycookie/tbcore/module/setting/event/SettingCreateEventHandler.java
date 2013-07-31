package com.crazycookie.tbcore.module.setting.event;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.interfaces.setting.ISetting;
import com.crazycookie.tbcore.module.setting.qualifier.SettingRemoteBean;
import com.crazycookie.tbcore.setting.entity.UserSettingBean;
import com.crazycookie.tbcore.setting.qualifier.UserSettingBeanQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;

@Dependent
public class SettingCreateEventHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -203676591100685L;
	
	@Inject @UserSettingBeanQualifier UserSettingBean userSettingBean;
	@Inject @SettingRemoteBean ISetting settingRemoteBean;
	@Inject @UserInfoQualifier UserInfo userInfo;

	public void createUserSettingBean(@Observes @SettingCreateEventQualifier SettingCreateEvent event){
		
		UserSettingBean tempUserSettingBean = userSettingBean.clone();
		boolean result = settingRemoteBean.createUserSettingBean(tempUserSettingBean);
		FacesContext.getCurrentInstance().addMessage(
				FacesMessage.FACES_MESSAGES, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, result ? "success" : "fail", ""));
		userSettingBean.clear();
	}
}
