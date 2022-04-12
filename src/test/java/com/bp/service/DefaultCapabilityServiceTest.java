package com.bp.service;

import com.bp.entity.DefaultCapability;
import com.bp.entity.Role;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.DefaultCapabilityRepository;
import com.bp.repository.RoleRepository;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.utility.CS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({CapabilityService.class, CapabilityInteractor.class, DefaultCapabilityService.class, UserService.class,
UserRoleService.class, RoleService.class})
public class DefaultCapabilityServiceTest {
	
	@Inject
	private IDefaultCapabilityService capabilityService;
	
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private DefaultCapabilityRepository defaultCapabilityRepository;
	
	@Test
	public void findByName() {
		Role role = new Role();
		role.setName("USER_IN_PROCEDURE_TEST");
		roleRepository.insert(role);
		
		DefaultCapability capability = new DefaultCapability();
		capability.setName("USER_IN_PROCEDURE_TEST");
		capability.setCreate(true);
		capability.setRead(true);
		capability.setUpdate(false);
		capability.setDelete(false);
		
		defaultCapabilityRepository.insert(capability);
		DefaultCapability defaultCapability = capabilityService.findByName(capability.getName());
		assertEquals(DefaultCapability.class, defaultCapability.getClass());
	}
	
	@Test
	public void createDefaultCapability() {
		AuthorizeModel authorizeModel = new AuthorizeModel();
		authorizeModel.setGivenRole("DOCTOR");
		authorizeModel.setEntityType(CS.VISIT);
		
		DefaultCapability defaultCapability = capabilityService.findByRoleAndByEntity(authorizeModel);
		assertEquals(DefaultCapability.class, defaultCapability.getClass());
	}
}
