package com.bp.entity;

import java.util.Date;

/**
 * Procedure model layer.
 */
public class Procedure {
	
	/**
	 * ID of procedure.
	 */
	private int procedureId;
	
	/**
	 * Type of procedure.
	 */
	private String procedureType;
	/**
	 * Result of procedure.
	 */
	private String result;
	/**
	 * Billing of procedure.
	 */
	private String billing;
	/**
	 * Datetime of procedure.
	 */
	private Date procedureTime;
	/**
	 * Visit of procedure.
	 */
	private int visitId;
	
	public int getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(final int procedure_id) {
		this.procedureId = procedure_id;
	}

	public String getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(final String procedure_type) {
		this.procedureType = procedure_type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(final String result) {
		this.result = result;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(final String billing) {
		this.billing = billing;
	}

	public Date getProcedureTime() {
		return procedureTime;
	}

	public void setProcedureTime(final Date procedureTime) {
		this.procedureTime = procedureTime;
	}

	public int getVisitId() {
		return visitId;
	}

	public void setVisitId(final int visitId) {
		this.visitId = visitId;
	}
}
