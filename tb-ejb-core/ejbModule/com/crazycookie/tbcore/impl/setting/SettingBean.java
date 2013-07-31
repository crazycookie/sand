package com.crazycookie.tbcore.impl.setting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.crazycookie.tbcore.interfaces.setting.ISetting;
import com.crazycookie.tbcore.setting.entity.UserSettingBean;
import com.crazycookie.tbcore.setting.helper.UserSettingBeanCondition;
import com.crazycookie.tbcore.setting.helper.UserSettingBeanQueryResult;
import com.crazycookie.tbcore.system.bean.PageObject;
import com.crazycookie.tbcore.system.helper.DatabaseHelper;
import com.crazycookie.tbcore.system.interfaces.IQueryResult;

@Stateless
public class SettingBean implements ISetting {

	@Inject DatabaseHelper helper;
	@Resource EJBContext context;
	
	private static final String UPDATE_PASSWORD = "update users set pwd = ?, entryUpdate = now() where id = ?";
	
	@Override
	public boolean createUserSettingBean(UserSettingBean userSettingBean) {
		Object[] params = new Object[]{
			//TODO: Add parameters here.
		};
		return helper.doDMLOperation("", params) == 1; //TODO: Replace "" with proper sql.
	}
	
	@Override
	public int deleteUserSettingBean(UserSettingBean userSettingBean) {
		Object[] params = new Object[]{
			//TODO: Add parameters here.
		};
		return helper.doDMLOperation("", params); //TODO: Replace "" with proper sql.
	}
	
	@Override
	public int updateUserSettingBean(UserSettingBean userSettingBean) {
		Object[] params = new Object[]{
			userSettingBean.getPassword(), userSettingBean.getId()
		};
		return helper.doDMLOperation(UPDATE_PASSWORD, params);
	}

	@Override
	public IQueryResult<UserSettingBean> queryUserSettingBean(UserSettingBeanCondition condition,
			PageObject pageObject) {
		IQueryResult<UserSettingBean> returnResult = new UserSettingBeanQueryResult();
		
		// query total number
		List<Object[]> result = helper.doQuery("", null); //TODO: Replace "" with proper sql.
		if (result != null && result.size() != 0){
			pageObject.setTotalRecordsNumber(0);
		}
		((UserSettingBeanQueryResult)returnResult).setPageObject(pageObject);
		
		// query paging data
		Object[] params = new Object[] {
				pageObject.getOffset(),
				pageObject.getMaxRecordsPerPage()
			};
		result = helper.doQuery("", params); //TODO: Replace "" with proper sql.
		List<UserSettingBean> listUserSettingBean = new ArrayList<UserSettingBean>();
		if (result != null && result.size() != 0){
			UserSettingBean tempUserSettingBean = new UserSettingBean();
			for(Object[] row: result){
				UserSettingBean userSettingBean = tempUserSettingBean.clone();
//				userSettingBean.setId((Integer)row[0]);
				listUserSettingBean.add(userSettingBean);
			}
		}
		((UserSettingBeanQueryResult)returnResult).setListUserSettingBean(listUserSettingBean);
		
		return returnResult;
	}
	
	/**
	 * Return UserSettingBean Object
	 */
	@Override
	public UserSettingBean loadUserSettingBean(int userSettingBeanId) {
		Object[] params = new Object[]{userSettingBeanId};
		List<Object[]> result = helper.doQuery("", params);
		if (result != null && result.size() != 0){
			Object[] row = result.get(0);
			UserSettingBean userSettingBean = new UserSettingBean();
//			userSettingBean.setId((Integer)row[0]);
			return userSettingBean;
		}
		return null;
	}
}
