package com.crazycookie.tbcore.module.admin.action;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.crazycookie.tbcore.api.CrazyCookieTbApi;
import com.crazycookie.tbcore.api.CrazyCookieTbApi.IRequestMaker;
import com.crazycookie.tbcore.module.admin.nav.AdminNav;
import com.crazycookie.tbcore.module.system.action.TBCoreNotificationDaemon;
import com.crazycookie.tbcore.system.qualifier.TBCoreLogged;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.Item;
import com.taobao.api.request.CometDiscardinfoGetRequest;
import com.taobao.api.request.IncrementCustomerPermitRequest;
import com.taobao.api.request.IncrementItemsGetRequest;
import com.taobao.api.request.ItemUpdateRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.TopatsIncrementMessagesGetRequest;
import com.taobao.api.response.CometDiscardinfoGetResponse;
import com.taobao.api.response.IncrementCustomerPermitResponse;
import com.taobao.api.response.IncrementItemsGetResponse;
import com.taobao.api.response.ItemUpdateResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.TopatsIncrementMessagesGetResponse;

@ConversationScoped
@Named
public class AdminAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4888489796928953731L;

	@Inject Conversation conversation;
	@Inject CrazyCookieTbApi api;
	@Inject @TBCoreLogged Logger log;
	@Inject TBCoreNotificationDaemon daemon;
	private String selectedUserId;

	public Object startConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		return AdminNav.ADMIN_HOME;
	}

	public void endConversation() {
		if (!(conversation.isTransient())) {
			conversation.end();
		}
	}
	
	public void asyncGetIncresementInfo(){
		
		TopatsIncrementMessagesGetResponse response = (TopatsIncrementMessagesGetResponse)api.execute(new IRequestMaker() {
			
			@Override
			public TaobaoRequest<? extends TaobaoResponse> buildRequest() {
				TopatsIncrementMessagesGetRequest req=new TopatsIncrementMessagesGetRequest();
				req.setTopics("trade,item,refund");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MINUTE, -1);
				req.setEnd(c.getTime());
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				req.setStart(c.getTime());
				return req;
			}
		});
		
		if (response != null && response.getErrorCode() == null){
			System.out.println(response.getBody());
		}else{
			log.error("TopatsIncrementMessagesGetResponse with error code: " + response.getErrorCode());
		}
	
	}
	
	public void retrieveItemChangeNotificationMessage(){
		
		IncrementItemsGetResponse response = (IncrementItemsGetResponse)api.execute(new IRequestMaker() {
			
			@Override
			public TaobaoRequest<? extends TaobaoResponse> buildRequest() {
				IncrementItemsGetRequest req=new IncrementItemsGetRequest();
				req.setStatus("ItemUpdate");
				req.setPageNo(1L);
				req.setPageSize(40L);
				req.setNick(selectedUserId);
				return req;
			}
		});
		
		if (response != null && response.getErrorCode() == null){
			System.out.println(response.getBody());
		}else{
			log.error("CometDiscardinfoGetResponse with error code: " + response.getErrorCode());
		}
	}
	
	public void retrieveDiscardInfo(){
		CometDiscardinfoGetResponse notifyResponse = (CometDiscardinfoGetResponse)api.execute(new IRequestMaker() {
			
			@Override
			public TaobaoRequest<? extends TaobaoResponse> buildRequest() {
				CometDiscardinfoGetRequest req=new CometDiscardinfoGetRequest();
				req.setTypes("item");
				Calendar c = Calendar.getInstance();
				req.setEnd(c.getTime());
				c.add(Calendar.HOUR_OF_DAY, -1);
				req.setStart(c.getTime());
				return req;
			}
		});
		
		if (notifyResponse != null && notifyResponse.getErrorCode() == null){
			System.out.println(notifyResponse.getBody());
		}else{
			log.error("CometDiscardinfoGetResponse with error code: " + notifyResponse.getErrorCode());
		}
	}

	public void updateFirstItemForSpecifiedUser() {
		if (selectedUserId != null && !"".equals(selectedUserId.trim())) { 
			
			IncrementCustomerPermitResponse notifyResponse = (IncrementCustomerPermitResponse)api.execute(new IRequestMaker() {
				
				@Override
				public TaobaoRequest<? extends TaobaoResponse> buildRequest() {
					IncrementCustomerPermitRequest req=new IncrementCustomerPermitRequest();
					req.setType("get,syn,notify");
					req.setTopics("trade;refund;item");
					req.setStatus("all");
					return req;
				}
			});
			if (notifyResponse.getErrorCode() == null || "".equals(notifyResponse.getErrorCode())){
				log.info("IncrementCustomerPermitResponse return OK.");
			}else{
				log.error("IncrementCustomerPermitResponse fail with code: " + notifyResponse.getErrorCode());
			}
			
			ItemsOnsaleGetResponse response = (ItemsOnsaleGetResponse) api
					.execute(new IRequestMaker() {
						@Override
						public TaobaoRequest<? extends TaobaoResponse> buildRequest() {
							ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
							req.setFields("num_iid");
							req.setPageNo(1L);
							req.setPageSize(1L);
							return req;
						}
					});
			List<Item> listItem =  response.getItems();
			if (listItem != null && listItem.size() != 0){
				final Item item = listItem.get(0);
				ItemUpdateResponse updateResponse = (ItemUpdateResponse)api.execute(new IRequestMaker() {
					
					@Override
					public TaobaoRequest<? extends TaobaoResponse> buildRequest() {
						ItemUpdateRequest req = new ItemUpdateRequest();
						req.setNumIid(item.getNumIid());
						String hiddenString = "<div id=\"test-data\" style=\"float:left;text-indent:-9999px;\">test data</div>";
						req.setDesc((item.getDesc() + hiddenString).replaceAll(hiddenString, hiddenString));
						return req;
					}
				});
				
				if (updateResponse.getErrorCode() == null || "".equals(updateResponse.getErrorCode())){
					log.info("Item update success.");
				}else{
					log.error("Item udpate fail with code: " + updateResponse.getErrorCode());
				}
			}else {
				log.error("ItemsOnsaleGetResponse has no item.");
				return;
			}
		}
	}

	public String getSelectedUserId() {
		return selectedUserId;
	}

	public void setSelectedUserId(String selectedUserId) {
		this.selectedUserId = selectedUserId;
	}

}
