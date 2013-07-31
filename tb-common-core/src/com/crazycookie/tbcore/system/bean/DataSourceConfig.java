package com.crazycookie.tbcore.system.bean;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataSourceConfig {
	
	private String databaseJNDI = "java:/TBCoreDB";

	public String getDatabaseJNDI() {
		return databaseJNDI;
	}

	public void setDatabaseJNDI(String databaseJNDI) {
		this.databaseJNDI = databaseJNDI;
	}
}
