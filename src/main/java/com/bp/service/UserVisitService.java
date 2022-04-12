package com.bp.service;

import com.bp.entity.Capability;
import com.bp.entity.Role;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.CapabilityRepository;
import com.bp.repository.UserVisitRepository;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

public class UserVisitService implements IUserVisitService {
	
	Logger logger = Logger.getLogger(UserVisitService.class.getName());
	
	/**
	 * User visit repository.
	 */
	private final UserVisitRepository userVisitRepository;
	
	/**
	 * Capability application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	/**
	 * Capability repository.
	 */
	private final CapabilityRepository capabilityRepository;
	
	/**
	 * User service.
	 */
	private final IUserService userService;
	
	/**
	 * User role service.
	 */
	private final IUserRoleService userRoleService;
	
	/**
	 * Capability service.
	 */
	private final ICapabilityService capabilityService;
	
	public UserVisitService(
		UserVisitRepository userVisitRepository,
		CapabilityInteractor capabilityInteractor,
		IUserRoleService userRoleService,
		ICapabilityService capabilityService,
		CapabilityRepository capabilityRepository,
		IUserService userService) {
		this.userVisitRepository = userVisitRepository;
		this.capabilityInteractor = capabilityInteractor;
		this.capabilityRepository = capabilityRepository;
		this.userService = userService;
		this.userRoleService = userRoleService;
		this.capabilityService = capabilityService;
	}
	
	// Assign visit to user and create capability with default behavior (Doctor / Sister)
	@Override
	@Transactional
	public void assignUserToVisit(int userId, int visitId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setOperation(CS.sCAPABILITY);
			authorizeModel.setEntityId(visitId);
			authorizeModel.setHostUserId(userId);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		
		if (userService.existById(userId)) {
			userVisitRepository.addUserVisit(userId, visitId);
			if (authorizeModel != null && authorizeModel.getGivenRole().equals(CS.OWNER)) {
				capabilityInteractor.createPersonalCapability(authorizeModel);
			} else {
				capabilityInteractor.createDefaultCapability(authorizeModel);
			}
		}
	}
	
	@Override
	@Transactional
	public void deleteUserFromVisit(int userId, int visitId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setOperation(CS.sCAPABILITY);
			authorizeModel.setEntityId(visitId);
			authorizeModel.setHostUserId(userId);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		UserEntity user = userVisitRepository.getUserInVisit(userId, visitId);
		List<Role> roles = userRoleService.getRoles(userId, null);
		
		if (user != null) {
			List<Capability> capabilities = capabilityRepository.findCapability(roles, CS.VISIT, visitId, user.getEmail());
			for (Capability capability : capabilities) {
				if (capability.getPriority() != 1) {
					userRoleService.deleteRole(userId, capability.getRole(), null);
				}
			}
			
			AuthorizeModel am = getAuthorizeModel(visitId, roles, user);
			Capability capability = capabilityService.findCapability(am);
			
			if (capability != null && capability.getPriority() == 3) {
				capabilityService.deleteCapability(capability.getCapId(), authorizeModel);
			}
		} else {
			logger.info("User with ID: " + userId + "do not exist in visit with ID: " + visitId);
			throw new NullPointerException("The user not exist in this visit !");
		}
	}
	
	private AuthorizeModel getAuthorizeModel(int visitId, List<Role> roles, UserEntity user) {
		AuthorizeModel am = new AuthorizeModel();
		am.setRoles(roles);
		am.setEntityType(CS.VISIT);
		am.setEntityId(visitId);
		am.setAuthenticatedUser(user);
		return am;
	}
}
