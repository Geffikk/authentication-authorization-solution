package com.bp.repository;

import com.bp.entity.DefaultCapability;
import com.bp.entity.Role;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@MybatisTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DefaultCapabilityRepositoryTest {
	
	@Inject
	private DefaultCapabilityRepository defaultCapabilityRepository;
	
	@Inject
	private RoleRepository roleRepository;
	
	@BeforeEach
	public void init() {
		defaultCapabilityRepository.deleteAll();
		roleRepository.deleteAll();
	}
	
	@Test
	public void findAll() {
		defaultCapabilityRepository.findAll();
		assertTrue(defaultCapabilityRepository.findAll().size() > 0);
	}
	
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
		capability = defaultCapabilityRepository.findByName(capability.getName());
		assertEquals(defaultCapabilityRepository.findByName(capability.getName()).getName(), capability.getName());
	}
	
	@Test
	public void findByRoleAndByEntity() {
		DefaultCapability capability = defaultCapabilityRepository.findByRoleAndByEntity("DOCTOR", "visit");
		assertEquals(defaultCapabilityRepository.findByRoleAndByEntity("DOCTOR", "visit").getName(), capability.getName());
	}
	
	@Test
	public void insert() {
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
		assertEquals(capability.getName(), defaultCapabilityRepository.findByName(capability.getName()).getName());
	}
	
	@Test
	public void update() {
		Role role = new Role();
		role.setName("USER_IN_PROCEDURE_TEST");
		roleRepository.insert(role);
		
		DefaultCapability capability = new DefaultCapability();
		capability.setName("USER_IN_PROCEDURE_TEST");
		capability.setCreate(true);
		capability.setRead(true);
		capability.setUpdate(false);
		capability.setDelete(false);
		capability.setCapability(false);
		
		defaultCapabilityRepository.insert(capability);
		capability = defaultCapabilityRepository.findByName(capability.getName());
		capability.setCreate(false);
		assertEquals(capability.isCreate(), defaultCapabilityRepository.findByName(capability.getName()).isCreate());
	}
	
	@Test
	public void delete() {
		Role role = new Role();
		role.setName("USER_IN_PROCEDURE_TEST");
		roleRepository.insert(role);
		
		DefaultCapability capability = new DefaultCapability();
		capability.setName("USER_IN_PROCEDURE_TEST");
		capability.setCreate(true);
		capability.setRead(true);
		capability.setUpdate(false);
		capability.setDelete(false);
		capability.setCapability(false);
		
		defaultCapabilityRepository.insert(capability);
		assertThat(defaultCapabilityRepository.findByName(capability.getName())).isNotNull();
		defaultCapabilityRepository.delete(capability.getName());
		assertThat(defaultCapabilityRepository.findByName(capability.getName())).isNull();
	}
}
