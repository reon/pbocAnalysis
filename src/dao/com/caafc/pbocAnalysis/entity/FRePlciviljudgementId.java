package com.caafc.pbocAnalysis.entity;

// Generated 2016-11-15 16:17:32 by Hibernate Tools 3.4.0.CR1

/**
 * FRePlciviljudgementId generated by hbm2java
 */
public class FRePlciviljudgementId implements java.io.Serializable {

	private String reportNo;
	private int serialNo;

	public FRePlciviljudgementId() {
	}

	public FRePlciviljudgementId(String reportNo, int serialNo) {
		this.reportNo = reportNo;
		this.serialNo = serialNo;
	}

	public String getReportNo() {
		return this.reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public int getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FRePlciviljudgementId))
			return false;
		FRePlciviljudgementId castOther = (FRePlciviljudgementId) other;

		return ((this.getReportNo() == castOther.getReportNo()) || (this
				.getReportNo() != null && castOther.getReportNo() != null && this
				.getReportNo().equals(castOther.getReportNo())))
				&& (this.getSerialNo() == castOther.getSerialNo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getReportNo() == null ? 0 : this.getReportNo().hashCode());
		result = 37 * result + this.getSerialNo();
		return result;
	}

}
