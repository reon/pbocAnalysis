package com.caafc.pbocAnalysis.entity;

// Generated 2016-11-16 15:40:46 by Hibernate Tools 3.4.0.CR1

/**
 * FRePllatest24monthpaystateId generated by hbm2java
 */
public class FRePllatest24monthpaystateId implements java.io.Serializable {

	private String reportNo;
	private int uplevel;
	private int idenNo;

	public FRePllatest24monthpaystateId() {
	}

	public FRePllatest24monthpaystateId(String reportNo, int uplevel, int idenNo) {
		this.reportNo = reportNo;
		this.uplevel = uplevel;
		this.idenNo = idenNo;
	}

	public String getReportNo() {
		return this.reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public int getUplevel() {
		return this.uplevel;
	}

	public void setUplevel(int uplevel) {
		this.uplevel = uplevel;
	}

	public int getIdenNo() {
		return this.idenNo;
	}

	public void setIdenNo(int idenNo) {
		this.idenNo = idenNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FRePllatest24monthpaystateId))
			return false;
		FRePllatest24monthpaystateId castOther = (FRePllatest24monthpaystateId) other;

		return ((this.getReportNo() == castOther.getReportNo()) || (this
				.getReportNo() != null && castOther.getReportNo() != null && this
				.getReportNo().equals(castOther.getReportNo())))
				&& (this.getUplevel() == castOther.getUplevel())
				&& (this.getIdenNo() == castOther.getIdenNo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getReportNo() == null ? 0 : this.getReportNo().hashCode());
		result = 37 * result + this.getUplevel();
		result = 37 * result + this.getIdenNo();
		return result;
	}

}
