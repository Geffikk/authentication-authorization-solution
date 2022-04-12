package com.bp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import com.bp.entity.Patient;
import com.bp.entity.Visit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import javax.inject.Inject;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VisitRepositoryTest {
	
	@Inject
	private VisitRepository visitRepository;
	
	@Inject
	private PatientRepository patientRepository;
	
	@Test
	public void findAll() {
		Visit visit = new Visit();
		visit.setVisitId(8);
		
		Patient patient = new Patient();
		patientRepository.insert(patient);
		patient.setPatientId(6);
		patient = patientRepository.findById(patient.getPatientId());
		visit.setPatientId(patient.getPatientId());
		
		visitRepository.insert(visit);
		List<Visit> visits = visitRepository.findAll();
		assertEquals(8, visits.size());
	}
	
	@Test
	public void findById() {
		Visit visit = new Visit();
		visit.setVisitId(8);
		
		Patient patient = new Patient();
		patientRepository.insert(patient);
		patient.setPatientId(6);
		patient = patientRepository.findById(patient.getPatientId());
		visit.setPatientId(patient.getPatientId());
		
		visitRepository.insert(visit);
		assertEquals(visit.getVisitId(), visitRepository.findById(visit.getVisitId()).getVisitId());
	}
	
	@Test
	public void insert() {
		Visit visit = new Visit();
		visit.setVisitId(8);
		
		Patient patient = new Patient();
		patientRepository.insert(patient);
		patient.setPatientId(6);
		patient = patientRepository.findById(patient.getPatientId());
		visit.setPatientId(patient.getPatientId());
		
		visitRepository.insert(visit);
		assertEquals(visit.getVisitId(), visitRepository.findById(visit.getVisitId()).getVisitId());
		visitRepository.delete(visit.getVisitId());
	}
	
	@Test
	public void update() {
		Visit visit = new Visit();
		visit.setVisitId(8);
		visit.setFirstName("Jozef");
		
		Patient patient = new Patient();
		patientRepository.insert(patient);
		patient.setPatientId(6);
		patient = patientRepository.findById(patient.getPatientId());
		visit.setPatientId(patient.getPatientId());
		
		visitRepository.insert(visit);
		visit = visitRepository.findById(visit.getVisitId());
		visit.setFirstName("Peter");
		visitRepository.update(visit);
		assertEquals(visit.getFirstName(), visitRepository.findById(visit.getVisitId()).getFirstName());
	}
	
	@Test
	public void delete() {
		Visit visit = new Visit();
		visit.setVisitId(8);
		
		Patient patient = new Patient();
		patientRepository.insert(patient);
		patient.setPatientId(6);
		patient = patientRepository.findById(patient.getPatientId());
		visit.setPatientId(patient.getPatientId());
		
		visitRepository.insert(visit);
		assertThat(visitRepository.findById(visit.getVisitId())).isNotNull();
		visitRepository.delete(visit.getVisitId());
		assertThat(visitRepository.findById(visit.getVisitId())).isNull();
		visitRepository.delete(visit.getVisitId());
	}
	
	@Test
	public void findVisitsByPatientId() {
		Visit visit = new Visit();
		visit.setVisitId(8);
		
		Patient patient = new Patient();
		patient.setPatientId(6);
		patient.setVisitId(visit.getVisitId());
		visit.setPatientId(patient.getPatientId());
		patientRepository.insert(patient);
		visitRepository.insert(visit);
		
		List<Visit> visits = visitRepository.findVisitsByPatientId(patient.getPatientId());
		patient = patientRepository.findById(patient.getPatientId());
		for (Visit visit2 : visits) {
			assertEquals(patient.getPatientId(), visit2.getPatientId());
		}
		visitRepository.delete(visit.getVisitId());
		patientRepository.delete(patient.getPatientId());
	}
}