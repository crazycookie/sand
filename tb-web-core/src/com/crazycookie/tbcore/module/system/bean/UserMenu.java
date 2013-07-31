 package com.crazycookie.tbcore.module.system.bean;


import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named
public class UserMenu implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -248939498885547L;
	
	private Object currentMenuNav;

	public Object getCurrentMenuNav() {
		return currentMenuNav;
	}

	public void setCurrentMenuNav(Object currentMenuNav) {
		this.currentMenuNav = currentMenuNav;
	}

}
