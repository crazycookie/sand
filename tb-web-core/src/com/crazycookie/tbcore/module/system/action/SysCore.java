package com.crazycookie.tbcore.module.system.action;

import java.net.URLDecoder;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.api.CrazyCookieOauthApi;
import com.crazycookie.tbcore.interfaces.system.ITBCoreSystem;
import com.crazycookie.tbcore.interfaces.system.ITBCoreSystemSingleton;
import com.crazycookie.tbcore.module.system.nav.SystemNav;
import com.crazycookie.tbcore.module.system.qualifier.TBCoreSystemRemoteBean;
import com.crazycookie.tbcore.module.system.qualifier.TBCoreSystemSingleRemoteBean;
import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.module.system.tool.JSecurityCheckHelper;
import com.crazycookie.tbcore.module.system.tool.PasswordGenerator;
import com.crazycookie.tbcore.system.bean.UserInfo;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;
import com.crazycookie.tbcore.system.qualifier.TBCoreTrace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Model
public class SysCore {
	
	@Inject @TBCoreSystemRemoteBean ITBCoreSystem systemRemoteBean;
	@Inject @TBCoreSystemSingleRemoteBean ITBCoreSystemSingleton systemSingleton;
	@Inject @UserInfoQualifier UserInfo userInfo;
	@Inject @TBCoreLogged Logger log;
	
	public Object logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return SystemNav.HOME;
	}
	
	public void register(){
		if (!userInfo.getPassword().equals(userInfo.getPasswordRetry())){
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = ResourceBundle.getBundle("messages.interest.Message", context.getViewRoot().getLocale());
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("interest.validator.passwordnotsame"), ""));
			return;
		}
		UserInfo user = userInfo.clone();
		user.setPassword(PasswordGenerator.encryptPassword(user.getPassword()));
		systemRemoteBean.createUser(user);
	}
	
	/**
	 * 用户从淘宝网站链接过来的用户的远程登陆
	 * 登陆算法：
	 * 1，取得url中的code
	 * 2，利用code值调用淘宝api换取accessToken和refreshToken等信息
	 * 3，检测本地数据库中是否存在'taobao_user_nick'的用户
	 * 4，如果存在该用户则更新accessToken和refreshToken信息
	 * 5，如果不存在该用户，则创建该用户，使用随机数创建密码，让用户成功登陆提示他修改密码以方便让其从网站登陆
	 */
	@TBCoreTrace
	public void remoteLogin(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		final String host = systemSingleton.retrieveSystemConfiguration().get("HOST").get(0);
		final String contextPath = context.getExternalContext().getRequestContextPath(); // '/love-web'
		final String servletPath = context.getExternalContext().getRequestServletPath(); // '/core/remote/login.faces'
		final Map<String, String> returnMap = context.getExternalContext().getRequestParameterMap();
		
		String sid = ((HttpSession)context.getExternalContext().getSession(true)).getId();
		
		if (returnMap.containsKey("code")){
			String returnJson = new CrazyCookieOauthApi().login(new CrazyCookieOauthApi.Setting() {
				
				@Override
				public String getAppKey() {
					return systemSingleton.retrieveSystemConfiguration().get("APPKEY").get(0);
				}
				@Override
				public String getAppSecret() {
					return systemSingleton.retrieveSystemConfiguration().get("APPSECRET").get(0);
				}
				@Override
				public String getRedirectUrl() {
					return host + contextPath + servletPath;
				}
				@Override
				public String getCode() {
					return returnMap.get("code");
				}
				@Override
				public String getOAuthUrl() {
					return systemSingleton.retrieveSystemConfiguration().get("OAUTHURL").get(0);
				}
			});
			
			log.debug(returnJson);
			
			Gson gson = new Gson();
			Map<String, Object> map = gson.fromJson(returnJson, new TypeToken<Map<String, Object>>(){}.getType());
			
			userInfo.setAccessToken((String)map.get("access_token"));
			userInfo.setRefreshToken((String)map.get("refresh_token"));
			userInfo.setTbUserId((String)map.get("taobao_user_id"));
			try {
				userInfo.setTbUserNick(URLDecoder.decode(
						(String) map.get("taobao_user_nick"), "utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			userInfo.setPassword(PasswordGenerator.generateRandomPassword());
			
			UserInfo backInfo = systemRemoteBean.createOrUpdateUser(userInfo.clone());
			
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			String url = context.getExternalContext().getRequestContextPath(); // '/love-web'
			try{
				response.sendRedirect(url);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			ResourceBundle bundle = ResourceBundle.getBundle("messages.ApplicationConfiguration");
			String securityCheckUrl = bundle.getString("jaas.security.checkPath");
			String nextUrl = bundle.getString("jaas.security.nextUrl");
			
			log.debug("securityCheckUrl=" + securityCheckUrl);
			log.debug("nextUrl=" + nextUrl);
			log.debug("begin Jaas authentication");
			
			try{
				JSecurityCheckHelper helper = new JSecurityCheckHelper();
				helper.doCheck(backInfo.getTbUserNick(), backInfo.getPassword(), sid, host + contextPath + nextUrl, host + contextPath + securityCheckUrl);
				log.info("authentication success, user: " + userInfo.getTbUserNick());
			}catch(Exception e){
				log.error("exception occur, user: " + userInfo.getTbUserNick());
				log.error(e);
			}
			
			
		}else if (returnMap.containsKey("error")){
			
			String errorCode = returnMap.get("error");
			String errorDescription = returnMap.get("error_description");
			
			log.error("Taobao send us an error callback url which within 'error' parameter");
			log.error("error code: " + errorCode);
			log.error("error description: " + errorDescription);
			
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			String url = context.getExternalContext().getRequestContextPath(); // '/love-web'
			try{
				response.sendRedirect(url);
			}catch(Exception e){
				e.printStackTrace();
			}
			context.responseComplete();
		}
		
	}
	
}
