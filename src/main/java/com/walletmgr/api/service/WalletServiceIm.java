package com.walletmgr.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walletmgr.api.WalletMgrApiApplication;
import com.walletmgr.api.Utils.CommonUtil;
import com.walletmgr.api.Utils.FileLog;
import com.walletmgr.api.Utils.ProcessTrxUserWallet;
import com.walletmgr.api.dao.BuyHistoryDao;
import com.walletmgr.api.dao.NodeHistoryDao;
import com.walletmgr.api.dao.RestfulDao;
import com.walletmgr.api.dao.TransactionInDao;
import com.walletmgr.api.dao.TransactionOutDao;
import com.walletmgr.api.dao.WalletDao;
import com.walletmgr.api.dto.BuyHistoryDTO;
import com.walletmgr.api.dto.NodeHistoryDTO;
import com.walletmgr.api.dto.RewardUsrDTO;
import com.walletmgr.api.dto.WalletInfoDTO;

@Service
public class WalletServiceIm implements WalletService {
    @Resource
    RestfulDao restfulDao;
		
	@Autowired
	private TransactionOutDao outDao;
	
	@Autowired
	private TransactionInDao inDao;	
	
	@Autowired
	private WalletDao walletDao;	
	
	@Autowired
	private NodeHistoryDao nodeDao;			
	
	@Autowired
	private CommonUtil commonDao;		
	
	@Autowired
	private BuyHistoryDao bhDao;
	
