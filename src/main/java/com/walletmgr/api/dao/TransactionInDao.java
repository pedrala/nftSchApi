package com.walletmgr.api.dao;

import java.util.HashMap;
import java.util.List;

public interface TransactionInDao {
	
	/**
	 * 요청 정보 등록
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer insertrequest(HashMap<String, Object> params) throws Exception;

	/**
	 * 요청 정보 수정
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer updaterequest(HashMap<String, Object> params) throws Exception;

	/**
	 * 요청 정보 조회
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectrequest(HashMap<String, Object> params) throws Exception;

	/**
	 * 요청 전체 카운트
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object>  countrequest(HashMap<String, Object> params) throws Exception;
	
	/**
	 * REQ_UUID를 이용한 요청 찾기
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectone(HashMap<String, Object> params) throws Exception;
	
	/**
	 * 요청 사용불가
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer realdeleterequest(HashMap<String, Object> params) throws Exception;
	
	/**
	 * REQ_UUID를 이용한 요청 찾기
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectwallet(HashMap<String, Object> params) throws Exception;	
}
