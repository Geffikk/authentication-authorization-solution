package com.bp.service;

import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.RoleRepository;
import com.bp.repository.UserRoleRepository;
import com.bp.entity.Role;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.List;

public class RoleService implements IRoleService {
	
	/**
	 * Role repository.
	 */
	private final RoleRepository roleRepository;
	
	/**
	 * User role repository.
	 */
	private final UserRoleRepository userRoleRepository;
	
	/**
	 * Capability application logic.
	 */
	@Inject
	private ICapabilityInteractor capabilityInteractor;
	
	@Inject
	public RoleService(RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
	}
	
	@Override
	public Role findById(final String id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.ROLE);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return roleRepository.findById(id);
	}
	
	@Override
	public boolean existById(String roleId) {
		return roleRepository.existById(roleId) == 1;
	}
	
	@Override
	public List<Role> findAll(AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.ROLE);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return roleRepository.findAll();
	}
	
	@Override
	public void createRole(final Role role, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.ROLE);
			authorizeModel.setOperation(CS.sCREATE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		try {
			roleRepository.insert(role);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Resource already exists");
		}
	}
	
	@Override
	public void deleteRole(final String id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.ROLE);
			authorizeModel.setOperation(CS.sDELETE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		roleRepository.delete(id);
	}
	
	@Override
	public List<Role> getUsers(final int userId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.ROLE);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return userRoleRepository.getRoles(userId);
	}
}

