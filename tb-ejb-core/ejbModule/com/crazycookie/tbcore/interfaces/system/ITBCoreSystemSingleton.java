package com.crazycookie.tbcore.interfaces.system;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface ITBCoreSystemSingleton {
	
	public Map<String, List<String>> retrieveSystemConfiguration();
	
}
