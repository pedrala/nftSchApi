package com.walletmgr.api.dto;

public class TransactionDTO {
	private String 	txid;
	private String 	inout;
	private String 	c_tm;
	private String 	from;
	private String 	to;
	private String 	amount;
	private String 	fee;
	private String 	price;
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public String getInout() {
		return inout;
	}
	public void setInout(String inout) {
		this.inout = inout;
	}
	public String getC_tm() {
		return c_tm;
	}
	public void setC_tm(String c_tm) {
		this.c_tm = c_tm;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "TransactionDTO [txid=" + txid + ", inout=" + inout + ", c_tm=" + c_tm + ", from=" + from + ", to=" + to
				+ ", amount=" + amount + ", fee=" + fee + ", price=" + price + "]";
	}
	public TransactionDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
