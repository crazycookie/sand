package com.crazycookie.tbcore.impl.system;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.crazycookie.tbcore.interfaces.system.ITBCoreSystem;
import com.crazycookie.tbcore.system.bean.UserInfo;
import com.crazycookie.tbcore.system.helper.DatabaseHelper;

@Stateless
public class TBCoreSystemBean implements ITBCoreSystem {

	@Inject DatabaseHelper helper;
	@Resource EJBContext context;
	
	public static final int ADMIN = 1;
	public static final int MEMBER_ROLE = ADMIN + 1;
	
	private static final String INSERT_USER_SQL = "insert into users(userName, pwd, nickName, tbUserId, accessToken, refreshToken, entryUpdate) values(?, ?, ?, ?, ?, ?, now())";
	private static final String INSERT_USER_ROLE_REF_SQL = "insert into userRoleRef(userId, roleId) values(?, ?)";
	private static final String QUERY_USER_BY_NICKNAME_SQL = "select id, pwd from users where userName = ?";
	private static final String UPDATE_USER_SQL = "update users set accessToken = ?, refreshToken = ?, entryUpdate = now() where id = ?";
	
	@Override
	public boolean createUser(UserInfo userInfo) {
		Object[] params = new Object[]{
				userInfo.getTbUserNick(), 
				userInfo.getPassword(), 
				userInfo.getTbUserNick(),
				userInfo.getTbUserId(), 
				userInfo.getAccessToken(), 
				userInfo.getRefreshToken()};
		int returnKey = helper.doDMLOperationWithGeneratedKeyReturn(INSERT_USER_SQL, params);
		if (returnKey == -1){
			context.setRollbackOnly();
			return false;
		}
		params = new Object[]{returnKey, MEMBER_ROLE};
		int effectRowCount = helper.doDMLOperation(INSERT_USER_ROLE_REF_SQL, params);
		if (effectRowCount != 1){
			context.setRollbackOnly();
			return false;
		}
		
		return true;
	}

	@Override
	public UserInfo createOrUpdateUser(UserInfo userInfo) {
		Object[] params = new Object[]{userInfo.getTbUserNick()};
		List<Object[]> result = helper.doQuery(QUERY_USER_BY_NICKNAME_SQL, params);
		if (result != null && result.size() != 0){
			int userId = (Integer)result.get(0)[0];
			String pwd = (String)result.get(0)[1];
			/*
			 * Update mode
			 */
			params = new Object[]{userInfo.getAccessToken(), userInfo.getRefreshToken(), userId};
			helper.doDMLOperation(UPDATE_USER_SQL, params);
			userInfo.setPassword(pwd);
		}else{
			/*
			 * Create mode
			 */
			createUser(userInfo);
		}
		return userInfo;
	}
}
