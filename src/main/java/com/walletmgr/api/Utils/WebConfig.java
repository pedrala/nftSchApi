package com.walletmgr.api.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	/*
     * 로그인 인증 Interceptor 설정
     */
    @Autowired
    AuthInterceptor authInterceptor;
    
    public static String uploadImagePath = "";
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/view/**")
                .excludePathPatterns("/loginForm").excludePathPatterns("/login")
                .excludePathPatterns("/signupForm").excludePathPatterns("/signup");
        /*
                .excludePathPatterns("/send/auth-code").excludePathPatterns("/check/auth-code").excludePathPatterns("/update/pwd")
                .excludePathPatterns("/css/**").excludePathPatterns("/demo/**").excludePathPatterns("/fonts/**")
                .excludePathPatterns("/images/**").excludePathPatterns("/js/**"); 
                */
    }
    
    // 서버 업로드 파일 경로 - application.properties 설정에서 가져오기
    public WebConfig(@Value("${custom.path.upload-images}") String uploadImagePath) {
    	this.uploadImagePath = uploadImagePath;
    	//System.out.println("uploadImagePath : " + uploadImagePath);
    }
}
