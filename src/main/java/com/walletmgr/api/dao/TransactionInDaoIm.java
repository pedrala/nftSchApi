package com.walletmgr.api.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class TransactionInDaoIm extends AbstractDao implements TransactionInDao {
	private String ns = "tblin.";
	
	@Override
	public Integer insertrequest(HashMap<String, Object> params) throws Exception {
		return (Integer) insert(ns + "insertrequest", params);
	}
	
	@Override
	public Integer updaterequest(HashMap<String, Object> params) throws Exception {
		return (Integer) update(ns + "updaterequest", params);
	}

	@Override
	public List selectrequest(HashMap<String, Object> params) throws Exception {
		return selectList(ns+"selectrequest", params);
	}

	@Override
	public HashMap<String, Object>  countrequest(HashMap<String, Object> params) throws Exception {
		return (HashMap<String, Object> ) selectOne(ns + "countrequest", params);
	}
	
	@Override
	public HashMap<String, Object> selectone(HashMap<String, Object> params) throws Exception {
		return (HashMap<String, Object>) selectOne(ns + "selectone", params);
	}
	
	@Override
	public Integer realdeleterequest(HashMap<String, Object> params) throws Exception {
		return (Integer)update(ns+"realdeleterequest", params);
	}
	
	@Override
	public HashMap<String, Object> selectwallet(HashMap<String, Object> params) throws Exception {
		return (HashMap<String, Object>) selectOne(ns + "selectwallet", params);
	}
}
