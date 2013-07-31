package com.crazycookie.tbcore.module.system.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.jboss.logging.Logger;

public class TBCoreLifeCycleListener implements javax.faces.event.PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -206165997207663L;

	Logger log = Logger.getLogger(TBCoreLifeCycleListener.class);
	
	@Override
	public void afterPhase(PhaseEvent event) {
		log.trace("invoke afterPhase, " + event.getPhaseId().toString());
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		log.trace("invoke beforePhase, " + event.getPhaseId().toString());
		log.trace(event.getFacesContext().getExternalContext().getRequestParameterMap());
	}
	
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
