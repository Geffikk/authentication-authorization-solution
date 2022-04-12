package com.bp.integration;

import com.bp.beans.Authentication;
import com.bp.beans.Message;
import com.bp.beans.OAuthJwtToken;
import com.bp.beans.OTP;
import com.bp.config.JwtAuthenticationEntryPoint;
import com.bp.config.JwtRequestFilter;
import com.bp.config.SecurityConfiguration;
import com.bp.config.oauth.OAuth2LoginSuccessHandler;
import com.bp.config.smsapi.SmsVerification;
import com.bp.consumer.CapabilityConsumer;
import com.bp.consumer.LoginConsumer;
import com.bp.consumer.PatientConsumer;
import com.bp.consumer.ProcedureConsumer;
import com.bp.consumer.RoleConsumer;
import com.bp.consumer.UserConsumer;
import com.bp.consumer.VisitConsumer;
import com.bp.entity.Capability;
import com.bp.entity.Patient;
import com.bp.entity.Procedure;
import com.bp.entity.Role;
import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import com.bp.model.email.MailProperties;
import com.bp.model.role.RoleRequestModel;
import com.bp.model.user.UserResponseModel;
import com.bp.model.visit.VisitRequestModel;
import com.bp.service.CapabilityService;
import com.bp.service.CustomOAuth2UserService;
import com.bp.service.DefaultCapabilityService;
import com.bp.service.IUserService;
import com.bp.service.MyUserDetailService;
import com.bp.service.PatientService;
import com.bp.service.ProcedureService;
import com.bp.service.RoleService;
import com.bp.service.SendingMailService;
import com.bp.service.UserRoleService;
import com.bp.service.UserService;
import com.bp.service.UserVisitService;
import com.bp.service.VisitService;
import com.bp.model.user.UserRequestModel;
import com.bp.usecase.PatientInteractor;
import com.bp.usecase.ProcedureInteractor;
import com.bp.usecase.RoleInteractor;
import com.bp.usecase.UserInteractor;
import com.bp.usecase.VisitIntercator;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.utility.CS;
import com.bp.utility.JSONUtility;
import com.bp.utility.JwtTokenUtil;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import javax.inject.Inject;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMybatis
@ContextConfiguration(
classes = {Message.class, SecurityConfiguration.class, CustomOAuth2UserService.class, OAuthJwtToken.class, OTP.class, PasswordEncoder.class})
@Import({LoginConsumer.class, ProcedureConsumer.class, MyUserDetailService.class, VisitConsumer.class, VisitService.class, CapabilityService.class,
PatientService.class, CapabilityInteractor.class, UserVisitService.class, VisitIntercator.class, UserInteractor.class,
UserInteractor.class, SmsVerification.class, UserService.class, JwtAuthenticationEntryPoint.class, PatientInteractor.class,
OAuth2LoginSuccessHandler.class, Authentication.class, UserRoleService.class, DefaultCapabilityService.class, ProcedureService.class,
JwtRequestFilter.class, RoleConsumer.class, JwtTokenUtil.class, RoleService.class, SendingMailService.class, MailProperties.class,
ProcedureInteractor.class, RoleInteractor.class, CapabilityConsumer.class, UserConsumer.class, PatientConsumer.class})
@MapperScan("com.bp.repository")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CapabilityIntegrationTest {
	
	@Inject
	private IUserService userService;
	
	private static final String LOCAL_HOST = "http://localhost:8080";
	
	final Gson gson = new Gson();
	
	@Inject
	private MockMvc mvc;
	
	/**
	 * JWT auth token holder.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	@Test
	public void loginAsDoctorCreateVisitAndLogout() throws Exception {
		String token = login("doctor@gmail.com", "1", null);
		createVisitIsOk("patient@gmail.com", 2, token);
		logout(token);
	}
	
	@Test
	public void loginAsSisterGetVisitEditVisit401() throws Exception {
		String token = login("sister@gmail.com", "1", null);
		getVisitIsOk("7", token);
		updateVisit401("7", token);
	}
	
	@Test
	public void loginAsManagerCreateVisit401GetVisitAddUserDeleteVisit() throws Exception {
		String token = login("management@gmail.com", "1", null);
		createVisitIsOk("patient@gmail.com", 2, token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		Visit visit = createVisitIsOk("patient@gmail.com", 2, token);
		logout(token);
		token = login("management@gmail.com", "1", null);
		addUserToVisitIsOk("DOCTOR", String.valueOf(visit.getVisitId()), "2", token);
		deleteVisitIsOk(String.valueOf(visit.getVisitId()), token);
	}
	
	@Test
	public void loginAsSisterEditVisit401LoginAsDoctorCreateVisitAddSisterLoginAsSisterUpdateVisit() throws Exception {
		String token = login("sister@gmail.com", "1", null);
		updateVisit401("5", token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		Visit visit = createVisitIsOk("patient@gmail.com", 2, token);
		UserEntity sister = userService.findUserByEmail("sister@gmail.com");
		addUserToVisitIsOk("SISTER", String.valueOf(visit.getVisitId()), String.valueOf(sister.getUserId()), token);
	}
	
	@Test
	public void LogAsDocEditVisit401AddUser401LogAsManAddDocLogAsDocEditVisitAddUser() throws Exception {
		String token = login("doctor@gmail.com", "1", null);
		updateVisit401("5", token);
		addUserToVisit401("SISTER", "5", "3", token);
		token = login("management@gmail.com", "1", null);
		addUserToVisitIsOk("OWNER", "5", "1", token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		updateVisitIsOk("5", token);
		addUserToVisitIsOk("SISTER", "5", "3", token);
	}
	
	@Test
	public void DocCreateVisitAddDocAndSisCreateProcedureDocEditProcedureCreateCap401SisEditProcedure401() throws Exception {
		String token = login("doctor@gmail.com", "1", null);
		Visit visit = createVisitIsOk("patient@gmail.com", 2, token);
		addUserToVisitIsOk("DOCTOR", String.valueOf(visit.getVisitId()), "2", token);
		addUserToVisitIsOk("SISTER", String.valueOf(visit.getVisitId()), "3", token);
		Procedure procedure = createProcedureIsOk(String.valueOf(visit.getVisitId()), token);
		String procedureId = String.valueOf(procedure.getProcedureId());
		logout(token);
		token = login("doctor2@gmail.com", "1", null);
		updateProcedureIsOk(String.valueOf(visit.getVisitId()), procedureId, token);
		createCapability401("visit", visit.getVisitId(), CS.PERSONAL, "sister@gmail.com", token);
		logout(token);
		token = login("sister@gmail.com", "1", null);
		updateProcedureIsOk(String.valueOf(visit.getVisitId()), procedureId, token);
	}
	
	@Test
	public void UserGetVisit401ManCreateRoleCreateCapUserGetVisit() throws Exception {
		String token = login("user@gmail.com", "1", null);
		getVisit401("5", token);
		logout(token);
		token = login("management@gmail.com", "1", null);
		Role role = createRoleIsOk("NEW_ROLE", token);
		String userId = String.valueOf(userService.findUserByEmail("user@gmail.com").getUserId());
		createCapabilityIsOk(CS.VISIT, 5, "NEW_ROLE", null, token);
		assignRoleToUserIsOk(role.getName(), userId, token);
		logout(token);
		token = login("user@gmail.com", "1", null);
		//getVisitIsOk("5", token);
	}
	
	@Test
	public void ManCreateAddDocToVisitDoctorEditVisitManDeleteRoleDocEditVisit401() throws Exception {
		String token = login("management@gmail.com", "1", null);
		addUserToVisitIsOk("DOCTOR", "5", "1", token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		updateVisitIsOk("5", token);
		logout(token);
		token = login("management@gmail.com", "1", null);
		/*deleteUserRoleIsOk("DOCTOR", "1", token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		updateVisit401("5", token);*/
	}
	
	@Test
	public void ManCreateAddDocToVisitDoctorEditVisitManDeleteFromVisitDocEditVisit401() throws Exception {
		String token = login("management@gmail.com", "1", null);
		addUserToVisitIsOk("DOCTOR", "5", "1", token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		updateVisitIsOk("5", token);
		logout(token);
		token = login("management@gmail.com", "1", null);
		/*deleteUserFromVisitIsOk("5", "1", token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		updateVisit401("5", token);*/
	}
	
	@Test
	public void DocCreateVisitCreateProcedureManEditProcedure() throws Exception {
		String token = login("doctor@gmail.com", "1", null);
		Visit visit = createVisitIsOk("patient@gmail.com", 2, token);
		Procedure procedure = createProcedureIsOk(String.valueOf(visit.getVisitId()), token);
		logout(token);
		token = login("management@gmail.com", "1", null);
		updateProcedureIsOk(String.valueOf(visit.getVisitId()), String.valueOf(procedure.getProcedureId()), token);
	}
	
	@Test
	public void LoginSendOtpEmailAcceptLoginGetVisit() throws Exception {
		UserRequestModel user = loginWithOtpEmail("geffert.maros@gmail.com", "1");
		login("geffert.maros@gmail.com", "1", user.getOtp());
	}
	
	@Test
	public void LoginSendOtpMobileAcceptLoginGetVisit() throws Exception {
		//FIXME Uncomment me when you want lose some $ !
		/*UserRequestModel user = loginWithOtpMobile("geffert.maros@gmail.com", "1");
		login("geffert.maros@gmail.com", "1", user.getOtp());*/
	}
	
	@Test
	public void DocCreateVisitAddDoctorDoc2AddSisterCreateCap401DocCreateCapD2Doc2CreateCapSis() throws Exception {
		String token = login("doctor@gmail.com", "1", null);
		Visit visit = createVisitIsOk("patient@gmail.com", 2, token);
		addUserToVisitIsOk("DOCTOR", String.valueOf(visit.getVisitId()), "2", token);
		logout(token);
		token = login("doctor2@gmail.com", "1", null);
		addUserToVisit401("SISTER", String.valueOf(visit.getVisitId()), "3", token);
		createCapability401(CS.VISIT, visit.getVisitId(), CS.PERSONAL, "doctor2@gmail.com", token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		/*createCapabilityIsOk(CS.VISIT, visit.getVisitId(), CS.PERSONAL, "doctor2@gmail.com", token);
		logout(token);
		token = login("doctor2@gmail.com", "1", null);
		createCapabilityIsOk(CS.VISIT, visit.getVisitId(), CS.PERSONAL, "sister@gmail.com", token);
		addUserToVisitIsOk("SISTER", String.valueOf(visit.getVisitId()), "3", token);*/
	}
	
	@Test
	public void DocCreateVisitAddSisterSisterCreateProcedureDocEditProcedure() throws Exception {
		String token = login("doctor@gmail.com", "1", null);
		Visit visit = createVisitIsOk("patient@gmail.com", 2, token);
		addUserToVisitIsOk("SISTER", String.valueOf(visit.getVisitId()), "3", token);
		logout(token);
		token = login("sister@gmail.com", "1", null);
		Procedure procedure = createProcedureIsOk(String.valueOf(visit.getVisitId()), token);
		logout(token);
		token = login("doctor@gmail.com", "1", null);
		//updateProcedureIsOk(String.valueOf(visit.getVisitId()), String.valueOf(procedure.getProcedureId()), token);
	}
	
	@Test
	public void UserShowUserInfo() throws Exception {
		String token = login("user@gmail.com", "1", null);
		showUserInformation("5", token);
	}
	
	@Test
	public void DocCreatePatient() throws Exception {
		String token = login("doctor@gmail.com", "1", null);
		createPatientIsOk(token);
	}
	
	@Test
	public void ManCreatePatient401() throws Exception {
		String token = login("management@gmail.com", "1", null);
		createPatient401(token);
	}
	
	@Test
	public void UserCreateVisit() throws Exception {
		String token = login("user@gmail.com", "1", null);
		createVisitIsOk("patient@gmail.com", 2, token);
	}
	
	@Test
	public void UserCreateVisitAddDoctorToVisit401UpdateVisit() throws Exception {
		String token = login("user@gmail.com", "1", null);
		Visit visit = createVisitIsOk("user@gmail.com", 5, token);
		addUserToVisit401("DOCTOR", String.valueOf(visit.getVisitId()), "1", token);
		updateVisitIsOk(String.valueOf(visit.getVisitId()), token);
	}
	
	private void createPatient401(String token) throws Exception {
		Patient patient = new Patient();
		patient.setIdentification(9902286361L);
		String requestJson = JSONUtility.objToJSON(patient);
		
		mvc.perform(post(LOCAL_HOST + "/api/v1/patient")
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().is4xxClientError());
	}
	
	private void createPatientIsOk(String token) throws Exception {
		Patient patient = new Patient();
		patient.setIdentification(9902286361L);
		String requestJson = JSONUtility.objToJSON(patient);
		
		mvc.perform(post(LOCAL_HOST + "/api/v1/patient")
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().isOk());
	}
	
	private void showUserInformation(String userId, String token) throws Exception {
		mvc.perform(get(LOCAL_HOST + "/api/v1/user/" + userId)
			.header("Authorization", token))
			.andExpect(status().isOk());
	}
	
	private UserRequestModel loginWithOtpEmail(String email, String password) throws Exception {
		UserRequestModel userRequestModel = new UserRequestModel();
		userRequestModel.setEmail(email);
		userRequestModel.setPassword(password);
		
		String requestJson = JSONUtility.objToJSON(userRequestModel);
		String result = mvc.perform(post(LOCAL_HOST + "/api/v1/email/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		return JSONUtility.jsonToUser(result);
	}
	
	private UserRequestModel loginWithOtpMobile(String email, String password) throws Exception {
		UserRequestModel userRequestModel = new UserRequestModel();
		userRequestModel.setEmail(email);
		userRequestModel.setPassword(password);
		
		String requestJson = JSONUtility.objToJSON(userRequestModel);
		String result = mvc.perform(post(LOCAL_HOST + "/api/v1/mobile/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		return JSONUtility.jsonToUser(result);
	}
	
	private void deleteUserFromVisitIsOk(String vistiId, String userId, String token) throws Exception {
		mvc.perform(delete(LOCAL_HOST + "/api/v1/visit/" + vistiId + "/user/" + userId)
			.header("Authorization", token))
			.andExpect(status().isOk());
	}
	
	private void deleteUserRoleIsOk(String role, String userId, String token) throws Exception {
		mvc.perform(delete(LOCAL_HOST + "api/v1/role/" + role + "/user/" + userId)
			.header("Authorization", token))
			.andExpect(status().isOk());
	}
	
	private void assignRoleToUserIsOk(String role, String userId, String token) throws Exception {
		mvc.perform(post(LOCAL_HOST + "/api/v1/role/" + role +"/user/" + userId)
			.header("Authorization", token))
			.andExpect(status().isOk());
	}
	
	private Role createRoleIsOk(String roleName, String token) throws Exception {
		RoleRequestModel role = new RoleRequestModel();
		role.setRole(roleName);
		String requestJson = JSONUtility.objToJSON(role);
		
		String result = mvc.perform(post(LOCAL_HOST + "/api/v1/role")
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return JSONUtility.jsonToRole(result);
	}
	
	private void createCapability401(String entityType, int entityId, String role, String user, String token) throws Exception {
		Capability capability = new Capability();
		capability.setEntityType(entityType);
		capability.setEntityId(entityId);
		capability.setRole(role);
		capability.setUser(user);
		String requestJson = JSONUtility.objToJSON(capability);
		
		mvc.perform(post(LOCAL_HOST + "/api/v1/capability")
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().is4xxClientError());
	}
	
	private void createCapabilityIsOk(String entityType, int entityId, String role, String user, String token) throws Exception {
		Capability capability = new Capability();
		capability.setEntityType(entityType);
		capability.setEntityId(entityId);
		capability.setRole(role);
		capability.setUser(user);
		capability.setRead(true);
		capability.setCreate(true);
		capability.setDelete(true);
		capability.setUpdate(true);
		capability.setCapability(true);
		String requestJson = JSONUtility.objToJSON(capability);
		
		mvc.perform(post(LOCAL_HOST + "/api/v1/capability")
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().isOk());
	}
	
	private void updateProcedureIsOk(String visitId, String procedureId, String token) throws Exception {
		Procedure procedure = new Procedure();
		procedure.setVisitId(Integer.parseInt(visitId));
		String requestJson = JSONUtility.objToJSON(procedure);
		
		mvc.perform(put(LOCAL_HOST + "/api/v1/visit/" + visitId + "/procedure/" + procedureId)
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().isOk());
	}
	
	private void updateProcedure401(String visitId, String procedureId) throws Exception {
		Procedure procedure = new Procedure();
		procedure.setVisitId(Integer.parseInt(visitId));
		String requestJson = JSONUtility.objToJSON(procedure);
		
		mvc.perform(put(LOCAL_HOST + "/api/v1/visit/" + visitId + "/procedure/" + procedureId)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().is4xxClientError());
	}
	
	private Procedure createProcedureIsOk(String visitId, String token) throws Exception {
		Procedure procedure = new Procedure();
		procedure.setVisitId(Integer.parseInt(visitId));
		String requestJson = JSONUtility.objToJSON(procedure);
		
		String result = mvc.perform(post(LOCAL_HOST + "/api/v1/visit/" + visitId + "/procedure")
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		return JSONUtility.jsonToProcedure(result);
	}
	
	private void createProcedure401(String visitId) throws Exception {
		Procedure procedure = new Procedure();
		procedure.setVisitId(Integer.parseInt(visitId));
		String requestJson = JSONUtility.objToJSON(procedure);
		
		mvc.perform(post(LOCAL_HOST + "/api/v1/visit/" + visitId + "/procedure")
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().is4xxClientError());
	}
	
	public String login(String email, String password, String otp) throws Exception {
		UserRequestModel userRequestModel = new UserRequestModel();
		userRequestModel.setEmail(email);
		userRequestModel.setPassword(password);
		userRequestModel.setOtp(Objects.requireNonNullElse(otp, "0"));
		
		String requestJson = JSONUtility.objToJSON(userRequestModel);
		
		String responseModel = mvc.perform(post(LOCAL_HOST + "/api/v1/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		UserResponseModel userResponseModel = JSONUtility.jsonToUserResponse(responseModel);
		return "Bearer " + userResponseModel.getJwtToken();
	}
	
	private void deleteVisitIsOk(String visitId, String token) throws Exception {
		mvc.perform(delete(LOCAL_HOST + "/api/v1/visit/" + visitId)
			.header("Authorization", token))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
	}
	
	public Visit createVisitIsOk(String patientEmail, int patientId, String token) throws Exception {
		Visit visit = new Visit();
		visit.setEmail(patientEmail);
		visit.setIdentification(9902286361L);
		visit.setPatientId(patientId);
		String requestJson = gson.toJson(visit);
		
		String result = mvc.perform(post(LOCAL_HOST + "/api/v1/visit")
			.contentType(APPLICATION_JSON)
			.content(requestJson)
			.header("Authorization", token))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		return JSONUtility.jsonToVisit(result);
	}
	
	public void createVisit401(String patientEmail, int patientId) throws Exception {
		Visit visit = new Visit();
		visit.setEmail(patientEmail);
		visit.setIdentification(9902286361L);
		visit.setPatientId(patientId);
		String requestJson = gson.toJson(visit);
		
		mvc.perform(post(LOCAL_HOST + "/api/v1/visit")
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().is4xxClientError());
	}
	
	public void getVisitIsOk(String visitId, String token) throws Exception {
		mvc.perform(get(LOCAL_HOST + "/api/v1/visit/" + visitId)
			.header("Authorization", token))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
	}
	
	public void getVisit401(String visitId, String token) throws Exception {
		mvc.perform(get(LOCAL_HOST + "/api/v1/visit/" + visitId)
			.header("Authorization", token))
			.andExpect(status().is4xxClientError());
	}
	
	public void updateVisit401(String visitId, String token) throws Exception {
		VisitRequestModel visit = new VisitRequestModel();
		visit.setFirstName("Fero");
		visit.setLastName("Kapusta");
		String requestJson = JSONUtility.objToJSON(visit);
		
		mvc.perform(put(LOCAL_HOST + "/api/v1/visit/" + visitId)
			.contentType(APPLICATION_JSON).content(requestJson)
			.header("Authorization", token))
			.andExpect(status().is4xxClientError());
	}
	
	public void updateVisitIsOk(String visitId, String token) throws Exception {
		VisitRequestModel visit = new VisitRequestModel();
		visit.setFirstName("Fero");
		visit.setLastName("Kapusta");
		String requestJson = JSONUtility.objToJSON(visit);
		
		mvc.perform(put(LOCAL_HOST + "/api/v1/visit/" + visitId)
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
	}
	
	public void addUserToVisitIsOk(String roleName, String visitId, String userId, String token) throws Exception {
		RoleRequestModel role = new RoleRequestModel();
		role.setRole(roleName);
		String requestJson = JSONUtility.objToJSON(role);
		
		mvc.perform(post(LOCAL_HOST + "/api/v1/visit/" + visitId + "/user/" + userId)
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestJson))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
	}
	
	public void addUserToVisit401(String requestData, String visitId, String userId, String token) throws Exception {
		mvc.perform(post(LOCAL_HOST + "/api/v1/visit/" + visitId + "/user/" + userId)
			.header("Authorization", token)
			.contentType(APPLICATION_JSON).content(requestData))
			.andExpect(status().is4xxClientError())
			.andReturn().getResponse().getContentAsString();
	}
	
	public void logout(String token) throws Exception {
		mvc.perform(get(LOCAL_HOST + "api/v1/user/logout")
			.header("Authorization", token))
			.andExpect(status().isOk());
	}
}
