package com.walletmgr.api.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.walletmgr.api.WalletMgrApiApplication;
import com.walletmgr.api.Utils.CommonUtil;
import com.walletmgr.api.Utils.FileLog;
import com.walletmgr.api.dao.RestfulDao;
import com.walletmgr.api.dto.BuyHistoryDTO;
import com.walletmgr.api.dto.DepositResponseDto;
import com.walletmgr.api.dto.JsonResponseDto;
import com.walletmgr.api.dto.WalletInfoDTO;
import com.walletmgr.api.service.BuyHistoryService;
import com.walletmgr.api.service.WalletService;

@RestController
public class UserWalletController {
	
	@Resource
	RestfulDao restfulDao;

	@Resource
	CommonUtil commonUtil;
	
	@Resource
	WalletService walletreq;	
	
	@Resource
	BuyHistoryService buyreq;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/user/select", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String UserSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		WalletInfoDTO pdto = new WalletInfoDTO();
		JsonResponseDto resMsg = new JsonResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/user/select [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			
			try {
				//
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = walletreq.GetWalletInfo(params.get("symbol").toString(), params.get("addr").toString());
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	
				result.put("status", "error");	
				result.put("message", WalletMgrApiApplication.g_respmsg);	
				return result.toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			JSONObject result = new JSONObject();
			result.put("code", 10007);	result.put("status", "error");	result.put("message", "Internal server error.");	
			return result.toString();	
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/user/selectlist", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String UserSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		ArrayList<WalletInfoDTO> plist = null;
		JsonResponseDto resMsg = new JsonResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/user/selectlist [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonArrayString = "";
			try {
				//ArrayList<WalletInfoDTO> GetWalletList
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				plist = walletreq.GetWalletList(params.get("symbol").toString());
				jsonArrayString = gson.toJson(plist);
				
			} catch(Exception e) {
				plist = null;
			}

			if (plist == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	
				result.put("status", "error");	
				result.put("message", WalletMgrApiApplication.g_respmsg);	
				return result.toString();
			} else {
				
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonArrayString + "}";
			}
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			JSONObject result = new JSONObject();
			result.put("code", 10007);	result.put("status", "error");	result.put("message", "Internal server error.");	
			return result.toString();
		}
	}
		
	//성공하면 Symbol과 Addr을 리턴한다.
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/user/insert", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String UserInsertRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		WalletInfoDTO pdto = new WalletInfoDTO();
		DepositResponseDto resMsg = new DepositResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/user/insert [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";			
			try {
				//
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = walletreq.UserInsertRequest(params);
				if(pdto != null) {
					jsonString = gson.toJson(pdto);
					FileLog.WriteLog(jsonString);	
				}
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	
				result.put("status", "error");	
				result.put("message", WalletMgrApiApplication.g_respmsg);	
				return result.toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			JSONObject result = new JSONObject();
			result.put("code", 10007);	result.put("status", "error");	result.put("message", "Internal server error.");	
			return result.toString();
		}
	}
	
	//성공하면 Symbol과 Node Serial을 리턴한다.
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/user/buyadd", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String BuyInsertRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		BuyHistoryDTO pdto = new BuyHistoryDTO();
		DepositResponseDto resMsg = new DepositResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/user/buyadd [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";					
			try {
				//
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = walletreq.BuyInsertRequest(params);
				jsonString = gson.toJson(pdto);
				FileLog.WriteLog(jsonString);					
			} catch(Exception e) {
				//pdto.setReason("buy insert fail");
				pdto = null;
			}

			if (pdto == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	
				result.put("status", "error");	
				result.put("message", WalletMgrApiApplication.g_respmsg);	
				return result.toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			JSONObject result = new JSONObject();
			result.put("code", 10007);	result.put("status", "error");	result.put("message", "Internal server error.");	
			return result.toString();
		}
	}
		
	//성공하면 unlock 처리한 카운트를 리턴한다,
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/user/unlockwallet", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String UnlockUserWalletRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		int unlockcnt = 0;
		DepositResponseDto resMsg = new DepositResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/user/unlockwallet [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";				
			try {
				//
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				unlockcnt = walletreq.UnlockUserWallet(params.get("symbol").toString());
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("count", unlockcnt);
				jsonString = gson.toJson(jsonObject);
			} catch(Exception e) {
				unlockcnt = -1;
			}

			if (unlockcnt < 0) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	
				result.put("status", "error");	
				result.put("message", WalletMgrApiApplication.g_respmsg);	
				return result.toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			JSONObject result = new JSONObject();
			result.put("code", 10007);	result.put("status", "error");	result.put("message", "Internal server error.");	
			return result.toString();
		}
	}
	
	//성공하면 Symbol과 Node Serial을 리턴한다.
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/user/buyupdate", method = RequestMethod.POST) 
	public String BuyUpdateRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		BuyHistoryDTO pdto = new BuyHistoryDTO();
		DepositResponseDto resMsg = new DepositResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "] " +  request.getRequestURI() + " [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";					
			try {
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				JSONObject result = buyreq.UpdateRequest(params);
				jsonString = result.toString();
				FileLog.WriteLog(jsonString);					
			} catch(Exception e) {
				//pdto.setReason("buy insert fail");
				pdto = null;
			}

			if (pdto == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	
				result.put("status", "error");	
				result.put("message", WalletMgrApiApplication.g_respmsg);	
				return result.toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			JSONObject result = new JSONObject();
			result.put("code", 10007);	result.put("status", "error");	result.put("message", "Internal server error.");	
			return result.toString();
		}
	}
							
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/user/test", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String GetTestInfo(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		WalletInfoDTO pdto = new WalletInfoDTO();
		JsonResponseDto resMsg = new JsonResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/user/test [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			String apiaddr = validatemsg.split("-")[1];
			
			Gson gson = new Gson();
			String jsonString = "";
			
			try {
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = walletreq.TestWalletInfo(params.get("symbol").toString(), apiaddr);
				jsonString = gson.toJson(pdto);
				FileLog.WriteLog(jsonString);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	
				result.put("status", "error");	
				result.put("message", WalletMgrApiApplication.g_respmsg);	
				return result.toString();
			} else {			
				//resMsg = JsonResponseDto.builder().data(jsonString).status("true").message("Success").build();
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			JSONObject result = new JSONObject();
			result.put("code", 10007);	result.put("status", "error");	result.put("message", "Internal server error.");	
			return result.toString();
		}
	}
	
}
