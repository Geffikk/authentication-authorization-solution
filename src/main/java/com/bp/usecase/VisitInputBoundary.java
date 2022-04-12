package com.bp.usecase;

import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.visit.VisitRequestModel;

import java.util.List;

/**
 * Visit application logic.
 */
public interface VisitInputBoundary {
	
	/**
	 * Create visit.
	 * @param visit entity
	 * @param authorizeModel authorization model
	 */
	void createVisit(Visit visit, AuthorizeModel authorizeModel);
	
	/**
	 * Fill visit entity with request data.
	 * @param visit entity
	 * @param visitRequestModel request data
	 * @return visit
	 */
	Visit fillVisit(Visit visit, VisitRequestModel visitRequestModel);
	
	/**
	 * Assign user to visit.
	 * @param userId id of user
	 * @param visitId id of visit
	 * @param authorizeModel authorization model
	 * @return true, if assigning was successful
	 */
	boolean assignUserToVisit(int userId, int visitId, AuthorizeModel authorizeModel);
	
	/**
	 * Find all visit.
	 * @param authorizeModel authorization model
	 * @return list of visits.
	 */
	List<Visit> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Find visit by id.
	 * @param visitId id of visit
	 * @param authorizeModel authorization model
	 * @return visit
	 */
	Visit findById(int visitId, AuthorizeModel authorizeModel);
	
	/**
	 * Save visit.
	 * @param visit entity
	 * @param authorizeModel authorization model
	 */
	void saveVisit(Visit visit, AuthorizeModel authorizeModel);
	
	/**
	 * Delete visit.
	 * @param visitId id of visit
	 * @param authorizeModel authorization model
	 */
	void deleteVisit(int visitId, AuthorizeModel authorizeModel);
	
	/**
	 * Delete user from visit.
	 * @param userId id of user
	 * @param visitId id of visit
	 * @param authorizeModel authorization model
	 */
	void deleteUserFromVisit(int userId, int visitId, AuthorizeModel authorizeModel);
}

