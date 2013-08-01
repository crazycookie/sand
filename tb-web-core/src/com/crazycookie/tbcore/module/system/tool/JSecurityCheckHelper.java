package com.crazycookie.tbcore.module.system.tool;

import java.io.IOException;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * <code>JSecurityCheckHelper</code>利用HttpClient实现JAAS签权。
 * 
 * @author newman.huang
 * @version 1.0 2007/2/26
 */
public class JSecurityCheckHelper {

	private static final boolean debugOn = false;

	private HttpClient httpClient;

	/**
	 * 构建。
	 * 
	 */
	public JSecurityCheckHelper() {
		this.httpClient = new HttpClient();
	}

	/**
	 * 执行j_security_check。
	 * 
	 * @param userName
	 *            待签权的用户名。
	 * @param password
	 *            待签权用户的密码。
	 * @param jSessionId
	 *            代表当前web会话的session id。
	 * @param jSecCheckFullURL
	 *            j_security_check的完整url。
	 * @return 返回签权成功后的重定向url。
	 * 
	 * @throws SecurityCheckException
	 *             如果执行j_security_check的过程发生异常、或者签权不通过时掷出。
	 */
	public String doCheck(String userName, String password, String jSessionId, String protectedUrl, String jSecCheckFullURL) throws SecurityCheckException {

		if (userName == null || "".equals(userName.trim())) {
			throw new SecurityCheckException("invalid username");
		}

		String redirectURL = null;
		PostMethod postMethod = null;
		try {
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(30000);
			httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
			HttpState initialState = new HttpState();
			initialState.addCookie(genRequestCookie(jSessionId, jSecCheckFullURL));
			httpClient.setState(initialState);
			postMethod = new UTF8PostMethod(jSecCheckFullURL);
			NameValuePair[] postData = new NameValuePair[2];
			postData[0] = new NameValuePair("j_username", userName);
			postData[1] = new NameValuePair("j_password", password);
			postMethod.addParameters(postData);
			try {
				httpClient.executeMethod(new GetMethod(protectedUrl));
				int statusCode = httpClient.executeMethod(postMethod);
				Header locationHeader = postMethod.getResponseHeader("location");
				redirectURL = locationHeader != null ? locationHeader.getValue() : null;
				if (HttpStatus.SC_MOVED_PERMANENTLY == statusCode
						|| HttpStatus.SC_MOVED_TEMPORARILY == statusCode) {
					debug("authentication success");
				} else if (HttpStatus.SC_NOT_FOUND == statusCode) {
					debug("page not found");
				} else {
					debug("authentication fail：" + statusCode);
					debug("message body:"
							+ postMethod.getResponseBodyAsString());
					throw new SecurityCheckException(
							"execute j_security_check complete, but failed.");
				}

			} catch (HttpException httpe) {
				httpe.printStackTrace();
				throw new SecurityCheckException("HttpException Occur");
			} catch (IOException ioe) {
				throw new SecurityCheckException("IOException Occur");
			}
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return redirectURL;
	}

	// 生成HttpClient Cookie
	private Cookie genRequestCookie(String jSessionId, String fullURL) {
		Cookie cookie = new Cookie();
		cookie.setDomain(parseCookieDomainName(fullURL));
		cookie.setPath("/");
		cookie.setName("JSESSIONID");
		cookie.setValue(jSessionId);
		return cookie;
	}

	// 通过url获取cookie的域名
	private static String parseCookieDomainName(String url) {
		if (url == null)
			return null;
		String domainName = null;
		String aUrl = url.replaceAll(" ", "");
		if (!aUrl.startsWith("http")) {
			return null;
		}
		int beginPos = 7;
		if (aUrl.startsWith("https://")) {
			beginPos = 8;
		}
		aUrl = aUrl.substring(beginPos, aUrl.length());
		while (aUrl.endsWith("/")) {
			aUrl = aUrl.substring(0, aUrl.length() - 1);
		}
		int firstSlashPos = aUrl.indexOf("/");
		String[] urlSegs = null;
		if (firstSlashPos > 0) {
			urlSegs = aUrl.split("/");
		} else {
			urlSegs = new String[1];
			urlSegs[0] = aUrl;
		}
		if (urlSegs[0].indexOf(":") > 0) {
			domainName = urlSegs[0].split(":")[0];
		} else {
			domainName = urlSegs[0];
		}
		debug("cookie的域名为:" + domainName);
		return domainName;
	}

	public void doGetRequest(String url) {
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);
			int statusCode = httpClient.executeMethod(getMethod);
			System.out.println(statusCode);
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				getMethod.releaseConnection();
			} catch (Exception x1) {

			}
		}
	}

	public String getJSessionId() {
		String jSessionId = null;
		Cookie[] cookies = httpClient.getState().getCookies();
		for (Cookie cookie : cookies) {
			if ("JSESSIONID".equals(cookie.getName())) {
				jSessionId = cookie.getValue();
				break;
			}
		}
		return jSessionId;
	}

	// 辅助方法，简单log屏幕输出
	private static void debug(String message) {
		if (debugOn) {
			System.out.println(message);
		}
	}

	/**
	 * 测试用。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 受保护的url
		String securityUrl = "http://yourhost/your_protected_page";
		// j_security_check的url
		String jSecChkUrl = "http://yourhost/j_security_check";
		String userName = "username";// 受访问系统的用户名
		String password = "password";// 受访问系统的用户密码
		JSecurityCheckHelper helper = new JSecurityCheckHelper();
		try {
			// 先访问受保护的url
			helper.doGetRequest(securityUrl);
			// 获取当前JSESSIONID
			String jSessionId = helper.getJSessionId();
			if (jSessionId == null) {
				throw new SecurityCheckException("无法获取需要进行签权的会话ID");
			}
			// 执行签权
			String redirectUrl = helper.doCheck(userName, password, jSessionId, "", 
					jSecChkUrl);
			// 返回的redirectUrl应该=securityUrl
			debug("重定向url=" + redirectUrl);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	static class UTF8PostMethod extends PostMethod{
		
		public UTF8PostMethod(String url){
            super(url);
        }
		
        @Override
        public String getRequestCharSet() {
            //return super.getRequestCharSet();
            return "UTF-8";
        }
	}

}
