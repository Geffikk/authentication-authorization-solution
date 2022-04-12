package com.bp.usecase;

import com.bp.entity.Patient;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.patient.PatientRequestModel;
import com.bp.service.IPatientService;
import com.bp.usecase.PatientInputBoundary;

import java.util.List;

public class PatientInteractor implements PatientInputBoundary {
	
	/**
	 * Patient service.
	 */
	private IPatientService patientService;
	
	public PatientInteractor(IPatientService patientService) {
		this.patientService = patientService;
	}
	
	@Override
	public List<Patient> findAll(AuthorizeModel authorizeModel) {
		return patientService.findAll(authorizeModel);
	}
	
	@Override
	public Patient findById(int patientId, AuthorizeModel authorizeModel) {
		return patientService.findById(patientId, authorizeModel);
	}
	
	@Override
	public void createPatient(Patient patient, AuthorizeModel authorizeModel) {
		patientService.createPatient(patient, authorizeModel);
	}
	
	@Override
	public void savePatient(Patient patient, AuthorizeModel authorizeModel) {
		patientService.savePatient(patient, authorizeModel);
	}
	
	@Override
	public void deletePatient(int patientId, AuthorizeModel authorizeModel) {
		patientService.deletePatient(patientId, authorizeModel);
	}
	
	@Override
	public Patient fillPatient(Patient patient, PatientRequestModel patientRequestModel) {
		if (patientRequestModel.getEmail() != null) {
			patient.setEmail(patientRequestModel.getEmail());
		}
		if (patientRequestModel.getIdentification() != 0) {
			patient.setIdentification(patientRequestModel.getIdentification());
		}
		if (patientRequestModel.getAddress() != null) {
			patient.setAddress(patientRequestModel.getAddress());
		}
		if (patientRequestModel.getCity() != null) {
			patient.setCity(patientRequestModel.getCity());
		}
		if (patientRequestModel.getFirstName() != null) {
			patient.setFirstName(patientRequestModel.getFirstName());
		}
		if (patientRequestModel.getLastName() != null) {
			patient.setLastName(patientRequestModel.getLastName());
		}
		if (patientRequestModel.getPhone() != null) {
			patient.setPhone(patientRequestModel.getPhone());
		}
		if (patientRequestModel.getBirthdate() != null) {
			patient.setBirthdate(patientRequestModel.getBirthdate());
		}
		return patient;
	}
}
