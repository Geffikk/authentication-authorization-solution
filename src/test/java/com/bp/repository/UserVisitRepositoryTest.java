package com.bp.repository;

import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserVisitRepositoryTest {
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private VisitRepository visitRepository;
	
	@Inject
	private UserVisitRepository userVisitRepository;
	
	@BeforeEach
	public void init() {
		visitRepository.deleteAll();
		userVisitRepository.deleteAll();
	}
	
	@Test
	public void addUserVisit() {
		UserEntity user = new UserEntity();
		Visit visit = new Visit();
		
		user.setEmail("email@gmail.com");
		user.setUserId(6);
		visit.setVisitId(8);
		visit.setPatientId(5);
		
		userRepository.insert(user);
		visitRepository.insert(visit);
		user = userRepository.findById(user.getUserId());
		visit = visitRepository.findById(visit.getVisitId());
		
		userVisitRepository.addUserVisit(user.getUserId(), visit.getVisitId());
		assertEquals(visit.getVisitId(), userVisitRepository.getVisits(user.getUserId()).get(0).getVisitId());
	}
	
	@Test
	public void deleteVisit() {
		UserEntity user = new UserEntity();
		Visit visit = new Visit();
		
		user.setEmail("email@gmail.com");
		user.setUserId(6);
		visit.setVisitId(7);
		visit.setPatientId(5);
		
		userRepository.insert(user);
		visitRepository.insert(visit);
		user = userRepository.findById(user.getUserId());
		visit = visitRepository.findById(visit.getVisitId());
		
		userVisitRepository.addUserVisit(user.getUserId(), visit.getVisitId());
		assertEquals(visit.getVisitId(), userVisitRepository.getVisits(user.getUserId()).get(0).getVisitId());
		
		userVisitRepository.deleteVisitFromUser(user.getUserId(), visit.getVisitId());
		List<Visit> visitList = userVisitRepository.getVisits(user.getUserId());
		assertEquals(Collections.emptyList(), visitList);
	}
	
	@Test
	public void getVisits() {
		UserEntity user = new UserEntity();
		Visit visit = new Visit();
		Visit visit2 = new Visit();
		
		user.setEmail("email@gmail.com");
		user.setUserId(6);
		visit.setVisitId(8);
		visit2.setVisitId(9);
		visit.setPatientId(5);
		visit2.setPatientId(5);
		
		userRepository.insert(user);
		visitRepository.insert(visit);
		visitRepository.insert(visit2);
		user = userRepository.findById(user.getUserId());
		visit = visitRepository.findById(visit.getVisitId());
		visit2 = visitRepository.findById(visit2.getVisitId());

		userVisitRepository.addUserVisit(user.getUserId(), visit.getVisitId());
		userVisitRepository.addUserVisit(user.getUserId(), visit2.getVisitId());
		List<Visit> visitList = userVisitRepository.getVisits(user.getUserId());
		assertEquals(2, visitList.size());
	}
	
	@Test
	public void getUsers() {
		UserEntity user = new UserEntity();
		UserEntity user2 = new UserEntity();
		Visit visit = new Visit();
		
		user.setEmail("email@gmail.com");
		user2.setEmail("email2@gmail.com");
		user.setUserId(6);
		user2.setUserId(7);
		visit.setVisitId(8);
		visit.setPatientId(5);
		
		userRepository.insert(user);
		userRepository.insert(user2);
		visitRepository.insert(visit);
		user = userRepository.findById(user.getUserId());
		user2 = userRepository.findById(user2.getUserId());
		visit = visitRepository.findById(visit.getVisitId());
		
		userVisitRepository.addUserVisit(user.getUserId(), visit.getVisitId());
		userVisitRepository.addUserVisit(user2.getUserId(), visit.getVisitId());
		List<UserEntity> userEntities = userVisitRepository.getUsers(visit.getVisitId());
		assertEquals(2, userEntities.size());
	}
}