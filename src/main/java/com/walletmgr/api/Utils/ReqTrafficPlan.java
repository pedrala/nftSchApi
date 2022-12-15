package com.walletmgr.api.Utils;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

public enum ReqTrafficPlan {
    //1시간에 3번 사용 가능한 무제한 요금제
    FREE{
        public Bandwidth getLimit(){
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
        }
    },

    BASIC{
        public Bandwidth getLimit(){
            return Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)));
        }
    },

    PROFESSIONAL{
        public Bandwidth getLimit(){
            return Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        }
    };

    public abstract Bandwidth getLimit();

    public static ReqTrafficPlan resolvePlanFromApiKey(String apiKey){
        if(apiKey == null || apiKey.isEmpty()){
            return FREE;
        }else if(apiKey.startsWith("BA")){
            return BASIC;
        }else if(apiKey.startsWith("PX")){
            return PROFESSIONAL;
        }

        return FREE;
    }
}
