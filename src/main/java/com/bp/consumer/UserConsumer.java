package com.bp.consumer;

import com.bp.beans.IAuthentication;
import com.bp.beans.OAuthJwtToken;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.user.UserRequestModel;
import com.bp.usecase.UserInputBoundary;
import com.bp.utility.JSONUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

/**
 * User request consumer.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class UserConsumer {
	
	Logger logger = Logger.getLogger(UserConsumer.class.getName());
	
	/**
	 * User application logic.
	 */
	private final UserInputBoundary userInput;
	
	/**
	 * User authentication.
	 */
	private final IAuthentication authentication;
	
	/**
	 * JWT auth token holder.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	public UserConsumer(UserInputBoundary userInput, IAuthentication authentication) {
		this.userInput = userInput;
		this.authentication = authentication;
	}
	
	@GetMapping("/users")
	public String getUsers(HttpServletRequest httpRequest) throws JsonProcessingException {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is getting users");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		List<UserEntity> users = userInput.findAll(authorizeModel);
		
		if (users == null) {
			throw new ResourceNotFoundException();
		}
		return JSONUtility.objToJSON(users);
	}
	
	@GetMapping("/user/{userId}")
	public String getUser(@PathVariable int userId, HttpServletRequest httpRequest) throws JsonProcessingException {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is getting a user");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		UserEntity userEntity;
		
		if (authenticatedUser.getUserId() == userId) {
			userEntity = userInput.findById(userId, null);
		} else {
			userEntity = userInput.findById(userId, authorizeModel);
		}
		if (userEntity == null) {
			throw new ResourceNotFoundException();
		}
		userEntity.setPassword("**********");
		return JSONUtility.objToJSON(userEntity);
	}
	
	@PostMapping("/user")
	public String createUser(@RequestBody UserRequestModel userRequestModel, HttpServletRequest httpRequest) throws Exception {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is creating users");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		UserEntity userEntity = userInput.fillUser(new UserEntity(), userRequestModel);
		userInput.createUser(userEntity, authorizeModel);
		return JSONUtility.objToJSON(userEntity);
	}
	
	@PutMapping("/user/{userId}")
	public String updateUser(@RequestBody UserRequestModel userRequestModel, @PathVariable int userId, HttpServletRequest httpRequest) throws Exception {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is updating a user");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		UserEntity userEntity = userInput.findById(userId, authorizeModel);
		
		if (userEntity == null) {
			throw new ResourceNotFoundException();
		}
		userInput.fillUser(userEntity, userRequestModel);
		userInput.saveUser(userEntity, authorizeModel);
		return JSONUtility.objToJSON(userEntity);
	}
	
	@DeleteMapping("/user/{userId}")
	public String deleteUser(@PathVariable int userId, HttpServletRequest httpRequest) {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is deleting a user");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		UserEntity userEntity = userInput.findById(userId, authorizeModel);
		
		if (userEntity == null) {
			throw new ResourceNotFoundException();
		}
		userInput.deleteUser(userId, authorizeModel);
		return "User was deleted !";
	}
}
