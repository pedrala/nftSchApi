package com.walletmgr.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiInfoDto {
    private String secret;

    private int wallet_no;

    private String symbol;

    private String addr;
}
