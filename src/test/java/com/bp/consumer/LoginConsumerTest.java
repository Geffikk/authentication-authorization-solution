package com.bp.consumer;

import com.bp.beans.Authentication;
import com.bp.beans.Message;
import com.bp.beans.OAuthJwtToken;
import com.bp.beans.OTP;
import com.bp.config.JwtAuthenticationEntryPoint;
import com.bp.config.JwtRequestFilter;
import com.bp.config.SecurityConfiguration;
import com.bp.config.oauth.OAuth2LoginSuccessHandler;
import com.bp.config.smsapi.SmsVerification;
import com.bp.model.email.MailProperties;
import com.bp.model.user.UserResponseModel;
import com.bp.service.CapabilityService;
import com.bp.service.CustomOAuth2UserService;
import com.bp.service.DefaultCapabilityService;
import com.bp.service.MyUserDetailService;
import com.bp.service.RoleService;
import com.bp.service.SendingMailService;
import com.bp.service.UserRoleService;
import com.bp.service.UserService;
import com.bp.usecase.UserInteractor;
import com.bp.model.user.UserRequestModel;
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
@ContextConfiguration(
classes = {Message.class, OTP.class, SecurityConfiguration.class, CustomOAuth2UserService.class, OAuthJwtToken.class})
@Import({LoginConsumer.class, MyUserDetailService.class, UserInteractor.class, UserService.class,
JwtAuthenticationEntryPoint.class, OAuth2LoginSuccessHandler.class, Authentication.class, UserRoleService.class,
JwtRequestFilter.class, JwtTokenUtil.class, CapabilityInteractor.class, SmsVerification.class,
DefaultCapabilityService.class, CapabilityService.class, RoleService.class, SendingMailService.class,
MailProperties.class})
@MapperScan("com.bp.repository")
public class LoginConsumerTest {
	
	private static final String LOCAL_HOST = "http://localhost:8080";
	
	@Inject
	private MockMvc mvc;
	
	@Test
	public void getLogin() throws Exception {
		String url = LOCAL_HOST + "/api/v1/login";
		UserRequestModel userRequestModel = new UserRequestModel();
		
		userRequestModel.setEmail("doctor@gmail.com");
		userRequestModel.setPassword("1");
		
		String requestJson = JSONUtility.objToJSON(userRequestModel);
		
		String token = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		UserResponseModel userResponseModel = JSONUtility.jsonToUserResponse(token);
		token = "Bearer " + userResponseModel.getJwtToken();
		mvc.perform(get("/").header("Authorization", token)).andExpect(status().isOk());
	}
	
	@Test
	public void getLogout() throws Exception {
		mvc.perform(get(LOCAL_HOST + "api/v1/user/logout")).andExpect(status().isOk());
	}
	
	@Test
	public void postLogin() throws Exception {
		String url = LOCAL_HOST + "/api/v1/login";
		UserRequestModel userRequestModel = new UserRequestModel();
		
		userRequestModel.setEmail("sister3@gmail.com");
		userRequestModel.setPassword("1");
		
		String requestJson = JSONUtility.objToJSON(userRequestModel);
		
		mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().is4xxClientError());
	}
}