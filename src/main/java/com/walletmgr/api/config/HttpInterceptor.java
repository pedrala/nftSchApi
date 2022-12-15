package com.walletmgr.api.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.walletmgr.api.WalletMgrApiApplication;
import com.walletmgr.api.Utils.ReqTraffic;
import com.walletmgr.api.dto.DepositResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@Component
public class HttpInterceptor implements HandlerInterceptor {
    
    @Override 
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Bucket bucket = WalletMgrApiApplication.g_reqTraffic.resolveBucket(request);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        long saveToken = probe.getRemainingTokens();
        response.setHeader("Remaining-Token", String.format("%d", saveToken));
        if(probe.isConsumed()){
            //System.out.println("Success");
            //System.out.println("Available Token : " + saveToken);
            return true;
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

        try {
            Gson gson = new Gson();
            DepositResponseDto resMsg = WalletMgrApiApplication.g_resMsg;
            resMsg = DepositResponseDto.builder()
                            .code(10005)
                            .status("error")
                            .message("Too many requests queued. Try Again later " + waitForRefill + " second.").build();
            response.setContentType("application/json");
            response.getWriter().println(gson.toJson(resMsg));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return false;   

        // if(bucket.tryConsume(1)){
        //     return true;
        // }else{
        //     try {
        //         Gson gson = new Gson();
        //         ResMsg resMsg = BitstoaApiApplication.g_resMsg;
        //         resMsg = ResMsg.builder()
        //                         .code(10005)
        //                         .status("error")
        //                         .message("Too many requests queued.").build();
        //         response.setContentType("application/json");
        //         response.getWriter().println(gson.toJson(resMsg));
        //     } catch (IOException e) {
        //         // TODO Auto-generated catch block
        //         System.out.println(e.getMessage());
        //     }
        //     response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        //     return false;   
        // }
    }
}
