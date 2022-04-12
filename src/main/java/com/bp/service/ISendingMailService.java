package com.bp.service;

/**
 * Interface of sending mail service.
 */
public interface ISendingMailService {
	
	/**
	 * Send verification email.
	 * @param toEmail send on email
	 * @param verificationCode verification code
	 * @param userId id of user
	 */
	String sendVerificationMail(String toEmail, String verificationCode, int userId);
	
	/**
	 * Send email with otp code.
	 * @param toEmail send on email
	 * @param userId id of user
	 * @return true, if email was successfully sent.
	 */
	boolean sendEmailOTP(String toEmail, int userId);
}
