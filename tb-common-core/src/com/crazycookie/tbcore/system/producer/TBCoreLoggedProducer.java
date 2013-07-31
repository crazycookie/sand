package com.crazycookie.tbcore.system.producer;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;

public class TBCoreLoggedProducer {
	
	@Produces
	@TBCoreLogged
	Logger produceLogger(InjectionPoint injectionPoint){		
 		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
}
