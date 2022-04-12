package com.bp.service;

import com.bp.entity.Role;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;

import java.util.List;

/**
 * User service layer (interface).
 */
public interface IUserService {
	
	/**
	 * Find role by id, after success authorization.
	 * @param id of role
	 * @param authorizeModel contain authorization rules
	 * @return role
	 */
	UserEntity findById(int id, AuthorizeModel authorizeModel);
	
	/**
	 * Find all users, after success authorization.
	 * @param authorizeModel contain authorization rules
	 * @return all users.
	 */
	List<UserEntity> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Create user, after success authorization.
	 * @param userEntity for create
	 * @param authorizeModel contain authorization rules
	 */
	void createUser(UserEntity userEntity, AuthorizeModel authorizeModel);
	
	/**
	 * Save user, after success authorization.
	 * @param userEntity for save
	 * @param authorizeModel contain authorization rules
	 */
	void saveUser(UserEntity userEntity, AuthorizeModel authorizeModel);
	
	/**
	 * Find user by email.
	 * @param mail for find user
	 * @return user
	 */
	UserEntity findUserByEmail(String mail);
	
	/**
	 * Delete user, after success authorization.
	 * @param id of user
	 * @param authorizeModel contain authorization rules
	 */
	void deleteUser(int id, AuthorizeModel authorizeModel);
	
	/**
	 * Get roles of user.
	 * @param userId of user
	 * @return roles
	 */
	List<Role> getRoles(int userId);
	
	/**
	 * Check, if user exist by email in database.
	 * @param email of user
	 * @return true, if user exist in database, otherwise false
	 */
	boolean existByEmail(String email);
	
	/**
	 * Check, if user exist by id in database.
	 * @param id of user
	 * @return true, if user exist in database, otherwise false
	 */
	boolean existById(int id);
	
	/**
	 * Check, if user contain specific role.
	 * @param userId id of user.
	 * @param role role of user.
	 * @return true, if user contain specific role.
	 */
	boolean hasRole(int userId, String role);
	
	/**
	 * Find user by token, after success authorization.
	 * @param substring token body
	 * @param authorizeModel authorization model
	 * @return user entity
	 */
	UserEntity findByToken(String substring, AuthorizeModel authorizeModel);
}
