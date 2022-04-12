package com.bp.service;

import com.bp.entity.Capability;
import com.bp.model.capability.AuthorizeModel;

import java.util.List;

/**
 * Interface of capability service.
 */
public interface ICapabilityService {
	
	/**
	 * Find all capabilities, after success authorization.
	 * @param authorizeModel authorization model
	 * @return list of capabilities
	 */
	List<Capability> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Find capability by id, after success authorization.
	 * @param capId of capability
	 * @param authorizeModel authorization model
	 * @return capability
	 */
	Capability findById(int capId, AuthorizeModel authorizeModel);
	
	/**
	 * Find specific capability by defined authorize model.
	 * @param authorizeModel authorize model.
	 * @return capability
	 */
	Capability findCapability(AuthorizeModel authorizeModel);
	
	/**
	 * Find specific capability by role.
	 * @param authorizeModel authorization model
	 * @return capability
	 */
	Capability findBySpecificRole(AuthorizeModel authorizeModel);
	
	/**
	 * Find list of capabilities by entity.
	 * @param entityType type of entity
	 * @param id id of entity
	 * @return list of capabilities
	 */
	List<Capability> findByEntity(String entityType, int id);
	
	/**
	 * Find capability by specific user.
	 * @param entityType type of entity
	 * @param entityId id of entity
	 * @param user user email
	 * @return capability
	 */
	Capability findByUser(String entityType, Integer entityId, String user);
	
	/**
	 * Delete capability from database.
	 * @param capId of capability
	 * @param authorizeModel authorization model
	 */
	void deleteCapability(int capId, AuthorizeModel authorizeModel);
	
	/**
	 * Create new capability for user, role or group.
	 * @param capability capability definition
	 * @param authorizeModel authorization model
	 */
	void createCapability(Capability capability, AuthorizeModel authorizeModel);
	
	/**
	 * Save capability.
	 * @param capability capability definition
	 * @param authorizeModel authorization model
	 */
	void saveCapability(Capability capability, AuthorizeModel authorizeModel);
}
