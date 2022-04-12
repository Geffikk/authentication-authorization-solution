package com.bp.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Verification token for email verification (in registration).
 */
public class VerificationToken {
	
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_VERIFIED = "VERIFIED";
	
	/**
	 * Auto generated id of verification token.
	 */
	private int verificationId;
	
	/**
	 * User email.
	 */
	private String email;
	
	/**
	 * Verification token body.
	 */
	private String token;
	
	/**
	 * Status of token (pending/verified).
	 */
	private String status;
	
	/**
	 * Datetime of expiration.
	 */
	private LocalDateTime expiredDateTime;
	
	/**
	 * Datetime of issued.
	 */
	private LocalDateTime issuedDateTime;
	
	/**
	 * Datetime of confirmation.
	 */
	private LocalDateTime confirmedDateTime;
	
	/**
	 * User identifier.
	 */
	private int userId;
	
	public VerificationToken() {
		this.token = UUID.randomUUID().toString();
		this.issuedDateTime = LocalDateTime.now();
		this.expiredDateTime = this.issuedDateTime.plusDays(1);
		this.status = STATUS_PENDING;
	}
	
	public int getVerificationId() {
		return verificationId;
	}
	
	public void setVerificationId(int verification_id) {
		this.verificationId = verification_id;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public LocalDateTime getExpiredDateTime() {
		return expiredDateTime;
	}
	
	public void setExpiredDateTime(LocalDateTime expiredDateTime) {
		this.expiredDateTime = expiredDateTime;
	}
	
	public LocalDateTime getIssuedDateTime() {
		return issuedDateTime;
	}
	
	public void setIssuedDateTime(LocalDateTime issuedDateTime) {
		this.issuedDateTime = issuedDateTime;
	}
	
	public LocalDateTime getConfirmedDateTime() {
		return confirmedDateTime;
	}
	
	public void setConfirmedDateTime(LocalDateTime confirmedDateTime) {
		this.confirmedDateTime = confirmedDateTime;
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
