package com.bp.usecase;

import com.bp.entity.Role;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.role.RoleRequestModel;

import java.util.List;

/**
 * Role application logic.
 */
public interface RoleInputBoundary {
	
	/**
	 * Find all roles.
	 * @param authorizeModel authorization model
	 * @return list of roles
	 */
	List<Role> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Find role by id.
	 * @param roleId id of role
	 * @param authorizeModel authorization model
	 * @return role
	 */
	Role findById(String roleId, AuthorizeModel authorizeModel);
	
	/**
	 * Fill role entity with request data.
	 * @param role of user
	 * @param roleRequestModel request data
	 * @return role entity
	 */
	Role fillRole(Role role, RoleRequestModel roleRequestModel);
	
	/**
	 * Create new role.
	 * @param role new role
	 * @param authorizeModel authorization model
	 */
	void createRole(Role role, AuthorizeModel authorizeModel);
	
	/**
	 * Delete patient.
	 * @param roleId id of role
	 * @param authorizeModel authorization model
	 */
	void deletePatient(String roleId, AuthorizeModel authorizeModel);
	
	/**
	 * Assign role to user.
	 * @param userId id of user
	 * @param roleId id of role
	 * @param authorizeModel authorization model
	 */
	void assignRoleToUser(int userId, String roleId, AuthorizeModel authorizeModel);
	
	/**
	 * Delete role from user.
	 * @param userId id of user
 	 * @param roleId id of role
	 * @param authorizeModel authorization model
	 */
	void deleteRoleFromUser(int userId, String roleId, AuthorizeModel authorizeModel);
}
