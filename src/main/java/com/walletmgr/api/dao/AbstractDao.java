package com.walletmgr.api.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * DAO의 추상클래스
 * @author ParkSungBin
 *
 */
public class AbstractDao {
    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate msqlSession;
    
    /**
     * 쿼리 아이디를 로깅한다.
     * @param queryId
     */
    protected void printQueryId(String queryId) {
    	System.out.println("\t QueryId \t: " + queryId);
    }
     
    public Object insert(String queryId, Object params){
        printQueryId(queryId);
        return msqlSession.insert(queryId, params);
    }
     
    public Object update(String queryId, Object params){
        printQueryId(queryId);
        return msqlSession.update(queryId, params);
    }
     
    public Object delete(String queryId, Object params){
        printQueryId(queryId);
        return msqlSession.delete(queryId, params);
    }
     
    public Object selectOne(String queryId){
        printQueryId(queryId);
        return msqlSession.selectOne(queryId);
    }
     
    public Object selectOne(String queryId, Object params){
        printQueryId(queryId + " - " + params.toString());
        return msqlSession.selectOne(queryId, params);
    }
     
    @SuppressWarnings("rawtypes")
    public List selectList(String queryId){
        printQueryId(queryId);
        return msqlSession.selectList(queryId);
    }
     
    @SuppressWarnings("rawtypes")
    public List selectList(String queryId, Object params){
        printQueryId(queryId);
        return msqlSession.selectList(queryId,params);
    }
}