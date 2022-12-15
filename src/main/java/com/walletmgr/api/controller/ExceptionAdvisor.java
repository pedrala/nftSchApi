package com.walletmgr.api.controller;

import com.walletmgr.api.WalletMgrApiApplication;
import com.walletmgr.api.dto.DepositResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice   // 전역 설정을 위한 annotaion
@RestController

public class ExceptionAdvisor {

    // private MessageSource messageSource;

    // @Autowired
    // public ExceptionAdvisor(MessageSource messageSource){
    //     this.messageSource = messageSource;
    // }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DepositResponseDto> processValidationError(MethodArgumentNotValidException exception) {
        DepositResponseDto resMsg =WalletMgrApiApplication.g_resMsg;
        try {
            String msg  = "Invalid data sent for a parameter.";
            resMsg = DepositResponseDto.builder()
                                .code(10006)
                                .status("error")
                                .message(msg).build();
                                
            return ResponseEntity.badRequest().body(resMsg);
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
            resMsg = DepositResponseDto.builder()
                            .code(10007)
                            .status("error")
                            .message("Internal server error.").build();
            return ResponseEntity.internalServerError().body(resMsg);
        }
    }

}
