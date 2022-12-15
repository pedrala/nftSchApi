package com.walletmgr.api.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.walletmgr.api.service.RewardUsrService;
import com.walletmgr.api.service.WalletInService;
import com.walletmgr.api.service.WalletListService;
import com.walletmgr.api.service.WalletOutService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WalletViewController {
	
	@Resource
	RestfulDao restfulDao;

	@Resource
	CommonUtil commonUtil;
	
	@Resource
	WalletListService walletreq;	
	
	@Resource
	WalletInService winreq;	
	
	@Resource
	WalletOutService woutreq;	
	
	@Resource
	RewardUsrService rusrreq;
		
	@RequestMapping(value = "/view/wallet/select", method = RequestMethod.POST) 
	public String WvSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/wallet/select [ " + params.toString() + " ]");
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
				pdto = walletreq.SelectOne(commonUtil.keyChangeUpperMap(params));
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
	
	@RequestMapping(value = "/view/wallet/selectlist", method = RequestMethod.POST) 
	public String WvSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/wallet/selectlist [ " + params.toString() + " ]");
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
				pdto = walletreq.SelectRequest(commonUtil.keyChangeUpperMap(params));
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
	
	@RequestMapping(value = "/view/trxin/select", method = RequestMethod.POST) 
	public String TrxInSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/trxin/select [ " + params.toString() + " ]");
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
				pdto = winreq.SelectOne(commonUtil.keyChangeUpperMap(params));
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
	
	@RequestMapping(value = "/view/trxin/selectlist", method = RequestMethod.POST) 
	public String TrxInSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/trxin/selectlist [ " + params.toString() + " ]");
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
				pdto = winreq.SelectRequest(commonUtil.keyChangeUpperMap(params));
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
	
	@RequestMapping(value = "/view/trxout/select", method = RequestMethod.POST) 
	public String TrxOutSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/trxout/select [ " + params.toString() + " ]");
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
				pdto = woutreq.SelectOne(commonUtil.keyChangeUpperMap(params));
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
	
	@RequestMapping(value = "/view/trxout/selectlist", method = RequestMethod.POST) 
	public String TrxOutSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/trxout/selectlist [ " + params.toString() + " ]");
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
				pdto = woutreq.SelectRequest(commonUtil.keyChangeUpperMap(params));
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
	
	@RequestMapping(value = "/view/rewardusr/select", method = RequestMethod.POST) 
	public String RewardUsrSelectRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/trxout/select [ " + params.toString() + " ]");
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
				pdto = rusrreq.SelectOne(commonUtil.keyChangeUpperMap(params));
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
	
	@RequestMapping(value = "/view/rewardusr/selectlist", method = RequestMethod.POST) 
	public String RewardUsrSelectListRequest(HttpServletRequest request, @RequestBody HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
		JsonResponseDto resMsg = new JsonResponseDto();
		FileLog.WriteLog(methodName + "-] " + "[" + commonUtil.getRemoteAddr(request) + "]/view/trxout/selectlist [ " + params.toString() + " ]");
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
				pdto = rusrreq.SelectRequest(commonUtil.keyChangeUpperMap(params));
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
