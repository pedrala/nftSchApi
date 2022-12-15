package com.walletmgr.api.service;

import java.util.HashMap;

import org.json.simple.JSONObject;

/**
 * 광고내역 관련 비즈니스 로직 실행
 * 
 * @author KimHyeonjeong
 *
 */
public interface UnlockHistoryService {
	public JSONObject SelectRequest(HashMap<String, Object> params) throws Exception;
	
	public JSONObject CountRequest(HashMap<String, Object> params) throws Exception;
		
	public JSONObject SelectOne(HashMap<String, Object> params) throws Exception;
	
	public JSONObject InsertRequest(HashMap<String, Object> params) throws Exception;
	
	public JSONObject UpdateRequest(HashMap<String, Object> params) throws Exception;
	
}
