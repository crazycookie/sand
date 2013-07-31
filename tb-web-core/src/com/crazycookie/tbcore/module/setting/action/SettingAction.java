package com.crazycookie.tbcore.module.setting.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.module.setting.event.SettingCreateEvent;
import com.crazycookie.tbcore.module.setting.event.SettingCreateEventQualifier;
import com.crazycookie.tbcore.module.setting.event.SettingDeleteEvent;
import com.crazycookie.tbcore.module.setting.event.SettingDeleteEventQualifier;
import com.crazycookie.tbcore.module.setting.event.SettingQueryEvent;
import com.crazycookie.tbcore.module.setting.event.SettingQueryEventQualifier;
import com.crazycookie.tbcore.module.setting.event.SettingUpdateEvent;
import com.crazycookie.tbcore.module.setting.event.SettingUpdateEventQualifier;
import com.crazycookie.tbcore.module.setting.event.SettingQueryEvent.QueryMode;
import com.crazycookie.tbcore.module.setting.nav.SettingNav;
import com.crazycookie.tbcore.module.setting.qualifier.SettingSingleRemoteBean;
import com.crazycookie.tbcore.interfaces.setting.ISettingSingleton;
import com.crazycookie.tbcore.system.bean.PageObject;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;

@ConversationScoped
@Named
public class SettingAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -28032282102292L;
	
	@Inject Conversation conversation;
	@Inject @SettingSingleRemoteBean ISettingSingleton settingSingleton;
	@Inject @SettingCreateEventQualifier Event<SettingCreateEvent> settingCreateEvent;
	@Inject @SettingDeleteEventQualifier Event<SettingDeleteEvent> settingDeleteEvent;
	@Inject @SettingQueryEventQualifier Event<SettingQueryEvent> settingQueryEvent;
	@Inject @SettingUpdateEventQualifier Event<SettingUpdateEvent> settingUpdateEvent;
	@Inject @TBCoreLogged Logger log;
	private PageObject pageObject;
	
	public List<SelectItem> getUserSettingTypeList() {
		return null;
	}
	
	public void createUserSettingBean() {
		settingCreateEvent.fire(new SettingCreateEvent());
	}
	
	public void deleteUserSettingBean() {
		settingDeleteEvent.fire(new SettingDeleteEvent());
	}
	
	public void updateUserSettingBean(){
		settingUpdateEvent.fire(new SettingUpdateEvent());
	}
	
	public void queryUserSettingBean() {
		settingQueryEvent.fire(new SettingQueryEvent(QueryMode.LOAD_ALL_RECORD));
	}
	
	public Object startConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		return SettingNav.SETTING_HOME;
	}
	
	public void endConversation() {
		if (!(conversation.isTransient())) {
			conversation.end();
		}
	}

	public void loadPrePage(){
		if (pageObject.getCurrentPageNumber() > 1){
			int offset = pageObject.getOffset() - pageObject.getMaxRecordsPerPage();
			pageObject.setOffset(offset < 0 ? 0 : offset);
			pageObject.setCurrentPageNumber(pageObject.getCurrentPageNumber() - 1);
		}
		queryUserSettingBean();
	}
	
	public void loadNextPage(){
		if (pageObject.getCurrentPageNumber() < pageObject.getMaxPageNumber()){
			pageObject.setOffset(pageObject.getOffset() + pageObject.getMaxRecordsPerPage());
			pageObject.setCurrentPageNumber(pageObject.getCurrentPageNumber() + 1);
		}
		queryUserSettingBean();
	}
	
}
