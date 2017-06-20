package com.caafc.pbocAnalysis.entity;

// Generated 2016-11-15 16:17:32 by Hibernate Tools 3.4.0.CR1

/**
 * FRePladminaward generated by hbm2java
 */
public class FRePladminaward implements java.io.Serializable {

	private FRePladminawardId id;
	private String organname;
	private String content;
	private String begindate;
	private String enddate;

	public FRePladminaward() {
	}

	public FRePladminaward(FRePladminawardId id) {
		this.id = id;
	}

	public FRePladminaward(FRePladminawardId id, String organname,
			String content, String begindate, String enddate) {
		this.id = id;
		this.organname = organname;
		this.content = content;
		this.begindate = begindate;
		this.enddate = enddate;
	}

	public FRePladminawardId getId() {
		return this.id;
	}

	public void setId(FRePladminawardId id) {
		this.id = id;
	}

	public String getOrganname() {
		return this.organname;
	}

	public void setOrganname(String organname) {
		this.organname = organname;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBegindate() {
		return this.begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return this.enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

}