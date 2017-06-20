package com.caafc.pbocAnalysis.entity;

// Generated 2016-11-15 16:17:32 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * FRePlguaranteesummary generated by hbm2java
 */
public class FRePlguaranteesummary implements java.io.Serializable {

	private String reportNo;
	private Long count;
	private BigDecimal amount;
	private BigDecimal balance;

	public FRePlguaranteesummary() {
	}

	public FRePlguaranteesummary(String reportNo) {
		this.reportNo = reportNo;
	}

	public FRePlguaranteesummary(String reportNo, Long count,
			BigDecimal amount, BigDecimal balance) {
		this.reportNo = reportNo;
		this.count = count;
		this.amount = amount;
		this.balance = balance;
	}

	public String getReportNo() {
		return this.reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public Long getCount() {
		return this.count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return this.balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
