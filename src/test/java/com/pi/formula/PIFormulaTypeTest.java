package com.pi.formula;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This test class is mainly used to test how the {@link PIFormulaType#getPIFormulaType(String)} method returns
 * an enum {@link PIFormulaType} corresponding to string 
 * 
 * @author Truong Nguyen
 * */
public class PIFormulaTypeTest {

	/**
	 * Objective:
	 *    The test case verifies PIFormulaType.LEIBNIZ shall be returned if leibniz is passed 
	 * 
	 * Precondition:
	 *    formulaStr = "leibniz"
	 * 
	 * Success/Failure criteria
	 * 
	 * Success:
	 * 	  PIFormulaType.LEIBNIZ returned
	 * 
	 * Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPICalType_IfLeibnizIsPassed() {
		String formulaStr = "leibniz";
		
		assertEquals( PIFormulaType.LEIBNIZ, PIFormulaType.getPIFormulaType( formulaStr ) );
	}

	/**
	 * Objective:
	 *    The test case verifies null shall be returned if invalid formula string is passed 
	 * 
	 * Precondition:
	 *    formulaStr = "arctangent"
	 * 
	 * Success/Failure criteria
	 * 
	 * Success:
	 * 	  Null returned
	 * 
	 * Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPICalType_IfLeibnizIsInvalid() {
		String formulaStr = "arctangent";
		
		assertNull( PIFormulaType.getPIFormulaType( formulaStr ));
	}

}
