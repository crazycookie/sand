package com.crazycookie.tbcore.interfaces.system;

import javax.ejb.Remote;

import com.crazycookie.tbcore.system.bean.UserInfo;


@Remote
public interface ITBCoreSystem {
	
	public boolean createUser(UserInfo userInfo);
	public UserInfo createOrUpdateUser(UserInfo userInfo);

}