	public boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}	
	
	@Override
	public WalletInfoDTO GetWalletInfo(String symbol, String addr) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = new WalletInfoDTO();
		//pdto.setNo(1);
		//pdto.setTxid(txid);		
		
		//
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", symbol);
		params.put("ADDR", addr);
		
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(addr == null) return null;
		if(addr.length() < 2) return null;
		
		HashMap<String, Object> reqobj = walletDao.selectone(params);
		
		if(reqobj != null) {
			System.out.println(reqobj.toString());
			String usr_no = reqobj.get("usr_no").toString();
			pdto.setUsr_no(Integer.parseInt(usr_no));
			pdto.setV_amount(reqobj.get("v_amount").toString());
			pdto.setT_amount(reqobj.get("t_amount").toString());
			pdto.setI_amount(reqobj.get("i_amount").toString());
			pdto.setCreatetime(reqobj.get("c_tm").toString());
			pdto.setAddr(reqobj.get("addr").toString());
			
			FileLog.WriteLog(methodName + " " + usr_no + " - " + pdto.toString());
		} else {
			FileLog.WriteLog(methodName + " No search addr in " + symbol + "_wallet [ " + addr + " ]");
			WalletMgrApiApplication.g_respmsg = "No search addr in " + symbol + "_wallet [ " + addr + " ]";
			return null;	
		}			
		
		return pdto;
	}	
	
	@Override
	public ArrayList<WalletInfoDTO> GetWalletList(String symbol) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		ArrayList<WalletInfoDTO> plist = new ArrayList<WalletInfoDTO>();
		//pdto.setNo(1);
		//pdto.setTxid(txid);		
		
		//
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", symbol);
		
		if(symbol == null) {
			return null;
		}
		if(symbol.length() < 2) {
			return null;
		}
		
		List<HashMap<String, Object>> reqList = walletDao.selectrequest(params);
		
		if(reqList != null) {
			for(int idx=0;idx<reqList.size();idx++) {
				WalletInfoDTO pdto = new WalletInfoDTO();
				HashMap<String, Object> reqobj = reqList.get(idx);
				System.out.println(reqobj.toString());
				String usr_no = reqobj.get("usr_no").toString();
				pdto.setUsr_no(Integer.parseInt(usr_no));
				pdto.setV_amount(reqobj.get("v_amount").toString());
				pdto.setT_amount(reqobj.get("t_amount").toString());
				pdto.setCreatetime(reqobj.get("c_tm").toString());
				pdto.setAddr(reqobj.get("addr").toString());
				
				plist.add(pdto);
			}	
			FileLog.WriteLog(methodName + " size : " + plist.size());
		} else {
			FileLog.WriteLog(methodName + " No search list in " + symbol);
			WalletMgrApiApplication.g_respmsg = "No search list in " + symbol;
			return null;	
		}			
		
		return plist;
	}	
		
	@Override
	public WalletInfoDTO InsertNodeWallet(String symbol, HashMap<String, Object> param) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = new WalletInfoDTO();
		//pdto.setNo(1);
		//pdto.setTxid(txid);	
		NodeHistoryDTO pnode = new NodeHistoryDTO();
		if(param.get("serial") != null) pnode.setSerial(param.get("serial").toString());
		//if(param.get("ipa") != null) pnode.setIpa(param.get("ipa").toString());
		if(param.get("price") != null) pnode.setPrice(param.get("price").toString());
		if(param.get("maxp") != null) pnode.setMaxp(param.get("maxp").toString());
		if(param.get("reward") != null) pnode.setReward(param.get("reward").toString());
		if(param.get("startday") != null) pnode.setStartday(Integer.parseInt(param.get("startday").toString()));
		if(param.get("lastday") != null) pnode.setLastday(Integer.parseInt(param.get("lastday").toString()));
		if(param.get("op_time") != null) pnode.setOp_time(param.get("op_time").toString());
		if(param.get("addr") != null) pnode.setAddr(param.get("addr").toString());
		
		FileLog.WriteLog(methodName + " param " + param.toString());
		
		String addr = pnode.getAddr();
		
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(addr == null) return null;
		if(addr.length() < 2) return null;
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", symbol);
		params.put("SERIAL", pnode.getSerial());
						
		if(pnode.getSerial() != null) params.put("SERIAL", pnode.getSerial());
		else return null;
		
		params.put("ADDR", pnode.getAddr());
		HashMap<String, Object> reqobj = nodeDao.selectone(params);
		if(reqobj != null) {
			if(reqobj.get("serial").equals(pnode.getSerial()) == true) {
				WalletMgrApiApplication.g_respmsg = "aleady exist serial " + pnode.getSerial();
				return null;
			}
			FileLog.WriteLog(methodName + " nodehistory not null no serial ");
			WalletMgrApiApplication.g_respmsg = "nodehistory not null no serial - " + pnode.getSerial();
		} else {
			FileLog.WriteLog(methodName + " nodehistory null");
			WalletMgrApiApplication.g_respmsg = "nodehistory null";
		}
			
		HashMap<String, Object> reqwobj = walletDao.selectone(params);
		if(reqwobj != null) {
			if(reqwobj.get("addr").equals(addr) == true) {
				FileLog.WriteLog(methodName + " aleady exist addr " + addr);
				WalletMgrApiApplication.g_respmsg = "aleady exist addr " + addr;
				return null;
			}
		}
					
		if(pnode.getIpa() != null) params.put("IPA", pnode.getIpa());
		if(pnode.getPrice() != null) params.put("PRICE", pnode.getPrice());
		if(pnode.getMaxp() != null) params.put("MAXP", pnode.getMaxp());
		if(pnode.getReward() != null) params.put("REWARD", pnode.getReward());
		if(pnode.getStartday() > 0) params.put("STARTDAY", pnode.getStartday());
		if(pnode.getLastday() > 0) params.put("LASTDAY", pnode.getLastday());
		if(pnode.getOp_time() != null) params.put("OP_TIME", pnode.getOp_time());

		int rno = nodeDao.insertrequest(params);
		if(rno < 1) {
			FileLog.WriteLog(methodName + " node add error " + addr + " - " + params.toString());
			WalletMgrApiApplication.g_respmsg = "node add error " + addr + " - " + params.toString();
			return null;
		} else {
			FileLog.WriteLog(methodName + " node add ok " + addr + " - " + rno + ", return : " + params.toString());
		}
		
		if(params.get("IDX") != null) 
			rno = Integer.parseInt(params.get("IDX").toString());
		else {
			FileLog.WriteLog(methodName + " add and return idx no exist");
			WalletMgrApiApplication.g_respmsg = "add and return idx no exist";
			return null;
		}
		
		params.put("USR_NO", rno);
		/*
		HashMap<String, Object> reqbobj = nodeDao.selectone(params);
		if(reqbobj == null) {
			FileLog.WriteLog(methodName + " select one nodeDao reqbobj null ");
			return null;
		}		
		FileLog.WriteLog(methodName + " nodeDao select result " + reqbobj.toString());
		
		//리턴받은 index를 USR_NO로 넣어준다.
		if(reqbobj.get("idx") == null) {
			FileLog.WriteLog(methodName + " select one nodeDao is not null but idx null");
			return null;		
		}
		
		params.put("USR_NO", Integer.parseInt(reqbobj.get("idx").toString()));
		*/
		//params.put("ADDR", pnode.getAddr());
		rno = 0;
		FileLog.WriteLog(methodName + " wallet add params " + params.toString());
		rno = walletDao.insertrequest(params);
		if(rno < 1) {
			FileLog.WriteLog(methodName + " wallet add error " + addr + " - " + params.toString());
			WalletMgrApiApplication.g_respmsg = "wallet add error " + addr + " - " + params.toString();
			return null;
		} else {
			FileLog.WriteLog(methodName + " wallet add ok " + addr + " - " + rno);
		}
		
		HashMap<String, Object> reqobj1 = walletDao.selectone(params);		
		if(reqobj1 != null) {
			System.out.println(reqobj1.toString());
			String usr_no = reqobj1.get("usr_no").toString();
			pdto.setV_amount(reqobj1.get("v_amount").toString());
			pdto.setT_amount(reqobj1.get("t_amount").toString());
			pdto.setCreatetime(reqobj1.get("c_tm").toString());
			pdto.setAddr(reqobj1.get("addr").toString());
			
			FileLog.WriteLog(methodName + " " + usr_no + " - " + pdto.toString());
		} else {
			FileLog.WriteLog(methodName + " No search addr in " + symbol + "_wallet [ " + addr + " ]");
			WalletMgrApiApplication.g_respmsg = "No search addr in " + symbol + "_wallet [ " + addr + " ]";
			return null;
		}			
		
		return pdto;
	}	
	
	@Override
	public WalletInfoDTO UpdateNodeWallet(String symbol, String addr, String amount) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = new WalletInfoDTO();
		
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(addr == null) return null;
		if(addr.length() < 2) return null;
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", symbol);
		params.put("ADDR", addr);
		//
		HashMap<String, Object> reqobj = walletDao.selectone(params);		
		if(reqobj != null) {
			System.out.println(reqobj.toString());
			String usr_no = reqobj.get("usr_no").toString();
			pdto.setV_amount(reqobj.get("v_amount").toString());
			pdto.setT_amount(reqobj.get("t_amount").toString());
			pdto.setCreatetime(reqobj.get("c_tm").toString());
			pdto.setAddr(reqobj.get("addr").toString());
			
			FileLog.WriteLog(methodName + " " + usr_no + " - " + pdto.toString());
		} else {
			FileLog.WriteLog(methodName + " No search addr in " + symbol + "_wallet [ " + addr + " ]");
			WalletMgrApiApplication.g_respmsg = "No search addr in " + symbol + "_wallet [ " + addr + " ]";
			return null;
		}
		
		BigDecimal bigvamount = new BigDecimal(pdto.getV_amount());
		BigDecimal addvamount = new BigDecimal(amount);
		BigDecimal non_div = new BigDecimal("1");	
		
		bigvamount = bigvamount.add(addvamount);
		bigvamount = bigvamount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);
		
		params.put("V_AMOUNT", bigvamount.toPlainString());
		
		int rno = 0;
		rno = walletDao.updaterequest(params);
		if(rno < 1) {
			FileLog.WriteLog(methodName + " wallet update error " + addr + " - " + params.toString());
			WalletMgrApiApplication.g_respmsg = "wallet update error " + addr + " - " + params.toString();
			return null;
		} else {
			FileLog.WriteLog(methodName + " wallet update ok " + addr + " - " + rno);
		}
		
		HashMap<String, Object> reqobj1 = walletDao.selectone(params);		
		if(reqobj1 != null) {
			System.out.println(reqobj1.toString());
			String usr_no = reqobj1.get("usr_no").toString();
			pdto.setV_amount(reqobj1.get("v_amount").toString());
			pdto.setT_amount(reqobj1.get("t_amount").toString());
			pdto.setCreatetime(reqobj1.get("c_tm").toString());
			pdto.setAddr(reqobj1.get("addr").toString());
			
			FileLog.WriteLog(methodName + " " + usr_no + " - " + pdto.toString());
		} else {
			FileLog.WriteLog(methodName + " No search addr in " + symbol + "_wallet [ " + addr + " ]");
			WalletMgrApiApplication.g_respmsg = "No search addr in " + symbol + "_wallet [ " + addr + " ]";
			return null;
		}			
		
		return pdto;
	}
	
	public String InsertTblOutInfo(String symbol, String from, String to, String amount) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(from == null) return null;
		if(from.length() < 2) return null;		
		if(to == null) return null;
		if(to.length() < 2) return null;			
		WalletInfoDTO pdto = GetWalletInfo(symbol, to);
	
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", symbol);
		params.put("USR_NO", pdto.getUsr_no());
		params.put("FROM", from);
		params.put("TO", to);
		params.put("AMOUNT", amount);
		params.put("REQ_CODE", commonDao.makeReqCodeNum());
		params.put("APPROVAL", "1");
		params.put("DONE", "1");
		params.put("CONFIRM_NUM", 7);
		String txid = commonDao.makeTxid(2, symbol);
		params.put("TXID", txid);
		
		
		int rno = outDao.insertrequest(params);
		
		if(rno > 0) {
			FileLog.WriteLog(methodName + " insert ok " + rno);
		} else {
			FileLog.WriteLog(methodName + " insert failed " + symbol + " [ " + params.toString() + " ]");
			WalletMgrApiApplication.g_respmsg = "insert failed " + symbol + " [ " + params.toString() + " ]";
			return null;
		}			
		
		return txid;
	}	
	
	public String InsertTblInInfo(String symbol, String from, String to, String amount, String txid) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(from == null) return null;
		if(from.length() < 2) return null;		
		if(to == null) return null;
		if(to.length() < 2) return null;			
		WalletInfoDTO pdto = GetWalletInfo(symbol, to);
	
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", symbol);
		params.put("USR_NO", pdto.getUsr_no());
		params.put("FROM", from);
		params.put("TO", to);
		params.put("AMOUNT", amount);
		params.put("CONFIRM_NUM", 7);
		params.put("TXID", txid);
		
		
		int rno = inDao.insertrequest(params);
		
		if(rno > 0) {
			FileLog.WriteLog(methodName + " insert ok " + rno);
		} else {
			FileLog.WriteLog(methodName + " insert failed " + symbol + " [ " + params.toString() + " ]");
			WalletMgrApiApplication.g_respmsg = "insert failed " + symbol + " [ " + params.toString() + " ]";
			return null;
		}			

		return txid;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public synchronized WalletInfoDTO TransferNodeWallet(String symbol, String from, String to, String amount) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO fromdto = new WalletInfoDTO();
		WalletInfoDTO todto = new WalletInfoDTO();
		WalletInfoDTO pdto = new WalletInfoDTO();		
		//
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(from == null) return null;
		if(from.length() < 2) return null;	
		if(to == null) return null;
		if(to.length() < 2) return null;		
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", symbol);
		params.put("ADDR", from);
		//
		
		HashMap<String, Object> reqfromobj = walletDao.selectone(params);		
		if(reqfromobj != null) {
			System.out.println(reqfromobj.toString());
			String usr_no = reqfromobj.get("usr_no").toString();
			fromdto.setV_amount(reqfromobj.get("v_amount").toString());
			fromdto.setT_amount(reqfromobj.get("t_amount").toString());
			fromdto.setCreatetime(reqfromobj.get("c_tm").toString());
			fromdto.setAddr(reqfromobj.get("addr").toString());
			
			FileLog.WriteLog(methodName + " " + usr_no + " - " + fromdto.toString());
		} else {
			FileLog.WriteLog(methodName + " No search addr in " + symbol + "_wallet [ " + from + " ]");
			WalletMgrApiApplication.g_respmsg = "No search addr in " + symbol + "_wallet [ " + from + " ]";
			return null;	
		}
		
		params.clear();
		params.put("SYMBOL", symbol);
		params.put("ADDR", to);
		//
		
		HashMap<String, Object> reqtoobj = walletDao.selectone(params);		
		if(reqtoobj != null) {
			System.out.println(reqtoobj.toString());
			String usr_no = reqtoobj.get("usr_no").toString();
			todto.setV_amount(reqtoobj.get("v_amount").toString());
			todto.setT_amount(reqtoobj.get("t_amount").toString());
			todto.setCreatetime(reqtoobj.get("c_tm").toString());
			todto.setAddr(reqtoobj.get("addr").toString());
			
			FileLog.WriteLog(methodName + " " + usr_no + " - " + fromdto.toString());
		} else {
			FileLog.WriteLog(methodName + " No search addr in " + symbol + "_wallet [ " + to + " ]");
			WalletMgrApiApplication.g_respmsg = "No search addr in " + symbol + "_wallet [ " + to + " ]";
			return null;
		}
			
		BigDecimal fromvamount = new BigDecimal(fromdto.getV_amount());
		BigDecimal tovamount = new BigDecimal(todto.getV_amount());
		BigDecimal addvamount = new BigDecimal(amount);
		BigDecimal non_div = new BigDecimal("1");	
		
		if (addvamount.compareTo(new BigDecimal("0.00")) == 0) {
			FileLog.WriteLog(methodName + " [zero amount] zero [ " + amount + " ]");
			WalletMgrApiApplication.g_respmsg = "[zero amount] zero [ " + amount + " ]";
			return null;		
		}
		
		if(fromvamount.compareTo(addvamount) < 0) {
			FileLog.WriteLog(methodName + " [serious error] small " + from + " amount " + " from [ " + fromvamount.toPlainString() + " ]" + " to [ " + tovamount.toPlainString() + " ]" );
			WalletMgrApiApplication.g_respmsg = "[serious error] small " + from + " amount " + " from [ " + fromvamount.toPlainString() + " ]" + " to [ " + tovamount.toPlainString() + " ]";
			return null;
		}
		
		fromvamount = fromvamount.subtract(addvamount);
		fromvamount = fromvamount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);
		tovamount = tovamount.add(addvamount);
		tovamount = tovamount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);
	
		//from쪽을 먼저 감소를 시킨다.
		params.clear();
		params.put("SYMBOL", symbol);
		params.put("ADDR", from);		
		params.put("V_AMOUNT", fromvamount.toPlainString());
		
		int rno = 0;
		rno = walletDao.updaterequest(params);
		if(rno < 1) {
			FileLog.WriteLog(methodName + " from wallet update error " + from + " - " + params.toString());
			WalletMgrApiApplication.g_respmsg = "from wallet update error " + from + " - " + params.toString();
			throw new Exception();
		} else {
			FileLog.WriteLog(methodName + " from wallet update ok " + from + " - " + rno);
		}
		
		//다음으로 to쪽을 먼저 증가를 시킨다.
		params.clear();
		params.put("SYMBOL", symbol);
		params.put("ADDR", to);		
		params.put("V_AMOUNT", tovamount.toPlainString());
		
		rno = 0;
		rno = walletDao.updaterequest(params);
		if(rno < 1) {
			FileLog.WriteLog(methodName + " to wallet update error " + to + " - " + params.toString());
			WalletMgrApiApplication.g_respmsg = "to wallet update error " + to + " - " + params.toString();
			throw new Exception();
		} else {
			FileLog.WriteLog(methodName + " to wallet update ok " + to + " - " + rno);
		}
		
		params.clear();
		params.put("SYMBOL", symbol);
		
		HashMap<String, Object> reqobj = walletDao.selectone(params);		
		if(reqobj != null) {
			System.out.println(reqobj.toString());
			String usr_no = reqobj.get("usr_no").toString();
			pdto.setV_amount(reqobj.get("v_amount").toString());
			pdto.setT_amount(reqobj.get("t_amount").toString());
			pdto.setCreatetime(reqobj.get("c_tm").toString());
			pdto.setAddr(reqobj.get("addr").toString());
			
			FileLog.WriteLog(methodName + " " + usr_no + " - " + pdto.toString());
		} else {
			FileLog.WriteLog(methodName + " No search addr in " + symbol + "_wallet [ " + to + " ]");
			WalletMgrApiApplication.g_respmsg = "No search addr in " + symbol + "_wallet [ " + to + " ]";
			throw new Exception();
		}			
		
		String outtxid = null;
		try {
			outtxid = InsertTblOutInfo(symbol, from, to, amount);
			pdto.setOuttxid(outtxid);
		} catch(Exception e) {
			FileLog.WriteLog(methodName + e.toString());
			WalletMgrApiApplication.g_respmsg = "Insert tbl_out fail";
			throw new Exception();
		}
		
		StringBuilder intxid = new StringBuilder(outtxid);
		intxid.setCharAt(5, '0');		
		try {
			String txid = InsertTblInInfo(symbol, from, to, amount, intxid.toString());
			pdto.setIntxid(txid);
		} catch(Exception e) {
			FileLog.WriteLog(methodName + e.toString());
			WalletMgrApiApplication.g_respmsg = "Insert tbl_in fail";
			throw new Exception();
		}
		
		try {
			//node 보상을 사용자들에게 분배한다. 노드의 주소이 to를 입력으로 주어야 한다.
			BigDecimal rewardtotal = ProcessRewardDistribute(to, amount);
			if(rewardtotal != null) {
				FileLog.WriteLog(methodName + " : rewardtotal " + rewardtotal.toPlainString());
			} else {
				FileLog.WriteLog(methodName + " : rewardtotal null");
				WalletMgrApiApplication.g_respmsg = "rewardtotal null";
			}
			
			if (rewardtotal.compareTo(new BigDecimal("0.00")) != 0) {
				//사용자 지갑으로 보상을 해도 노드 지갑은 차감하지 않는다. 노드 지갑의 v_amount는 사용자 지갑의 분배 이력과의 매핑으로 활용
				//node_wallet은 finalchain과의 주고 받은 기록을 유기하기 위하여 사용함. 보상된 금액은 i_amount에 기록한다.
				//subtract를 통하여 계속 음수로 기록을 유지한다.
				BigDecimal bigiamount = new BigDecimal(todto.getI_amount());
				
				bigiamount = bigiamount.subtract(rewardtotal);
				bigiamount = bigiamount.divide(non_div, 9, BigDecimal.ROUND_HALF_DOWN);
				
				params.clear();
				params.put("SYMBOL", symbol);
				params.put("ADDR", to);		
				params.put("I_AMOUNT", bigiamount.toPlainString());
				
				rno = 0;
				rno = walletDao.updaterequest(params);
				if(rno < 1) {
					FileLog.WriteLog(methodName + " to wallet update error " + to + " - " + params.toString());
					WalletMgrApiApplication.g_respmsg = "to wallet update error " + to + " - " + params.toString();
					//throw new Exception();
				} else {
					FileLog.WriteLog(methodName + " to wallet update ok " + to + " - " + rno);
				}
			}
		} catch(Exception e) {
			FileLog.WriteLog(methodName + " ProcessRewardDistribute : " + e.toString());
			WalletMgrApiApplication.g_respmsg = "reward distribute execption";
			//throw new Exception();
		}
		
		return pdto;
	}		
	
	@Override
	public WalletInfoDTO UserInsertRequest(HashMap<String, Object> req) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		ProcessTrxUserWallet ptrxwallet = new ProcessTrxUserWallet();
		//여기서 usr_no를 찾는 루틴이 포함되어야 한다.
		String uid = null;
		String parent = null;
		String addr = null;
		if(req.get("uid") != null) uid = req.get("uid").toString();
		if(req.get("parent") != null) parent = req.get("parent").toString();
		if(req.get("addr").toString() != null) addr = req.get("addr").toString();
		if(uid == null || addr == null) {
			ptrxwallet.CloseProcessSession();
			FileLog.WriteLog(methodName + " error : " + "uid || addr is null " + uid + ", " + addr);
			WalletMgrApiApplication.g_respmsg = "error : " + "uid || addr is null " + uid + ", " + addr;
			return null;
		}	
		
		//먼저 uid를 검색해서 존재하지 않아야 한다.
		RewardUsrDTO usrdto = ptrxwallet.GetUsrInfo(uid);
		if(usrdto != null) {
			if(usrdto.getIdx() != 0) {
				ptrxwallet.CloseProcessSession();
				FileLog.WriteLog(methodName + " error : " + "aleady exist uid " + uid);
				WalletMgrApiApplication.g_respmsg = "error : " + "aleady exist uid " + uid;
				return null;
			}
		}

		int usr_no = 0;
		WalletInfoDTO pdto = ptrxwallet.GetUsrWalletInfo(addr);
		if(pdto != null) {
			if(pdto.getNo() != 0) {
				ptrxwallet.CloseProcessSession();
				FileLog.WriteLog(methodName + " error : " + "aleady exist addr " + addr);
				WalletMgrApiApplication.g_respmsg = "error : " + "aleady exist addr " + addr;
				return null;
			}
		}
		
		//if(parent == null) parent = "root";
		
		RewardUsrDTO udto = ptrxwallet.InsertUserInfo(uid, parent);
		//등록한 후에 가져온 RewardUsrDTO 정보로 부터 사용자 번호인 idx를 가져온다.
		usr_no = udto.getIdx();
				
		FileLog.WriteLog(methodName + " reward usr add ok : " + udto.toString());
		WalletInfoDTO tdto = ptrxwallet.InsertUserWallet(usr_no, addr);
		FileLog.WriteLog(methodName + " usr wallet add ok : " + tdto.toString());

		
		pdto = ptrxwallet.GetUsrWalletInfo(addr);
		if(pdto == null) {
			if(pdto.getNo() == 0) {
				ptrxwallet.CloseProcessSession();
				FileLog.WriteLog(methodName + " error : " + "add ok but no exist addr " + addr);
				WalletMgrApiApplication.g_respmsg = "error : " + "add ok but no exist addr " + addr;
				return null;
			}
		}	
		
		if(pdto.getNo() == 0) {
			ptrxwallet.CloseProcessSession();
			FileLog.WriteLog(methodName + " error : " + "add ok no exist No addr " + addr);
			WalletMgrApiApplication.g_respmsg = "error : " + "add ok no exist No addr " + addr;
			return null;
		}		
		ptrxwallet.CloseProcessSession();
		
		return pdto;
	}
	
	@Override
	public BuyHistoryDTO BuyInsertRequest(HashMap<String, Object> req) throws Exception {	
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		BuyHistoryDTO pnode = new BuyHistoryDTO();
		if(req.get("tid") != null) pnode.setTid(req.get("tid").toString());
		if(req.get("wid") != null) pnode.setWid(req.get("wid").toString());
		if(req.get("nftid") != null) pnode.setNftid(req.get("nftid").toString());
		if(req.get("hash") != null) pnode.setHash(req.get("hash").toString());
		if(req.get("price") != null) pnode.setPrice(req.get("price").toString());
		if(req.get("maxp") != null) pnode.setMaxp(req.get("maxp").toString());
		if(req.get("reward") != null) pnode.setReward(req.get("reward").toString());
		if(req.get("startday") != null) pnode.setStartday(Integer.parseInt(req.get("startday").toString()));
		if(req.get("lastday") != null) pnode.setLastday(Integer.parseInt(req.get("lastday").toString()));
		if(req.get("addr") != null) pnode.setAddr(req.get("addr").toString());
		if(req.get("naddr") != null) pnode.setNaddr(req.get("naddr").toString());
		
		FileLog.WriteLog(methodName + " pnode : " + pnode.toString());

		ProcessTrxUserWallet ptrxwallet = new ProcessTrxUserWallet();
		/*
		int nidx = ptrxwallet.GetNodeIndex(pnode.getNaddr());
		if(nidx < 1) {
			FileLog.WriteLog(methodName + " node find error nidx : " + nidx);
			return null;
		}
		pnode.setNidx(nidx);
		*/
		WalletInfoDTO udto = ptrxwallet.GetUsrWalletInfo(pnode.getAddr());		
		if(udto == null) {
			FileLog.WriteLog(methodName + " user wallet find error addr : " + pnode.getAddr());
			WalletMgrApiApplication.g_respmsg = "user wallet find error addr : " + pnode.getAddr();
			return null;			
		}
		pnode.setNidx(1);
		pnode.setStatus(1);
		BuyHistoryDTO buyh = ptrxwallet.InsertBuyHistory(pnode);
		
		ptrxwallet.CloseProcessSession();
		
		/*
		JSONObject result = buyservice.InsertRequest(req);
		int idx = Integer.parseInt(result.get("result").toString());
		*/
		return buyh;		
	}
	
	public double SumTotalPrice(ArrayList<BuyHistoryDTO> plist) {
		double sum = 0;
		if(plist == null) return 0;
		
		for(int idx=0;idx<plist.size();idx++) {
			String pricestr = plist.get(idx).getPrice();
			if(isNumeric(pricestr) == true) {
				double pprice = Double.parseDouble(pricestr);
				sum += pprice;
			}
		}
		
		return sum;
	}

	public double round(double number, int n) {
	    double m = Math.pow(10.0, n);
	    return Math.floor(number * m) / m;
	}
	
	public double CalculateReward(String amount, double total, double price) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		double reward = 0;
		double amt = 0;
		if(isNumeric(amount) == true) {
			amt = Double.parseDouble(amount);
		}
		
		double percentage = price / total;
		
		//소수점 2자리로 제한한다.
		double percut = round(percentage, 2);
		
		reward = amt * percut;
		
		FileLog.WriteLog(methodName + "amount : " + amount + ", total : " + total + ", price : " + price);
		FileLog.WriteLog(methodName + "percentage : " + percentage + ", percut : " + percut + ", reward : " + reward);
		return reward;
	}
	
	public BigDecimal CalculateBigReward(String amount, double total, double price) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		BigDecimal reward = new BigDecimal("0");
		BigDecimal btotal = new BigDecimal(total);
		BigDecimal amt = new BigDecimal(amount);
		BigDecimal bprc = new BigDecimal(price);
		BigDecimal non_div = new BigDecimal("1");
		//reward = reward.divide(non_div, 10, BigDecimal.ROUND_HALF_DOWN);
		
		BigDecimal percentage = bprc.divide(btotal, 18, BigDecimal.ROUND_FLOOR);
		
		reward = amt.multiply(percentage);
		reward = reward.divide(non_div, 18, BigDecimal.ROUND_FLOOR);
		
		
		FileLog.WriteLog(methodName + "amount : " + amt.toPlainString() + ", total : " + btotal.toPlainString() + ", price : " + bprc.toPlainString());
		FileLog.WriteLog(methodName + "percentage : " + percentage.toPlainString() + ", reward : " + reward.toPlainString());
		return reward;
	}
	
	public BigDecimal CalculateRewardAmount(String amount, double price) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		double perprice = price/1000000;
		BigDecimal reward = new BigDecimal("0");
		BigDecimal amt = new BigDecimal(amount);
		BigDecimal bprc = new BigDecimal(perprice);
		BigDecimal non_div = new BigDecimal("1");
		//reward = reward.divide(non_div, 10, BigDecimal.ROUND_HALF_DOWN);

		reward = amt.multiply(bprc);
		reward = reward.divide(non_div, 18, BigDecimal.ROUND_FLOOR);
		
		
		FileLog.WriteLog(methodName + "amount : " + amt.toPlainString() + ", price : " + bprc.toPlainString());
		FileLog.WriteLog(methodName + "perprice : " + bprc.toPlainString() + ", reward : " + reward.toPlainString());
		return reward;
	}
				
	public ArrayList<BuyHistoryDTO> SetRewardAmount(String amount, ArrayList<BuyHistoryDTO> plist) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		double sum = 0;
		if(plist == null) return null;
		
		sum = SumTotalPrice(plist);
		
		if(sum > 0) {
			for(int idx=0;idx<plist.size();idx++) {
				BuyHistoryDTO pdto = plist.get(idx);
				double price = 0;
				if(isNumeric(pdto.getPrice()) == true) {
					price = Double.parseDouble(pdto.getPrice());
				}				
				//double value = CalculateReward(amount, sum, price);CalculateBigReward
				//BigDecimal reward = new BigDecimal(value);
				//BigDecimal reward = CalculateBigReward(amount, sum, price);
				BigDecimal reward = CalculateRewardAmount(amount, price);
				
				BigDecimal non_div = new BigDecimal("1");
				reward = reward.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);
				
				pdto.setReward(reward.toPlainString());
				plist.set(idx, pdto);
				FileLog.WriteLog(methodName + " SetRewardAmount : " + pdto.toString());
			}
			return plist;
		}
		
		return null;
	}
	
	@Override
	public BigDecimal ProcessRewardDistribute(String from, String amount) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		BigDecimal total = new BigDecimal("0");
		ArrayList<BuyHistoryDTO> plist = new ArrayList<BuyHistoryDTO>();
		
		HashMap<String, Object> req = new HashMap<String, Object>();
		req.put("naddr", from);
		
		FileLog.WriteLog(methodName + " from : " + from + ", amount : " + amount);
		
		plist = GetBuyHistoryList(req);
		if(plist != null) {
			if(plist.size() > 0) {
				plist = SetRewardAmount(amount, plist);
				
				for(int idx=0;idx<plist.size();idx++) {
					BuyHistoryDTO pdto = plist.get(idx);
					if(pdto != null) {
						WalletInfoDTO wdto = DistributeUserWallet("user", "0x8D035BA57455a180B9bfcFF9DC512Dfa3ebCE1F3", pdto.getAddr(), pdto.getReward(), pdto.getNaddr() + pdto.getNftid());
						if(wdto != null) {
							BigDecimal bigamt = new BigDecimal(pdto.getReward());
							total.add(bigamt);
							FileLog.WriteLog(methodName + " : " + wdto.toString());
						}
					}
				}
			}
		}
		
		FileLog.WriteLog(methodName + " test : ");
		FileLog.WriteLog(methodName + " test : " + total.toPlainString());
		return total;
	}
			
	@Override
	public ArrayList<BuyHistoryDTO> GetBuyHistoryList(HashMap<String, Object> req) throws Exception {	
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		ArrayList<BuyHistoryDTO> plist = new ArrayList<BuyHistoryDTO>();
		
		String naddr = ""; 
		if(req.get("naddr") != null) naddr = req.get("naddr").toString();
		if(naddr == null) {
			return null;
		}
		if(naddr.length() < 2) {
			return null;
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SYMBOL", "user");	//Node 주소가 같은 모든 구매 이력을 가져온다.
		params.put("NADDR", naddr);	//Node 주소가 같은 모든 구매 이력을 가져온다.
		params.put("STATUS", 1);	//사용 상태인것 들만 가져와야 한다.
				
		List<HashMap<String, Object>> reqList = bhDao.selectrequest(params);
		
		if(reqList != null) {
			for(int idx=0;idx<reqList.size();idx++) {
				BuyHistoryDTO pdto = new BuyHistoryDTO();
				HashMap<String, Object> reqobj = reqList.get(idx);
				//System.out.println(reqobj.toString());
				if(reqobj != null) {
					if(reqobj.get("idx") != null) pdto.setIdx(Integer.parseInt(reqobj.get("idx").toString()));
					if(reqobj.get("nidx") != null) pdto.setNidx(Integer.parseInt(reqobj.get("nidx").toString()));
					if(reqobj.get("tid") != null) pdto.setTid(reqobj.get("tid").toString());
					if(reqobj.get("wid") != null) pdto.setWid(reqobj.get("wid").toString());
					if(reqobj.get("addr") != null) pdto.setAddr(reqobj.get("addr").toString());
					if(reqobj.get("naddr") != null) pdto.setNaddr(reqobj.get("naddr").toString());
					if(reqobj.get("nftid") != null) pdto.setNftid(reqobj.get("nftid").toString());
					if(reqobj.get("hash") != null) pdto.setHash(reqobj.get("hash").toString());
					if(reqobj.get("price") != null) pdto.setPrice(reqobj.get("price").toString());
					if(reqobj.get("maxp") != null) pdto.setMaxp(reqobj.get("maxp").toString());
					if(reqobj.get("reward") != null) pdto.setReward(reqobj.get("reward").toString());
					if(reqobj.get("startday") != null) pdto.setStartday(Integer.parseInt(reqobj.get("startday").toString()));
					if(reqobj.get("lastday") != null) pdto.setLastday(Integer.parseInt(reqobj.get("lastday").toString()));
					if(reqobj.get("c_tm") != null) pdto.setC_tm(reqobj.get("c_tm").toString());
					if(reqobj.get("u_tm") != null) pdto.setU_tm(reqobj.get("u_tm").toString());
					if(reqobj.get("d_tm") != null) pdto.setD_tm(reqobj.get("d_tm").toString());
					//d_tm이 값이 있으면... 삭제된 건이다.
					if(pdto.getD_tm().equals("0") != true) continue;
					
					if(pdto != null) plist.add(pdto);
				}	
			}
			FileLog.WriteLog(methodName + " size : " + plist.size());
			if(plist.size() < 1) {
				WalletMgrApiApplication.g_respmsg = "No search list in " + naddr;
				return null;
			}
		} else {
			FileLog.WriteLog(methodName + " No search list in " + naddr);
			WalletMgrApiApplication.g_respmsg = "No search list in " + naddr;
			return null;	
		}				
		return plist;
	}
		
	@Override
	public WalletInfoDTO TransferUserWallet(String symbol, String from, String to, String amount) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = new WalletInfoDTO();
		//
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(symbol.equals("user") != true) return null;

		ProcessTrxUserWallet ptrxwallet = new ProcessTrxUserWallet();

		WalletInfoDTO pdto1 = ptrxwallet.TransferUsrWalletInfo(from, to, amount);

		ptrxwallet.CloseProcessSession();
		
		FileLog.WriteLog(methodName + " : " + pdto1.toString());
		
		return pdto;
	}			
	
	@Override
	public WalletInfoDTO DistributeUserWallet(String symbol, String from, String to, String amount, String nftid) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = null;
		//
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;

		if(symbol.equals("user") != true) return null;

		ProcessTrxUserWallet ptrxwallet = new ProcessTrxUserWallet();

		if(ptrxwallet != null) {
			pdto = ptrxwallet.DistributeUsrWalletInfo(from, to, amount, nftid);	
			ptrxwallet.CloseProcessSession();
			if(pdto != null)
				FileLog.WriteLog(methodName + " : " + pdto.toString());
			else 
				FileLog.WriteLog(methodName + " pdto1 : null");
		}
			
		return pdto;
	}			
	
	@Override
	public int UnlockUserWallet(String symbol) throws Exception {
		final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		if (symbol == null) {
			return -1;
		}
		if (symbol.length() < 2) {
			return -1;
		}
		
		ProcessTrxUserWallet ptrxwallet = new ProcessTrxUserWallet();
		int count = ptrxwallet.ProcessUnlock("2022-07-17 00:00:00", "2022-07-17 23:59:59");
		ptrxwallet.CloseProcessSession();

		FileLog.WriteLog(methodName + " count : " + count);

		return count;
	}

	@Override
	public WalletInfoDTO TestWalletInfo(String symbol, String from) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = new WalletInfoDTO();
		//
		HashMap<String, Object> params = new HashMap<String, Object>();
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;

		
		params.put("SYMBOL", symbol);
		//
		String addr = "0xA1FD14cBB10ab7EC912E6617C1b6E74a50B39bB3";
		NodeHistoryDTO pnode = new NodeHistoryDTO();
		pnode.setSerial("nodesn01");
		pnode.setIpa("192.168.0.11");
		pnode.setPrice("1000000");
		pnode.setMaxp("200000");
		pnode.setReward("200000");
		pnode.setStartday(20220715);
		pnode.setLastday(20320714);
		pnode.setOp_time("2022-07-15 00:00:00");
		//WalletInfoDTO pdto = InsertNodeWalletInfo(symbol, addr, pnode);
		//WalletInfoDTO pdto = TransferNodeWalletInfo(symbol, from, addr, "50.000000000000000000");
		
		ProcessTrxUserWallet ptrxwallet = new ProcessTrxUserWallet();
		
		String fromd = "0xF1FD14cBB10ab7EC912E6617C1b6E74a50B39bB2";
		String tod = "0xA1FD14cBB10ab7EC912E6617C1b6E74a50B39bB3";
		String amount = "100";
		
		//WalletInfoDTO pdto1 = ptrxwallet.TransferUsrWalletInfo(fromd, tod, amount);
		//WalletInfoDTO pdto = ptrxwallet.DistributeUsrWalletInfo(fromd, tod, amount);
		int count = ptrxwallet.ProcessUnlock("2022-07-17 00:00:00", "2022-07-17 23:59:59");
		
		ptrxwallet.CloseProcessSession();
		
		FileLog.WriteLog(methodName + " ProcessUnlock process count : " + count);
		
		return pdto;
	}			
}
