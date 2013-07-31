package com.crazycookie.tbcore.module.system.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.module.system.listener.TBCoreConnectionLifeCycleListener;
import com.crazycookie.tbcore.module.system.listener.TBCoreTopCometMessageListener;
import com.crazycookie.tbcore.module.system.qualifier.TBCoreSystemSingleRemoteBean;
import com.crazycookie.tbcore.interfaces.system.ITBCoreSystemSingleton;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;
import com.taobao.api.internal.stream.Configuration;
import com.taobao.api.internal.stream.TopCometStream;
import com.taobao.api.internal.stream.TopCometStreamFactory;
import com.taobao.api.internal.stream.TopCometStreamRequest;

@ApplicationScoped
public class TBCoreNotificationDaemon {
	
	@Inject @TBCoreSystemSingleRemoteBean ITBCoreSystemSingleton systemSingleton;
	@Inject @New TBCoreConnectionLifeCycleListener connectionLifeCycleListener;
	@Inject @New TBCoreTopCometMessageListener topCometMessageListener;
	@Inject @TBCoreLogged Logger log;
	
	private TopCometStream stream;
	private boolean isServiceOn;
	
	@PostConstruct
	public void onConstruct(){
		 isServiceOn = Boolean.parseBoolean(systemSingleton.retrieveSystemConfiguration().get("TB_PUSH_SERVICE_ENABLE").get(0));
	}
	
	public void startup(){

		if (!isServiceOn){
			return;
		}
		
		log.info("TBCoreNotificationDaemon is starting");
		
		/*
		 * setup long connection with Taobao
		 */
		
		String identifier = generateIdentifier();
		log.info("Taobao long connection with identifier: " + identifier);
		
		Set<TopCometStreamRequest> cometReqs = new HashSet<TopCometStreamRequest>();
		int maxThreadNum = Integer.parseInt(systemSingleton.retrieveSystemConfiguration().get("TB_PUSH_THREAD_NUM").get(0));
		for (int i = 0; i < maxThreadNum; i ++){
			TopCometStreamRequest request = new TopCometStreamRequest(
					systemSingleton.retrieveSystemConfiguration().get("APPKEY").get(0),
					systemSingleton.retrieveSystemConfiguration().get("APPSECRET").get(0),
					null,
					identifier + "-" + i);
			request.setConnectListener(connectionLifeCycleListener);
			request.setMsgListener(topCometMessageListener);
			cometReqs.add(request);
		}
		Configuration conf = new Configuration(cometReqs);
		
		if (stream == null){
			stream = new TopCometStreamFactory(conf).getInstance();
			stream.start();
		}
	}
	
	public String generateIdentifier(){
		String identifier = null;
		InetAddress addr = null;
		try{
			 addr = InetAddress.getLocalHost();
		}catch(UnknownHostException e){
			log.error(e);
		}
		if (addr != null){
			if (addr.getHostAddress() != null){
				identifier = addr.getHostAddress();
			}
		}else{
			identifier = System.currentTimeMillis() + "";
			identifier = identifier.substring(identifier.length() - 6, identifier.length());
		}
		return identifier;
	}
	
	public void shutdown(){
		
		if (!isServiceOn){
			return;
		}
		
		log.info("TBCoreNotificationDaemon is shutdown");
		
		if (stream != null){
			stream.stop();
		}
	}
}
