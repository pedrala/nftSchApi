package com.walletmgr.api.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class DepositResultDto {
    //@JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String symbol;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int from_usr_no;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int to_usr_no;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String req_code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String in_txid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String out_txid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String addr;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal fee;  
 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createtime;    
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fromaddr;    
     
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String toaddr;        
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String current;          
        
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String balance;       
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String t_amount;   
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String eth;   
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String usdte;   
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String usdtt;       
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int count;
}
