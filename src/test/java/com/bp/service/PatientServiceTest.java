package com.bp.service;

import com.bp.entity.Patient;
import com.bp.usecase.capability.CapabilityInteractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({
PatientService.class, VisitService.class, CapabilityInteractor.class, UserService.class, DefaultCapabilityService.class,
CapabilityService.class, UserRoleService.class, RoleService.class, ProcedureService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PatientServiceTest {
	
	@Inject
	private PatientService patientService;
	
	@Inject
	private VisitService visitService;
	
	@Test
	public void findAll() {
		assertEquals(5 , patientService.findAll(null).size());
	}
	
	@Test
	public void findById() {
		Patient patient = new Patient();
		patient.setIdentification(999999999L);
		patient.setEmail("test1234@gmail.com");
		patientService.createPatient(patient, null);
		assertEquals(patient.getPatientId(), patientService.findById(patient.getPatientId(), null).getPatientId());
	}
	
	@Test
	public void createPatient() {
		Patient patient = new Patient();
		patientService.createPatient(patient, null);
		assertEquals(patient.getPatientId(), patientService.findById(patient.getPatientId(), null).getPatientId());
	}
	
	@Test
	public void savePatient() {
		Patient patient = new Patient();
		patient.setIdentification(999999999L);
		patient.setEmail("test1234@gmail.com");
		patientService.createPatient(patient, null);
		
		patient = patientService.findById(patient.getPatientId(), null);
		patient.setFirstName("Dezider");
		patientService.savePatient(patient, null);
		assertEquals(patient.getFirstName(), patientService.findById(patient.getPatientId(), null).getFirstName());
	}
	
	@Test
	public void deletePatient() {
		Patient patient = new Patient();
		patient.setIdentification(999999999L);
		patient.setEmail("test1234@gmail.com");
		patientService.createPatient(patient, null);
		
		patient = patientService.findById(patient.getPatientId(), null);
		assertThat(patientService.findById(patient.getPatientId(), null)).isNotNull();
		assertThat(visitService.findVisitsByPatientId(patient.getPatientId(), null)).isNotNull();
		patientService.deletePatient(patient.getPatientId(), null);
		assertThat(patientService.findById(patient.getPatientId(), null)).isNull();
	}
}