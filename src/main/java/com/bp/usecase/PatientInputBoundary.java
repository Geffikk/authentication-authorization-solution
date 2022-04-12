package com.bp.usecase;

import com.bp.entity.Patient;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.patient.PatientRequestModel;

import java.util.List;

/**
 * Patient application logic.
 */
public interface PatientInputBoundary {
	
	/**
	 * Find all patients from database.
	 * @param authorizeModel authorization model
	 * @return all patients.
	 */
	List<Patient> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Find patient by id .
	 * @param patientId id of patient
	 * @param authorizeModel authorization model
	 * @return Patient
	 */
	Patient findById(int patientId, AuthorizeModel authorizeModel);
	
	/**
	 * Fill patient entity with request data.
	 * @param patient entity
	 * @param patientRequestModel request data
	 * @return patient
	 */
	Patient fillPatient(Patient patient, PatientRequestModel patientRequestModel);
	
	/**
	 * Create patient.
	 * @param patient entity
	 * @param authorizeModel authorization model
	 */
	void createPatient(Patient patient, AuthorizeModel authorizeModel);
	
	/**
	 * Save patient.
	 * @param patient entity
	 * @param authorizeModel authorization model
	 */
	void savePatient(Patient patient, AuthorizeModel authorizeModel);
	
	/**
	 * Delete patient.
	 * @param patientId id of patient
	 * @param authorizeModel authorization model
	 */
	void deletePatient(int patientId, AuthorizeModel authorizeModel);
}
