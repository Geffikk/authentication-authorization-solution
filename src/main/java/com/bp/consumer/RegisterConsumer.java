package com.bp.consumer;

import com.bp.beans.RegistrationID;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.user.UserRequestModel;
import com.bp.model.user.UserResponseModel;
import com.bp.service.IUserRoleService;
import com.bp.service.IVerificationTokenService;
import com.bp.service.VerificationTokenService;
import com.bp.usecase.UserInputBoundary;
import com.bp.utility.CS;
import com.bp.utility.JSONUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Register request consumer.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class RegisterConsumer {
	
	/**
	 * User application logic.
	 */
	private final UserInputBoundary userInput;
	
	/**
	 * Service implementation of verification token.
	 */
	private final IVerificationTokenService verificationTokenService;
	
	/**
	 * Service implementation of user role entity.
	 */
	private final IUserRoleService userRoleService;
	
	/**
	 * Registration bean, is used for holding userId during registration process.
	 */
	@Resource(name = "registrationID")
	RegistrationID registrationID;
	
	public RegisterConsumer(
		UserInputBoundary userInput,
		VerificationTokenService verificationTokenService,
		IUserRoleService userRoleService) {
		this.verificationTokenService = verificationTokenService;
		this.userInput = userInput;
		this.userRoleService = userRoleService;
	}
	
	@PostMapping("/register")
	public String formPost(@RequestBody UserRequestModel userRequestModel, BindingResult bindingResult, Model model) throws JsonProcessingException {
		if (!bindingResult.hasErrors()) {
			model.addAttribute("noErrors", true);
		}
		if (userRequestModel == null || userRequestModel.getEmail() == null) {
			return "Request body is null !";
		}
		model.addAttribute("userRequestModel", userRequestModel);
		model.addAttribute("requestModel", new UserRequestModel());
		
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userRequestModel.getEmail());
		userInput.createUser(userEntity, null);
		userRoleService.addRole(userEntity.getUserId(), CS.USER, null);
		String token = verificationTokenService.createVerification(userRequestModel.getEmail());
		
		UserResponseModel userResponseModel = new UserResponseModel(null);
		userResponseModel.setEmailActivationUrl(userResponseModel.getEmail());
		userResponseModel.setEmailActivationUrl(token);
		return JSONUtility.objToJSON(userResponseModel);
	}
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyEmail(String code, Model model) {
		String userIdStr = code.substring(code.lastIndexOf("/") + 1);
		
		code = code.substring(0, code.lastIndexOf("/"));
		int userId = Integer.parseInt(userIdStr);
		registrationID.setUserId(userId);
		verificationTokenService.verifyEmail(code).getBody();
		model.addAttribute("userRequestModel", new UserRequestModel());
		return ResponseEntity.ok("Set your password (post request on 'api/v1/verify' with json (password:'<password>')");
	}
	
	@PostMapping("/verify")
	public ResponseEntity<?> setInfo(@RequestBody UserRequestModel userRequestModel, BindingResult bindingResult, Model model) {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		
		if (!bindingResult.hasErrors()) {
			model.addAttribute("noErrors", true);
		}
		model.addAttribute("userRequestModel", userRequestModel);
		UserEntity userEntity = userInput.findById(registrationID.getUserId(), authorizeModel);
		
		userInput.fillUser(userEntity, userRequestModel);
		userInput.saveUser(userEntity, authorizeModel);
		return ResponseEntity.ok("You were successfully registered !");
	}
}
