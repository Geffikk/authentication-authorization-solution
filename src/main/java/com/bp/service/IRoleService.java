package com.bp.service;

import com.bp.entity.Role;
import com.bp.model.capability.AuthorizeModel;

import java.util.List;

/**
 * Role service layer (interface).
 */
public interface IRoleService {
	
	/**
	 * Find role by id, after success authorization.
	 * @param roleId of role
	 * @return role
	 */
	Role findById(String roleId, AuthorizeModel authorizeModel);
	
	/**
	 * Find all roles, after success authorization.
	 * @param authorizeModel contain authorization rules
	 * @return all roles
	 */
	List<Role> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Save role, after success authorization.
	 * @param role for save
	 */
	void createRole(Role role, AuthorizeModel authorizeModel);
	
	/**
	 * Delete role, after success authorization.
	 * @param id for role
	 */
	void deleteRole(String id, AuthorizeModel authorizeModel);
	
	/**
	 * Get role users, after success authorization.
	 * @param userId of user
	 * @return users
	 */
	List<Role> getUsers(int userId, AuthorizeModel authorizeModel);
	
	/**
	 * Check, if role exist in database.
	 * @param roleId of role
	 * @return role
	 */
	boolean existById(String roleId);
}
