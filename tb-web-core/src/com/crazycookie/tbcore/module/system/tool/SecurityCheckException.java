package com.crazycookie.tbcore.module.system.tool;

/**
 * 当执行j_security_check检查发生异常时掷出。
 * 
 * @author newman.huang
 * @version 1.0 2007/2/26
 */
public class SecurityCheckException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SecurityCheckException(){
		super();
	}
	
	public SecurityCheckException(String message){
		super(message);
	}
}
