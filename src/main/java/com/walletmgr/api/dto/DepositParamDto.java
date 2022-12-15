package com.walletmgr.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class DepositParamDto {
    @NotNull
    private String symbol;
    
    @NotNull
    private String apiKey;

    private String timestamp;
 
    private String version;
    
    private String signature;

    //@NotNull
    //@DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;

    //@NotNull
    private String addr;

    private Integer from_usr_no;

    private String from_addr;
    
    private String	txid;
    
    private String	email;    
}
