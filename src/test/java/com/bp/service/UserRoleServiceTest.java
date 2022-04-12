package com.bp.service;

import com.bp.entity.Role;
import com.bp.entity.UserEntity;
import com.bp.usecase.capability.CapabilityInteractor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({CapabilityService.class, CapabilityInteractor.class, DefaultCapabilityService.class, UserService.class,
UserRoleService.class, RoleService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRoleServiceTest {
	
	@Inject
	private IUserRoleService userRoleService;
	
	@Inject
	private IUserService userService;
	
	@Inject
	private RoleService roleService;
	
	@Test
	void addRole() {
		Role role = new Role();
		role.setName("NEW_ROLE");
		roleService.createRole(role, null);
		
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("test@gmail.com");
		userEntity.setPassword("Makfse123Q");
		userService.createUser(userEntity, null);
		
		userRoleService.addRole(userEntity.getUserId(), role.getName(), null);
		List<Role> roleList = userRoleService.getRoles(userEntity.getUserId(), null);
		assertEquals(1, roleList.size());
	}
}