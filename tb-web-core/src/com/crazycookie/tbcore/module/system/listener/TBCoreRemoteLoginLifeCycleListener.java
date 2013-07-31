package com.crazycookie.tbcore.module.system.listener;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.crazycookie.tbcore.module.system.action.SysCore;

public class TBCoreRemoteLoginLifeCycleListener implements javax.faces.event.PhaseListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1673709372104398620L;
	
	@Override
	public void afterPhase(PhaseEvent event) {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforePhase(PhaseEvent event) {
		
		BeanManager bm = null;
		try{
			bm = (BeanManager)new InitialContext().lookup("java:comp/BeanManager");
		}catch(NamingException e){
			e.printStackTrace();
		}
		
		if (bm != null){
			Bean<SysCore> bean = (Bean<SysCore>) bm.getBeans(SysCore.class).iterator().next();
			if (bean != null){
		        CreationalContext<SysCore> ctx = bm.createCreationalContext(bean);
		        SysCore sysCore = (SysCore) bm.getReference(bean, SysCore.class, ctx);
				sysCore.remoteLogin();
			}
		}
		
	}
	
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
