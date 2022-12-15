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
import com.walletmgr.api.Utils.CommonUtil;
import com.walletmgr.api.Utils.FileLog;
import com.walletmgr.api.dao.RestfulDao;
import com.walletmgr.api.dto.JsonResponseDto;
import com.walletmgr.api.service.BuyHistoryService;
import com.walletmgr.api.service.NodeHistoryService;
import com.walletmgr.api.service.UnlockHistoryService;

@RestController
public class HistoryViewController {
	
	@Resource
	RestfulDao restfulDao;

	@Resource
	CommonUtil commonUtil;
	
	@Resource
	BuyHistoryService buyservice;	
	
	@Resource
	NodeHistoryService nodeservice;	
	
	@Resource
	UnlockHistoryService unlockservice;	
		
	@RequestMapping(value = "/view/buyhist/select", method = RequestMethod.POST) 
	public String WvSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/v1/tgp/wallet [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			//String apiaddr = validatemsg.split("-")[1];
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			JSONObject pdto = null;
			try {
				//Parameter의 Key값들의 대문자화가 이루어져야 한다. (Mapper에서의 구분 문제)
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = buyservice.SelectOne(commonUtil.keyChangeUpperMap(params));
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				resMsg = JsonResponseDto.builder().code(10008).status("error").message("select address error")
						.build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
			
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			resMsg = JsonResponseDto.builder().code(10007).status("error").message("Internal server error.").build();
			return ResponseEntity.internalServerError().body(resMsg).toString();
		}
	}
	
	@RequestMapping(value = "/view/buyhist/selectlist", method = RequestMethod.POST) 
	public String WvSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/v1/tgp/wallet [ " + params.toString() + " ]");
		try {
			FileLog.WriteLog(methodName + "-] " + params.toString());
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			JSONObject pdto = null;
			try {
				//Parameter의 Key값들의 대문자화가 이루어져야 한다. (Mapper에서의 구분 문제)
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = buyservice.SelectRequest(commonUtil.keyChangeUpperMap(params));
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				resMsg = JsonResponseDto.builder().code(10008).status("error").message("select address error")
						.build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
			
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			resMsg = JsonResponseDto.builder().code(10007).status("error").message("Internal server error.").build();
			return ResponseEntity.internalServerError().body(resMsg).toString();
		}
	}	
	
	@RequestMapping(value = "/view/nodehist/select", method = RequestMethod.POST) 
	public String TrxInSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/v1/tgp/wallet [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			//String apiaddr = validatemsg.split("-")[1];
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			JSONObject pdto = null;
			try {
				//Parameter의 Key값들의 대문자화가 이루어져야 한다. (Mapper에서의 구분 문제)
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = nodeservice.SelectOne(commonUtil.keyChangeUpperMap(params));
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				resMsg = JsonResponseDto.builder().code(10008).status("error").message("select address error")
						.build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
			
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			resMsg = JsonResponseDto.builder().code(10007).status("error").message("Internal server error.").build();
			return ResponseEntity.internalServerError().body(resMsg).toString();
		}
	}
	
	@RequestMapping(value = "/view/nodehist/selectlist", method = RequestMethod.POST) 
	public String TrxInSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/v1/tgp/wallet [ " + params.toString() + " ]");
		try {
			FileLog.WriteLog(methodName + "-] " + params.toString());
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			JSONObject pdto = null;
			try {
				//Parameter의 Key값들의 대문자화가 이루어져야 한다. (Mapper에서의 구분 문제)
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = nodeservice.SelectRequest(commonUtil.keyChangeUpperMap(params));
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				resMsg = JsonResponseDto.builder().code(10008).status("error").message("select address error")
						.build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
			
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			resMsg = JsonResponseDto.builder().code(10007).status("error").message("Internal server error.").build();
			return ResponseEntity.internalServerError().body(resMsg).toString();
		}
	}		
	
	@RequestMapping(value = "/view/unlockhist/select", method = RequestMethod.POST) 
	public String TrxOutSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/v1/tgp/wallet [ " + params.toString() + " ]");
		try {
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			//String apiaddr = validatemsg.split("-")[1];
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			JSONObject pdto = null;
			try {
				//Parameter의 Key값들의 대문자화가 이루어져야 한다. (Mapper에서의 구분 문제)
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = unlockservice.SelectOne(commonUtil.keyChangeUpperMap(params));
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				resMsg = JsonResponseDto.builder().code(10008).status("error").message("select address error")
						.build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
			
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			resMsg = JsonResponseDto.builder().code(10007).status("error").message("Internal server error.").build();
			return ResponseEntity.internalServerError().body(resMsg).toString();
		}
	}
	
	@RequestMapping(value = "/view/unlockhist/selectlist", method = RequestMethod.POST) 
	public String TrxOutSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/v1/tgp/wallet [ " + params.toString() + " ]");
		try {
			FileLog.WriteLog(methodName + "-] " + params.toString());
			String validatemsg = commonUtil.CheckSignatureValidate(params);
			if(validatemsg.split("-")[0].equals("validate") != true) return validatemsg;
			
			Gson gson = new Gson();
			String jsonString = "";
			JSONObject pdto = null;
			try {
				//Parameter의 Key값들의 대문자화가 이루어져야 한다. (Mapper에서의 구분 문제)
				//HashMap<String, Object> pparams = commonUtil.GetParameterMap(params);
				pdto = unlockservice.SelectRequest(commonUtil.keyChangeUpperMap(params));
				jsonString = gson.toJson(pdto);
			} catch(Exception e) {
				pdto = null;
			}

			if (pdto == null) {
				resMsg = JsonResponseDto.builder().code(10008).status("error").message("select address error")
						.build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			} else {
				return "{\"status\":\"true\",\"message\":\"Success\",\"data\":" + jsonString + "}";
			}
			
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			resMsg = JsonResponseDto.builder().code(10007).status("error").message("Internal server error.").build();
			return ResponseEntity.internalServerError().body(resMsg).toString();
		}
	}			
}
