package com.crazycookie.tbcore.module.system.listener;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.crazycookie.tbcore.module.system.action.SysCore;
import com.crazycookie.tbcore.system.bean.UserInfo;

public class TBCoreInitialSessionListener implements javax.faces.event.PhaseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1623986720431346669L;
	
	@Override
	public void afterPhase(PhaseEvent event) {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforePhase(PhaseEvent event) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		UserInfo userInfo = context.getApplication().evaluateExpressionGet(context,"#{userInfo}", UserInfo.class);
		if (userInfo != null && userInfo.getUserId() != 0) {
			return;
		}
		
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
				sysCore.initSession();
			}
		}
	}
	
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
