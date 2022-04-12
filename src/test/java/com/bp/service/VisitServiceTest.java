package com.bp.service;

import com.bp.entity.Patient;
import com.bp.entity.Visit;
import com.bp.usecase.capability.CapabilityInteractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({
ProcedureService.class, VisitService.class, PatientService.class, UserService.class, RoleService.class,
UserRoleService.class, CapabilityInteractor.class, CapabilityService.class, DefaultCapabilityService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VisitServiceTest {
	
	@Inject
	private ProcedureService procedureService;
	
	@Inject
	private VisitService visitService;
	
	@Inject
	private PatientService patientService;
	
	@Test
	public void findAll() {
		assertEquals(7 , visitService.findAll(null).size());
	}
	
	@Test
	public void findById() {
		Visit visit = new Visit();
		visit.setIdentification(9902286361L);
		visitService.createVisit(visit, null);
		
		assertEquals(visit.getVisitId(), visitService.findById(visit.getVisitId(), null).getVisitId());
	}
	
	@Test
	public void createVisit() {
		Visit visit = new Visit();
		visit.setIdentification(9902286361L);
		visitService.createVisit(visit, null);
		assertEquals(visit.getVisitId(), visitService.findById(visit.getVisitId(), null).getVisitId());
		assertEquals(patientService.findById(visit.getPatientId(), null).getPatientId(), visit.getPatientId());
	}
	
	@Test
	public void saveVisit() {
		Visit visit = new Visit();
		visit.setIdentification(9902286361L);
		visitService.createVisit(visit, null);
		
		visit = visitService.findById(visit.getVisitId(), null);
		visit.setAddress("Bezručova 22");
		visitService.saveVisit(visit, null);
		assertEquals(visit.getAddress(), visitService.findById(visit.getVisitId(), null).getAddress());
	}
	
	@Test
	public void deleteVisit() {
		Visit visit = new Visit();
		visit.setIdentification(9902286361L);
		visitService.createVisit(visit, null);
		
		visit = visitService.findById(visit.getVisitId(), null);
		assertThat(visitService.findById(visit.getVisitId(), null)).isNotNull();
		assertThat(procedureService.findProceduresByVisitId(visit.getVisitId(), null)).isNotNull();
		visitService.deleteVisit(visit.getVisitId(), null);
		assertThat(visitService.findById(visit.getVisitId(), null)).isNull();
	}
	
	@Test
	public void findVisitsByPatientId() {
		Visit visit = new Visit();
		visit.setIdentification(9902286361L);
		visit.setPatientId(1);
		visitService.createVisit(visit, null);
		
		Patient patient = patientService.findById(1, null);
		List<Visit> visits = visitService.findVisitsByPatientId(patient.getPatientId(), null);
		for (Visit v : visits) {
			assertEquals(patient.getPatientId(), v.getPatientId());
		}
	}
}