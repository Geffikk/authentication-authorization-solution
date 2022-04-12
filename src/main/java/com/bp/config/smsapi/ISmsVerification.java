package com.bp.config.smsapi;

import com.bp.entity.UserEntity;

/**
 * SMS verification service.
 */
public interface ISmsVerification {
	
	/**
	 * Send sms with otp code on mobile.
	 * @param authenticatedUser instantiation
	 */
	void smsVerify(UserEntity authenticatedUser);
}
