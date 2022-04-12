package com.bp.usecase;

import com.bp.entity.DefaultCapability;
import com.bp.entity.Procedure;
import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.visit.VisitRequestModel;
import com.bp.repository.ProcedureRepository;
import com.bp.repository.UserVisitRepository;
import com.bp.service.IDefaultCapabilityService;
import com.bp.service.IUserService;
import com.bp.service.IUserVisitService;
import com.bp.service.IVisitService;
import com.bp.service.VisitService;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;
import com.bp.utility.UserUtility;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.logging.Logger;

public class VisitIntercator implements VisitInputBoundary {
	
	Logger logger = Logger.getLogger(VisitIntercator.class.getName());
	
	/**
	 * Visit service.
	 */
	private final IVisitService visitService;
	
	/**
	 * Capability application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	/**
	 * User service.
	 */
	private final IUserService userService;
	
	/**
	 * User visit service.
	 */
	private final IUserVisitService userVisitService;
	
	/**
	 * User visit repository.
	 */
	private final UserVisitRepository userVisitRepository;
	
	/**
	 * Procedure repository.
	 */
	private final ProcedureRepository procedureRepository;
	
	/**
	 *  Default capability service.
	 */
	private final IDefaultCapabilityService defaultCapabilityService;
	
	public VisitIntercator(
		VisitService visitService,
		CapabilityInteractor capabilityInteractor,
		IUserService userService,
		IUserVisitService userVisitService,
		ProcedureRepository procedureRepository,
		UserVisitRepository userVisitRepository,
		IDefaultCapabilityService defaultCapabilityService) {
		this.visitService = visitService;
		this.capabilityInteractor = capabilityInteractor;
		this.userService = userService;
		this.userVisitService = userVisitService;
		this.userVisitRepository = userVisitRepository;
		this.procedureRepository = procedureRepository;
		this.defaultCapabilityService = defaultCapabilityService;
	}
	
	@Override
	public void createVisit(Visit visit, AuthorizeModel authorizeModel) {
		visitService.createVisit(visit, authorizeModel);
		capabilityInteractor.createOwner(authorizeModel);
		userVisitRepository.addUserVisit(authorizeModel.getAuthenticatedUser().getUserId(), visit.getVisitId());
	}
	
	@Override
	public boolean assignUserToVisit(int userId, int visitId, AuthorizeModel authorizeModel) {
		if (userService.hasRole(authorizeModel.getHostUserId(), authorizeModel.getGivenRole())) {
			DefaultCapability capability = defaultCapabilityService.findByRoleAndByEntity(authorizeModel);
			authorizeModel.setDefaultCapability(capability);
			userVisitService.assignUserToVisit(userId, visitId, authorizeModel);
			
			// Inherit capabilities
			List<Procedure> procedures = procedureRepository.findProceduresByVisitId(authorizeModel.getEntityId());
			for (Procedure procedure : procedures) {
				AuthorizeModel am = AuthorizeModel.copyAuthorizeModel(authorizeModel);
				am.setEntityType(CS.PROCEDURE);
				am.setEntityId(procedure.getProcedureId());
				capabilityInteractor.copyCapabilitiesForProcedure(userService.findById(userId, null), am);
			}
			
			logger.info("User with ID: " + userId + " was assigned to visit with ID: " + visitId);
		} else if (authorizeModel.getGivenRole().equals(CS.OWNER))  {
			DefaultCapability capability = defaultCapabilityService.findByRoleAndByEntityAndPositionStranger(authorizeModel, CS.OWNER);
			authorizeModel.setDefaultCapability(capability);
			userVisitService.assignUserToVisit(userId, visitId, authorizeModel);
		} else {
			try {
				throw new RoleNotFoundException("The user with ID: " + userId + " has not role: " + authorizeModel.getGivenRole());
			} catch (RoleNotFoundException e) {
				logger.info("The user does not have the necessary role: " + authorizeModel.getGivenRole());
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<Visit> findAll(AuthorizeModel authorizeModel) {
		return visitService.findAll(authorizeModel);
	}
	
	@Override
	public Visit findById(int visitId, AuthorizeModel authorizeModel) {
		return visitService.findById(visitId, authorizeModel);
	}
	
	@Override
	public void saveVisit(Visit visit, AuthorizeModel authorizeModel) {
		visitService.saveVisit(visit, authorizeModel);
	}
	
	@Override
	public void deleteVisit(int visitId, AuthorizeModel authorizeModel) {
		visitService.deleteVisit(visitId, authorizeModel);
	}
	
	@Override
	public void deleteUserFromVisit(int userId, int visitId, AuthorizeModel authorizeModel) {
		userVisitService.deleteUserFromVisit(userId, visitId, authorizeModel);
	}
	
	@Override
	public Visit fillVisit(Visit visit, VisitRequestModel visitRequestModel) {
		try {
			if (visitRequestModel.getEmail() != null) {
				if (UserUtility.isEmailValid(visitRequestModel.getEmail())) {
					visit.setEmail(visitRequestModel.getEmail());
				}
			}
			if (visitRequestModel.getIdentification() != null) {
				visit.setIdentification(UserUtility.transformIdentification(visitRequestModel.getIdentification()));
			}
			if (visitRequestModel.getAddress() != null) {
				visit.setAddress(visitRequestModel.getAddress());
			}
			if (visitRequestModel.getCity() != null) {
				visit.setCity(visitRequestModel.getCity());
			}
			if (visitRequestModel.getFirstName() != null) {
				visit.setFirstName(visitRequestModel.getFirstName());
			}
			if (visitRequestModel.getLastName() != null) {
				visit.setLastName(visitRequestModel.getLastName());
			}
			if (visitRequestModel.getPhone() != null) {
				visit.setPhone(visitRequestModel.getPhone());
			}
			return visit;
		} catch (Exception e) {
			return null;
		}
	}
}
