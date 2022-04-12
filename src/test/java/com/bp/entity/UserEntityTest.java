package com.bp.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Unit user testing.
 */
public class UserEntityTest {
	/**
	 * Create user.
	 */
	@Test
	public void createUser() throws ParseException {
		UserEntity userEntity = new UserEntity();
		userEntity.setActive(1);
		assertEquals(1, userEntity.getActive());
		userEntity.setEmail("maros.geffert@gmail.com");
		assertEquals("maros.geffert@gmail.com", userEntity.getEmail());
		userEntity.setFirstName("Maros");
		assertEquals("Maros", userEntity.getFirstName());
		userEntity.setLastName("Geffert");
		assertEquals("Geffert", userEntity.getLastName());
		userEntity.setPassword("1234");
		assertEquals("1234", userEntity.getPassword());
		Date date=new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998");
		userEntity.setBirthdate(date);
		assertEquals(date, userEntity.getBirthdate());
		userEntity.setPhone("445-485-663");
		assertEquals("445-485-663", userEntity.getPhone());
	}
	
	@Test
	public void isPasswordValid() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail("geffert@gmail.com");
		userEntity.setPassword("abc123");
		
		Throwable thrown = catchThrowable(() -> {
			userEntity.isPasswordValid(userEntity.getPassword());
		});
		assertThat(thrown).isInstanceOf(AccessDeniedException.class);
	}
}
