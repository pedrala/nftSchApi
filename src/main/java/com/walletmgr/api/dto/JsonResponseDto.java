package com.walletmgr.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  //null json 보내지 않음
public class JsonResponseDto {
    private Integer code; 
    private String status; 
    private String message; 
    private Integer count; 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String data;
}
