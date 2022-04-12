package com.bp.entity;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit testing procedure.
 */
public class ProcedureTest {
	/**
	 * Create procedure.
	 */
	@Test
	public void createProcedure() throws ParseException {
		Procedure procedure = new Procedure();
		procedure.setBilling("80$");
		assertEquals("80$", procedure.getBilling());
		Date date=new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998");
		procedure.setProcedureTime(date);
		assertEquals(date, procedure.getProcedureTime());
		procedure.setProcedureType("Test on covid");
		assertEquals("Test on covid", procedure.getProcedureType());
		procedure.setResult("Negative");
		assertEquals("Negative", procedure.getResult());
	}
}
