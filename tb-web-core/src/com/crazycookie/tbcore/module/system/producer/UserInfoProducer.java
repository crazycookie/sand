package com.crazycookie.tbcore.module.system.producer;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;
import com.crazycookie.tbcore.system.helper.DatabaseHelper;
import com.crazycookie.tbcore.system.qualifier.TBCoreTrace;

public class UserInfoProducer {
	
	@Inject DatabaseHelper dbHelper;
	@Inject java.security.Principal principal;
	@Inject UserInfo userInfoSession;

	private static final String USER_QUERY_SQL = 
			 "select  "
					 +"    users.id, "
					 +"    users.tbUserNick, "
					 +"    users.tbUserId, "
					 +"    users.accessToken, "
					 +"    users.refreshToken, "
					 +"    roles.roleName "
					 +"from "
					 +"    users, "
					 +"    roles, "
					 +"    userRoleRef "
					 +"where "
					 +"    users.tbUserNick = ? "
					 +"        and users.id = userRoleRef.userId "
					 +"        and userRoleRef.roleId = roles.id ";
	
	@Produces
	@UserInfoQualifier
	@TBCoreTrace
	public UserInfo produceUserInfo(){
		
		if (userInfoSession.getUserId() != 0){
			return userInfoSession;
		}

		String userName = null;
		try{
			userName = principal.getName();
		}catch(Exception e){
			return userInfoSession;
		}
		
		List<Object[]> list = dbHelper.doQuery(USER_QUERY_SQL, new Object[]{userName});
		if (list != null && list.size() != 0){
			Object[] data = list.get(0);
			userInfoSession.setUserId((Integer)data[0]);
			userInfoSession.setTbUserNick((String)data[1]);
			userInfoSession.setTbUserId((String)data[2]);
			userInfoSession.setAccessToken((String)data[3]);
			userInfoSession.setRefreshToken((String)data[4]);
			
			String role = "";
			for (Object[] object: list){
				role += ("," + object[5]);
			}
			if (role.length() != 0) {
				role = role.substring(1);
			}
			userInfoSession.setRole(role);
		}
		return userInfoSession;
	}
}
