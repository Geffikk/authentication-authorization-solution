package com.bp.service;

import com.bp.entity.UserEntity;
import com.bp.entity.VerificationToken;
import com.bp.repository.UserRepository;
import com.bp.repository.VerificationTokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class VerificationTokenService implements IVerificationTokenService {
	
	/**
	 * User repository.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Verification token repository.
	 */
	private final VerificationTokenRepository verificationTokenRepository;
	
	/**
	 * Sending mail service.
	 */
	private final ISendingMailService sendingMailService;
	
	public VerificationTokenService(
		UserRepository userRepository,
        VerificationTokenRepository verificationTokenRepository,
	    SendingMailService sendingMailService) {
		this.userRepository = userRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.sendingMailService = sendingMailService;
	}
	
	@Transactional
	@Override
	public String createVerification(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		VerificationToken verificationToken = verificationTokenRepository.findByUserEmail(email);
		
		if (verificationToken == null) {
			verificationToken = new VerificationToken();
			verificationToken.setUserId(userEntity.getUserId());
			verificationToken.setEmail(email);
			verificationTokenRepository.insert(verificationToken);
		}
		return sendingMailService.sendVerificationMail(email, verificationToken.getToken(), userEntity.getUserId());
	}
	
	@Transactional
	@Override
	public ResponseEntity<String> verifyEmail(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		if (verificationToken == null) {
			return ResponseEntity.badRequest().body("Invalid token.");
		}
		if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {
			return ResponseEntity.unprocessableEntity().body("Expired token.");
		}
		
		verificationToken.setConfirmedDateTime(LocalDateTime.now());
		verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
		userRepository.findById(verificationToken.getUserId()).setActive(1);
		verificationTokenRepository.insert(verificationToken);
		
		return ResponseEntity.ok("You have successfully verified your email address. Please set your password");
	}
}
