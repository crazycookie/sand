package com.crazycookie.tbcore.impl.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.interfaces.setting.ISettingSingleton;
import com.crazycookie.tbcore.setting.entity.UserSettingType;
import com.crazycookie.tbcore.system.helper.DatabaseHelper;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(unit = TimeUnit.SECONDS, value = 5)
public class SettingSingletonBean implements ISettingSingleton {
	
	@Inject @TBCoreLogged Logger log;
	@Inject DatabaseHelper helper;
	
	private List<UserSettingType> returnList = null;

	@Override
	@Lock(LockType.READ)
	public List<UserSettingType> retrieveUserSettingTypeList() {
		return returnList;
	}
	
	@Schedule(minute = "*/10", hour = "*", persistent = false)
	public void timer(){
		List<UserSettingType> list = new ArrayList<UserSettingType>();
		//TODO: Write logic here, add values to 'list'.
		returnList = list;
	}
	
	@PostConstruct
	public void postConstruct(){
		log.info("[[ SettingSingletonBean start. ]]");
		timer();
	}
}
