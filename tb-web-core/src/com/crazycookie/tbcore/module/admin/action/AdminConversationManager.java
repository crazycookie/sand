package com.crazycookie.tbcore.module.admin.action;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class AdminConversationManager {
	
	@Inject AdminAction adminAction;
	
	public void endTotalConversation(){
		adminAction.endConversation();
	}
}
