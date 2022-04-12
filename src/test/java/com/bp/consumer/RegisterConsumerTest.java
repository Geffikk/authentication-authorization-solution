package com.bp.consumer;

import com.bp.beans.Authentication;
import com.bp.beans.Message;
import com.bp.beans.OAuthJwtToken;
import com.bp.beans.OTP;
import com.bp.beans.RegistrationID;
import com.bp.config.JwtAuthenticationEntryPoint;
import com.bp.config.JwtRequestFilter;
import com.bp.config.SecurityConfiguration;
import com.bp.config.oauth.OAuth2LoginSuccessHandler;
import com.bp.config.smsapi.SmsVerification;
import com.bp.entity.VerificationToken;
import com.bp.model.email.MailProperties;
import com.bp.model.user.UserRequestModel;
import com.bp.repository.VerificationTokenRepository;
import com.bp.service.CapabilityService;
import com.bp.service.CustomOAuth2UserService;
import com.bp.service.DefaultCapabilityService;
import com.bp.service.MyUserDetailService;
import com.bp.service.RoleService;
import com.bp.service.SendingMailService;
import com.bp.service.UserRoleService;
import com.bp.service.UserService;
import com.bp.service.VerificationTokenService;
import com.bp.usecase.UserInteractor;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.utility.JSONUtility;
import com.bp.utility.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMybatis
@ContextConfiguration(classes = {
Message.class, SecurityConfiguration.class, CustomOAuth2UserService.class,
RegistrationID.class, OAuthJwtToken.class, OTP.class})
@Import({
RegisterConsumer.class, MyUserDetailService.class, UserService.class, CapabilityInteractor.class,
VerificationTokenService.class, SendingMailService.class, MailProperties.class, JwtAuthenticationEntryPoint.class,
OAuth2LoginSuccessHandler.class, Authentication.class, UserRoleService.class, LoginConsumer.class, UserInteractor.class,
SmsVerification.class, JwtRequestFilter.class, JwtTokenUtil.class, DefaultCapabilityService.class,
CapabilityService.class, RoleService.class})
@MapperScan("com.bp.repository")
public class RegisterConsumerTest {
	
	private static final String LOCAL_HOST = "http://localhost:8080";
	
	@Inject
	private MockMvc mvc;
	
	@Inject
	private VerificationTokenRepository verificationTokenRepository;
	
	@Test
	public void registerUserAndVerifyEmailAndThenLogin() throws Exception {
		UserRequestModel userRequestModel;
		
		registerAndVerifyEmail("hlina@gmail.com", "NoveHeslo282@");
		
		String url = LOCAL_HOST + "/api/v1/login";
		userRequestModel = new UserRequestModel();
		
		userRequestModel.setEmail("hlina@gmail.com");
		userRequestModel.setPassword("NoveHeslo282@");
		
		String requestJson = JSONUtility.objToJSON(userRequestModel);
		
		mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk());
	}
	
	public void registerAndVerifyEmail(String email, String password) throws Exception {
		String url = LOCAL_HOST + "/api/v1/register";
		UserRequestModel userRequestModel = new UserRequestModel();
		
		userRequestModel.setEmail(email);
		
		String requestJson = JSONUtility.objToJSON(userRequestModel);
		
		mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk());
		
		VerificationToken verificationToken = verificationTokenRepository.findByUserEmail(userRequestModel.getEmail());
		
		url = LOCAL_HOST + "/api/v1/verify?code="+verificationToken.getToken()+"/"+verificationToken.getUserId();
		mvc.perform(get(url)).andExpect(status().isOk());
		
		url = LOCAL_HOST + "/api/v1/verify";
		userRequestModel = new UserRequestModel();
		
		userRequestModel.setPassword(password);
		requestJson = JSONUtility.objToJSON(userRequestModel);
		
		mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk());
	}
}