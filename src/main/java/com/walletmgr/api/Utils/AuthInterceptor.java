package com.walletmgr.api.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthInterceptor implements HandlerInterceptor{
	
	@Autowired
	private CommonUtil commonUtil;
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	 	//AdminDto admin = new AdminDto();
	 	HttpSession session = request.getSession();
	 	
	 	System.out.println("접속 IP : " + request.getRemoteAddr() + " - " + session.toString());
        
        if(request.getRemoteAddr().equals("220.86.111.196")){
            return true;
        }

        if(request.getRemoteHost().equals("13.229.98.86")){
            return true;
        }
        
        if(request.getRemoteHost().equals("147.46.239.39")){
            return true;
        }
        
        if(request.getRemoteHost().equals("10.10.11.42")){
            return true;
        }
        
        if(request.getRemoteHost().equals("127.0.0.1")){
            return true;
        }
        
        if(request.getRemoteHost().equals("203.238.181.162")){
            return true;
        }
        
        if(request.getRemoteHost().equals("220.86.113.214")){
            return true;
        }    
        
        if(request.getRemoteHost().equals("203.238.181.164")){
            return true;
        }   
        
        if(request.getRemoteHost().equals("195.123.222.16")){
            return true;
        }
        
        // Ian Shin
        if(request.getRemoteHost().equals("10.10.11.219")){
            return true;
        }
        
        // FINL Admin
        if(request.getRemoteHost().equals("218.38.12.134")){
            return true;
        }
        
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED); 
        FileLog.WriteLog(methodName + " - validation check fail!!!");
        return false;     		
	 	/*	 	
	 	//세션이 있는지 확인
		if (session == null) {
			System.out.println("session is null");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			//response.sendRedirect("/loginForm");
			return false;
		}

		FileLog.WriteLog(methodName + " session : " + session.getId());
		//세션에 사용자 정보가 있는지 확인한다.
		if (session.getAttribute("loginInfo") == null) {
			
			if(request.getHeader("AJAX") == null) { //로그인으로 이동
				System.out.println("##### NOT AJAX #####");
				session.removeAttribute("loginInfo");
				response.sendRedirect("/loginForm"); 
				return false; //접속한 컨트롤러의 실행을 중지하고 새로 접속하는 곳을 지정한다. 
			} else if(request.getHeader("AJAX").equals("true")){ //2. ajax로 사용시 확인
				System.out.println("##### AJAX #####");
				session.removeAttribute("loginInfo");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED); 
				return false; 
			}

		}
	 
        String id = (String) session.getAttribute("loginInfo");
		
		if (id == null) { 
			System.out.println("admin info is null");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			//response.sendRedirect("/loginForm");
			return false;
		}
		*/
		//System.out.println("validation check success!!!");
        //return true;
    }
	 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }
 
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
    
    private boolean isAjaxRequest(HttpServletRequest req) {
        String header = req.getHeader("AJAX");
        if ("true".equals(header)){
         return true;
        }else{
         return false;
        }
    }

    
}
