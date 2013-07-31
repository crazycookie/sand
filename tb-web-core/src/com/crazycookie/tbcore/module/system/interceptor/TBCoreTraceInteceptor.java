package com.crazycookie.tbcore.module.system.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.system.qualifier.TBCoreTrace;

@Interceptor
@TBCoreTrace
public class TBCoreTraceInteceptor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -99696416510131L;
	
	Logger log = Logger.getLogger(TBCoreTraceInteceptor.class);
	
	@AroundInvoke
	public java.lang.Object invoke(javax.interceptor.InvocationContext context) throws Exception{
		Method m = context.getMethod();
		log.trace("Entering method: " + m.getDeclaringClass().getName() + "#" + m.getName());
		return context.proceed();
	}
	
}
