package com.crazycookie.tbcore.module.system.listener;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.module.system.action.TBCoreNotificationDaemon;

public class TBCoreSysEventListener implements SystemEventListener {
	
	Logger log = Logger.getLogger(TBCoreSysEventListener.class);
	
	@Override
	public boolean isListenerForSource(Object arg0) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		
		BeanManager bm = null;
		try{
			bm = (BeanManager)new InitialContext().lookup("java:comp/BeanManager");
		}catch(NamingException e){
			e.printStackTrace();
		}
		
		TBCoreNotificationDaemon daemon = null;
		
		if (bm != null){
			Bean<TBCoreNotificationDaemon> bean = (Bean<TBCoreNotificationDaemon>) bm.getBeans(TBCoreNotificationDaemon.class).iterator().next();
			if (bean != null){
		        CreationalContext<TBCoreNotificationDaemon> ctx = bm.createCreationalContext(bean);
		        daemon = (TBCoreNotificationDaemon) bm.getReference(bean, TBCoreNotificationDaemon.class, ctx);
			}
		}
		
		if (event instanceof PostConstructApplicationEvent){
			log.info("invoke PostConstructApplicationEvent");
			if (daemon != null){
				daemon.startup();
			}
		}else if (event instanceof PreDestroyApplicationEvent){
			log.info("invoke PreDestroyApplicationEvent");
			daemon.shutdown();
		}
	}

}
