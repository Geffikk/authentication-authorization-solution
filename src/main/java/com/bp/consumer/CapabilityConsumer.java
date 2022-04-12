package com.bp.consumer;

import com.bp.beans.IAuthentication;
import com.bp.beans.OAuthJwtToken;
import com.bp.entity.Capability;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.capability.CapabilityRequestModel;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.JSONUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
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
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Capability request consumer.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class CapabilityConsumer {
	
	/**
	 * Capability application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	/**
	 * Authentication information of user.
	 */
	private final IAuthentication authentication;
	
	/**
	 * JWT auth token holder.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	CapabilityConsumer(ICapabilityInteractor capabilityInteractor, IAuthentication authentication) {
		this.capabilityInteractor = capabilityInteractor;
		this.authentication = authentication;
	}
	
	@GetMapping("/capabilities")
	public String getCapabilities(HttpServletRequest httpRequest) throws JsonProcessingException {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new org.springframework.security.access.AccessDeniedException("Token has expired, please log in");
		}
		
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		List<Capability> capability = capabilityInteractor.findAll(authorizeModel);
		return JSONUtility.objToJSON(capability);
	}
	
	@GetMapping("/capability/{capId}")
	public String getCapability(@PathVariable int capId, HttpServletRequest httpRequest) throws JsonProcessingException {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new org.springframework.security.access.AccessDeniedException("Token has expired, please log in");
		}
		
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Capability capability = capabilityInteractor.findById(capId, authorizeModel);
		return JSONUtility.objToJSON(capability);
	}
	
	@PostMapping("/capability")
	public String createCapability(HttpServletRequest httpRequest, @RequestBody CapabilityRequestModel capReqModel) throws JsonProcessingException {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new org.springframework.security.access.AccessDeniedException("Token has expired, please log in");
		}
		
		authorizeModel.setEntityType(capReqModel.getEntityType());
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		
		Capability capability = capabilityInteractor.createCapability(capReqModel, authorizeModel);
		return JSONUtility.objToJSON(capability);
	}
	
	@DeleteMapping("/capability/{capId}")
	public String deleteCapability(HttpServletRequest httpRequest, @PathVariable int capId) throws JsonProcessingException, AccessDeniedException {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		}
		
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Capability capability = capabilityInteractor.findById(capId, authorizeModel);
		if (capability == null) {
			throw new ResourceNotFoundException();
		}
		
		capabilityInteractor.deleteCapability(capId, authorizeModel);
		return JSONUtility.objToJSON(capability);
	}
}
