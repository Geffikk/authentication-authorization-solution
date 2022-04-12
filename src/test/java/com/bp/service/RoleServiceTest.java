package com.bp.service;

import com.bp.entity.Role;
import com.bp.usecase.capability.CapabilityInteractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({CapabilityService.class, CapabilityInteractor.class, DefaultCapabilityService.class, UserService.class,
UserRoleService.class, RoleService.class})
public class RoleServiceTest {
	
	@Inject
	private IRoleService roleService;
	
	@Test
	public void findAll() {
		assertTrue(roleService.findAll(null).size() > 0);
	}
	
	@Test
	public void findById() {
		Role role = new Role();
		role.setName("NEW_ROLE");
		
		roleService.createRole(role, null);
		roleService.findById(role.getName(), null);
		assertEquals(role.getName(), roleService.findById(role.getName(), null).getName());
	}
	
	@Test
	public void createRole() {
		Role role = new Role();
		role.setName("NEW_ROLE");
		
		roleService.createRole(role, null);
		roleService.findById(role.getName(), null);
		assertEquals(role.getName(), roleService.findById(role.getName(), null).getName());
	}

	@Test
	public void deleteCapability() {
		Role role = new Role();
		role.setName("NEW_ROLE");
		
		roleService.createRole(role, null);
		role = roleService.findById(role.getName(), null);
		assertThat(roleService.findById(role.getName(), null)).isNotNull();
		roleService.deleteRole(role.getName(), null);
		assertThat(roleService.findById(role.getName(), null)).isNull();
	}
}
