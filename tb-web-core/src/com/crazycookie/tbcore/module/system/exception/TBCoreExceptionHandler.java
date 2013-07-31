package com.crazycookie.tbcore.module.system.exception;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.module.system.nav.SystemNav;

public class TBCoreExceptionHandler extends
		ExceptionHandlerWrapper {

	private Logger log = Logger.getLogger(TBCoreExceptionHandler.class);
	private ExceptionHandler wrapped;

	public TBCoreExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	@Override
	public void handle() throws FacesException {
		
		Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
		
		while (iterator.hasNext()) {
			
			ExceptionQueuedEvent event = (ExceptionQueuedEvent)iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext)event.getSource();
			Throwable t = context.getException();
			FacesContext fc = FacesContext.getCurrentInstance();
			
			try{
				
				if (t.getClass().getName().contains("NonexistentConversationException")){
					Flash flash = fc.getExternalContext().getFlash();
					flash.putNow("detailDetails", t.getMessage());
					System.out.println("the error is put in the flash: " + t.getMessage());
					NavigationHandler navHandler = fc.getApplication().getNavigationHandler();
					navHandler.handleNavigation(fc, null, SystemNav.HOME.toString());
					fc.renderResponse();
				} else if (t instanceof ViewExpiredException){
					Flash flash = fc.getExternalContext().getFlash();
					flash.putNow("detailDetails", t.getMessage());
					System.out.println("the error is put in the flash: " + t.getMessage());
					NavigationHandler navHandler = fc.getApplication().getNavigationHandler();
					navHandler.handleNavigation(fc, null, SystemNav.HOME.toString());
					fc.renderResponse();
				} else {
					NavigationHandler navHandler = fc.getApplication().getNavigationHandler();
					navHandler.handleNavigation(fc, null, SystemNav.HOME.toString());
					fc.renderResponse();
				}
				
			}finally{
				iterator.remove();
				log.error(t.toString());
			}
		}
		getWrapped().handle();
	}

}
