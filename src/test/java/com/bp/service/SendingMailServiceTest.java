package com.bp.service;

import com.bp.beans.OTP;
import com.bp.entity.UserEntity;
import com.bp.entity.VerificationToken;
import com.bp.model.email.MailProperties;
import com.bp.repository.UserRepository;
import com.bp.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MybatisTest
@ContextConfiguration(classes = {freemarker.template.Configuration.class, OTP.class})
@Import({SendingMailService.class, MailProperties.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@MapperScan("com.bp.repository")
class SendingMailServiceTest {
	
	@Inject
	private ISendingMailService sendingMailService;
	
	@Inject
	private VerificationTokenRepository verificationTokenRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Test
	void sendVerificationMail() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("QwerT1234");
		userRepository.insert(userEntity);

		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken("1234");
		verificationToken.setEmail(userEntity.getEmail());
		verificationTokenRepository.insert(verificationToken);
		
		sendingMailService.sendVerificationMail(userEntity.getEmail(), verificationToken.getToken(), userEntity.getUserId());
	}
	
	@Test
	void sendEmailOTP() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("QwerT1234");
		userRepository.insert(userEntity);
		
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken("1234");
		verificationToken.setEmail(userEntity.getEmail());
		verificationTokenRepository.insert(verificationToken);
		
		boolean sent = sendingMailService.sendEmailOTP(userEntity.getEmail(), userEntity.getUserId());
		assertTrue(sent);
	}
}