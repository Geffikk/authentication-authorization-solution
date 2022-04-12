package com.bp.service;

import com.bp.entity.DefaultCapability;
import com.bp.model.capability.AuthorizeModel;

/**
 * Interface of default capability service.
 */
public interface IDefaultCapabilityService {
	
	/**
	 * Find default capability by name (user for only for tests !)
	 * @param name of default capability
	 * @return default capability
	 */
	DefaultCapability findByName(String name);
	
	/**
	 * Find default capability by role and entity, after success authorization.
	 * @param authorizeModel authorization model
	 * @return default capability
	 */
	DefaultCapability findByRoleAndByEntity(AuthorizeModel authorizeModel);
	
	/**
	 * Find default capability by role, entity and position, after success authorization.
	 * @param authorizeModel authorization model
	 * @param position position of user
	 * @return default capability
	 */
	DefaultCapability findByRoleAndByEntityAndPosition(AuthorizeModel authorizeModel, String position);
	
	/**
	 * Find default capability by role, entity and position foreign user, after success authorization.
	 * @param authorizeModel authorization model
	 * @param position position of user
	 * @return default capability
	 */
	DefaultCapability findByRoleAndByEntityAndPositionStranger(AuthorizeModel authorizeModel, String position);
}
