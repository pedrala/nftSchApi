package com.walletmgr.api.dto;

public class ExchangeDTO {
	private String 	symbol;
	private String 	current;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	@Override
	public String toString() {
		return "ExchangeDTO [symbol=" + symbol + ", current=" + current + "]";
	}
	public ExchangeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
