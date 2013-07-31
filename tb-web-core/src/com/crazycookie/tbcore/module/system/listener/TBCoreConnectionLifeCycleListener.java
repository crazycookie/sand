package com.crazycookie.tbcore.module.system.listener;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;
import com.taobao.api.internal.stream.connect.ConnectionLifeCycleListener;

@RequestScoped
public class TBCoreConnectionLifeCycleListener implements
		ConnectionLifeCycleListener {

	@Inject @TBCoreLogged Logger log;
	
	@Override
	public void onBeforeConnect() {
		log.info(TBCoreConnectionLifeCycleListener.class.getName() + "#onBeforeConnect has been invoked.");
	}

	@Override
	public void onException(Throwable e) {
		log.error(e.getMessage());
	}

	@Override
	public void onMaxReadTimeoutException() {
		log.error(TBCoreConnectionLifeCycleListener.class.getName() + "#onMaxReadTimeoutException has been invoked.");
	}

}
