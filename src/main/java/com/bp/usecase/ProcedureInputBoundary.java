package com.bp.usecase;

import com.bp.entity.Procedure;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.procedure.ProcedureRequestModel;

import java.util.List;

/**
 * Procedure application logic.
 */
public interface ProcedureInputBoundary {
	
	/**
	 * Create procedure.
	 * @param visitId id of visit
	 * @param procedure entity
	 * @param authorizeModel authorization model
	 */
	void createProcedure(int visitId, Procedure procedure, AuthorizeModel authorizeModel);
	
	/**
	 * Find all procedures.
	 * @param authorizeModel authorization model
	 * @return list of procedures
	 */
	List<Procedure> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Find procedure by id.
	 * @param procedureId id of procedure
	 * @param authorizeModel authorization model
	 * @return procedure
	 */
	Procedure findById(int procedureId, AuthorizeModel authorizeModel);
	
	/**
	 * Fill procedure with request data.
	 * @param procedure entity
	 * @param procedureRequestModel request data
	 * @return procedure
	 */
	Procedure fillProcedure(Procedure procedure, ProcedureRequestModel procedureRequestModel);
	
	/**
	 * Save procedure.
	 * @param procedure entity
	 * @param authorizeModel authorization model
	 */
	void saveProcedure(Procedure procedure, AuthorizeModel authorizeModel);
	
	/**
	 * Delete procedure.
	 * @param procedureId id of procedure
	 * @param authorizeModel authorization model
	 */
	void deleteProcedure(int procedureId, AuthorizeModel authorizeModel);
	
	/**
	 * Find procedures by visit.
	 * @param visitId id of visit
	 * @param authorizeModel authorization model
	 * @return list of procedures
	 */
	List<Procedure> findProceduresByVisit(int visitId, AuthorizeModel authorizeModel);
}
