package com.bp.consumer;

import com.bp.beans.IAuthentication;
import com.bp.beans.OAuthJwtToken;
import com.bp.entity.Patient;
import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.patient.PatientRequestModel;
import com.bp.service.IVisitService;
import com.bp.usecase.PatientInputBoundary;
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
 * Patient request consumer.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class PatientConsumer {
	
	Logger logger = Logger.getLogger(PatientConsumer.class.getName());
	
	/**
	 * Patient application logic.
	 */
	private final PatientInputBoundary patientInput;
	
	/**
	 * Service implementation of visit entity.
	 */
	private final IVisitService visitService;
	
	/**
	 * User authentication.
	 */
	private final IAuthentication authentication;
	
	/**
	 * JWT auth token holder.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	PatientConsumer(IAuthentication authentication, PatientInputBoundary patientInput, IVisitService visitService) {
		this.authentication = authentication;
		this.patientInput = patientInput;
		this.visitService = visitService;
	}
	
	@GetMapping("/patients")
	public String getPatients(HttpServletRequest httpRequest) throws JsonProcessingException {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		
		if (authenticatedUser == null) {
			throw new AccessDeniedException("Token has expired, please log in");
		} else {
			logger.info("User " + authenticatedUser.getEmail() + " is get a patient card");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		List<Patient> patients = patientInput.findAll(authorizeModel);
		
		if (patients == null) {
			throw new ResourceNotFoundException();
		}
		return JSONUtility.objToJSON(patients);
	}
	
	@GetMapping("/patient/{patientId}/visits")
	public String getVisitsOfPatient(HttpServletRequest httpRequest, @PathVariable int patientId) throws JsonProcessingException {
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
			logger.info("User " + authenticatedUser.getEmail() + " is get a patient card");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		List<Visit> visitList = visitService.findVisitsByPatientId(patientId, authorizeModel);
		
		if (visitList == null) {
			throw new ResourceNotFoundException();
		}
		return JSONUtility.objToJSON(visitList);
	}
	
	@GetMapping("/patient/{patientId}")
	public String getPatient(HttpServletRequest httpRequest, @PathVariable int patientId) throws JsonProcessingException {
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
			logger.info("User " + authenticatedUser.getEmail() + " is get a patient card");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		logger.info("User " + authenticatedUser.getEmail() + " is entering patient card with id: " + patientId);
		
		Patient patient = patientInput.findById(patientId, authorizeModel);
		
		if (patient == null) {
			throw new ResourceNotFoundException();
		}
		return JSONUtility.objToJSON(patient);
	}
	
	@PostMapping("/patient")
	public String createPatient(HttpServletRequest httpRequest, @RequestBody PatientRequestModel patientRequestModel) throws Exception {
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
			logger.info("User " + authenticatedUser.getEmail() + " is creating a patient card");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Patient patient = patientInput.fillPatient(new Patient(), patientRequestModel);
		
		if (patient != null) {
			patientInput.createPatient(patient, authorizeModel);
		}
		return JSONUtility.objToJSON(patient);
	}
	
	@PutMapping("/patient/{patientId}")
	public String updatePatient(HttpServletRequest httpRequest, @RequestBody PatientRequestModel patientRequestModel, @PathVariable int patientId) throws Exception {
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
			logger.info("User " + authenticatedUser.getEmail() + " is updating a patient");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		Patient patient = patientInput.findById(patientId, authorizeModel);
		
		if (patient == null) {
			throw new ResourceNotFoundException();
		}
		patient = patientInput.fillPatient(patient, patientRequestModel);
		patientInput.savePatient(patient, authorizeModel);
		return JSONUtility.objToJSON(patient);
	}
	
	@DeleteMapping("/patient/{patientId}")
	public String deletePatient(HttpServletRequest httpRequest, @PathVariable int patientId) {
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
			logger.info("User " + authenticatedUser.getEmail() + " is updating a patient");
		}
		authorizeModel.setAuthenticatedUser(authenticatedUser);
		logger.info("User " + authenticatedUser.getEmail() + " is deleting a patient");
		
		Patient patient = patientInput.findById(patientId, authorizeModel);
		if (patient == null) {
			throw new ResourceNotFoundException();
		}
		patientInput.deletePatient(patientId, authorizeModel);
		return "Patient was deleted !";
	}
}
