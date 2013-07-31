package com.crazycookie.tbcore.module.system.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class TBCoreExceptionHandlerFactory extends
		ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;
	
	public TBCoreExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler result = parent.getExceptionHandler();
		return new TBCoreExceptionHandler(result);
	}

}
