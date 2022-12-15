package com.walletmgr.api.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class UserWalletParamDto {
    @NotNull
    private String symbol;
    
    @NotNull
    private String apiKey;

    private String timestamp;
 
    private String version;
    
    private String signature;
    
    private String email;

    private String status;
    
    private String addr;

    private String nodesn;

    private String price;

    private String maxp;
    
    private String	startday;
    
    private String	lastday;
    
    private String	froma;
    
    private String	toa;
    
    private String	amount;
}
