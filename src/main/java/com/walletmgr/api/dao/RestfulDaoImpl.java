package com.walletmgr.api.dao;

import java.util.HashMap;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.walletmgr.api.dto.ApiInfoDto;
import com.walletmgr.api.dto.DepositParamDto;
import com.walletmgr.api.dto.DepositResultDto;
import com.walletmgr.api.dto.UserParamDto;

@Repository
public class RestfulDaoImpl implements RestfulDao {

    @Resource
    private SqlSessionTemplate sqlSession;

    // Mapper Name
    private String ns = "restfulMapper.";

    @Override
    public HashMap<String, Object> selectCoinSymbol(String symbol) {
        HashMap<String, Object> result = sqlSession.selectOne(ns + "selectCoinSymbol", symbol);
        return result;
    }

    @Override
    public HashMap<String, Object> selectUsrAddr(DepositParamDto params) {
        HashMap<String, Object> result = sqlSession.selectOne(ns + "selectUsrAddr", params);
        return result;
    }

    @Override
    public HashMap<String, Object> seletFromUsrAddr(DepositParamDto params) {
        HashMap<String, Object> result = sqlSession.selectOne(ns + "seletFromUsrAddr", params);
        return result;
    }

    @Override
    public HashMap<String, Object> selectTxid(int txid_no) {
        HashMap<String, Object> txid_info = sqlSession.selectOne(ns + "selectTxid", txid_no);
        return txid_info;
    }

    @Override
    public ApiInfoDto selectApiInfo(String api){
        ApiInfoDto result = sqlSession.selectOne(ns +"selectApiInfo", api);
        return result;
    }

    @Override
    public HashMap<String, Object> selectUsrInfo(UserParamDto params) {
        HashMap<String, Object> result = sqlSession.selectOne(ns +"selectUserInfo", params);
        return result;
    }

    @Override
    public void insertDepositHistory(DepositResultDto params) {
        // TODO Auto-generated method stub
        sqlSession.insert(ns + "insertDepositHistory", params);
    }
    
    @Override
    public HashMap<String, Object> selectUserNo(String email) {
        HashMap<String, Object> result = sqlSession.selectOne(ns + "selectUserNo", email);
        return result;
    }
}
