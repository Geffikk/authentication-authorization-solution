package com.bp.service;

import com.bp.entity.Role;
import com.bp.model.capability.AuthorizeModel;

import java.util.List;

/**
 * Interface of capability service.
 */
public interface IUserRoleService {
	
	/**
	 * Assign role to user, after success authorization.
	 * @param userId of user
	 * @param role of user
	 * @param authorizeModel authorization model
	 */
	void addRole(int userId, String role, AuthorizeModel authorizeModel);
	
	/**
	 * Assign role to user with origin format of role, after success authorization.
	 * @param userId of user
	 * @param role of user
	 * @param origin of role
	 * @param authorizeModel authorization model
	 */
	void addRoleWithOrigin(int userId, String role, String origin, AuthorizeModel authorizeModel);
	
	/**
	 * Delete role from user, after success authorization.
	 * @param userId of user
	 * @param role of user
	 * @param authorizeModel authorization model
	 */
	void deleteRole(int userId, String role, AuthorizeModel authorizeModel);
	
	/**
	 * Get all roles from database, after success authorization.
	 * @param userId of user
	 * @return all roles
	 */
	List<Role> getRoles(int userId, AuthorizeModel authorizeModel);
}
