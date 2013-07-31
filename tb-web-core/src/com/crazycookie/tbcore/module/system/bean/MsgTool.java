package com.crazycookie.tbcore.module.system.bean;

import java.util.ResourceBundle;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Model
public class MsgTool {
	
	public void addMessage(String msg, boolean isErrorMsg, String bundleString, String key){
		
		FacesContext context = FacesContext.getCurrentInstance();
		String message = msg;
		
		if (bundleString != null && key != null){
			ResourceBundle bundle = ResourceBundle.getBundle(bundleString, context.getViewRoot().getLocale());
			message = bundle.getString(key);
		}
		
		context.addMessage(FacesMessage.FACES_MESSAGES, 
				new FacesMessage(isErrorMsg ? FacesMessage.SEVERITY_ERROR : FacesMessage.SEVERITY_INFO, message , ""));
		
	}
	
	public String getBundleString(String bundleString, String key){
		
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle(bundleString, context.getViewRoot().getLocale());
		return bundle.getString(key);
		
	}
}
