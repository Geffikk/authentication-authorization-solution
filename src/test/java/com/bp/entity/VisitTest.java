package com.bp.entity;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit testing visit.
 */
public class VisitTest {
	/**
	 * Create visit.
	 */
	@Test
	public void createVisit() throws ParseException {
		Visit visit = new Visit();
		visit.setFirstName("Jozko");
		assertEquals("Jozko", visit.getFirstName());
		visit.setLastName("Mrkva");
		assertEquals("Mrkva", visit.getLastName());
		visit.setIdentification(2493958302L);
		assertEquals(2493958302L, visit.getIdentification());
		visit.setEmail("jozko.mrvka@gmail.com");
		assertEquals("jozko.mrvka@gmail.com", visit.getEmail());
		visit.setReason("Covid test");
		assertEquals("Covid test", visit.getReason());
		visit.setSymptoms("Fever");
		assertEquals("Fever", visit.getSymptoms());
		Date date=new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998");
		visit.setVisitTime(date);
		assertEquals(date, visit.getVisitTime());
	}
}
