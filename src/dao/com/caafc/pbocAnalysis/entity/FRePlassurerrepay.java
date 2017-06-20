package com.caafc.pbocAnalysis.entity;

// Generated 2016-11-15 16:17:32 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * FRePlassurerrepay generated by hbm2java
 */
public class FRePlassurerrepay implements java.io.Serializable {

	private FRePlassurerrepayId id;
	private String organname;
	private String latestassurerrepaydate;
	private BigDecimal money;
	private String latestrepaydate;
	private BigDecimal balance;

	public FRePlassurerrepay() {
	}

	public FRePlassurerrepay(FRePlassurerrepayId id) {
		this.id = id;
	}

	public FRePlassurerrepay(FRePlassurerrepayId id, String organname,
			String latestassurerrepaydate, BigDecimal money,
			String latestrepaydate, BigDecimal balance) {
		this.id = id;
		this.organname = organname;
		this.latestassurerrepaydate = latestassurerrepaydate;
		this.money = money;
		this.latestrepaydate = latestrepaydate;
		this.balance = balance;
	}

	public FRePlassurerrepayId getId() {
		return this.id;
	}

	public void setId(FRePlassurerrepayId id) {
		this.id = id;
	}

	public String getOrganname() {
		return this.organname;
	}

	public void setOrganname(String organname) {
		this.organname = organname;
	}

	public String getLatestassurerrepaydate() {
		return this.latestassurerrepaydate;
	}

	public void setLatestassurerrepaydate(String latestassurerrepaydate) {
		this.latestassurerrepaydate = latestassurerrepaydate;
	}

	public BigDecimal getMoney() {
		return this.money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getLatestrepaydate() {
		return this.latestrepaydate;
	}

	public void setLatestrepaydate(String latestrepaydate) {
		this.latestrepaydate = latestrepaydate;
	}

	public BigDecimal getBalance() {
		return this.balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}