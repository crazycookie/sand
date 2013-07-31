package com.crazycookie.tbcore.module.setting.producer;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.naming.NamingException;

import com.crazycookie.tbcore.module.setting.qualifier.SettingRemoteBean;
import com.crazycookie.tbcore.module.system.TBCoreDaemon;
import com.crazycookie.tbcore.interfaces.setting.ISetting;

public class SettingRemoteBeanProducer {
	
	@EJB
	TBCoreDaemon tbCoreDaemon;
	
	@Produces
	@SettingRemoteBean
	ISetting produce(){
		ISetting settings = null;
 		try {
 			settings = (ISetting)tbCoreDaemon.lookup("SettingBean/remote");
 		} catch (NamingException e) {
			e.printStackTrace();
		}
 		return settings;
	}
}
