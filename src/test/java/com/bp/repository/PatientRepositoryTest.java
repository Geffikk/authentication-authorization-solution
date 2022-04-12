package com.bp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bp.entity.Patient;
import com.bp.entity.Visit;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PatientRepositoryTest {
	
	@Inject
	private PatientRepository patientRepository;
	
	@Inject
	private VisitRepository visitRepository;
	
	@BeforeEach
	public void init() {
		patientRepository.deleteAll();
		visitRepository.deleteAll();
	}
	
	@Test
	public void findAll() {
		assertEquals(5, patientRepository.findAll().size());
	}
	
	@Test
	public void findById() {
		Patient patient = new Patient();
		patient.setPatientId(6);
		patientRepository.insert(patient);
		assertEquals(patient.getPatientId(), patientRepository.findById(patient.getPatientId()).getPatientId());
	}
	
	@Test
	public void insert() {
		Patient patient = new Patient();
		patient.setPatientId(6);
		patientRepository.insert(patient);
		assertEquals(patient.getPatientId(), patientRepository.findById(patient.getPatientId()).getPatientId());
	}
	
	@Test
	public void delete() {
		Patient patient = new Patient();
		patient.setPatientId(6);
		patientRepository.insert(patient);
		assertThat(patientRepository.findById(patient.getPatientId())).isNotNull();
		patientRepository.delete(patient.getPatientId());
		assertThat(patientRepository.findById(patient.getPatientId())).isNull();
	}
	
	@Test
	public void update() {
		Patient patient = new Patient();
		patient.setPatientId(6);
		patientRepository.insert(patient);
		patient = patientRepository.findById(patient.getPatientId());
		patient.setFirstName("Filip");
		patientRepository.update(patient);
		assertEquals(patient.getFirstName(), patientRepository.findById(patient.getPatientId()).getFirstName());
	}
	
	@Test
	public void findVisitsByPatientId() {
		Patient patient = new Patient();
		patient.setPatientId(6);
		patientRepository.insert(patient);
		patient = patientRepository.findById(6);
		
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();
		visit1.setPatientId(patient.getPatientId());
		visit2.setPatientId(patient.getPatientId());
		visitRepository.insert(visit1);
		visitRepository.insert(visit2);
		
		List<Visit> visits = visitRepository.findVisitsByPatientId(patient.getPatientId());
		for (Visit visit : visits) {
			assertEquals(patient.getPatientId(), visit.getPatientId());
		}
	}
}