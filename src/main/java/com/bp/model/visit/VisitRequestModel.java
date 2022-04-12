package com.bp.model.visit;

/**
 * Visit request model.
 */
public class VisitRequestModel {
	
	/**
	 * Identification of patient.
	 */
	private String identification;
	
	/**
	 * First name of patient
	 */
	private String firstName;
	
	/**
	 * Last name of patient.
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
	 * Email of patient.
	 */
	private String email;
	
	/**
	 * Mobile phone of patient.
	 */
	private String phone;
	
	/**
	 * City of patient
	 */
	private String city;
	
	/**
	 * Address of patient
	 */
	private String address;

	//private Date visit_time;
	
	private int patientId;
	
	public String getIdentification() {
		return identification;
	}
	
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getSymptoms() {
		return symptoms;
	}
	
	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
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
	
	/*public Date getVisitTime() {
		return visit_time;
	}*/
	
	/*public void setVisitTime(Date visit_time) {
		this.visit_time = visit_time;
	}*/
	
	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
}
