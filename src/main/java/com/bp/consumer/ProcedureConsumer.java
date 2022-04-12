package com.bp.consumer;

import com.bp.beans.IAuthentication;
import com.bp.beans.OAuthJwtToken;
import com.bp.entity.Procedure;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.procedure.ProcedureRequestModel;
import com.bp.usecase.ProcedureInputBoundary;
import com.bp.utility.CS;
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

/**
 * Procedure request consumer.
 */
@RestController
@RequestMapping(value = "/api/v1/visit/{visitId}")
public class ProcedureConsumer {
	
	Logger logger = Logger.getLogger(ProcedureConsumer.class.getName());
	
	/**
	 * User authentication.
	 */
	private final IAuthentication authentication;
	
	/**
	 * Procedure application logic.
	 */
	private final ProcedureInputBoundary procedureInput;
	
	/**
	 * JWT auth token holder.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	ProcedureConsumer(ProcedureInputBoundary procedureInput, IAuthentication authentication) {
		this.authentication = authentication;
		this.procedureInput = procedureInput;
	}
	
	@GetMapping("/procedures")
	public String getProcedures(HttpServletRequest httpRequest, @PathVariable int visitId) throws JsonProcessingException {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is get a procedures");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		List<Procedure> procedures = procedureInput.findProceduresByVisit(visitId, authorizeModel);
		
		if (procedures == null) {
			throw new ResourceNotFoundException();
		}
		return JSONUtility.objToJSON(procedures);
	}
	
	@GetMapping("/procedure/{procedureId}")
	public String getProcedure(HttpServletRequest httpRequest, @PathVariable int visitId, @PathVariable int procedureId) throws JsonProcessingException {
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		AuthorizeModel authorizeModel = new AuthorizeModel();
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = new Timer();
		metricRegistry.register("Timer 1 (GET PROCEDURE)", timer);
		
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is entering a procedure");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Procedure procedure = procedureInput.findById(procedureId, authorizeModel);
		
		if (procedure == null) {
			throw new ResourceNotFoundException();
		}
		if (procedure.getVisitId() != visitId) {
			throw new AccessDeniedException("You cannot access procedure via this visit");
		}
		return JSONUtility.objToJSON(procedure);
	}
	
	@PostMapping("/procedure")
	public String createProcedure(HttpServletRequest httpRequest, @RequestBody ProcedureRequestModel procedureRequestModel, @PathVariable int visitId) throws Exception {
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
			logger.info("User " + authenticatedUser.getEmail() + " is creating a procedure");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		procedureRequestModel.setVisitId(visitId);
		Procedure procedure = procedureInput.fillProcedure(new Procedure(), procedureRequestModel);
		procedureInput.createProcedure(visitId, procedure, authorizeModel);
		return JSONUtility.objToJSON(procedure);
	}
	
	@PutMapping("/procedure/{procedureId}")
	public String updateProcedure(
		HttpServletRequest httpRequest,
		@RequestBody ProcedureRequestModel procedureRequestModel,
		@PathVariable int procedureId) throws Exception {
		
		if (oAuthJwtToken.getJwtToken() != null) {
			if (oAuthJwtToken.getJwtToken().equals("null")) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
			}
		}
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = new Timer();
		metricRegistry.register("Timer 1 (UPDATE PROCEDURE)", timer);
		
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is updating a procedure");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		
		Procedure procedure = procedureInput.findById(procedureId, authorizeModel);
		if (procedure == null) {
			throw new ResourceNotFoundException();
		}
		procedure = procedureInput.fillProcedure(procedure, procedureRequestModel);
		procedureInput.saveProcedure(procedure, authorizeModel);
		return JSONUtility.objToJSON(procedure);
	}
	
	@DeleteMapping("/procedure/{procedureId}")
	public String deleteProcedure(HttpServletRequest httpRequest, @PathVariable int visitId, @PathVariable int procedureId) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is deleting a procedure");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		authorizeModel.setEntityId(procedureId);
		authorizeModel.setOperation(CS.sREAD);
		
		Procedure procedure = procedureInput.findById(procedureId, authorizeModel);
		if (procedure == null) {
			throw new ResourceNotFoundException();
		}
		if (procedure.getVisitId() != visitId) {
			throw new AccessDeniedException("You cannot access procedure via this visit: ID " + visitId);
		}
		procedureInput.deleteProcedure(procedureId, authorizeModel);
		return "Procedure was deleted !";
	}
}
