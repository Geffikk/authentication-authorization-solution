package com.bp.service;

import org.springframework.http.ResponseEntity;

/**
 * Verification token service layer (interface).
 */
public interface IVerificationTokenService {
	
	/**
	 * Create verification token.
	 * @param email of user
	 */
	String createVerification(String email);
	
	/**
	 * Verify email of user.
	 * @param token token body
	 * @return Success / failure verification
	 */
	ResponseEntity<String> verifyEmail(String token);
}
