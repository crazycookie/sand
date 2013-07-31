package com.crazycookie.tbcore.module.system.producer;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.naming.NamingException;

import com.crazycookie.tbcore.module.system.TBCoreDaemon;
import com.crazycookie.tbcore.module.system.qualifier.TBCoreSystemSingleRemoteBean;
import com.crazycookie.tbcore.interfaces.system.ITBCoreSystemSingleton;

public class TBCoreSystemSingleBeanProducer {
	
	@EJB
	TBCoreDaemon tbCoreDaemon;
	
	@Produces
	@TBCoreSystemSingleRemoteBean
	ITBCoreSystemSingleton produce(){
		ITBCoreSystemSingleton systems = null;
 		try {
 			systems = (ITBCoreSystemSingleton)tbCoreDaemon.lookup("TBCoreSystemSingletonBean/remote");
 		} catch (NamingException e) {
			e.printStackTrace();
		}
 		return systems;
	}
}
