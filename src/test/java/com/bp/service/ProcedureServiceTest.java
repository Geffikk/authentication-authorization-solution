package com.bp.service;

import com.bp.entity.Procedure;
import com.bp.usecase.capability.CapabilityInteractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({ProcedureService.class, VisitService.class, PatientService.class, CapabilityInteractor.class,
RoleService.class, UserRoleService.class, DefaultCapabilityService.class, CapabilityService.class, UserService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProcedureServiceTest {
	
	@Inject
	private ProcedureService procedureService;
	
	@Test
	public void findAll() {
		assertEquals(5 , procedureService.findAll(null).size());
	}
	
	@Test
	public void findById() {
		Procedure procedure = new Procedure();
		procedure.setVisitId(1);
		procedureService.createProcedure(procedure, null, procedure.getVisitId());
		assertEquals(procedure.getProcedureId(), procedureService.findById(procedure.getProcedureId(), null).getProcedureId());
	}
	
	@Test
	public void createProcedure() {
		Procedure procedure = new Procedure();
		procedure.setVisitId(1);
		procedureService.createProcedure(procedure, null, procedure.getVisitId());
		assertEquals(procedure.getProcedureId(), procedureService.findById(procedure.getProcedureId(), null).getProcedureId());
	}
	
	@Test
	public void saveProcedure() {
		Procedure procedure = new Procedure();
		procedure.setVisitId(1);
		procedureService.createProcedure(procedure, null, procedure.getVisitId());
		
		procedure = procedureService.findById(procedure.getProcedureId(), null);
		procedure.setProcedureType("Vaccination");
		procedureService.saveProcedure(procedure, null);
		assertEquals(procedure.getProcedureType(), procedureService.findById(procedure.getProcedureId(), null).getProcedureType());
	}
	
	@Test
	public void deleteProcedure() {
		Procedure procedure = new Procedure();
		procedure.setVisitId(1);
		procedureService.createProcedure(procedure, null, procedure.getVisitId());
		
		procedure = procedureService.findById(procedure.getProcedureId(), null);
		assertThat(procedureService.findById(procedure.getProcedureId(), null)).isNotNull();
		procedureService.deleteProcedure(procedure.getProcedureId(), null);
		assertThat(procedureService.findById(procedure.getProcedureId(), null)).isNull();
	}
}