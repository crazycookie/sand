package com.crazycookie.tbcore.module.setting.event;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.module.setting.qualifier.SettingRemoteBean;
import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.interfaces.setting.ISetting;
import com.crazycookie.tbcore.module.setting.action.SettingAction;
import com.crazycookie.tbcore.setting.entity.UserSettingBean;
import com.crazycookie.tbcore.setting.qualifier.UserSettingBeanQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;

@Dependent
public class SettingQueryEventHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -180978816434607L;
	
	@Inject @UserInfoQualifier UserInfo userInfo;
	@Inject SettingAction settingAction;
	@Inject @SettingRemoteBean ISetting settingRemoteBean;
	@Inject @TBCoreLogged Logger log;
	@Inject @UserSettingBeanQualifier UserSettingBean userSettingBean;

	public void queryUserSettingBean(@Observes @SettingQueryEventQualifier SettingQueryEvent event){
		switch (event.getQueryMode()) {
		case LOAD_ALL_RECORD:
			loadAllSetting();
			break;
		case LOAD_SINGLE_RECORD:
			loadSingleSetting();
			break;

		default:
			break;
		}
	}
	
	private void loadSingleSetting(){
		UserSettingBean userSettingBean = settingRemoteBean.loadUserSettingBean(this.userSettingBean.getId());
		this.userSettingBean.setUserSettingBean(userSettingBean);
	}
	
	private void loadAllSetting(){
//		UserSettingBeanCondition userSettingBeanCondition = userSettingBeanQueryConversation.getUserSettingBeanCondition();
//		UserSettingBeanCondition tempUserSettingBeanCondition = userSettingBeanCondition.clone();
//		PageObject pageObject = userSettingBeanQueryConversation.getPageObject();
//		IQueryResult<UserSettingBean> result = settingRemoteBean.queryUserSettingBean(tempUserSettingBeanCondition, pageObject.clone());
//		userSettingBeanQueryConversation.setListUserSettingBean(result.getResultData());
//		
//		// set paging object
//		pageObject.setTotalRecordsNumber(result.getPageObject().getTotalRecordsNumber());
//		pageObject.setCurrentPageNumber(pageObject.getOffset() / (int)pageObject.getMaxRecordsPerPage() + 1);
//		pageObject.setMaxPageNumber((int)pageObject.getTotalRecordsNumber() / pageObject.getMaxRecordsPerPage() + ((int)pageObject.getTotalRecordsNumber() % pageObject.getMaxRecordsPerPage() == 0 ? 0 : 1));

	}
}
