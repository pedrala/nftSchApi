package com.walletmgr.api.dto;

import org.springframework.http.HttpStatus;

public class ExceptionInfo {
    private HttpStatus code;
    private String cause;
    private String url;
	public HttpStatus getCode() {
		return code;
	}
	public void setCode(HttpStatus code) {
		this.code = code;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ExceptionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ExceptionInfo [code=" + code + ", cause=" + cause + ", url=" + url + "]";
	}
    
}
