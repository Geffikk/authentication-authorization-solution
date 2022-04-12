package com.bp.repository;

import com.bp.entity.Capability;
import com.bp.entity.UserEntity;
import com.bp.utility.CS;
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
public class CapabilityRepositoryTest {
	
	@Inject
	private CapabilityRepository capabilityRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@BeforeEach
	public void init() {
		capabilityRepository.deleteAll();
	}
	
	@Test
	public void findAll() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("Mfas123ke");
		userRepository.insert(userEntity);
		
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser(userEntity.getEmail());
		capability.setCapId(1);
		capabilityRepository.insert(capability);
		assertTrue(capabilityRepository.findAll().size() > 0);
		capabilityRepository.delete(capability.getCapId());
	}
	
	@Test
	public void findById() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("Mfas123ke");
		userRepository.insert(userEntity);
		
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser(userEntity.getEmail());
		capability.setCapId(1);
		capabilityRepository.insert(capability);
		assertEquals(capability.getCapId(), capabilityRepository.findById(capability.getCapId()).getCapId());
		capabilityRepository.delete(capability.getCapId());
	}
	
	@Test
	public void findByRole() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("Mfas123ke");
		userRepository.insert(userEntity);
		
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser(userEntity.getEmail());
		capability.setCapId(1);
		capability.setEntityType("new_entity");
		capability.setEntityId(1);
		capabilityRepository.insert(capability);
		assertEquals(
			capability.getRole(), capabilityRepository.findBySpecificRole(capability.getEntityType(),
			capability.getEntityId(), capability.getRole()).getRole());
		capabilityRepository.delete(capability.getCapId());
	}
	
	@Test
	public void findByCapability() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("test@gmail.com");
		userEntity.setPassword("Mfas123ke");
		userRepository.insert(userEntity);
		
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser(userEntity.getEmail());
		capability.setCapId(1);
		capability.setEntityType("new_entity");
		capability.setEntityId(1);
		capabilityRepository.insert(capability);
		assertEquals(
			capability.getRole(), capabilityRepository.findBySpecificRole(capability.getEntityType(),
				capability.getEntityId(), capability.getRole()).getRole());
		capabilityRepository.delete(capability.getCapId());
	}
	
	@Test
	public void insert() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("Mfas123ke");
		userRepository.insert(userEntity);
		
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser(userEntity.getEmail());
		capability.setCapId(1);
		capabilityRepository.insert(capability);
		assertEquals(capability.getCapId(), capabilityRepository.findById(capability.getCapId()).getCapId());
		capabilityRepository.delete(capability.getCapId());
	}
	
	@Test
	public void update() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("Mfas123ke");
		userRepository.insert(userEntity);
		
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser(userEntity.getEmail());
		capability.setRead(true);
		capability.setCapId(1);
		capabilityRepository.insert(capability);
		capability = capabilityRepository.findById(capability.getCapId());
		capability.setRead(false);
		capabilityRepository.update(capability);
		assertEquals(capability.isRead(), capabilityRepository.findById(capability.getCapId()).isRead());
		capabilityRepository.delete(capability.getCapId());
	}
	
	@Test
	public void delete() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("maros.geffert@gmail.com");
		userEntity.setPassword("Mfas123ke");
		userRepository.insert(userEntity);
		
		Capability capability = new Capability();
		capability.setRole(CS.PERSONAL);
		capability.setUser(userEntity.getEmail());
		capability.setRead(true);
		capability.setCapId(1);
		capabilityRepository.insert(capability);
		assertThat(capabilityRepository.findById(capability.getCapId())).isNotNull();
		capabilityRepository.delete(capability.getCapId());
		assertThat(capabilityRepository.findById(capability.getCapId())).isNull();
	}
}