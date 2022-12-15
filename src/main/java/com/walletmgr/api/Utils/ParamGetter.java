package com.walletmgr.api.Utils;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * HashMap형태로 넘어오는 파라미터 처리 클래스
 * 
 * @author ParkSungBin
 *
 */
public class ParamGetter {

	/**
	 * HashMap형태로 넘어오는 파라미터 설정
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> pGetter(HttpServletRequest req) throws Exception {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if (req != null) {
			Enumeration e = req.getParameterNames();
			String tmp = "";
			while (e.hasMoreElements()) {
				tmp = (String) e.nextElement();
				hm.put(tmp.toUpperCase(), req.getParameter(tmp));
			}
		}
		return hm;
	}
}