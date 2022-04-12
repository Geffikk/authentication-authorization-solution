package com.bp.service;

import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;

import java.util.List;

/**
 * Visit service layer (interface).
 */
public interface IVisitService {
	
	/**
	 * Find visit by id, after success authorization.
	 * @param id of visit
	 * @return visit
	 */
	Visit findById(int id, AuthorizeModel authorizeModel);
	
	/**
	 * Find all visits, after success authorization.
	 * @return all visits
	 */
	List<Visit> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Create visit, after success authorization.
	 * @param visit for create
	 * @param authorizeModel contain authorization rules
	 */
	void createVisit(Visit visit, AuthorizeModel authorizeModel);
	
	/**
	 * Save visit, after success authorization.
	 * @param visit for save
	 */
	void saveVisit(Visit visit, AuthorizeModel authorizeModel);
	
	/**
	 *  Delete visit, after success authorization.
	 * @param id of visit
	 */
	void deleteVisit(int id, AuthorizeModel authorizeModel);
	
	/**
	 * Find visits by patientId, after success authorization.
	 * @param patientId of patient
	 * @return visits of patient
	 */
	List<Visit> findVisitsByPatientId(long patientId, AuthorizeModel authorizeModel);
}
