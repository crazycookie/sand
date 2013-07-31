package com.crazycookie.tbcore.setting.helper;

import java.io.Serializable;
import java.util.List;

import com.crazycookie.tbcore.setting.entity.UserSettingBean;
import com.crazycookie.tbcore.system.bean.PageObject;
import com.crazycookie.tbcore.system.interfaces.IQueryResult;

public class UserSettingBeanQueryResult implements IQueryResult<UserSettingBean>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -172252773998353L;
	
	private List<UserSettingBean> listUserSettingBean;
	private PageObject pageObject;
	
	@Override
	public List<UserSettingBean> getResultData() {
		return listUserSettingBean;
	}

	@Override
	public PageObject getPageObject() {
		return pageObject;
	}

	public void setListUserSettingBean(List<UserSettingBean> listUserSettingBean) {
		this.listUserSettingBean = listUserSettingBean;
	}

	public void setPageObject(PageObject pageObject) {
		this.pageObject = pageObject;
	}

}
