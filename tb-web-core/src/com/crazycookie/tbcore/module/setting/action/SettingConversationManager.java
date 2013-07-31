package com.crazycookie.tbcore.module.setting.action;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class SettingConversationManager {
	
	@Inject SettingAction settingAction;
	
	public void endTotalConversation(){
		settingAction.endConversation();
	}
}
