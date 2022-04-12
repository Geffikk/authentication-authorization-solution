package com.bp.model.user;

import java.io.Serializable;

/**
 * User response model.
 */
public class UserResponseModel implements Serializable {
	
	private static final long serialVersionUID = -8091879091924046844L;
	
	/**
	 * Email of user.
	 */
	private String email;
	
	/**
	 * Time of logged in.
	 */
	private String loginTime;
	
	/**
	 * Time of account creation.
	 */
	private String creationTime;
	
	/**
	 * Email activation code.
	 */
	private String emailActivationUrl;
	
	/**
	 * JWT token body.
	 */
	private final String jwtToken;
	
	public UserResponseModel(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	public String getJwtToken() {
		return jwtToken;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getLoginTime() {
		return loginTime;
	}
	
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	
	public String getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	
	public String getEmailActivationUrl() {
		return emailActivationUrl;
	}
	
	public void setEmailActivationUrl(String emailActivationUrl) {
		this.emailActivationUrl = emailActivationUrl;
	}
}
