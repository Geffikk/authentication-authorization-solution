package com.bp.service;

import com.bp.entity.Role;
import com.bp.entity.UserEntity;
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
UserService.class, VisitService.class, PatientService.class, CapabilityInteractor.class, ProcedureService.class,
CapabilityService.class, DefaultCapabilityService.class, UserRoleService.class, RoleService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserEntityServiceTest {
	
	@Inject
	private UserService userService;
	
	@Test
	public void findAll() {
		assertTrue( userService.findAll(null).size() > 0);
	}
	
	@Test
	public void findById() {
		assertEquals(1, userService.findById(1, null).getUserId());
	}
	
	@Test
	public void createUser() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("user12@gmail.com");
		userService.createUser(userEntity, null);
		assertEquals(6, userService.findById(6, null).getUserId());
	}
	
	@Test
	public void saveUser() {
		UserEntity userEntity = userService.findById(1, null);
		userEntity.setPhone("999-451-339");
		userService.saveUser(userEntity, null);
		assertEquals(userEntity.getPhone(), userService.findById(userEntity.getUserId(), null).getPhone());
	}
	
	@Test
	public void deleteUser() {
		UserEntity userEntity = userService.findById(1, null);
		assertThat(userService.findById(userEntity.getUserId(), null)).isNotNull();
		userService.deleteUser(userEntity.getUserId(), null);
		assertThat(userService.findById(userEntity.getUserId(), null)).isNull();
	}
	
	@Test
	public void findUserByEmail() {
		assertEquals("doctor2@gmail.com", userService.findById(2, null).getEmail());
	}
	
	@Test
	public void getRoles() {
		List<Role> roles = userService.getRoles(1);
		assertEquals(Role.class, roles.get(0).getClass());
	}
	
	@Test
	public void existByEmail() {
		assertTrue(userService.existByEmail("doctor2@gmail.com"));
	}
}