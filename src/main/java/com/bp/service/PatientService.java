package com.bp.service;

import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.PatientRepository;
import com.bp.entity.Patient;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PatientService implements IPatientService {
	
	/**
	 * Patient repository.
	 */
	private final PatientRepository patientRepository;
	
	/**
	 * Capability application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	public PatientService(PatientRepository patientRepository, CapabilityInteractor capabilityInteractor) {
		this.patientRepository = patientRepository;
		this.capabilityInteractor = capabilityInteractor;
	}
	
	@Override
	public Patient findById(final int id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PATIENT);
			authorizeModel.setOperation(CS.sREAD);
			authorizeModel.setEntityId(id);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return patientRepository.findById(id);
	}
	
	@Override
	public List<Patient> findAll(AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PATIENT);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return patientRepository.findAll();
	}
	
	@Override
	@Transactional
	public void createPatient(final Patient patient, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PATIENT);
			authorizeModel.setOperation(CS.sCREATE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		patientRepository.insert(patient);
		if (authorizeModel != null) {
			authorizeModel.setEntityId(patient.getPatientId());
			if (authorizeModel.getAuthenticatedUser() != null) {
				capabilityInteractor.createOwner(authorizeModel);
			}
		}
	}
	
	@Override
	public void savePatient(final Patient patient, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PATIENT);
			authorizeModel.setOperation(CS.sUPDATE);
			authorizeModel.setEntityId(patient.getPatientId());
			capabilityInteractor.isPermitted(authorizeModel);
		}
		patientRepository.update(patient);
	}

	@Override
	public void deletePatient(final int id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PATIENT);
			authorizeModel.setOperation(CS.sDELETE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		patientRepository.delete(id);
	}
	
	@Override
	public Patient findByIdentification(long identification, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PATIENT);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return patientRepository.findByIdentification(identification);
	}
}
