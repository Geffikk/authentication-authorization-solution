package com.bp.service;

import com.bp.entity.Patient;
import com.bp.model.capability.AuthorizeModel;

import java.util.List;

/**
 * Patient service layer (interface).
 */
public interface IPatientService {
	
    /**
     * Find patient by ID, after success authorization..
     * @param id of patient
     * @return patient
     */
    Patient findById(int id, AuthorizeModel authorizeModel);
    
	/**
	 * Find all patients, after success authorization..
	 * @return all patients.
	 */
	List<Patient> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Create patient, after success authorization..
	 * @param patient for create
	 */
	void createPatient(final Patient patient, AuthorizeModel authorizeModel);
	
	/**
	 * Save patient, after success authorization..
	 * @param patient for save
	 */
    void savePatient(Patient patient, AuthorizeModel authorizeModel);
    
	/**
	 * Delete patient, after success authorization..
	 * @param id of patient
	 */
	void deletePatient(int id, AuthorizeModel authorizeModel);
	
	/**
	 * Find patient by identification, after success authorization..
	 * @param identification of patient
	 * @return patient
	 */
	Patient findByIdentification(long identification, AuthorizeModel authorizeModel);
}
