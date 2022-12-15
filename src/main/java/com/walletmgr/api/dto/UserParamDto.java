package com.walletmgr.api.dto;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserParamDto {
    @NotNull
    private String symbol;
    
    @NotNull
    private String apiKey;

    @NotNull
    private String timestamp;

    @NotNull
    private String version;
    
    @NotNull
    private String signature;

    @Email
    @NotNull
    private String email;
  
}
