package com.bp.entity;

import java.util.Date;


/**
 * Visit model layer.
 */
public class Visit {
	
	/**
	 * ID of procedure.
	 */
	private int visitId;
	
	/**
	 * Type of procedure.
	 */
	private long identification;
	
	/**
	 * First name of visitor.
	 */
	private String firstName;
	
	/**
	 * Last name of visitor.
	 */
	private String lastName;
	
	/**
	 * Reason of visit.
	 */
	private String reason;
	
	/**
	 * Symptoms of patient.
	 */
	private String symptoms;
	
	/**
	 * Mail of patient.
	 */
	private String email;
	
	/**
	 * Phone of patient.
	 */
	private String phone;
	
	/**
	 * City of patient.
 	 */
	private String city;
	
	/**
	 * Address of patient.
	 */
	private String address;
	
	/**
	 * Birthdate of patient.
	 */
	private Date birthdate;
	
	/**
	 * Datetime of visit.
	 */
	private Date visitTime;
	
	/**
	 * Patient of visit.
	 */
	private int patientId;
	
	/**
	 * User id of visit.
 	 */
	private int userId;
	
	public int getVisitId() {
		return visitId;
	}

	public void setVisitId(final int visitId) {
		this.visitId = visitId;
	}

	public long getIdentification() {
		return identification;
	}

	public void setIdentification(final long identification) {
		this.identification = identification;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(final String symptoms) {
		this.symptoms = symptoms;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(final Date datetime) {
		this.visitTime = datetime;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(final int patient_id) {
		this.patientId = patient_id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(final int userId) {
		this.userId = userId;
	}
}
