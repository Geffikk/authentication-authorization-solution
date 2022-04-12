package com.bp.service;

import com.bp.entity.Capability;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.utility.CS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({CapabilityService.class, CapabilityInteractor.class, DefaultCapabilityService.class, UserService.class,
UserRoleService.class, RoleService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CapabilityServiceTest {
	
	@Inject
	private ICapabilityService capabilityService;
	
	@Test
	public void findAll() {
		assertTrue(capabilityService.findAll(null).size() > 0);
	}
	
	@Test
	public void findById() {
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser("doctor@gmail.com");
		capability.setEntityType(CS.VISIT);
		capability.setEntityId(1);
		
		capabilityService.createCapability(capability, null);
		assertEquals(capability.getCapId(), capabilityService.findById(capability.getCapId(), null).getCapId());
	}
	
	@Test
	public void createCapability() {
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser("doctor@gmail.com");
		capability.setEntityType(CS.VISIT);
		capability.setEntityId(1);
		
		capabilityService.createCapability(capability, null);
		assertEquals(capability.getCapId(), capabilityService.findById(capability.getCapId(), null).getCapId());
	}
	
	@Test
	public void saveCapability() {
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser("doctor@gmail.com");
		capability.setEntityType(CS.VISIT);
		capability.setEntityId(1);
		capabilityService.createCapability(capability, null);
		
		capability = capabilityService.findById(capability.getCapId(), null);
		capability.setEntityType(CS.PROCEDURE);
		capabilityService.saveCapability(capability, null);
		assertEquals(capability.getEntityType(), capabilityService.findById(capability.getCapId(), null).getEntityType());
	}
	
	@Test
	public void deleteCapability() {
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser("doctor@gmail.com");
		capability.setEntityType(CS.VISIT);
		capability.setEntityId(1);
		capabilityService.createCapability(capability, null);
		
		capability = capabilityService.findById(capability.getCapId(), null);
		assertThat(capabilityService.findById(capability.getCapId(), null)).isNotNull();
		capabilityService.deleteCapability(capability.getCapId(), null);
		assertThat(capabilityService.findById(capability.getCapId(), null)).isNull();
	}
}
