package com.crazycookie.tbcore.module.setting.event;

import java.io.Serializable;

public class SettingQueryEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -190148856475163L;

	public SettingQueryEvent(QueryMode mode){
		queryMode = mode;
	}
	
	private QueryMode queryMode;

	public QueryMode getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(QueryMode queryMode) {
		this.queryMode = queryMode;
	}

	public static enum QueryMode{
		LOAD_ALL_RECORD, LOAD_SINGLE_RECORD;
	}

}
