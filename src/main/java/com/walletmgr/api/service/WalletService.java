package com.walletmgr.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.walletmgr.api.dto.BuyHistoryDTO;
import com.walletmgr.api.dto.WalletInfoDTO;

public interface WalletService {
	public WalletInfoDTO GetWalletInfo(String symbol, String address) throws Exception;
	public ArrayList<WalletInfoDTO> GetWalletList(String symbol) throws Exception;
	public WalletInfoDTO InsertNodeWallet(String symbol, HashMap<String, Object> pnode) throws Exception;
	public WalletInfoDTO UpdateNodeWallet(String symbol, String addr, String amount) throws Exception;
	public WalletInfoDTO TransferNodeWallet(String symbol, String from, String to, String amount) throws Exception;
	public WalletInfoDTO TestWalletInfo(String symbol, String from) throws Exception;
	public WalletInfoDTO UserInsertRequest(HashMap<String, Object> req) throws Exception;
	public BuyHistoryDTO BuyInsertRequest(HashMap<String, Object> req) throws Exception;
	public WalletInfoDTO TransferUserWallet(String symbol, String from, String to, String amount) throws Exception;
	public WalletInfoDTO DistributeUserWallet(String symbol, String from, String to, String amount, String nftid) throws Exception;
	public int UnlockUserWallet(String symbol) throws Exception;
	public ArrayList<BuyHistoryDTO> GetBuyHistoryList(HashMap<String, Object> req) throws Exception;
	public BigDecimal ProcessRewardDistribute(String from, String amount) throws Exception;
}
