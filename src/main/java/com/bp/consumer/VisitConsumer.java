package com.bp.consumer;

import com.bp.beans.IAuthentication;
import com.bp.beans.OAuthJwtToken;
import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.role.RoleRequestModel;
import com.bp.model.visit.VisitRequestModel;
import com.bp.usecase.VisitInputBoundary;
import com.bp.utility.JSONUtility;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
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

@RestController
@RequestMapping(value = "/api/v1")
public class VisitConsumer {
	
	Logger logger = Logger.getLogger(VisitConsumer.class.getName());
	
	/**
	 * Visit application logic.
	 */
	private final VisitInputBoundary visitInput;
	
	/**
	 * User authentication.
	 */
	private final IAuthentication authentication;
	
	/**
	 * JWT auth token holder.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	VisitConsumer(IAuthentication authentication, VisitInputBoundary visitInteractor) {
		this.authentication = authentication;
		this.visitInput = visitInteractor;
	}
	
	@GetMapping("/visits")
	public String getVisits(HttpServletRequest httpRequest) throws JsonProcessingException {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is getting visits");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		List<Visit> visitList = visitInput.findAll(authorizeModel);
		if (visitList == null) {
			throw new ResourceNotFoundException();
		}
		return JSONUtility.objToJSON(visitList);
	}
	
	@GetMapping("/visit/{visitId}")
	public String getVisit(HttpServletRequest httpRequest, @PathVariable int visitId) throws JsonProcessingException, InterruptedException {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = new Timer();
		metricRegistry.register("Timer 1 (GET VISIT)", timer);
		
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + "  is entering visit with id: " + visitId);
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Visit visit = visitInput.findById(visitId, authorizeModel);
		
		if (visit == null) {
			throw new ResourceNotFoundException();
		}
		return JSONUtility.objToJSON(visit);
	}
	
	@PostMapping("/visit")
	public String createVisit(HttpServletRequest httpRequest, @RequestBody VisitRequestModel visitRequestModel) throws Exception {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is creating visit");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Visit visit = visitInput.fillVisit(new Visit(), visitRequestModel);
		
		if (visit != null) {
			visitInput.createVisit(visit, authorizeModel);
		}
		return JSONUtility.objToJSON(visit);
	}
	
	@PutMapping("/visit/{visitId}")
	public String updateVisit(HttpServletRequest httpRequest, @RequestBody VisitRequestModel visitRequestModel, @PathVariable int visitId) throws Exception {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = new Timer();
		metricRegistry.register("Timer 1 (UPDATE VISIT)", timer);
		
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is updating visit");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Visit visit = visitInput.findById(visitId, authorizeModel);
		
		if (visit == null) {
			throw new ResourceNotFoundException();
		}
		visit = visitInput.fillVisit(visit, visitRequestModel);
		
		if (visit != null) {
			visitInput.saveVisit(visit, authorizeModel);
		}
		return JSONUtility.objToJSON(visit);
	}
	
	@PostMapping("/visit/{visitId}/user/{userId}")
	public String addUserToVisit(HttpServletRequest httpRequest, @PathVariable int userId, @PathVariable int visitId, @RequestBody RoleRequestModel roleRequestModel) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is adding user to visit");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		authorizeModel.setGivenRole(roleRequestModel.getRole());
		authorizeModel.setHostUserId(userId);
		
		Visit visit = visitInput.findById(visitId, authorizeModel);
		if (visit == null) {
			throw new ResourceNotFoundException();
		}
		if (!visitInput.assignUserToVisit(userId, visitId, authorizeModel)) {
			return "The user does not have the necessary role: " + roleRequestModel.getRole();
		}
		return roleRequestModel.getRole() + " was added to visit !";
	}
	
	@DeleteMapping("/visit/{visitId}")
	public String deleteVisit(HttpServletRequest httpRequest, @PathVariable int visitId) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is deleting visit");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Visit visit = visitInput.findById(visitId, authorizeModel);
		
		if (visit == null) {
			throw new ResourceNotFoundException();
		}
		visitInput.deleteVisit(visitId, authorizeModel);
		return "Visit was deleted !";
	}
	
	@DeleteMapping("/visit/{visitId}/user/{userId}")
	public String deleteUserFromVisit(HttpServletRequest httpRequest, @PathVariable int visitId, @PathVariable int userId) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is deleting user from visit");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Visit visit = visitInput.findById(visitId, authorizeModel);
		
		if (visit == null) {
			throw new ResourceNotFoundException();
		}
		visitInput.deleteUserFromVisit(userId, visitId, authorizeModel);
		return "User was deleted !";
	}
}
