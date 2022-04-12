package com.bp.usecase;

import com.bp.beans.Authentication;
import com.bp.beans.OAuthJwtToken;
import com.bp.beans.OTP;
import com.bp.config.smsapi.ISmsVerification;
import com.bp.entity.JwtBlacklist;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.user.UserRequestModel;
import com.bp.model.user.UserResponseModel;
import com.bp.repository.JwtBlacklistRepository;
import com.bp.service.ISendingMailService;
import com.bp.service.IUserService;
import com.bp.service.UserService;
import com.bp.utility.JwtTokenUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class UserInteractor implements UserInputBoundary {
	
	Logger logger = Logger.getLogger(UserInteractor.class.getName());
	
	/**
	 * User service.
	 */
	private final IUserService userService;
	
	/**
	 * Blacklist repository.
	 */
	private final JwtBlacklistRepository jwtBlacklistRepository;
	
	/**
	 * JWT token utility class.
	 */
	private final JwtTokenUtil jwtTokenUtil;
	
	/**
	 * Sms verification implementation.
	 */
	private final ISmsVerification smsVerification;
	
	/**
	 * Sending mail service.
	 */
	private final ISendingMailService sendingMailService;
	
	private final Authentication authentication;
	
	/**
	 * Password encoder.
	 */
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * JWT token bean, which hold token for oAuth authentication.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	/**
	 * OTP bean, which hold otp code for authentication.
	 */
	@Resource(name = "OTP")
	OTP otpBean;
	
	UserInteractor(
		UserService userService,
		JwtBlacklistRepository jwtBlacklistRepository,
		JwtTokenUtil jwtTokenUtil,
		ISendingMailService sendingMailService,
		PasswordEncoder passwordEncoder,
		Authentication authentication,
		ISmsVerification smsVerification) {
		this.userService = userService;
		this.jwtBlacklistRepository = jwtBlacklistRepository;
		this.jwtTokenUtil = jwtTokenUtil;
		this.smsVerification = smsVerification;
		this.sendingMailService = sendingMailService;
		this.authentication = authentication;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public void otpLogin(UserRequestModel requestModel) {
		UserEntity user = userService.findUserByEmail(requestModel.getEmail());
		
		if (!passwordEncoder.matches(requestModel.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid credentials");
		}
		smsVerification.smsVerify(user);
		requestModel.setOtp(otpBean.getOtp());
	}
	
	@Override
	public void emailOtpLogin(UserRequestModel requestModel) {
		UserEntity user = userService.findUserByEmail(requestModel.getEmail());
		
		if (!passwordEncoder.matches(requestModel.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid credentials");
		}
		sendingMailService.sendEmailOTP(user.getEmail(), user.getUserId());
		requestModel.setOtp(otpBean.getOtp());
	}
	
	@Override
	public UserResponseModel login(UserRequestModel requestModel) {
		UserEntity user = userService.findUserByEmail(requestModel.getEmail());
		
		if (user == null || user.getPassword() == null) {
			throw new BadCredentialsException("You are not registered !");
		}
		if (!passwordEncoder.matches(requestModel.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid credentials");
		}
		if (otpBean.getOtp() != null && !otpBean.getOtp().equals(requestModel.getOtp()) && requestModel.getOtp() != null) {
			throw new BadCredentialsException("Your otp code is wrong");
		}
		logger.info("Your otp code is correct !");
		otpBean.setOtp(null);
		
		final String token = jwtTokenUtil.generateToken(requestModel.getEmail());
		user.setJwtToken(token);
		userService.saveUser(user, null);
		logger.info("[Logged in]: " + user.getEmail() + " successfully authenticated !");
		
		LocalDateTime now = LocalDateTime.now();
		UserResponseModel accountResponseModel = new UserResponseModel(token);
		accountResponseModel.setEmail(user.getEmail());
		accountResponseModel.setLoginTime(now.toString());
		
		return accountResponseModel;
	}
	
	@Override
	public void logout(HttpServletRequest httpServletRequest) { // Delete prefix Bearer
		UserEntity user = authentication.authorizeRequest(httpServletRequest);
		oAuthJwtToken.setJwtToken(null);
		JwtBlacklist jwtBlacklist = new JwtBlacklist();
		jwtBlacklist.setToken(user.getJwtToken());
		jwtBlacklistRepository.insert(jwtBlacklist);
		logger.info("[Logged out]: Bye bye: " + user.getEmail());
	}
	
	@Override
	public UserEntity findById(int userId, AuthorizeModel authorizeModel) {
		return userService.findById(userId, authorizeModel);
	}
	
	@Override
	public List<UserEntity> findAll(AuthorizeModel authorizeModel) {
		return userService.findAll(authorizeModel);
	}
	
	@Override
	public UserEntity fillUser(UserEntity userEntity, UserRequestModel userRequestModel) {
		if (userRequestModel.getPassword() != null) {
			userEntity.setPassword(passwordEncoder.encode(userRequestModel.getPassword()));
		}
		if (userRequestModel.getBirthdate() != null) {
			userEntity.setBirthdate(userRequestModel.getBirthdate());
		}
		if (userRequestModel.getFirstName() != null) {
			userEntity.setFirstName(userRequestModel.getFirstName());
		}
		if (userRequestModel.getLastName() != null) {
			userEntity.setLastName(userRequestModel.getLastName());
		}
		if (userRequestModel.getPhone() != null) {
			userEntity.setPhone(userRequestModel.getPhone());
		}
		return userEntity;
	}
	
	@Override
	public void createUser(UserEntity userEntity, AuthorizeModel authorizeModel) {
		userService.createUser(userEntity, authorizeModel);
	}
	
	@Override
	public void saveUser(UserEntity userEntity, AuthorizeModel authorizeModel) {
		if (!userEntity.isPasswordValid(userEntity.getPassword())) {
			System.out.println("Password is not valid, must contain at least one " +
				"(Upper case, lower case and a number)!");
			return;
		}
		userService.saveUser(userEntity, authorizeModel);
	}
	
	@Override
	public void deleteUser(int userId, AuthorizeModel authorizeModel) {
		userService.deleteUser(userId, authorizeModel);
	}
}
