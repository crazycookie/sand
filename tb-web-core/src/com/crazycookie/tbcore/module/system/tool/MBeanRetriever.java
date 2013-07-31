package com.crazycookie.tbcore.module.system.tool;

import java.lang.management.ManagementFactory;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.security.SimplePrincipal;

public class MBeanRetriever {
	
	public String getInfo(){
		try{
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			ObjectName jaasMgr = new ObjectName("jboss.security:service=JaasSecurityManager");
			Object[] params = {"love-domain", new SimplePrincipal("streetpoet"), "111111"};
			String[] signature = {"java.lang.String", "java.security.Principal", "java.lang.Object"};
			MBeanInfo info = server.getMBeanInfo(jaasMgr);
			MBeanOperationInfo[] infos = info.getOperations();
			for (MBeanOperationInfo o: infos){
				System.out.println(o.getName() + "|"+o.getReturnType());
				MBeanParameterInfo[] pinfos =  o.getSignature();
				for (MBeanParameterInfo i: pinfos){
					System.out.println(i.getName() + "-" + i.getType());
				}
			}
			Boolean result = (Boolean)server.invoke(jaasMgr, "isValid", params, signature);
			return result + "";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
