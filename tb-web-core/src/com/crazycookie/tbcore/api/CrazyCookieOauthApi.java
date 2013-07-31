package com.crazycookie.tbcore.api;

import java.util.HashMap;
import java.util.Map;

import com.taobao.api.internal.util.WebUtils;

public class CrazyCookieOauthApi {
	
	public String login(Setting setting){
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("grant_type", "authorization_code");
		param.put("code", setting.getCode());
		param.put("client_id", setting.getAppKey());
		param.put("client_secret", setting.getAppSecret());
		param.put("redirect_uri", setting.getRedirectUrl());
		param.put("scope", "item");
		param.put("view", "web");
		try{
			String responseJson = WebUtils.doPost(setting.getOAuthUrl(), param, 5000, 5000);
			return responseJson;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static abstract class Setting{
		public abstract String getCode();
		public abstract String getAppKey();
		public abstract String getAppSecret();
		public abstract String getRedirectUrl();
		public abstract String getOAuthUrl();
	}
	
}
