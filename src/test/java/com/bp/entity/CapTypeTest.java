package com.bp.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit role testing.
 */
public class CapTypeTest {
	/**
	 * Create role.
	 */
	@Test
	public void createRole() {
		Role role = new Role();
		role.setName("DOCTOR");
		assertEquals("DOCTOR", role.getName());
	}
}
