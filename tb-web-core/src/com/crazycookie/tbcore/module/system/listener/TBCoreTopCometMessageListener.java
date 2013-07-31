package com.crazycookie.tbcore.module.system.listener;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.api.CrazyCookieTbApi;
import com.crazycookie.tbcore.api.CrazyCookieTbApi.IRequestMaker;
import com.crazycookie.tbcore.interfaces.system.ITBCoreSystemSingleton;
import com.crazycookie.tbcore.module.system.listener.pushhelper.MessageDecode;
import com.crazycookie.tbcore.module.system.listener.pushhelper.NotifyTopats;
import com.crazycookie.tbcore.module.system.qualifier.TBCoreSystemSingleRemoteBean;
import com.crazycookie.tbcore.module.system.qualifier.UserInfoQualifier;
import com.crazycookie.tbcore.system.bean.UserInfo;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.NotifyTrade;
import com.taobao.api.internal.stream.message.TopCometMessageListener;
import com.taobao.api.request.TopatsResultGetRequest;
import com.taobao.api.response.TopatsResultGetResponse;

@RequestScoped
public class TBCoreTopCometMessageListener implements TopCometMessageListener {

	@Inject
	@TBCoreLogged
	Logger log;
	@Inject
	@TBCoreSystemSingleRemoteBean
	ITBCoreSystemSingleton systemSingleton;
	@Inject
	@UserInfoQualifier
	UserInfo userInfo;
	@Inject
	CrazyCookieTbApi api;

	@Override
	public void onClientKickOff() {
		log.error("onClientKickOff()");
	}

	@Override
	public void onConnectMsg(String msg) {
		log.info("onConnectMsg: " + msg);
	}

	@Override
	public void onDiscardMsg(String msg) {
		log.info("onDiscardMsg: " + msg);
	}

	@Override
	public void onException(Exception e) {
		log.error("onException: " + e.getMessage());
	}

	@Override
	public void onHeartBeat() {
		log.info("onHeartBeat()");
	}

	@Override
	public void onOtherMsg(String msg) {
		log.info("onOtherMsg: " + msg);
	}

	@Override
	public void onReceiveMsg(final String message) {
		new Thread() {
			public void run() {
				
				System.out.println(message);
				
				try {
					Object obj = MessageDecode.decodeMsg(message);
					
					if (obj instanceof NotifyTopats) { // 异步任务
						final NotifyTopats nt = (NotifyTopats) obj;
						if ("done".equals(nt.getTaskStatus())) {
							TopatsResultGetResponse notifyResponse = (TopatsResultGetResponse) api
									.execute(new IRequestMaker() {

										@Override
										public TaobaoRequest<? extends TaobaoResponse> buildRequest() {
											TopatsResultGetRequest req = new TopatsResultGetRequest();
											req.setTaskId(nt.getTaskId());
											return req;
										}
									});
							String url = "";
							if (notifyResponse.isSuccess()) {
								if ("done".equals(notifyResponse.getTask().getStatus())) {
									url = notifyResponse.getTask().getDownloadUrl();
								}
							}

							if (url != null) {
								// download result
							}
						}
					} else if (obj instanceof NotifyTrade) { // 交易消息
						NotifyTrade nt = (NotifyTrade) obj;
						System.out.println(nt.getSellerNick() + ":" + nt.getTid());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onServerKickOff() {
		log.info("onServerKickOff()");
	}

	@Override
	public void onServerRehash() {
		log.info("onServerRehash()");
	}

	@Override
	public void onServerUpgrade(String msg) {
		log.info("onServerUpgrade: " + msg);
	}

}
