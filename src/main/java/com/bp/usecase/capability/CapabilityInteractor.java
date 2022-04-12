package com.bp.usecase.capability;

import com.bp.entity.Capability;
import com.bp.entity.DefaultCapability;
import com.bp.entity.Role;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.capability.CapabilityRequestModel;
import com.bp.repository.UserRoleRepository;
import com.bp.service.ICapabilityService;
import com.bp.service.IDefaultCapabilityService;
import com.bp.service.IRoleService;
import com.bp.service.IUserRoleService;
import com.bp.service.IUserService;
import com.bp.utility.CS;
import org.springframework.security.access.AccessDeniedException;

import javax.inject.Inject;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.logging.Logger;

public class CapabilityInteractor implements ICapabilityInteractor {
	
	Logger logger = Logger.getLogger(CapabilityInteractor.class.getName());
	
	/**
	 * Capability service
	 * (Annotation dependency injection due to circular dependencies)
	 */
	@Inject
	private ICapabilityService capabilityService;
	
	/**
	 * User service
	 * (Annotation dependency injection due to circular dependencies)
	 */
	@Inject
	private IUserService userService;
	
	/**
	 * User Role service
	 * (Annotation dependency injection due to circular dependencies)
	 */
	@Inject
	private IUserRoleService userRoleService;
	
	/**
	 * User Role service
	 * (Annotation dependency injection due to circular dependencies)
	 */
	@Inject
	private UserRoleRepository userRoleRepository;
	
	/**
	 * Default capability service
	 * (Annotation dependency injection due to circular dependencies)
	 */
	@Inject
	private IDefaultCapabilityService defaultCapabilityService;
	
	/**
	 * Role service
	 * (Annotation dependency injection due to circular dependencies)
	 */
	@Inject
	private IRoleService roleService;
	
	
	public boolean isPermitted(AuthorizeModel authorizeModel) {
		// Automatic operations, no executed by user.
		if (authorizeModel.getAuthenticatedUser() == null) {
			return true;
		}
		
		List<Role> roles = userRoleRepository.getRoles(authorizeModel.getAuthenticatedUser().getUserId());
		authorizeModel.setRoles(roles);
		Boolean isPermitted = null;
		Capability capability;
		
		capability = capabilityService.findCapability(authorizeModel);
		
		// Choose operation for permitting
		if (capability != null) {
			switch (authorizeModel.getOperation()) {
				case CS.sREAD:
					isPermitted = capability.isRead();
					break;
				case CS.sUPDATE:
					isPermitted = capability.isUpdate();
					break;
				case CS.sCREATE:
					isPermitted = capability.isCreate();
					break;
				case CS.sDELETE:
					isPermitted = capability.isDelete();
					break;
				case CS.sCAPABILITY:
					isPermitted = capability.isCapability();
					break;
				default:
					throw new InvalidParameterException("Invalid operation !");
			}
		}
		if (isPermitted == null || !isPermitted) {
			logger.info(authorizeModel.getAuthenticatedUser().getEmail() + " does not have access to do this !");
			throw new AccessDeniedException(authorizeModel.getAuthenticatedUser().getEmail() + " does not have access do this !");
		}
		logger.info(authorizeModel.getAuthenticatedUser().getEmail() + " has access for: " + authorizeModel.getOperation());
		return true;
	}
	
	@Override
	public void createDefaultCapability(AuthorizeModel authorizeModel) {
		if (authorizeModel.getGivenRole().equals(CS.PERSONAL)) {
			createOwner(authorizeModel);
			return;
		}
		Role newRole = new Role();
		newRole.setRole(authorizeModel.getGivenRole());
		newRole.setName(
			authorizeModel.getGivenRole().toUpperCase() + "_" +
			authorizeModel.getEntityType().toUpperCase() + "_" +
			authorizeModel.getEntityId());
		List<Role> roles = userRoleService.getRoles(authorizeModel.getHostUserId(), null);
		authorizeModel.setGivenRole(newRole.getName());
		authorizeModel.setRoles(roles);
		
		// If role no exist, then create it.
		if (!roleService.existById(newRole.getName())) {
			roleService.createRole(newRole, null);
		}
		// If user not has this role, add it to him.
		if (!userService.hasRole(authorizeModel.getHostUserId(), newRole.getName())) {
			userRoleService.addRoleWithOrigin(authorizeModel.getHostUserId(), newRole.getName(), newRole.getRole(), null);
		}
		
		Capability capability = capabilityService.findBySpecificRole(authorizeModel);
		
		if (capability == null) {
			capability = Capability.prepareCapability(authorizeModel, 2);
			capability.setUser(null);
			logger.info("Capability for " + capability.getRole() + " created !");
			capabilityService.createCapability(capability, null);
		}
	}
	
