package com.walletmgr.api.Utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

import com.walletmgr.api.WalletMgrApiApplication;
import com.walletmgr.api.dto.BuyHistoryDTO;
import com.walletmgr.api.dto.RewardUsrDTO;
import com.walletmgr.api.dto.WalletInDTO;
import com.walletmgr.api.dto.WalletInfoDTO;

public class ProcessTrxUserWallet {

	FlagDBConnectionInstance dbconnection;
	
	public ProcessTrxUserWallet() {
		super();
		
	    String db_user = System.getenv("DB_USER");
	    String db_pw = System.getenv("DB_PW");
	    String db_port = System.getenv("DB_PORT");
	    
		// TODO Auto-generated constructor stub
		dbconnection = new FlagDBConnectionInstance();
		dbconnection.getConnection(db_user, db_pw,"jdbc:mariadb://127.0.0.1", db_port, "?allowMultiQueries=true");
		
		FileLog.WriteLog("Re-connect Database");		
	}
	
	//리소스 관리를 위해서 꼭 CloseProcessSession를 호출하여 DB와의 세션을 닫도록 하자.
	public void CloseProcessSession() throws SQLException {
		Connection flagconn = dbconnection.flag_connection;
		
		flagconn.close();
	}
	
    public String generateRandom(int nmr) {

        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        String string_generated = "";
        int nmr_loops = nmr;
        StringBuilder sb = new StringBuilder(nmr_loops);

        for (int i = 0; i < nmr_loops; i++)
            sb.append(characters.charAt(rnd.nextInt(characters.length())));

        string_generated = sb.toString();
        return string_generated;
    }
    
    public String makeTxid(int txid_no, String symbol) {

        /**
         * txid 생성 규칙 : 심볼 TXID + 구분코드 + 랜덤16자리(영대소문자+숫자) + microtime
         * 
         */

        // 1. 심볼 TXID 생성
        String symbol_ori = Base64.getEncoder().encodeToString(symbol.getBytes());
        String symbol_txid = symbol_ori.substring(0, 4);
        
        String separator = "a"+txid_no;

        // 3. 랜덤 16자리 생성
        String random16 = generateRandom(16);

        // 4. microtime
        long microtime = System.currentTimeMillis();

        String txid = symbol_txid + separator + random16 + microtime;

        return txid;
    }
    
    public String makeReqCodeNum() {

        String code = Long.toString(System.currentTimeMillis());
        Random rand = new Random();    
        String req_code = code + "." + String.format("%04d", rand.nextInt(10000));

        return req_code;

    }
    
 	public RewardUsrDTO GetUsrInfo(String uid) {
 		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
 		RewardUsrDTO pdto = new RewardUsrDTO();
 		Connection flagconn = dbconnection.flag_connection;
 		int count = 0;
 		Statement stmt;
 		try {
 			stmt = flagconn.createStatement();

 			String sql = "SELECT idx, uid, parent, c_tm, u_tm, d_tm from user_wallet.tbl_reward_usr WHERE uid = '" + uid + "';";
 			FileLog.WriteLog(methodName + " -] " + sql);
 			ResultSet rs = stmt.executeQuery(sql);
 			while (rs.next()) {
 				pdto.setIdx(rs.getInt("idx"));
 				pdto.setUid(rs.getString("uid"));
 				pdto.setParent(rs.getString("parent"));
 				pdto.setC_tm(rs.getString("c_tm"));
 				pdto.setU_tm(rs.getString("u_tm"));
 				pdto.setD_tm(rs.getString("d_tm"));
 				count ++;
 			}
 			
 			 FileLog.WriteLog(methodName + " -] " + " : " + pdto.toString());
 		} catch (SQLException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
 			return null;		
 		}				
 		if(count == 0) return null;
 		return pdto;
 	}
 	   
