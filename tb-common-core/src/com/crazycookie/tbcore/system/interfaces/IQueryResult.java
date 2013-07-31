package com.crazycookie.tbcore.system.interfaces;

import java.util.List;

import com.crazycookie.tbcore.system.bean.PageObject;

public interface IQueryResult<K> {
	public List<K> getResultData();
	public PageObject getPageObject();
}