	@Override
	public void createPersonalCapability(AuthorizeModel authorizeModel) {
		Capability capability = capabilityService.findByUser(
			authorizeModel.getEntityType(),
			authorizeModel.getEntityId(),
			userService.findById(authorizeModel.getHostUserId(), null).getEmail());
		
		if (capability == null) {
			capability = Capability.prepareCapability(authorizeModel, 3);
			capability.setRole(CS.PERSONAL);
			capability.setUser(userService.findById(authorizeModel.getHostUserId(), null).getEmail());
			logger.info("Capability for " + capability.getRole() + " created !");
			capabilityService.createCapability(capability, null);
		} else {
			capability = Capability.prepareCapability(authorizeModel, 3);
			capability.setUser(userService.findById(authorizeModel.getHostUserId(), null).getEmail());
			capability.setRole(CS.PERSONAL);
			logger.info("Capability for " + capability.getRole() + " updated !");
			capabilityService.saveCapability(capability, null);
		}
	}
	
	@Override
	public Capability createCapability(CapabilityRequestModel capReqModel, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setOperation(CS.sCAPABILITY);
			authorizeModel.setEntityType(capReqModel.getEntityType());
			authorizeModel.setEntityId(capReqModel.getEntityId());
			isPermitted(authorizeModel);
		}
		AuthorizeModel amForNewCapability = new AuthorizeModel();
		amForNewCapability.setEntityType(capReqModel.getEntityType());
		amForNewCapability.setEntityId(capReqModel.getEntityId());
		amForNewCapability.setGivenRole(capReqModel.getRole());
		
		if (authorizeModel != null && capReqModel.getUser() != null) {
			UserEntity user = userService.findUserByEmail(capReqModel.getUser());
			amForNewCapability.setAuthenticatedUser(user);
		} else if (authorizeModel != null) {
			amForNewCapability.setAuthenticatedUser(authorizeModel.getAuthenticatedUser());
		}
		Capability capability = capabilityService.findCapability(amForNewCapability);

		if (capability == null) {
			capability = new Capability();
			capabilityService.createCapability(capability, authorizeModel);
		}
		
		capability.setPriority(1);
		if (capReqModel.isCreate() != null) {
			capability.setCreate(capReqModel.isCreate());
		}
		if (capReqModel.isRead() != null) {
			capability.setRead(capReqModel.isRead());
		}
		if (capReqModel.isDelete() != null) {
			capability.setDelete(capReqModel.isDelete());
		}
		if (capReqModel.isCapability() != null) {
			capability.setCapability(capReqModel.isCapability());
		}
		if (capReqModel.isUpdate() != null) {
			capability.setUpdate(capReqModel.isUpdate());
		}
		if (capReqModel.getEntityType() != null) {
			capability.setEntityType(capReqModel.getEntityType());
		}
		if (capReqModel.getEntityId() != 0) {
			if (capReqModel.getRole().equals(CS.PERSONAL)) {
				capability.setPriority(2);
			}
			capability.setEntityId(capReqModel.getEntityId());
		}
		if (capReqModel.getUser() != null) {
			capability.setUser(capReqModel.getUser());
		}
		if (capReqModel.getRole() != null) {
			if (capReqModel.getRole().equals(CS.PERSONAL)) {
				capability.setPriority(3);
			}
			capability.setRole(capReqModel.getRole());
		}
		
		capabilityService.saveCapability(capability, null);
		logger.info("Capability for: " + capability.getUser() + " create !");
		return capability;
	}

	@Override
	public void createOwner(AuthorizeModel authorizeModel) {
		authorizeModel.setDefaultCapability(defaultCapabilityService.findByRoleAndByEntityAndPosition(authorizeModel, CS.OWNER));
		authorizeModel.setGivenRole(CS.PERSONAL);
		Capability capability = Capability.prepareCapability(authorizeModel, 3);
		capabilityService.createCapability(capability, null);
		logger.info(authorizeModel.getAuthenticatedUser().getEmail() + " owner capability created !");
	}
	
	@Override
	public List<Capability> findAll(AuthorizeModel authorizeModel) {
		authorizeModel.setEntityType(CS.sCAPABILITY);
		authorizeModel.setOperation(CS.sREAD);
		return capabilityService.findAll(authorizeModel);
	}
	
	@Override
	public Capability findById(int capId, AuthorizeModel authorizeModel) {
		return capabilityService.findById(capId, authorizeModel);
	}
	
	@Override
	public void deleteCapability(int capId, AuthorizeModel authorizeModel) {
		capabilityService.deleteCapability(capId, authorizeModel);
	}
	
	@Override
	public void copyCapabilitiesForProcedure(UserEntity user, AuthorizeModel authorizeModel) {
		List<Role> roles = userRoleService.getRoles(user.getUserId(), null);
		authorizeModel.setHostUserId(user.getUserId());
		for (Role role : roles) {
			authorizeModel.setGivenRole(role.getName());
			DefaultCapability capability = defaultCapabilityService.findByRoleAndByEntity(authorizeModel);
			authorizeModel.setDefaultCapability(capability);
			if (capability != null) {
				createDefaultCapability(authorizeModel);
			}
		}
	}
}
