package com.walletmgr.api.dao;

import java.util.HashMap;

import com.walletmgr.api.dto.ApiInfoDto;
import com.walletmgr.api.dto.DepositParamDto;
import com.walletmgr.api.dto.DepositResultDto;
import com.walletmgr.api.dto.UserParamDto;

public interface RestfulDao {

    public HashMap<String, Object> selectCoinSymbol(String symbol);

    public HashMap<String, Object> selectUsrAddr(DepositParamDto params);

    public HashMap<String, Object> seletFromUsrAddr(DepositParamDto params);

    public HashMap<String, Object> selectTxid(int txid_no);

    public ApiInfoDto selectApiInfo(String api);

    public HashMap<String, Object> selectUsrInfo(UserParamDto user);

    public void insertDepositHistory(DepositResultDto params);

    public HashMap<String, Object> selectUserNo(String email);
}
