package com.walletmgr.api.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.walletmgr.api.WalletMgrApiApplication;
import com.walletmgr.api.dto.DepositResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PrivateInterceptor implements HandlerInterceptor{
    
    @Override 
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("접속 IP : " + request.getRemoteAddr());
        
        if(request.getRemoteAddr().equals("220.86.111.196")){
            return true;
        }

        if(request.getRemoteHost().equals("13.229.98.86")){
            return true;
        }
        
        if(request.getRemoteHost().equals("147.46.239.39")){
            return true;
        }

        if(request.getRemoteHost().equals("127.0.0.1")){
            return true;
        }

        // 허용 ip 추가 2022.07.11 by hj.Yoon
        if(request.getRemoteHost().equals("54.164.161.105")){
            return true;
        }      

        if(request.getRemoteHost().equals("44.203.203.254")){
            return true;
        }
      
      
        DepositResponseDto resMsg = WalletMgrApiApplication.g_resMsg;
        resMsg = DepositResponseDto.builder()
                        .code(10009)
                        .status("error")
                        .message("IP Error").build();
        response.setContentType("application/json");
        try {
            response.getWriter().println(WalletMgrApiApplication.g_gson.toJson(resMsg));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return false;
    }
}
