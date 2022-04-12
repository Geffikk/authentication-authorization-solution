package com.bp.entity;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit testing patient.
 */
public class PatientTest {
	/**
	 * Create patient.
	 */
	@Test
	public void createPatient() throws ParseException {
		Patient patient = new Patient();
		patient.setFirstName("Jozko");
		assertEquals("Jozko", patient.getFirstName());
		patient.setLastName("Mrkva");
		assertEquals("Mrkva", patient.getLastName());
		Date date=new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998");
		patient.setBirthdate(date);
		assertEquals(date, patient.getBirthdate());
		patient.setCity("Kosice");
		assertEquals("Kosice", patient.getCity());
		patient.setAddress("Pod Lipkou 20");
		assertEquals("Pod Lipkou 20", patient.getAddress());
		patient.setEmail("pacient1@gmail.com");
		assertEquals("pacient1@gmail.com", patient.getEmail());
		patient.setPhone("124-485-632");
		assertEquals("124-485-632", patient.getPhone());
	}
}
