package com.bp.consumer;

import com.bp.beans.IAuthentication;
import com.bp.beans.Message;
import com.bp.entity.UserEntity;
import com.bp.usecase.UserInputBoundary;
import com.bp.model.user.UserRequestModel;
import com.bp.model.user.UserResponseModel;
import com.bp.usecase.UserInteractor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * Login request consumer.
 */
@Controller
public class LoginConsumer {
	
	Logger logger = Logger.getLogger(UserInteractor.class.getName());
	
	/**
	 * User/Login application logic.
	 */
	private final UserInputBoundary userInput;
	
	/**
	 * Authenticated user details.
	 */
	private final IAuthentication authentication;
	
	/**
	 * Message bean used in example of csrf attack.
	 */
	@Resource(name = "message")
	Message message;
	
	LoginConsumer(UserInputBoundary accountGateway, IAuthentication authentication) {
		this.userInput = accountGateway;
		this.authentication = authentication;
	}
	
	@GetMapping("/login")
	public String pleaseLogin(Model model) {
		model.addAttribute("userRequestModel", new UserRequestModel());
		model.addAttribute("requestModel", new UserRequestModel());
		return "welcome";
	}
	
	@PostMapping("/api/v1/mobile/login")
	public ResponseEntity<?> otpMobileLogin(@RequestBody UserRequestModel requestModel) {
		userInput.otpLogin(requestModel);
		requestModel.setPassword("**********");
		return ResponseEntity.ok(requestModel);
	}
	
	@PostMapping("/api/v1/email/login")
	public ResponseEntity<?> otpEmailLogin(@RequestBody UserRequestModel requestModel) {
		userInput.emailOtpLogin(requestModel);
		requestModel.setPassword("**********");
		return ResponseEntity.ok(requestModel);
	}
	
	@PostMapping("/api/v1/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserRequestModel requestModel, Model model, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			model.addAttribute("noErrors", true);
		}
		UserResponseModel userResponseModel = userInput.login(requestModel);
		model.addAttribute("requestModel", new UserRequestModel());
		return ResponseEntity.ok(userResponseModel);
	}
	
	@GetMapping(value = {"/api/v1/user/logout"})
	public String logout(Model model, HttpServletRequest request) {
		model.addAttribute("userRequestModel", new UserRequestModel());
		model.addAttribute("requestModel", new UserRequestModel());
		try {
			userInput.logout(request);
		} catch (NullPointerException e) {
			logger.info("You are not logged in");
		}
		return "welcome";
	}
	
	/**
	 * Return login page.
	 * @return Json hello world
	 */
	@GetMapping(value = {""})
	public String hello(HttpServletRequest httpRequest)  {
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		if (authenticatedUser == null) {
			return "redirect:/login";
		}
		return "hello";
	}
	
	/**
	 * Return login page.
	 * @return Json hello world
	 */
	@PostMapping(value = {"/api/v1/csrf"})
	public String csrfAttack()  {
		System.out.println("CSRF protection enabled request");
		System.out.println(message.getMsg());
		return "hello";
	}
}
