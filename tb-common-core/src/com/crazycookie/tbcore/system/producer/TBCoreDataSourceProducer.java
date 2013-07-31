package com.crazycookie.tbcore.system.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.InitialContext;

import com.crazycookie.tbcore.system.bean.DataSourceConfig;
import com.crazycookie.tbcore.system.qualifier.TBCoreDataSource;

public class TBCoreDataSourceProducer {

	@Inject DataSourceConfig config;
	
	@Produces
	@TBCoreDataSource
	@ApplicationScoped
	javax.sql.DataSource getDataSource(){
		javax.sql.DataSource ds = null;
		InitialContext ic = null;
		if (ic == null){
			try{
				ic = new InitialContext();
				ds = (javax.sql.DataSource)ic.lookup(config.getDatabaseJNDI());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return ds;
	}
}
