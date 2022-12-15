package com.walletmgr.api.dto;

public class NodeHistoryDTO {
	private int 	idx;			//Node의 index
	private String 	serial;			//Node의 고유 번호
	private String 	ipa;			//Node에 할당된 Ip address
	private String 	price;			//Node의 구매 금액
	private String 	maxp;			//Node 보상의 최대 크기 지급시 이 금액을 넘지 않도록 한다.
	private String 	reward;			//Node 보상의 최대 크기 지급시 이 금액을 넘지 않도록 한다.
	private int 	startday;		//Node가 가동되어 보상이 시작된일
	private int 	lastday;		//Node의 보상이 만료되는 마지막 지급일
	private String 	op_time;		//Node가 가동된 시간 정보
	private String 	addr;			//Node의 메인넷 주소
	private String 	c_tm;
	private String 	u_tm;
	private String 	d_tm;
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getIpa() {
		return ipa;
	}

	public void setIpa(String ipa) {
		this.ipa = ipa;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMaxp() {
		return maxp;
	}

	public void setMaxp(String maxp) {
		this.maxp = maxp;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getStartday() {
		return startday;
	}

	public void setStartday(int startday) {
		this.startday = startday;
	}

	public int getLastday() {
		return lastday;
	}

	public void setLastday(int lastday) {
		this.lastday = lastday;
	}

	public String getOp_time() {
		return op_time;
	}

	public void setOp_time(String op_time) {
		this.op_time = op_time;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getC_tm() {
		return c_tm;
	}

	public void setC_tm(String c_tm) {
		this.c_tm = c_tm;
	}

	public String getU_tm() {
		return u_tm;
	}

	public void setU_tm(String u_tm) {
		this.u_tm = u_tm;
	}

	public String getD_tm() {
		return d_tm;
	}

	public void setD_tm(String d_tm) {
		this.d_tm = d_tm;
	}
	@Override
	public String toString() {
		return "NodeHistoryDTO [idx=" + idx + ", serial=" + serial + ", ipa=" + ipa + ", price=" + price + ", maxp="
				+ maxp + ", reward=" + reward + ", startday=" + startday + ", lastday=" + lastday + ", op_time="
				+ op_time + ", addr=" + addr + ", c_tm=" + c_tm + ", u_tm=" + u_tm + ", d_tm=" + d_tm + "]";
	}

	public NodeHistoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
