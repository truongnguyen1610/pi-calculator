package com.pi.formula;

import java.util.concurrent.Callable;

/**
 * An abstract class is used to produce an approximation of PI. It defines common properties used to
 * calculate the PI. It implements Callable< Double > interface for thread support
 * 
 * <p> An abstract {@link #calculate()} is declared. This is used to calculate the PI from
 * {@link #startPoint} to {@link #endPoint}. The formula is used in this method depending on type of subclass
 * 
 * @author Truong Nguyen
 * */
public abstract class PIFormula implements Callable< Double > {

	/**
	 * Stores the start value of the range at which the calculation begins  
	 * */
	protected final long startPoint;
	
	/**
	 * Stores the end value of the range at which the calculation ends  
	 * */
	protected final long endPoint;

	/**
	 * Constructor
	 * 
	 * @param startPoint
	 *            the start value of the range at which the calculation begins
	 * @param endPoint
	 *            the end value of the range at which the calculation ends
	 * */
	public PIFormula( long startPoint, long endPoint ) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	/**
	 * The method is used to calculate the PI from start point to end point.
	 * The formula is used for calculation depending on the formula type of subclass
	 * 
	 * */
	protected abstract double calculate();
	
	/**
	 * The method is invoked when the thread is starting
	 * 
	 * @param Double the computed value
	 * */
	@Override
	public final Double call() throws Exception {
		return this.calculate();
	}

}