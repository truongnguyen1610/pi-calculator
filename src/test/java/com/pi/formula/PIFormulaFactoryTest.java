package com.pi.formula;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This test class is responsible for testing how an instance of PIFormula created via PIFormulaType
 * 
 * @author Truong Nguyen
 * */
public class PIFormulaFactoryTest {


	/**
	 * Objective:
	 *    The test case verifies an instance of PILeibnizFormula created
	 * 
	 * Precondition:
	 *    Leibniz PIFormulaType enum is passed as an argument.
	 * 
	 * Success/Failure criteria
	 * 
	 * Success:
	 * 	  an instance of PILeibnizFormula created
	 * 
	 * Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPIFormula_IfLeibnizIsPassed() {

		PIFormula piCal = new PIFormulaFactory()
				.getPIFormula( PIFormulaType.LEIBNIZ, 1, 1 );
		assertTrue( piCal instanceof PILeibnizFormula );
	}

	/**
	 * Objective:
	 *    The test case verifies the null returned while testing getPIFormula method
	 * 
	 * Precondition:
	 *    null is passed as an argument.
	 * 
	 * Success/Failure criteria
	 * 
	 * Success:
	 * 	  null value returned
	 * 
	 * Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPIFormula_IfPIFormulaTypeIsNull() {

		PIFormula piCal = new PIFormulaFactory()
				.getPIFormula( null, 1, 1 );
		assertNull( piCal );
	}

}
