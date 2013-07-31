package com.crazycookie.tbcore.module.setting.event;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.crazycookie.tbcore.module.setting.qualifier.SettingRemoteBean;
import com.crazycookie.tbcore.module.system.bean.MsgTool;
import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.module.system.tool.PasswordGenerator;
import com.crazycookie.tbcore.interfaces.setting.ISetting;
import com.crazycookie.tbcore.setting.entity.UserSettingBean;
import com.crazycookie.tbcore.setting.qualifier.UserSettingBeanQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;

@Dependent
public class SettingUpdateEventHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -53230388496619L;
	
	@Inject @UserSettingBeanQualifier UserSettingBean userSettingBean;
	@Inject @SettingRemoteBean ISetting settingRemoteBean;
	@Inject @UserInfoQualifier UserInfo userInfo;
	@Inject MsgTool msgTool;

	public void updateUserSettingBean(@Observes @SettingUpdateEventQualifier SettingUpdateEvent event){
		
		if (userSettingBean != null && userSettingBean.getPassword().equals(userSettingBean.getRePassword())){
			UserSettingBean bean = userSettingBean.clone();
			bean.setId(userInfo.getUserId());
			bean.setPassword(PasswordGenerator.encryptPassword(userSettingBean.getPassword()));
			int result = settingRemoteBean.updateUserSettingBean(bean);
			
			switch (result) {
			case 1:
				msgTool.addMessage(null, false, "messages.setting.Message", "setting.modify.msg.success");
				break;

			default:
				msgTool.addMessage(null, true, "messages.setting.Message", "setting.modify.msg.failure");
				break;
			}
		} else {
			msgTool.addMessage(null, true, "messages.setting.Message", "setting.modify.msg.differentPwd");
		}

	}
}
