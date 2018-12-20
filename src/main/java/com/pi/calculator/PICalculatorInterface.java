package com.pi.calculator;

import com.pi.model.PIData;

/**
 * This interface defines common methods which are used to produce an approximation of PI.
 * <p>The calculation process will be starting by invoking {@link #calculatePI(long)} method.
 * While the calculation is on-going, {@link #cancelCalculation()} may be used to cancel the calculation 
 * 
 * @author Truong Nguyen
 * */
public interface PICalculatorInterface {

	/**
	 * Calculates and returns an approximation of PI with n as a terminal point
	 * 
	 * @param n the terminal point used in a PI formula
	 * @return PIData The PI calculation result
	 * @throws Exception
	 *                The
	 *                <tt>Exception</tt> may be thrown if any error is occurred during the calculation
	 * */
	public PIData calculatePI( long n ) throws Exception;
	
	/**
	 * Cancels the remaining calculation. 
	 * <p>This method is used to stop the on-going calculation. So that {@link #calculatePI(long)} can stop its operation 
	 * and return the current computed PI
	 * 
	 * */
	public void cancelCalculation();
	
	/**
	 * Gets the PI data which were already computed
	 * 
	 * @return the PIData were already computed. Null will be returned if the calculation does not start
	 * */
	public PIData getPIData();
	
}
