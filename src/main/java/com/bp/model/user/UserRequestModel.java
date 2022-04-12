package com.bp.model.user;

import java.io.Serializable;
import java.util.Date;

/**
 * User request model.
 */
public class UserRequestModel implements Serializable {
	
	private static final long serialVersionUID = 7892752589110371167L;
	
	/**
	 * Email of user.
	 */
	private String email;
	
	/**
	 * Password of user.
	 */
	private String password;
	
	/**
	 * First name of user.
	 */
	private String firstName;
	
	/**
	 * Last name of user.
	 */
	private String lastName;
	
	/**
	 * Phone number of user.
	 */
	private String phone;
	
	/**
	 * Birthdate of user.
	 */
	private Date birthdate;
	
	/**
	 * Otp code for authentication.
	 */
	private String otp;

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String first_name) {
		this.firstName = first_name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String last_name) {
		this.lastName = last_name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getOtp() {
		return otp;
	}
	
	public void setOtp(String otp) {
		this.otp = otp;
	}
}
