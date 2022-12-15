package com.walletmgr.api.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
//import org.omg.CORBA.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.walletmgr.api.WalletMgrApiApplication;
import com.walletmgr.api.Utils.CommonUtil;
import com.walletmgr.api.Utils.FileLog;
import com.walletmgr.api.dao.RestfulDao;
import com.walletmgr.api.dto.DepositResponseDto;
import com.walletmgr.api.dto.WalletInfoDTO;
import com.walletmgr.api.service.WalletListService;
import com.walletmgr.api.service.WalletService;

@RestController
public class NodeWalletController {
	
	@Resource
	RestfulDao restfulDao;

	@Resource
	CommonUtil commonUtil;
	
	@Resource
	WalletService walletreq;
	
	@Resource
	WalletListService wlistreq;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/node/insert", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String NodeInsertRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		DepositResponseDto resMsg = new DepositResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/node/insert [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			//String apiaddr = validatemsg.split("-")[1];
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			WalletInfoDTO pdto = null;
			try {
				//Parameter의 Key값들의 대문자화가 이루어져야 한다. (Mapper에서의 구분 문제)
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = walletreq.InsertNodeWallet(params.get("symbol").toString(), params);
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	result.put("status", "error");	result.put("message", WalletMgrApiApplication.g_respmsg);	
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
	@RequestMapping(value = "/v1/node/select", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String NodeSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		//final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		DepositResponseDto resMsg = new DepositResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/node/select [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			//String apiaddr = validatemsg.split("-")[1];
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			WalletInfoDTO pdto = null;
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
				result.put("code", 10008);	result.put("status", "error");	result.put("message", WalletMgrApiApplication.g_respmsg);	
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
	@RequestMapping(value = "/v1/node/transfer", method = RequestMethod.POST) //2022.07.12 by jggu - 추가
	public String NodeTransferRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		//final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		DepositResponseDto resMsg = new DepositResponseDto();
		WalletMgrApiApplication.g_respmsg = "";
		FileLog.WriteLog("[" + commonUtil.getRemoteAddr(request) + "]/v1/node/transfer [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			//String apiaddr = validatemsg.split("-")[1];
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			WalletInfoDTO pdto = null;
			try {
				//
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = walletreq.TransferNodeWallet(params.get("symbol").toString(), params.get("froma").toString(), 
													params.get("toa").toString(), params.get("amount").toString());
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				JSONObject result = new JSONObject();
				result.put("code", 10008);	result.put("status", "error");	result.put("message", WalletMgrApiApplication.g_respmsg);	
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
}
