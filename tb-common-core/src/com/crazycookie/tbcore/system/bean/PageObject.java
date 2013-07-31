package com.crazycookie.tbcore.system.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;

@Model
public class PageObject implements Serializable, Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -55507559515796L;
	
	private int currentPageNumber;
	private int maxPageNumber;
	private long totalRecordsNumber;
	private int offset;
	private int maxRecordsPerPage;
	private List<Integer> listPageNo;

	public PageObject clone(){
		PageObject p = null;
		try{
			p = (PageObject)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return p;
	}
	
	public void updateListPageNo(){
		if (listPageNo == null){
			listPageNo = new ArrayList<Integer>();
		}
		listPageNo.clear();
		
		/*
		 * add list to size = 5;
		 */
		listPageNo.add(currentPageNumber);
		listPageNo.add(0, currentPageNumber - 1);
		listPageNo.add(0, currentPageNumber - 2);
		listPageNo.add(currentPageNumber + 1);
		listPageNo.add(currentPageNumber + 2);
		
		/*
		 * remove minus page
		 */
		int minusCnt = 0;
		for (int i = 0; i < listPageNo.size(); i ++){
			if (listPageNo.get(i) < 1){
				minusCnt ++;
			}
		}
		for (int i = 0; i < minusCnt; i ++){
			listPageNo.remove(0);
			listPageNo.add(listPageNo.get(listPageNo.size() - 1) + 1);
		}
		
		/*
		 * remove pages that more than maxinum
		 */
		for (int i = listPageNo.size() - 1; i >= 0; i --){
			if (listPageNo.get(i) > maxPageNumber){
				listPageNo.remove(i);
			}
		}
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public int getMaxPageNumber() {
		return maxPageNumber;
	}

	public void setMaxPageNumber(int maxPageNumber) {
		this.maxPageNumber = maxPageNumber;
	}

	public long getTotalRecordsNumber() {
		return totalRecordsNumber;
	}

	public void setTotalRecordsNumber(long totalRecordsNumber) {
		this.totalRecordsNumber = totalRecordsNumber;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getMaxRecordsPerPage() {
		return maxRecordsPerPage;
	}

	public void setMaxRecordsPerPage(int maxRecordsPerPage) {
		this.maxRecordsPerPage = maxRecordsPerPage;
	}

	public List<Integer> getListPageNo() {
		return listPageNo;
	}

	public void setListPageNo(List<Integer> listPageNo) {
		this.listPageNo = listPageNo;
	}

}
