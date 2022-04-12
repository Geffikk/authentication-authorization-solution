package com.bp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import com.bp.entity.Patient;
import com.bp.entity.Procedure;
import com.bp.entity.Visit;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import javax.inject.Inject;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProcedureRepositoryTest {
	
	@Inject
	private ProcedureRepository procedureRepository;
	
	@Inject
	private VisitRepository visitRepository;
	
	@Inject
	private PatientRepository patientRepository;
	
	@BeforeEach
	public void init() {
		procedureRepository.deleteAll();
		visitRepository.deleteAll();
	}
	
	@Test
	public void findAll() {
		Procedure procedure = new Procedure();
		procedure.setProcedureId(6);
		
		Visit visit = new Visit();
		visit.setVisitId(8);
		visit.setPatientId(5);
		procedure.setVisitId(visit.getVisitId());
		visitRepository.insert(visit);
		procedureRepository.insert(procedure);
		
		assertEquals(6, procedureRepository.findAll().size());
	}
	
	@Test
	public void findById() {
		Procedure procedure = new Procedure();
		procedure.setProcedureId(6);
		
		Visit visit = new Visit();
		visit.setVisitId(8);
		visit.setPatientId(5);
		procedure.setVisitId(visit.getVisitId());
		visitRepository.insert(visit);
		
		procedureRepository.insert(procedure);
		assertEquals(procedure.getProcedureId(), procedureRepository.findById(procedure.getProcedureId()).getProcedureId());
	}
	
	@Test
	public void insert() {
		Procedure procedure = new Procedure();
		procedure.setProcedureId(6);
		
		Visit visit = new Visit();
		visit.setVisitId(8);
		visit.setPatientId(5);
		procedure.setVisitId(visit.getVisitId());
		visitRepository.insert(visit);
		
		procedureRepository.insert(procedure);
		assertEquals(procedure.getProcedureId(), procedureRepository.findById(procedure.getProcedureId()).getProcedureId());
	}
	
	@Test
	public void update() {
		Procedure procedure = new Procedure();
		procedure.setProcedureId(6);
		
		Visit visit = new Visit();
		visit.setVisitId(8);
		visit.setPatientId(5);
		procedure.setVisitId(visit.getVisitId());
		visitRepository.insert(visit);
		
		procedureRepository.insert(procedure);
		procedure = procedureRepository.findById(procedure.getProcedureId());
		procedure.setProcedureType("Vaccination");
		procedureRepository.update(procedure);
		assertEquals(procedure.getProcedureType(), procedureRepository.findById(procedure.getProcedureId()).getProcedureType());
	}
	
	@Test
	public void delete() {
		Procedure procedure = new Procedure();
		procedure.setProcedureId(6);
		
		Visit visit = new Visit();
		visit.setVisitId(8);
		visit.setPatientId(5);
		procedure.setVisitId(visit.getVisitId());
		visitRepository.insert(visit);
		
		procedureRepository.insert(procedure);
		assertThat(procedureRepository.findById(procedure.getProcedureId())).isNotNull();
		procedureRepository.delete(procedure.getProcedureId());
		assertThat(procedureRepository.findById(procedure.getProcedureId())).isNull();
	}
	
	@Test
	public void findProceduresByVisitId() {
		Procedure procedure = new Procedure();
		procedure.setProcedureId(6);
		
		Patient patient = new Patient();
		patientRepository.insert(patient);
		patient.setPatientId(6);
		patient = patientRepository.findById(patient.getPatientId());
		
		Visit visit = new Visit();
		visit.setVisitId(8);
		visit.setPatientId(patient.getPatientId());
		procedure.setVisitId(visit.getVisitId());
		visitRepository.insert(visit);
		
		procedureRepository.insert(procedure);
		visit = visitRepository.findById(procedure.getVisitId());
		
		List<Procedure> procedures = procedureRepository.findProceduresByVisitId(visit.getVisitId());
		for (Procedure procedure_temp : procedures) {
			assertEquals(visit.getVisitId(), procedure_temp.getVisitId());
		}
	}
}