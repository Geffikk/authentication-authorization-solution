package com.bp.service;

import com.bp.model.capability.AuthorizeModel;

/**
 * User visit service layer (interface).
 */
public interface IUserVisitService {
	
	/**
	 * Assign user to visit, after success authorization.
	 * @param userId id of user
	 * @param visitId id of visit
	 * @param authorizeModel authorization model
	 */
	void assignUserToVisit(int userId, int visitId, AuthorizeModel authorizeModel);
	
	/**
	 * Delete user from visit, after success authorization.
	 * @param userId id of user
	 * @param visitId id of visit
	 * @param authorizeModel authorization model
	 */
	void deleteUserFromVisit(int userId, int visitId, AuthorizeModel authorizeModel);
}
