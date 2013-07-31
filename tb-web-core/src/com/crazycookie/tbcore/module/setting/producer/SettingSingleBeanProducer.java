package com.crazycookie.tbcore.module.setting.producer;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.naming.NamingException;

import com.crazycookie.tbcore.module.setting.qualifier.SettingSingleRemoteBean;
import com.crazycookie.tbcore.module.system.TBCoreDaemon;
import com.crazycookie.tbcore.interfaces.setting.ISettingSingleton;

public class SettingSingleBeanProducer {
	
	@EJB
	TBCoreDaemon tbCoreDaemon;
	
	@Produces
	@SettingSingleRemoteBean
	ISettingSingleton produce(){
		ISettingSingleton settings = null;
 		try {
 			settings = (ISettingSingleton)tbCoreDaemon.lookup("SettingSingletonBean/remote");
 		} catch (NamingException e) {
			e.printStackTrace();
		}
 		return settings;
	}
}
