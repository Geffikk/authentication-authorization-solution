package com.bp.service;

import com.bp.entity.Patient;
import com.bp.entity.Visit;

/**
 * Visit service helper methods class
 */
public class VisitServiceHelper {
	
	/**
	 * Copy information from visit to patient, then save patient to db.
	 * @param visit of patient
	 * @param patient patient entity
	 */
	protected void setPatientInformation(Visit visit, Patient patient) {
		if (visit.getIdentification() != 0) {
			patient.setIdentification(visit.getIdentification());
		}
		if (visit.getFirstName() != null) {
			patient.setFirstName(visit.getFirstName());
		}
		if (visit.getLastName() != null) {
			patient.setLastName(visit.getLastName());
		}
		if (visit.getPhone() != null) {
			patient.setPhone(visit.getPhone());
		}
		if (visit.getEmail() != null) {
			patient.setEmail(visit.getEmail());
		}
		if (visit.getAddress() != null) {
			patient.setAddress(visit.getAddress());
		}
		if (visit.getCity() != null) {
			patient.setCity(visit.getCity());
		}
		if (visit.getBirthdate() != null) {
			patient.setBirthdate(visit.getBirthdate());
		}
	}
}
