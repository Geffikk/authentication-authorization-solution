package com.bp.service;

import com.bp.entity.Role;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.UserRoleRepository;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;

import java.util.List;

public class UserRoleService implements IUserRoleService {
	
	/**
	 * User role repository.
	 */
	private final UserRoleRepository userRoleRepository;
	
	/**
	 * Capability application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	public UserRoleService(UserRoleRepository userRoleRepository, ICapabilityInteractor capabilityInteractor) {
		this.userRoleRepository = userRoleRepository;
		this.capabilityInteractor = capabilityInteractor;
	}

	@Override
	public void addRole(int userId, String role, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.sUSER);
			authorizeModel.setOperation(CS.sUPDATE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		userRoleRepository.addRole(userId, role, null);
	}
	
	@Override
	public void addRoleWithOrigin(int userId, String role, String origin, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.sUSER);
			authorizeModel.setOperation(CS.sUPDATE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		userRoleRepository.addRole(userId, role, origin);
	}
	
	@Override
	public void deleteRole(int userId, String role, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.sUSER);
			authorizeModel.setOperation(CS.sUPDATE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		userRoleRepository.deleteRole(userId, role);
		userRoleRepository.deleteExtraRolesFromUser(userId, role);
	}
	
	@Override
	public List<Role> getRoles(int userId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.ROLE);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return userRoleRepository.getRoles(userId);
	}
}
