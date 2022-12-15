package com.walletmgr.api.dto;

public class WalletAddrDTO {
	private String eth;
	private String usdte;
	private String usdtt;
	public String getEth() {
		return eth;
	}
	public void setEth(String eth) {
		this.eth = eth;
	}
	public String getUsdte() {
		return usdte;
	}
	public void setUsdte(String usdte) {
		this.usdte = usdte;
	}
	public String getUsdtt() {
		return usdtt;
	}
	public void setUsdtt(String usdtt) {
		this.usdtt = usdtt;
	}
	@Override
	public String toString() {
		return "WalletAddrDTO [eth=" + eth + ", usdte=" + usdte + ", usdtt=" + usdtt + "]";
	}
	public WalletAddrDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
