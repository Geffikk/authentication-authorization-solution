package com.bp.usecase;

import com.bp.entity.Role;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.role.RoleRequestModel;
import com.bp.service.IRoleService;
import com.bp.service.IUserRoleService;

import java.util.List;

public class RoleInteractor implements RoleInputBoundary {
	
	/**
	 * Role service.
	 */
	private final IRoleService roleService;
	
	/**
	 * User role service.
	 */
	private final IUserRoleService userRoleService;
	
	public RoleInteractor(IRoleService roleService, IUserRoleService userRoleService) {
		this.roleService = roleService;
		this.userRoleService = userRoleService;
	}
	
	@Override
	public List<Role> findAll(AuthorizeModel authorizeModel) {
		return roleService.findAll(authorizeModel);
	}
	
	@Override
	public Role findById(String roleId, AuthorizeModel authorizeModel) {
		return roleService.findById(roleId, authorizeModel);
	}
	
	@Override
	public void createRole(Role role, AuthorizeModel authorizeModel) {
		roleService.createRole(role, authorizeModel);
	}
	
	@Override
	public void deletePatient(String roleId, AuthorizeModel authorizeModel) {
		roleService.deleteRole(roleId, authorizeModel);
	}
	
	@Override
	public void assignRoleToUser(int userId, String roleId, AuthorizeModel authorizeModel) {
		userRoleService.addRole(userId, roleId, authorizeModel);
	}
	
	@Override
	public void deleteRoleFromUser(int userId, String roleId, AuthorizeModel authorizeModel) {
		userRoleService.deleteRole(userId, roleId, authorizeModel);
	}
	
	@Override
	public Role fillRole(Role role, RoleRequestModel roleRequestModel) {
		if (roleRequestModel.getRole() != null) {
			role.setName(roleRequestModel.getRole());
		}
		return role;
	}
}
