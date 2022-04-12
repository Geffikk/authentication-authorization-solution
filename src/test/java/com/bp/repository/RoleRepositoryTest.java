package com.bp.repository;

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
public class RoleRepositoryTest {
	
	@Inject
	private RoleRepository roleRepository;
	
	@BeforeEach
	public void init() {
		roleRepository.deleteAll();
	}
	
	@Test
	public void findAll() {
		assertTrue( roleRepository.findAll().size() > 0);
	}
	
	@Test
	public void findById() {
		Role role = new Role();
		role.setName("ROLE_TEST");
		roleRepository.insert(role);
		assertEquals(role.getName(), roleRepository.findById(role.getName()).getName());
	}
	
	@Test
	public void insert() {
		Role role = new Role();
		role.setName("ROLE_TEST");
		roleRepository.insert(role);
		assertEquals(role.getName(), roleRepository.findById(role.getName()).getName());
	}
	
	@Test
	public void delete() {
		Role role = new Role();
		role.setName("ROLE_TEST");
		roleRepository.insert(role);
		
		assertThat(roleRepository.findById(role.getName())).isNotNull();
		roleRepository.delete(role.getName());
		assertThat(roleRepository.findById(role.getName())).isNull();
	}
}
