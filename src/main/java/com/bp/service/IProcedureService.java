package com.bp.service;

import com.bp.entity.Procedure;
import com.bp.model.capability.AuthorizeModel;

import java.util.List;

/**
 * Procedure service layer (interface).
 */
public interface IProcedureService {
	
	/**
	 * Find procedure by id, after success authorization.
	 * @param id of procedure
	 * @param authorizeModel contain authorization rules
	 * @return procedure
	 */
	Procedure findById(int id, AuthorizeModel authorizeModel);
	
	/**
	 * Find all procedures, after success authorization.
	 * @param authorizeModel contain authorization rules
	 * @return all procedures
	 */
	List<Procedure> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Create procedure, after success authorization.
	 * @param procedure for create.
	 * @param visitId of visit
	 */
	void createProcedure(Procedure procedure, AuthorizeModel authorizeModel, int visitId);
	
	/**
	 * Update procedure, after success authorization.
	 * @param procedure for update
	 * @param authorizeModel contain authorization rules
	 */
	void saveProcedure(Procedure procedure, AuthorizeModel authorizeModel);
	
	/**
	 * Delete procedure, after success authorization.
	 * @param id of procedure
	 */
	void deleteProcedure(int id, AuthorizeModel authorizeModel);
	
	/**
	 * Find procedures by visit id, after success authorization.
	 * @param visit_id of visit
	 */
	List<Procedure> findProceduresByVisitId(int visit_id, AuthorizeModel authorizeModel);
}
