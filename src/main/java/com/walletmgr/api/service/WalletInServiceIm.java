package com.walletmgr.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walletmgr.api.Utils.FileLog;
import com.walletmgr.api.dao.TransactionInDao;

import lombok.RequiredArgsConstructor;


/**
 * Wallet System
 */
@RequiredArgsConstructor
@Service
public class WalletInServiceIm implements WalletInService {
	
	protected Log log = LogFactory.getLog(this.getClass());
	

	@Autowired
	private TransactionInDao wsDao;
	
	/**
	 * 함수 : InsertRequest  
	 * 설명 : 추가하기
	 **/		
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject InsertRequest(HashMap<String, Object> params) throws Exception {	
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		// 결과 
		JSONObject result = new JSONObject();
		
		int rstatus = 0;
		rstatus = wsDao.insertrequest(params);	

		result.put("result", rstatus);	
		
		FileLog.WriteLog(methodName + " InsertRequest : " + " : " + rstatus);
		
		//mv.setViewName("jsonView");
		return result;
	}	

	/**
	 * 함수 : SelectRequest  
	 * 설명 : 리스트 가져오기
	 **/		
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject SelectRequest(HashMap<String, Object> params) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		JSONObject result = new JSONObject();
		int totalCount = 0 ;
		int rstatus = 0;
		
		int pageNo = Integer.parseInt(params.get("PAGENO") == null ? "0" : params.get("PAGENO") + "");
		int rows = Integer.parseInt(params.get("ROWS") == null ? "0" : params.get("ROWS") + "");
		if(pageNo > 0 && rows > 0) {
			params.put("PAGENO", pageNo);
			params.put("ROWS", rows);
		}
		
		List<HashMap<String, Object>> reqList = wsDao.selectrequest(params);
		
		if(reqList != null) {
			totalCount = Integer.parseInt(wsDao.countrequest(params).get("COUNT").toString());
			result.put("reqList", reqList);	
			result.put("totalCount", totalCount);
			rstatus = 1;
		}

		result.put("rstatus", rstatus);		
		FileLog.WriteLog(methodName + " End");
		
		return result;
	}	
	
	/**
	 * 함수 		: CountRequest 
	 * 설명 		: 리스트 개수 가져오기
	 */	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject CountRequest(HashMap<String, Object> params) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		JSONObject result = new JSONObject();
		
		int totalCount = Integer.parseInt(wsDao.countrequest(params).get("COUNT").toString());
		FileLog.WriteLog(methodName + " totalCount : " + totalCount);		
		result.put("totalCount", totalCount);
		
		return result;
	}
		
	/**
	 * 함수 		: SelectOne 
	 * 설명 		: 선택 한개 가져오기
	 */	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject SelectOne(HashMap<String, Object> params) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		JSONObject result = new JSONObject();
		
		int idx = Integer.parseInt(params.get("IDX") == null ? "0" : params.get("IDX") + "");
		if(idx > 0) {
			params.put("IDX", idx);
		}
		
		result.put("contents", wsDao.selectone(params));
		
		FileLog.WriteLog(methodName + " done");
		
		return result;
	}
	
	/**
	 * 함수 		: UpdateRequest 
	 * 설명 		:
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject UpdateRequest(HashMap<String, Object> params) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		int rstatus = 0;
		JSONObject result = new JSONObject();	
		
		if(params != null) {
			int idx = params.get("IDX") == null ? 0 : Integer.parseInt(params.get("IDX") + "");
			params.put("IDX", idx);
			HashMap<String, Object> reqmap = null;
			reqmap = wsDao.selectone(params);		
			if(reqmap != null) {
				rstatus = wsDao.updaterequest(params);
			} else {
				result.put("result", 0);
				FileLog.WriteLog(methodName + " search error : " + params);
			}
		} else {
			FileLog.WriteLog(methodName + " get param error");
			rstatus = 0;
		}
		result.put("result", rstatus);
		
		FileLog.WriteLog(methodName + " rstatus " + rstatus);
		
		return result;
	}
	
}
