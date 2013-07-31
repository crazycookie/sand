package com.crazycookie.tbcore.module.system;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
public class TBCoreDaemon{

	@Inject @TBCoreLogged Logger log;
	private static InitialContext initialContext;
		
	@PostConstruct
	public void postConstruct(){
		log.info("### TBCoreDaemon start. ###");
	}
	
	@PreDestroy
	public void preDestory(){
		log.info("### TBCoreDaemon end. ###");
	}
	
	@Lock(LockType.READ)
	public Object lookup(String jndi) throws NamingException{
		return getInitialContext().lookup(jndi);
	}
	
	private InitialContext getInitialContext(){
		if (initialContext == null){
			try{
				initialContext = new InitialContext();
	 			initialContext.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
	 			initialContext.addToEnvironment(Context.PROVIDER_URL, "jnp://localhost:1099");
	 			initialContext.addToEnvironment(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			}catch(Exception e){
				log.error(e);
			}
		}
		return initialContext;
	}

}
