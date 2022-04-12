package com.bp.model.patient;

import java.io.Serializable;
import java.util.Date;

/**
 * Patient request model.
 */
public class PatientRequestModel implements Serializable {
	
	private static final long serialVersionUID = 9165987479093891215L;
	
	/**
	 * Patient identifier.
	 */
	private long identification;
	
	/**
	 * First name of patient.
	 */
	private String firstName;
	
	/**
	 * Last name of patient.
	 */
	private String lastName;
	
	/**
	 * Birthdate of patient.
	 */
	private Date birthdate;
	
	/**
	 * Phone number of patient.
	 */
	private String phone;
	
	/**
	 * Email address of patient.
	 */
	private String email;
	
	/**
	 * City of patient.
	 */
	private String city;
	
	/**
	 * Address of patient.
	 */
	private String address;
	
	public long getIdentification() {
		return identification;
	}
	
	public void setIdentification(long identification) {
		this.identification = identification;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(final String first_name) {
		this.firstName = first_name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(final String last_name) {
		this.lastName = last_name;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(final Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(final String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(final String email) {
		this.email = email;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(final String city) {
		this.city = city;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(final String address) {
		this.address = address;
	}
}
