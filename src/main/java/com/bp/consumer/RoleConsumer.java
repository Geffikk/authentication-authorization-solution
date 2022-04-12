package com.bp.consumer;

import com.bp.beans.IAuthentication;
import com.bp.beans.OAuthJwtToken;
import com.bp.entity.Role;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.role.RoleRequestModel;
import com.bp.usecase.RoleInputBoundary;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

/**
 * Role request consumer.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class RoleConsumer {
	
	Logger logger = Logger.getLogger(PatientConsumer.class.getName());
	
	/**
	 * Role application logic.
	 */
	private final RoleInputBoundary roleInput;
	
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
	
	RoleConsumer(IAuthentication authentication, RoleInputBoundary roleInput, UserInputBoundary userInput) {
		this.authentication = authentication;
		this.roleInput = roleInput;
		this.userInput = userInput;
	}
	
	@GetMapping("/roles")
	public String getRoles(HttpServletRequest httpRequest) throws JsonProcessingException {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is get roles");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		List<Role> roles = roleInput.findAll(authorizeModel);
		return JSONUtility.objToJSON(roles);
	}
	
	@GetMapping("/role/{roleId}")
	public String getRole(HttpServletRequest httpRequest, @PathVariable String roleId) throws JsonProcessingException {
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
			logger.info("User " + authenticatedUser.getEmail() + " is get a role");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		logger.info("User " + authenticatedUser.getEmail() + " is entering patient card with id: " + roleId);
		
		Role role = roleInput.findById(roleId, authorizeModel);
		return JSONUtility.objToJSON(role);
	}
	
	@PostMapping("/role")
	public String createRole(HttpServletRequest httpRequest, @RequestBody RoleRequestModel roleRequestModel) throws Exception {
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
			logger.info("User " + authenticatedUser.getEmail() + " is creating a role");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Role role = roleInput.fillRole(new Role(), roleRequestModel);
		if (role != null) {
			roleInput.createRole(role, authorizeModel);
		}
		return JSONUtility.objToJSON(role);
	}
	
	@DeleteMapping("/role/{roleId}")
	public String deleteRole(HttpServletRequest httpRequest, @PathVariable String roleId) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is deleting a role");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Role role = roleInput.findById(roleId, authorizeModel);
		if (role == null) {
			throw new ResourceNotFoundException("Role does not exist !");
		}
		roleInput.deletePatient(roleId, authorizeModel);
		return "Role was deleted !";
	}
	
	@PostMapping("/role/{roleId}/user/{userId}")
	public String assignRoleToUser(HttpServletRequest httpRequest, @PathVariable int userId, @PathVariable String roleId) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is assigning a role " + roleId + "to user " + userId);
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		authorizeModel.setHostUserId(userId);
		Role role = roleInput.findById(roleId, authorizeModel);
		
		if (role == null) {
			throw new ResourceNotFoundException();
		}
		UserEntity userEntity = userInput.findById(userId, authorizeModel);
		if (userEntity == null) {
			throw new ResourceNotFoundException();
		}
		roleInput.assignRoleToUser(userId, roleId, authorizeModel);
		return "Role was assigned to user!";
	}
	
	@DeleteMapping("/role/{roleId}/user/{userId}")
	public String deleteRoleFromUser(HttpServletRequest httpRequest, @PathVariable int userId, @PathVariable String roleId) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is deleting a role " + roleId + " from user " + userId);
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		authorizeModel.setHostUserId(userId);
		Role role = roleInput.findById(roleId, authorizeModel);
		
		if (role == null) {
			throw new ResourceNotFoundException();
		}
		UserEntity userEntity = userInput.findById(userId, authorizeModel);
		
		if (userEntity == null) {
			throw new ResourceNotFoundException();
		}
		roleInput.deleteRoleFromUser(userId, roleId, authorizeModel);
		return "Role from user was deleted !";
	}
}
