package com.bp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bp.entity.UserEntity;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import javax.inject.Inject;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserEntityRepositoryTest extends TestCase {
	
	@Inject
	private UserRepository userRepository;
	
	@Test
	public void findAll() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(6);
		userEntity.setEmail("email@gmail.com");
		userRepository.insert(userEntity);
		
		List<UserEntity> userEntities = userRepository.findAll();
		assertTrue(userEntities.size() > 0);
		userRepository.delete(userEntity.getUserId());
	}
	
	@Test
	public void findById() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(6);
		userEntity.setEmail("email@gmail.com");
		
		userRepository.insert(userEntity);
		assertEquals(userEntity.getUserId(), userRepository.findById(userEntity.getUserId()).getUserId());
		userRepository.delete(userEntity.getUserId());
	}
	
	@Test
	public void insert() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(6);
		userEntity.setEmail("user1@gmail.com");
		userRepository.insert(userEntity);
		
		boolean d = userEntity.getEmail().equals(userRepository.findByEmail("user1@gmail.com").getEmail());
		assertTrue(d);
		userRepository.delete(userEntity.getUserId());
	}
	
	@Test
	public void update() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(6);
		userEntity.setEmail("user1@gmail.com");
		userRepository.insert(userEntity);
		
		userEntity = userRepository.findById(userEntity.getUserId());
		userEntity.setPhone("145-956-444");
		
		userRepository.update(userEntity);
		assertEquals(userEntity.getPhone(), userRepository.findById(userEntity.getUserId()).getPhone());
		userRepository.delete(userEntity.getUserId());
	}
	
	@Test
	public void delete() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(6);
		userEntity.setEmail("user123@gmail.com");
		userRepository.insert(userEntity);
		
		assertThat(userRepository.findById(userEntity.getUserId())).isNotNull();
		userRepository.delete(userEntity.getUserId());
		assertThat(userRepository.findById(userEntity.getUserId())).isNull();
		userRepository.delete(userEntity.getUserId());
	}
	
	@Test
	public void existByEmail() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(11);
		userEntity.setEmail("user123@gmail.com");
		userRepository.insert(userEntity);
		
		int isExist = userRepository.existByEmail("user123@gmail.com");
		assertEquals(1, isExist);
		userRepository.delete(userEntity.getUserId());
	}
}