	public WalletInfoDTO GetUsrWalletInfo(String addr) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = new WalletInfoDTO();
		Connection flagconn = dbconnection.flag_connection;
		int count = 0;
		Statement stmt;
		try {
			stmt = flagconn.createStatement();

			String sql = "SELECT `no` as idx, c_tm, usr_no, addr, v_amount, t_amount, i_amount from user_wallet.tbl_wallet WHERE addr = '" + addr + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				pdto.setNo(rs.getInt("idx"));
				pdto.setCreatetime(rs.getString("c_tm"));
				pdto.setUsr_no(rs.getInt("usr_no"));
				pdto.setAddr(rs.getString("addr"));
				pdto.setV_amount(rs.getString("v_amount"));
				pdto.setT_amount(rs.getString("t_amount"));
				pdto.setI_amount(rs.getString("i_amount"));
				count ++;
			}
			
			if(pdto != null) FileLog.WriteLog(methodName + " -] " + " : " + pdto.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;		
		}				
		if(count == 0) return null;
		return pdto;
	}
	  
	public ArrayList<WalletInfoDTO> GetUsrWalletList(String addr) {
		final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		ArrayList<WalletInfoDTO> plist = new ArrayList<WalletInfoDTO>();
		Connection flagconn = dbconnection.flag_connection;

		Statement stmt;
		try {
			stmt = flagconn.createStatement();

			String sql = "SELECT `no` as idx, c_tm, usr_no, addr, v_amount, t_amount, i_amount from user_wallet.tbl_wallet WHERE addr = '"
					+ addr + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				WalletInfoDTO pdto = new WalletInfoDTO();
				pdto.setNo(rs.getInt("no"));
				pdto.setCreatetime(rs.getString("c_tm"));
				pdto.setUsr_no(rs.getInt("usr_no"));
				pdto.setAddr(rs.getString("addr"));
				pdto.setV_amount(rs.getString("v_amount"));
				pdto.setT_amount(rs.getString("t_amount"));
				pdto.setI_amount(rs.getString("i_amount"));
				
				plist.add(pdto);
			}

			FileLog.WriteLog(methodName + " -] " + " size : " + plist.size());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}

		return plist;
	}
			
	//
	public int GetNodeIndex(String addr) {
		final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		int index = 0;
		Connection flagconn = dbconnection.flag_connection;

		Statement stmt;
		try {
			stmt = flagconn.createStatement();

			String sql = "select idx, `serial` from node_wallet.tbl_node_history where `addr` = '" + addr + "';";	
			FileLog.WriteLog(methodName + " -] " + sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				index = rs.getInt("idx");
				//String pserial = rs.getString("serial");
			}

			FileLog.WriteLog(methodName + " -] " + " : " + index + ", " + addr);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return 0;
		}

		return index;
	}// String query = "select idx, serial from node_wallet.tbl_node_history where
		// serial = '" + pnode.getNodesn() + "';";
	
	WalletInfoDTO SetUsrWalletInfo(String addr, String amount) throws SQLException {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO pdto = GetUsrWalletInfo(addr);
		if(pdto == null) return null;
		
		Connection flagconn = dbconnection.flag_connection;
		
		Statement stmt;
		try {
			flagconn.setAutoCommit(false);
			
			stmt = flagconn.createStatement();
			
			BigDecimal v_amount = new BigDecimal(pdto.getV_amount());
			BigDecimal a_amount = new BigDecimal(amount);
			BigDecimal non_div = new BigDecimal("1");	
			
			v_amount = v_amount.add(a_amount);
			v_amount = v_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);

			String sql = "UPDATE user_wallet.tbl_wallet SET v_amount = '" + v_amount.toPlainString() + "' WHERE addr='" + addr + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			int rs = stmt.executeUpdate(sql);
			if(rs < 1) {
				FileLog.WriteLog(methodName + " -] " +  "BuildInInsertQuery insert fail" + sql);
				flagconn.rollback();
			} else {
				flagconn.commit();
			}
			
			FileLog.WriteLog(methodName + " -] " + " : " + rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			flagconn.rollback();
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;				
		}				

		return pdto;
	}	
	
	public BuyHistoryDTO InsertBuyHistory(BuyHistoryDTO pdto) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(pdto.getNidx() < 1) return null;
		if(pdto.getAddr() == null) return null;
		if(pdto.getAddr().length() < 2) return null;		
		if(pdto.getNaddr() == null) return null;
		if(pdto.getNaddr().length() < 2) return null;		
		
		Connection flagconn = dbconnection.flag_connection;
		
		String query = "";
		String fquery = "INSERT INTO user_wallet.tbl_buy_history(";
		String equery = "(";
		fquery += "nidx"; equery += pdto.getNidx();
		fquery += ",`status`"; equery += ",";equery += pdto.getStatus();
		
		if(pdto.getAddr() != null) {
			fquery += ",`addr`"; equery += ","; equery += "'"; equery += pdto.getAddr(); equery += "'";
		}
		if(pdto.getNaddr() != null) {
			fquery += ",`naddr`"; equery += ","; equery += "'"; equery += pdto.getNaddr(); equery += "'";
		}		
		if(pdto.getTid() != null) {
			fquery += ",`tid`"; equery += ","; equery += "'"; equery += pdto.getTid(); equery += "'";
		}
		if(pdto.getWid() != null) {
			fquery += ",`wid`"; equery += ","; equery += "'"; equery += pdto.getWid(); equery += "'";
		}		
		if(pdto.getNftid() != null) {
			fquery += ",`nftid`"; equery += ","; equery += "'"; equery += pdto.getNftid(); equery += "'";
		}
		if(pdto.getHash() != null) {
			fquery += ",`hash`"; equery += ","; equery += "'"; equery += pdto.getHash(); equery += "'";
		}			
		if(pdto.getPrice() != null) {
			fquery += ",price"; equery += ","; equery += "'"; equery += pdto.getPrice(); equery += "'";
		}
		if(pdto.getMaxp() != null) {
			fquery += ",maxp"; equery += ","; equery += "'"; equery += pdto.getMaxp(); equery += "'";
		}	
		fquery += ",reward"; equery += ","; equery += "'"; equery += "0.000000000000000000"; equery += "'";
		fquery += ",`startday`"; equery += ",";equery += pdto.getStartday();
		fquery += ",`lastday`"; equery += ",";equery += pdto.getLastday();
		
		fquery += ") VALUES ";
		equery += ");";
		query = fquery + equery;
		
		FileLog.WriteLog(methodName + " -] " + query);	
		
		Statement stmt;
		try {
			//flagconn.setAutoCommit(false);
			stmt = flagconn.createStatement();
			
			int trs = stmt.executeUpdate(query);
			if(trs < 1) {
				FileLog.WriteLog(methodName + " -] " + " insert fail" + query);
				return null;
			} else {
				flagconn.commit();
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}		
		return pdto;
	}	
	
	public RewardUsrDTO InsertUserInfo(String uid, String parent) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(uid == null) return null;
		if(uid.length() < 2) return null;		
		
		Connection flagconn = dbconnection.flag_connection;
		
		String query = "";
		String fquery = "INSERT INTO user_wallet.tbl_reward_usr(";
		String equery = "(";
		fquery += "`uid`"; equery += "'"; equery += uid; equery += "'";
		if(parent != null) {
			fquery += ",`parent`"; equery += ","; equery += "'"; equery += parent; equery += "'";
		}
		
		fquery += ") VALUES ";
		equery += ");";
		query = fquery + equery;
		
		FileLog.WriteLog(methodName + " -] " + query);	
		
		Statement stmt;
		try {
			//flagconn.setAutoCommit(false);
			stmt = flagconn.createStatement();
			
			int trs = stmt.executeUpdate(query);
			if(trs < 1) {
				FileLog.WriteLog(methodName + " -] " + " insert fail" + query);
				return null;
			} else {
				flagconn.commit();
				RewardUsrDTO pdto = GetUsrInfo(uid);
				return pdto;
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}			
	}	
	
	public WalletInfoDTO InsertUserWallet(int usr_no, String addr) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(addr == null) return null;
		if(addr.length() < 2) return null;		
		
		Connection flagconn = dbconnection.flag_connection;
		
		String query = "";
		String fquery = "INSERT INTO user_wallet.tbl_wallet(";
		String equery = "(";
		fquery += "usr_no"; equery += usr_no;
		
		if(addr != null) {
			fquery += ",`addr`"; equery += ","; equery += "'"; equery += addr; equery += "'";
		}
		
		fquery += ") VALUES ";
		equery += ");";
		query = fquery + equery;
		
		FileLog.WriteLog(methodName + " -] " + query);	
		
		Statement stmt;
		try {
			//flagconn.setAutoCommit(false);
			stmt = flagconn.createStatement();
			
			int trs = stmt.executeUpdate(query);
			if(trs < 1) {
				FileLog.WriteLog(methodName + " -] " + " insert fail" + query);
				return null;
			} else {
				flagconn.commit();
				WalletInfoDTO pdto = GetUsrWalletInfo(addr);
				return pdto;
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}			
	}	
				
	public String InsertTblOutInfo(String symbol, int usr_no, String from, String to, String amount, String fee, String nftid) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(symbol == null) return null;
		if(symbol.length() < 2) return null;
		if(from == null) return null;
		if(from.length() < 2) return null;		
		if(to == null) return null;
		if(to.length() < 2) return null;		
		
		Connection flagconn = dbconnection.flag_connection;
		
		String out_txid = makeTxid(1, symbol);
		
		String query = "";
		String fquery = "INSERT INTO user_wallet.tbl_out(";
		String equery = "(";
		fquery += "usr_no"; equery += usr_no;
		if(from != null) {
			fquery += ",`from`"; equery += ","; equery += "'"; equery += from; equery += "'";
		}
		if(to != null) {
			fquery += ",`to`"; equery += ","; equery += "'"; equery += to; equery += "'";
		}
		if(amount != null) {
			fquery += ",amount"; equery += ","; equery += "'"; equery += amount; equery += "'";
		}
		fquery += ",fee"; equery += ","; equery += "'"; equery += fee; equery += "'";	
		if(nftid != null) {
			fquery += ",req_code"; equery += ","; equery += "'"; equery += nftid; equery += "'";
		} else {
			fquery += ",req_code"; equery += ","; equery += "'"; equery += makeReqCodeNum(); equery += "'";
		}
		fquery += ",approval"; equery += ","; equery += "'"; equery += '1'; equery += "'";	
		fquery += ",done"; equery += ","; equery += "'"; equery += '1'; equery += "'";	
		fquery += ",confirm_num"; equery += ","; equery += "'"; equery += '7'; equery += "'";	
		fquery += ",txid"; equery += ","; equery += "'"; equery += out_txid; equery += "'";	
			
		fquery += ") VALUES ";
		equery += ");";
		query = fquery + equery;
		
		FileLog.WriteLog(methodName + " -] " + query);	
		
		Statement stmt;
		try {
			//flagconn.setAutoCommit(false);
			stmt = flagconn.createStatement();
			
			int trs = stmt.executeUpdate(query);
			if(trs < 1) {
				FileLog.WriteLog(methodName + " -] " + " to BuildInInsertQuery update fail" + query);
				return null;
			} else {
				
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}		
		return out_txid;
	}	
	
	//보상 입금건을 기록한다.
	//보상 건인지 일반 전송건인지 구분이 필요할듯.
	//nonce 필드를 사용하자 -> 0일 경우 일반, 1일 경우 보상
	public String InsertTblInInfo(int type, int usr_no, String from, String to, String amount, String txid, String nftid) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(from == null) return null;
		if(from.length() < 2) return null;		
		if(to == null) return null;
		if(to.length() < 2) return null;		
		
		Connection flagconn = dbconnection.flag_connection;
		
		String query = "";
		String fquery = "INSERT INTO user_wallet.tbl_in(";
		String equery = "(";
		fquery += "usr_no"; equery += usr_no;
		if(from != null) {
			fquery += ",`from`"; equery += ","; equery += "'"; equery += from; equery += "'";
		}
		if(to != null) {
			fquery += ",`to`"; equery += ","; equery += "'"; equery += to; equery += "'";
		}
		if(amount != null) {
			fquery += ",amount"; equery += ","; equery += "'"; equery += amount; equery += "'";
		}
		if(nftid != null) {
			fquery += ",prev_amount"; equery += ","; equery += "'"; equery += nftid; equery += "'";
		}		
		fquery += ",confirm_num"; equery += ","; equery += "'"; equery += '7'; equery += "'";	
		fquery += ",txid"; equery += ","; equery += "'"; equery += txid; equery += "'";	
		fquery += ",nonce"; equery += ","; equery += type;
					
		fquery += ") VALUES ";
		equery += ");";
		query = fquery + equery;
		
		FileLog.WriteLog(methodName + " -] " + query);	
		
		Statement stmt;
		try {
			//flagconn.setAutoCommit(false);
			stmt = flagconn.createStatement();
			
			int trs = stmt.executeUpdate(query);
			if(trs < 1) {
				FileLog.WriteLog(methodName + " -] " + " to BuildInInsertQuery update fail" + query);
				return null;
			} else {
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}		
		return txid;
	}	
	
	//unlock 처리건을 기록한다.
	//unlock된 tbl_in건의 confirm_num을 9로 변경한다. 7은 입력 OK, 9는 unlock 완료
	public String InsertUnlocInfo(int usr_no, String amount, String txid) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		if(usr_no < 1) return null;
	
		
		Connection flagconn = dbconnection.flag_connection;
		
		String query = "";
		String fquery = "INSERT INTO user_wallet.tbl_ulock_history(";
		String equery = "(";
		fquery += "usr_no"; equery += usr_no;
		if(amount != null) {
			fquery += ",`amount`"; equery += ","; equery += "'"; equery += amount; equery += "'";
		}
		fquery += ",txid"; equery += ","; equery += "'"; equery += txid; equery += "'";	
			
		fquery += ") VALUES ";
		equery += ");";
		query = fquery + equery;
		
		FileLog.WriteLog(methodName + " -] " + query);	
		
		Statement stmt;
		try {
			//flagconn.setAutoCommit(false);
			stmt = flagconn.createStatement();
			
			int trs = stmt.executeUpdate(query);
			if(trs < 1) {
				FileLog.WriteLog(methodName + " -] " + " to BuildInInsertQuery update fail" + query);
				return null;
			} else {
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}		
		return txid;
	}	
	
	//사용자 지갑간의 전송은 v_amount를 기준으로 한다. 
	public synchronized WalletInfoDTO TransferUsrWalletInfo(String from, String to, String amount) throws SQLException {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO fdto = GetUsrWalletInfo(from);
		if (fdto == null) return null;
		
		WalletInfoDTO tdto = GetUsrWalletInfo(to);
		if (tdto == null) return null;
	
		Connection flagconn = dbconnection.flag_connection;
		
		Statement stmt;
		try {
			flagconn.setAutoCommit(false);
			
			stmt = flagconn.createStatement();
			
			BigDecimal fv_amount = new BigDecimal(fdto.getV_amount());
			BigDecimal tv_amount = new BigDecimal(tdto.getV_amount());
			BigDecimal a_amount = new BigDecimal(amount);
			BigDecimal non_div = new BigDecimal("1");	
			
			if(fv_amount.compareTo(a_amount) < 0) {
				FileLog.WriteLog(methodName + " [serious error] small " + from + " amount " + " from [ " + fv_amount.toPlainString() + " ]" + " to [ " + a_amount.toPlainString() + " ]" );
				return null;
			}			
		
			fv_amount = fv_amount.subtract(a_amount);
			tv_amount = tv_amount.add(a_amount);
			
			fv_amount = fv_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);
			tv_amount = tv_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);

			String sql = "UPDATE user_wallet.tbl_wallet SET v_amount = '" + fv_amount.toPlainString() + "' WHERE addr='" + from + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			int rs = stmt.executeUpdate(sql);
			if(rs < 1) {
				FileLog.WriteLog(methodName + " -] " + " from BuildInInsertQuery update fail" + sql);
				flagconn.rollback();
				return null;
			} else {
				String tosql = "UPDATE user_wallet.tbl_wallet SET v_amount = '" + tv_amount.toPlainString() + "' WHERE addr='" + to + "';";
				FileLog.WriteLog(methodName + " -] " + sql);	
				int trs = stmt.executeUpdate(tosql);
				if(trs < 1) {
					FileLog.WriteLog(methodName + " -] " + " to BuildInInsertQuery update fail" + sql);
					flagconn.rollback();
					return null;
				} else {
					
				}
			}
			
			tdto = GetUsrWalletInfo(to);
			try {
				String otxid = InsertTblOutInfo("usr_wallet", fdto.getUsr_no(), from, to, amount, "0.000000000000000000", "none");
				if(otxid != null) tdto.setOuttxid(otxid);
				else {
					FileLog.WriteLog(methodName + " -] " +  "InsertTblInInfo call otxid null");
					flagconn.rollback();
					return null;			
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				FileLog.WriteLog(methodName + " -] " +  e.toString());
				flagconn.rollback();
				return null;
			}
			
			StringBuilder intxid = new StringBuilder(tdto.getOuttxid());
			intxid.setCharAt(5, '0');	
			
			try {
				//일반적인 거래이므로 첫번째 인자 type을 0으로 준다.
				String itxid = InsertTblInInfo(0, tdto.getUsr_no(), from, to, amount, intxid.toString(), "none") ;
				if(itxid != null) tdto.setIntxid(itxid);
				else {
					FileLog.WriteLog(methodName + " -] " +  "InsertTblInInfo call itxid null");
					flagconn.rollback();
					return null;		
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				FileLog.WriteLog(methodName + " -] " +  e.toString());
				flagconn.rollback();
				return null;
			}
			
			FileLog.WriteLog(methodName + " -] " + " OK Get : " + tdto.toString());
			
			flagconn.commit();
			return tdto;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			flagconn.rollback();
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}				
	}		
	
	//Node의 보상을 사용자들에게 분배한다. 
	//분배된 보상은 6개월간 Lock이 걸리기 때문에 t_amount (total 관리), i_amount (락 된 금액) 2개를 증가 시킨다.
	//보상은 락 개념이 있기 때문에 v_amount 증가가 아님을 명심하자.
	public synchronized WalletInfoDTO DistributeUsrWalletInfo(String from, String to, String amount, String nftid) throws SQLException {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		WalletInfoDTO fdto = GetUsrWalletInfo(from);
		if (fdto == null) {
			return null;
		}
		WalletInfoDTO tdto = GetUsrWalletInfo(to);
		if (tdto == null) {
			return null;
		}

		Connection flagconn = dbconnection.flag_connection;

		Statement stmt;
		try {
			flagconn.setAutoCommit(false);

			stmt = flagconn.createStatement();

			BigDecimal fv_amount = new BigDecimal(fdto.getV_amount());
			BigDecimal a_amount = new BigDecimal(amount);
			BigDecimal non_div = new BigDecimal("1");
			
			if (a_amount.compareTo(new BigDecimal("0.00")) == 0) {
				FileLog.WriteLog(methodName + " [zero amount] zero [ " + amount + " ]");
				return null;		
			}
						
			if(fv_amount.compareTo(a_amount) < 0) {
				FileLog.WriteLog(methodName + " [serious error] small " + from + " amount " + " from [ " + fv_amount.toPlainString() + " ]" + " to [ " + a_amount.toPlainString() + " ]" );
				return null;
			}	
			
			fv_amount = fv_amount.subtract(a_amount);

			fv_amount = fv_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);	

			String sql = "UPDATE user_wallet.tbl_wallet SET v_amount = '" + fv_amount.toPlainString() + "' WHERE addr='"
					+ from + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			int rs = stmt.executeUpdate(sql);
			if (rs < 1) {
				FileLog.WriteLog(methodName + " -] " + " from BuildInInsertQuery update fail" + sql);
				flagconn.rollback();
				return null;
			} else {
				//분할되는 코인들은 각 사용자 지갑의 t_amount(Total), i_amount(locked)에 추가된다.
				BigDecimal tv_amount = new BigDecimal(tdto.getT_amount());
				BigDecimal ti_amount = new BigDecimal(tdto.getI_amount());

				tv_amount = tv_amount.add(a_amount);
				ti_amount = ti_amount.add(a_amount);				

				tv_amount = tv_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);		
				ti_amount = ti_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);		
				
				String tosql = "UPDATE user_wallet.tbl_wallet SET t_amount = '" + tv_amount.toPlainString()
						+ "' WHERE addr='" + to + "';";
				FileLog.WriteLog(methodName + " -] " + tosql);
				int trs = stmt.executeUpdate(tosql);
				if (trs < 1) {
					FileLog.WriteLog(methodName + " -] " + " to BuildInInsertQuery update fail" + tosql);
					flagconn.rollback();
					return null;
				}

				tosql = "UPDATE user_wallet.tbl_wallet SET i_amount = '" + ti_amount.toPlainString()
						+ "' WHERE addr='" + to + "';";
				FileLog.WriteLog(methodName + " -] " + tosql);
				trs = stmt.executeUpdate(tosql);
				if (trs < 1) {
					FileLog.WriteLog(methodName + " -] " + " to BuildInInsertQuery update fail" + tosql);
					flagconn.rollback();
					return null;
				}			
			}

			tdto = GetUsrWalletInfo(to);
			try {
				String otxid = InsertTblOutInfo("usr_wallet", tdto.getUsr_no(), from, to, amount,
						"0.000000000000000000", nftid);
				if (otxid != null)
					tdto.setOuttxid(otxid);
				else {
					FileLog.WriteLog(methodName + " -] " + " InsertTblInInfo call otxid null");
					flagconn.rollback();
					return null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				FileLog.WriteLog(methodName + " -] " + e.toString());
				flagconn.rollback();
				return null;
			}

			StringBuilder intxid = new StringBuilder(tdto.getOuttxid());
			intxid.setCharAt(5, '0');

			try {
				//보상을 분배하는 것이므로 첫번째 인자 type을 1로 준다.
				String itxid = InsertTblInInfo(1, tdto.getUsr_no(), from, to, amount, intxid.toString(), nftid);
				if (itxid != null)
					tdto.setIntxid(itxid);
				else {
					FileLog.WriteLog(methodName + " -] " + "InsertTblInInfo call itxid null");
					flagconn.rollback();
					return null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				FileLog.WriteLog(methodName + " -] " + e.toString());
				flagconn.rollback();
				return null;
			}

			FileLog.WriteLog(methodName + " -] " + " OK Get : " + tdto.toString());

			flagconn.commit();
			return tdto;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			flagconn.rollback();
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}
	}
	
	//tbl_in의 해당 row의 txid를 넘겨주어야 한다.
	//Unlock 동작은 tbl_in의 6개월이 지난 건의 v_amount 추가를 진행한다.
	//i_amount에서 tbl_in의 amount 만큼 차감하고 v_amount를 amount 만큼 증가 시킨다.
	public WalletInfoDTO UnklockUserWallet(String addr, String amount, String txid) throws SQLException {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		FileLog.WriteLog(methodName + " addr : " + addr + ", amount : " + amount + ", txid : " + txid);
		//Unlock은 l_amount -> v_amount 로 자산을 이동한다.
		if(addr == null) return null;
		WalletInfoDTO pdto = GetUsrWalletInfo(addr);
		if (pdto == null) {
			FileLog.WriteLog(methodName + " GetUsrWalletInfo null");
			return null;
		}

		Connection flagconn = dbconnection.flag_connection;

		Statement stmt;
		try {
			flagconn.setAutoCommit(false);

			stmt = flagconn.createStatement();

			BigDecimal fv_amount = new BigDecimal(pdto.getV_amount());
			BigDecimal fl_amount = new BigDecimal(pdto.getI_amount());
			BigDecimal a_amount = new BigDecimal(amount);
			BigDecimal non_div = new BigDecimal("1");
			
			if(fl_amount.compareTo(a_amount) < 0) {
				FileLog.WriteLog(methodName + " [serious error] small " + addr + " amount " + " from [ " + fl_amount.toPlainString() + " ]" + " to [ " + a_amount.toPlainString() + " ]" );
				return null;
			}						

			fl_amount = fl_amount.subtract(a_amount);
			fv_amount = fv_amount.add(a_amount);
			fl_amount = fl_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);
			fv_amount = fv_amount.divide(non_div, 18, BigDecimal.ROUND_HALF_DOWN);

			String sql = "UPDATE user_wallet.tbl_wallet SET v_amount = '" + fv_amount.toPlainString() + "', i_amount = '" + fl_amount.toPlainString() + "' WHERE addr='"
					+ addr + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			int rs = stmt.executeUpdate(sql);
			if (rs < 1) {
				FileLog.WriteLog(methodName + " -] " + " from BuildInInsertQuery update fail" + sql);
				flagconn.rollback();
				return null;
			} else {

			}

			pdto = GetUsrWalletInfo(addr);
			
			StringBuilder utxid = new StringBuilder(txid);
			utxid.setCharAt(5, '3');
			
			try {
				String otxid = InsertUnlocInfo(pdto.getUsr_no(), amount, utxid.toString());
				if (otxid != null)
					pdto.setUntxid(otxid);
				else {
					FileLog.WriteLog(methodName + " -] " + " InsertTblInInfo call otxid null");
					flagconn.rollback();
					return null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				FileLog.WriteLog(methodName + " -] " + e.toString());
				flagconn.rollback();
				return null;
			}
			
			//입금 완료 7 상태에서 언락 완료 9 상태로 confirm_num을 증가 시킨다.
			sql = "UPDATE user_wallet.tbl_in SET confirm_num = " + 9 + " WHERE txid='" + txid + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			rs = stmt.executeUpdate(sql);
			if (rs < 1) {
				FileLog.WriteLog(methodName + " -] " + " from BuildInInsertQuery update confirm_num fail" + sql);
				flagconn.rollback();
				return null;
			} 	
			
			FileLog.WriteLog(methodName + " -] " + " OK Get : " + pdto.toString());

			flagconn.commit();
			return pdto;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			flagconn.rollback();
			FileLog.WriteLog(methodName + " -] " + e1.toString());
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
			return null;
		}		
	}
	
	//select no, c_tm, `from`, `to`, amount, confirm_num, nonce, txid from user_wallet.tbl_in where confirm_num = 7 and nonce = 1 and c_tm between fromday and today;
	public ArrayList<WalletInDTO> GetUnlockList(String fromday, String today) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		ArrayList<WalletInDTO> plist = new ArrayList<WalletInDTO>();
		
		Connection flagconn = dbconnection.flag_connection;
		
		Statement stmt;
		try {
			stmt = flagconn.createStatement();

			String sql = "select `no`, c_tm, `from`, `to`, amount, confirm_num, nonce, txid from user_wallet.tbl_in where confirm_num = 7 and nonce = 1 and c_tm between '" 
						+ fromday + "' and '" + today + "';";
			FileLog.WriteLog(methodName + " -] " + sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				WalletInDTO pdto = new WalletInDTO();
				pdto.setNo(rs.getInt("no"));
				pdto.setC_tm(rs.getString("c_tm"));
				pdto.setFrom(rs.getString("from"));
				pdto.setTo(rs.getString("to"));
				pdto.setAmount(rs.getString("amount"));
				pdto.setConfirm_num(rs.getString("confirm_num"));
				pdto.setNonce(rs.getString("nonce"));
				pdto.setTxid(rs.getString("txid"));
				
				FileLog.WriteLog(methodName + " -] " + " : " + pdto.toString());
				plist.add(pdto);
			}

			FileLog.WriteLog(methodName + " -] " + " count : " + plist.size());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			WalletMgrApiApplication.g_respmsg = "sql exception : " + e1.toString();
		}				

		return plist;
	}
	
	//인자인 fromday ~ today까지의 시간 내에 있는 unlock 미처리건들을 처리한다.
	public synchronized int ProcessUnlock(String fromday, String today) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		int count = 0;
		ArrayList<WalletInDTO> plist = GetUnlockList(fromday, today);
		if(plist != null) {
			for(int idx=0;idx<plist.size();idx++) {
				WalletInDTO pdto = plist.get(idx);
				try {
					WalletInfoDTO pwallet = UnklockUserWallet(pdto.getTo(), pdto.getAmount(), pdto.getTxid());
					if(pwallet != null) count++;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					FileLog.WriteLog(methodName + " -] " + " : " + e.toString());
					WalletMgrApiApplication.g_respmsg = "sql exception : " + e.toString();
				}
			}
		}
		
		return count;
	}
}
