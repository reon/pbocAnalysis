package com.caafc.pbocAnalysis.entity;

// Generated 2016-11-15 16:17:32 by Hibernate Tools 3.4.0.CR1

/**
 * FRePlcreditscore generated by hbm2java
 */
public class FRePlcreditscore implements java.io.Serializable {

	private String reportNo;
	private String score;
	private String month;
	private String scorelevel;
	private String scoreelements;

	public FRePlcreditscore() {
	}

	public FRePlcreditscore(String reportNo) {
		this.reportNo = reportNo;
	}

	public FRePlcreditscore(String reportNo, String score, String month,
			String scorelevel, String scoreelements) {
		this.reportNo = reportNo;
		this.score = score;
		this.month = month;
		this.scorelevel = scorelevel;
		this.scoreelements = scoreelements;
	}

	public String getReportNo() {
		return this.reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getScorelevel() {
		return this.scorelevel;
	}

	public void setScorelevel(String scorelevel) {
		this.scorelevel = scorelevel;
	}

	public String getScoreelements() {
		return this.scoreelements;
	}

	public void setScoreelements(String scoreelements) {
		this.scoreelements = scoreelements;
	}

}
