package com.bp.usecase.capability;

import com.bp.entity.Capability;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.capability.CapabilityRequestModel;

import java.util.List;

/**
 * Interface of capability application logic.
 */
public interface ICapabilityInteractor {
	
	/**
	 * User is permitted.
	 * @param authorizeModel authorization model
	 * @return true, if user is permitted, false otherwise
	 */
	boolean isPermitted(AuthorizeModel authorizeModel);
	
	/**
	 * Create owner in some specific entity. Owner available all operations with entity.
	 * @param authorizeModel authorization model
	 */
	void createOwner(AuthorizeModel authorizeModel);
	
	/**
	 * Create default capability based on role.
	 * @param authorizeModel authorization model
	 */
	void createDefaultCapability(AuthorizeModel authorizeModel);
	
	/**
	 * Create capability for user, role or group.
	 * @param capReqModel capability request model
	 * @param authorizeModel authorization model
	 * @return capability
	 */
	Capability createCapability(CapabilityRequestModel capReqModel, AuthorizeModel authorizeModel);
	
	/**
	 * Create personal capability for user, with specific conditions.
	 * @param authorizeModel authorization model
	 */
	void createPersonalCapability(AuthorizeModel authorizeModel);
	
	/**
	 * Find all capabilities.
	 * @param authorizeModel authorization model
	 * @return list of capabilities
	 */
	List<Capability> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Find capability by id.
	 * @param capId id of capability
	 * @param authorizeModel authorization model
	 * @return capability
	 */
	Capability findById(int capId, AuthorizeModel authorizeModel);
	
	/**
	 * Delete capability.
	 * @param capId id of capability
	 * @param authorizeModel authorization model
	 */
	void deleteCapability(int capId, AuthorizeModel authorizeModel);
	
	/**
	 * Inheritance capabilities
	 * @param user user, who capabilities will be copy
	 * @param authorizeModel authorize model specific user
	 */
	void copyCapabilitiesForProcedure(UserEntity user, AuthorizeModel authorizeModel);
}