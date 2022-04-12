package com.bp.model.procedure;

import java.io.Serializable;
import java.util.Date;

/**
 * Procedure request model.
 */
public class ProcedureRequestModel implements Serializable {
	
	private static final long serialVersionUID = -6743283616856830006L;
	/**
	 * Type of procedure.
	 */
	private String procedureType;
	
	/**
	 * Result of procedure.
	 */
	private String result;
	
	/**
	 *  Billing of procedure.
	 */
	private String billing;
	
	/**
	 * Datetime of procedure.
	 */
	private Date procedureTime;
	
	/**
	 * Visit ID of procedure.
	 */
	private int visitId;
	
	public String getProcedureType() {
		return procedureType;
	}
	
	public void setProcedureType(final String procedureType) {
		this.procedureType = procedureType;
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
	
	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}
}
