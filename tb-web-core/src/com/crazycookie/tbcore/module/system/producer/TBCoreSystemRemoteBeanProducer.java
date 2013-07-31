package com.crazycookie.tbcore.module.system.producer;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.naming.NamingException;

import com.crazycookie.tbcore.module.system.TBCoreDaemon;
import com.crazycookie.tbcore.module.system.qualifier.TBCoreSystemRemoteBean;
import com.crazycookie.tbcore.interfaces.system.ITBCoreSystem;

public class TBCoreSystemRemoteBeanProducer {
	
	@EJB
	TBCoreDaemon tbCoreDaemon;
	
	@Produces
	@TBCoreSystemRemoteBean
	ITBCoreSystem produce(){
		ITBCoreSystem systems = null;
 		try {
 			systems = (ITBCoreSystem)tbCoreDaemon.lookup("TBCoreSystemBean/remote");
 		} catch (NamingException e) {
			e.printStackTrace();
		}
 		return systems;
	}
}
