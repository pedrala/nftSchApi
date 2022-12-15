package com.walletmgr.api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.gson.Gson;
import com.walletmgr.api.Utils.ReqTraffic;
import com.walletmgr.api.dto.DepositResponseDto;
import com.walletmgr.api.dto.UserResponseDto;

//@EnableScheduling
@SpringBootApplication
public class WalletMgrApiApplication {
	public static DepositResponseDto g_resMsg = new DepositResponseDto();
	public static UserResponseDto g_userMsg = new UserResponseDto();
	public static ReqTraffic g_reqTraffic = new ReqTraffic();
	public static Gson g_gson = new Gson();
	public static String g_respmsg;
	public static void main(String[] args) {
		
		// JasyptConfig jasypt = new JasyptConfig();
		// StringEncryptor enc= jasypt.stringEncryptor();
		SpringApplication.run(WalletMgrApiApplication.class, args);
	}

}
