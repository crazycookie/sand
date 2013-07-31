package com.crazycookie.tbcore.api;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.interfaces.system.ITBCoreSystemSingleton;
import com.crazycookie.tbcore.module.system.qualifier.TBCoreSystemSingleRemoteBean;
import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;

@SessionScoped
public class CrazyCookieTbApi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8289090949307684710L;
	
	@Inject @TBCoreSystemSingleRemoteBean ITBCoreSystemSingleton systemSingleton;
	@Inject @UserInfoQualifier UserInfo user;
	@Inject @TBCoreLogged Logger log;
	
	private String url;
	private String appkey;
	private String secret;
	private String sessionKey;
	private TaobaoClient client;
	
	@PostConstruct
	public void construct(){
		url = systemSingleton.retrieveSystemConfiguration().get("APIURL").get(0);
		appkey = systemSingleton.retrieveSystemConfiguration().get("APPKEY").get(0);
		secret = systemSingleton.retrieveSystemConfiguration().get("APPSECRET").get(0);
		sessionKey = user.getAccessToken();
		client = new DefaultTaobaoClient(url, appkey, secret);
	}
	
	public void changeUserAccessToken(String newToken){
		sessionKey = newToken;
	}
	
	public TaobaoResponse execute(IRequestMaker requestMaker){
		
		TaobaoResponse response = null;
		
		try{
			response = client.execute(requestMaker.buildRequest(), sessionKey);
		}catch(Exception e){
			response = new TaobaoResponse() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
			};
			response.setErrorCode("API ERROR");
			response.setMsg(e.getMessage());
			e.printStackTrace();
		}

		return response;
	}
	
	public static interface IRequestMaker{
		public TaobaoRequest<? extends TaobaoResponse> buildRequest();
	}
}
