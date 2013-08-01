package com.crazycookie.tbcore.module.system.tool;

import org.jboss.security.auth.spi.Util;


public class PasswordGenerator {

	public static final String DEFAULT_PWD = "123456";
	
	public static String encryptPassword(String password){
		return Util.createPasswordHash("MD5", Util.BASE64_ENCODING, null, null, password);
	}
	
	public static String generateRandomPassword(){
		return encryptPassword(System.currentTimeMillis() + "");
	}
	
	public static void main(String args[]){
		System.out.println(PasswordGenerator.encryptPassword(DEFAULT_PWD));
		System.out.println(PasswordGenerator.generateRandomPassword());
	}

}
