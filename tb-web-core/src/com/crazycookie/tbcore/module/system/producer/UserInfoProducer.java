package com.crazycookie.tbcore.module.system.producer;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;
import com.crazycookie.tbcore.system.helper.DatabaseHelper;

public class UserInfoProducer {
	
	@Inject DatabaseHelper dbHelper;
	@Inject java.security.Principal principal;

	private static final String USER_QUERY_SQL = 
		   "select users.id, users.tbUserNick, users.tbUserId, users.accessToken, users.refreshToken "
		+ " from users"
		+ " where users.tbUserNick = ?";
	
	@Produces
	@UserInfoQualifier
	UserInfo produceUserInfo(){
		
		UserInfo userInfo = new UserInfo();
		String userName = "";
		try{
			userName = principal.getName();
		}catch(Exception e){
			return userInfo;
		}
		
		List<Object[]> list = dbHelper.doQuery(USER_QUERY_SQL, new Object[]{userName});
		if (list != null && list.size() != 0){
			Object[] data = list.get(0);
			userInfo.setUserId((Integer)data[0]);
			userInfo.setTbUserNick((String)data[1]);
			userInfo.setTbUserId((String)data[2]);
			userInfo.setAccessToken((String)data[3]);
			userInfo.setRefreshToken((String)data[4]);
		}
		return userInfo;
	}
}
