package com.pi.model;

/**
 * PIData is a model class storing PI's calculation result
 * 
 * <p> A PI's calculation result is a pair of PI and n.
 * 
 * @author Truong Nguyen
 * */
public class PIData {

	/**
	 * An double value to store the computed PI
	 * */
	private final double pi;
	
	/**
	 * An long value to store the n value at which PI is calculated
	 * */
	private final long n;
	
	/**
	 * Constructor with two parameters; PI and n values
	 * 
	 * @param pi the pi value of PIData
	 * @param n the n value of PIData
	 * */
	public PIData( double pi, long n ) {
		this.pi = pi;
		this.n = n;
	}

	/**
	 * Gets the PI value of PIData
	 * 
	 * @return the pi value
	 * */
	public double getPi() {
		return pi;
	}

	/**
	 * Gets the n value of PIData
	 * 
	 * @return the n value
	 * */
	public long getN() {
		return n;
	}

}
