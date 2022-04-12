package com.bp.repository;

import com.bp.entity.UserEntity;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRoleRepositoryTest {
	
	@Inject
	private UserRoleRepository userRoleRepository;
	
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@BeforeEach
	public void init() {
		roleRepository.deleteAll();
		userRoleRepository.deleteAll();
	}
	
	@Test
	public void userRoleTest() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("test@gmail.com");
		userEntity.setEmail("maMa123Qew");
		userRepository.insert(userEntity);
		
		userRoleRepository.addRole(userEntity.getUserId(), "DOCTOR", null);
		userRoleRepository.existByRole(userEntity.getUserId(), "DOCTOR");
		
		// If role exist in user
		assertEquals(1, userRoleRepository.existByRole(userEntity.getUserId(), "DOCTOR"));
		// Get roles of user
		assertEquals(1, userRoleRepository.getRoles(userEntity.getUserId()).size());
		userRoleRepository.deleteRole(userEntity.getUserId(), "DOCTOR");
		// Delete role from user
		assertEquals(0, userRoleRepository.getRoles(userEntity.getUserId()).size());
	}
}
