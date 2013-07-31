package com.crazycookie.tbcore.module.setting.event;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.crazycookie.tbcore.module.setting.qualifier.SettingRemoteBean;
import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.interfaces.setting.ISetting;
import com.crazycookie.tbcore.setting.entity.UserSettingBean;
import com.crazycookie.tbcore.setting.qualifier.UserSettingBeanQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;

@Dependent
public class SettingDeleteEventHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -256003621519759L;
	
	@Inject @UserSettingBeanQualifier UserSettingBean userSettingBean;
	@Inject @SettingRemoteBean ISetting settingRemoteBean;
	@Inject @UserInfoQualifier UserInfo userInfo;

	public void deleteUserSettingBean(@Observes @SettingDeleteEventQualifier SettingDeleteEvent event){
		
		int result = settingRemoteBean.deleteUserSettingBean(userSettingBean.clone());
		FacesContext context = FacesContext.getCurrentInstance();
		
		ResourceBundle bundle = ResourceBundle.getBundle("messages.setting.Message", context.getViewRoot().getLocale());
		switch (result) {
		case 1:
			context.addMessage(FacesMessage.FACES_MESSAGES, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("setting.modify.msg.success"), ""));
			break;

		default:
			context.addMessage(FacesMessage.FACES_MESSAGES, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("setting.modify.msg.failure"), ""));
			break;
		}
	}
}
