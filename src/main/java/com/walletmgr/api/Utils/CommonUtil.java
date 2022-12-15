package com.walletmgr.api.Utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.walletmgr.api.dao.RestfulDao;
import com.walletmgr.api.dto.ApiInfoDto;
import com.walletmgr.api.dto.JsonResponseDto;

@Component
public class CommonUtil {

    @Resource
    RestfulDao restfulDao;

    @Resource
    EncryptDecryptUtll encryptDecryptUtll;

    int UtmCnt = 0; // 고유 req_code 생성을 위한 숫자

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

    /**
     * 함수 : Rest api 요청 시간 체크 
     * 날짜 : 2022-05-19
     */

    public boolean CheckReqTime(long req_time){
        boolean ret = false;
        
        long server_time = System.currentTimeMillis();
        if (server_time > req_time - 30000 * 2 && server_time < req_time + 30000 * 2) {
            ret = true;
        }

        return ret;
    }

    /**
     * 함수 : 고유 req_code 생성
     * 날짜 : 2022-05-12
     */
    public String makeReqCodeNum() {

        String code = Long.toString(System.currentTimeMillis());
        String req_code = code + "." + String.format("%04d", UtmCnt);

        UtmCnt++;

        if (UtmCnt > 0 && UtmCnt % 10000 == 0) {
            UtmCnt = 0;
        }

        return req_code;

    }

    /**
     * 함수 : txid 생성
     * 날짜 : 2022-05-12
     */
    public String makeTxid(int txid_no, String symbol) {

        /**
         * txid 생성 규칙 : 심볼 TXID + 구분코드 + 랜덤16자리(영대소문자+숫자) + microtime
         * 
         */

        // 1. 심볼 TXID 생성
        String symbol_ori = Base64.getEncoder().encodeToString(symbol.getBytes());
        String symbol_txid = symbol_ori.substring(0, 4);

        // 2. 구분코드 생성 : txid_type + 입출금종류
        HashMap<String, Object> txid_info = restfulDao.selectTxid(txid_no);
        // String separator = txid_info.get("txid_type").toString() +
        // txid_info.get("inout").toString();
        String separator = txid_info.get("type").toString();

        // 3. 랜덤 16자리 생성
        String random16 = generateRandom(16);

        // 4. microtime
        long microtime = System.currentTimeMillis();

        String txid = symbol_txid + separator + random16 + microtime;

        return txid;
    }

    /**
     * 함수 : 랜덤 수 생성
     * 날짜 : 2022-05-12
     */
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
    
    public String getRemoteAddr(HttpServletRequest request){
        return request.getRemoteAddr();
    }  
    
	public String GetCurrentTime2String(String formattype) {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(formattype);
		return formatter.format(now.getTime());
	}    
	
	public HashMap keyChangeUpperMap(Map param) {
		Iterator<String> iteratorKey = param.keySet().iterator(); // 키값 오름차순
		HashMap newMap = new HashMap();
		// //키값 내림차순 정렬
		while (iteratorKey.hasNext()) {
			String key = iteratorKey.next();
			newMap.put(key.toUpperCase(), param.get(key));
		}
		return newMap;
	}
		
	public String CheckSignatureValidate(HashMap<String, Object> params) {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String msg = null;
		JsonResponseDto resMsg = new JsonResponseDto();
		try {
			FileLog.WriteLog(methodName + "-] " + params.toString());
			// API키 조회
			if(params.get("apiKey") == null) {
				resMsg = JsonResponseDto.builder().code(10002).status("error").message("Invalid apiKey").build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			}
			if(params.get("symbol") == null) {
				resMsg = JsonResponseDto.builder().code(10002).status("error").message("Invalid symbol").build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			}
			
			ApiInfoDto api_info = restfulDao.selectApiInfo(params.get("apiKey").toString());

			if (!api_info.getSymbol().equals(params.get("symbol").toString())) {
				//node, user 2개의 심볼까지만 지원
				if(params.get("symbol").toString().equals("node") == true || params.get("symbol").toString().equals("user") == true) {
					
				} else {
					resMsg = JsonResponseDto.builder().code(10002).status("error").message("Invalid symbol").build();
					return ResponseEntity.badRequest().body(resMsg).toString();
				}
			}

			// Signature 생성
			String plain = "apiKey=" + params.get("apiKey") + "&timestamp=" + params.get("timestamp") + "&version="
					+ params.get("version");
			HmacEncrypt hmac = new HmacEncrypt();
			String str_enc = hmac.hget(plain, api_info.getSecret());
			if (!params.get("signature").equals(str_enc)) {
				resMsg = JsonResponseDto.builder().code(10003).status("error")
						.message("Signature for this request is not valid.").build();
				return ResponseEntity.badRequest().body(resMsg).toString();
			}	
			
			FileLog.WriteLog(methodName + "-] " + "signature check ok : " + str_enc);
			
			msg = "validate" + "-" + api_info.getAddr();
		} catch (Exception e) {
			FileLog.WriteLog(e.getMessage());
			resMsg = JsonResponseDto.builder().code(10007).status("error").message("Internal server error.").build();
			return ResponseEntity.internalServerError().body(resMsg).toString();
		}
		return msg;
	}
	
	public HashMap<String,Object> GetParameterMap(HashMap<String,Object> prevMap) {
		HashMap<String,Object> allMap = new HashMap<String, Object>();
	    for(String key : prevMap.keySet()){
	    	Object obj = prevMap.get(key);

	        if(isNumeric(obj.toString()) == false) {
	        	allMap.put(key, obj.toString().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("=", "&#x3D;"));
	        } else {
	        	allMap.put(key, obj);
	        }
	    }
		return allMap;
	}	
}
