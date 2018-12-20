package com.pi.calculator;

import static org.junit.Assert.*;

import com.pi.model.PIData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pi.formula.PIFormulaType;

/**
 * This test class is responsible for testing how an approximation of Pi is calculated
 * 
 * @author Truong Nguyen
 * */
public class PICalculatorTest {

	/**
	 * Stores reference of an instance of PILeibnizCalculator class 
	 * */
	private PICalculatorInterface piCal = null;
	
	/**
	 * Uses to setup common preconditions for every test case
	 * This method is invoked before a test case is about to be executed 
	 * */
	@Before
	public void setUp() throws Exception {
		piCal = new PICalculator( PIFormulaType.LEIBNIZ );
	}

	/**
	 * Uses to release resources
	 * This method is invoked after a test case is completed 
	 * */
	@After
	public void tearDown() throws Exception {
		piCal = null;
	}

	/**
	 * <p>Objective:
	 *    The test case verifies the value of the produced Pi when the n is positive<p> 
	 * 
	 * <p>Precondition:
	 *   n = 100000,
	 *   deviation = 1e-5
	 * 
	 * Success/Failure criteria
	 * 
	 * <p>Success:
	 * 	   Deviation between Math.PI and the produced Pi is less than 1e-5
	 * 
	 * <p>Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPI_IfNIsPositive() {
		long n = 100000;
		double expectedDeviation = 1e-5;
		double actualDeviation;

		try {
			piCal.calculatePI( n );
			actualDeviation = Math.abs( ( Math.PI - piCal.getPIData().getPi() ) );
			assertTrue( actualDeviation < expectedDeviation );
			
		} catch ( Exception e ) {
			fail( "Exception was occurred, Reason " + e.getMessage() );
		}
	}

	/**
	 * <p>Objective:
	 *    The test case verifies the value of the produced Pi when the n is zero<p> 
	 * 
	 * <p>Precondition:
	 *   n = 0
	 * 
	 * Success/Failure criteria
	 * 
	 * <p>Success:
	 * 	   The returned Pi is 4.0
	 * 
	 * <p>Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPI_IfNIsZero() {
		long n = 0;

		try {
			PIData piModel = piCal.calculatePI( n );
			assertEquals( 4.0, piModel.getPi(), 0 );
		} catch ( Exception e ) {
			fail( "Exception was occurred, Reason " + e.getMessage() );
		}
	}

	/**
	 * <p>Objective:
	 *    The test case verifies the scenario when the n is negative<p> 
	 * 
	 * <p>Precondition:
	 *   n = -1
	 * 
	 * Success/Failure criteria
	 * 
	 * <p>Success:
	 * 	   The exception shall be thrown
	 * 
	 * <p>Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPI_IfNIsNegative() {
		long n = -1;

		try {
			piCal.calculatePI( n );
			fail("Exception should be throw when n is less than 0");
		} catch ( Exception e ) {
			// expected exception
			assertTrue( true );
		}
	}
	
	/**
	 * <p>Objective:
	 *    The test case verifies the coverage of the computed Pi using Leibniz formula<p> 
	 * 
	 * <p>Precondition:
	 *   initial n = 10000, step 1000 
	 *   loop = 10
	 * 
	 * Success/Failure criteria
	 * 
	 * <p>Success:
	 * 	   The deviation between the PI and Math.PI shall be decreasing when n is increasing
	 * 
	 * <p>Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_GetPI_Coverage_IfNIsIncrease() {
		int loop;
		long []nValues;
		long initN, step;
		double[] deviationList;
		PICalculatorInterface piCal;
		
		loop = 10;
		nValues = new long[ loop ];
		initN = 10000;
		step = 1000;
		deviationList = new double[ loop ];
		
		nValues[ 0 ] = initN;
		for ( int i = 1; i < loop; i++ ) {
			nValues[ i ] = nValues[ i - 1 ] + step; 
		}

		try {
			
			// calculate pi with n is increasing in each loop 
			for ( int i = 0; i < loop; i++ ) {
				piCal = new PICalculator( PIFormulaType.LEIBNIZ );
				deviationList[ i] = Math.abs( piCal.calculatePI( nValues[ i ] ).getPi() - Math.PI );
				
			}
			
			// verify
			for ( int i = 1; i < loop; i++ ) {
				assertTrue( deviationList[ i - 1 ] > deviationList[i]);
			}
			
		} catch ( Exception e ) {
			fail( "Exception was occurred, Reason " + e.getMessage() );
		}
	}
	
	/**
	 * <p>Objective:
	 *    The test case verifies the calculation is cancelled successfully 
	 * 
	 * <p>Precondition:
	 *   n = 1000000000,
	 *   waitTime = 2 seconds,
	 *   delta = 1e-14
	 * 
	 * Success/Failure criteria
	 * 
	 * <p>Success:
	 * 	   The Pi value is calculated correctly before the cancellation is triggered
	 * 
	 * <p>Failure:
	 *    other scenarios
	 * 
	 * */
	@Test
	public void test_cancelCalculation() {
		long n = 1000000000;
		final int waitTime = 2;
		PIData cancelledPI, normalPI;
		
		/*
		 * The maximum delta between expected and actual for which both numbers are still considered equal.
		 * Because Floating point calculations are not exact - there is often round-off errors,
		 * */
		double delta = 1e-14;

		try {

			Thread canCelThread = new Thread( new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep( waitTime * 1000 );
						piCal.cancelCalculation();
					} catch ( Exception e ) {
						fail( "Exception was occurred, Reason " + e.getMessage() );
					}
				}
			});
			
			canCelThread.start();
			
			cancelledPI = piCal.calculatePI( n );
			
			// calculates the Pi value normally before the interrupt point
			normalPI = new PICalculator( PIFormulaType.LEIBNIZ )
					.calculatePI( cancelledPI.getN() );

			// verify if the Pi value is calculated correctly or not when the cancellation is triggered
			assertEquals( normalPI.getPi(), cancelledPI.getPi(), delta);
			
			
		} catch ( Exception e ) {
			fail( "Exception was occurred, Reason " + e.getMessage() );
		}
	}
	
}