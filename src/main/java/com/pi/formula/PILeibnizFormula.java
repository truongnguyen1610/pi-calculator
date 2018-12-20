package com.pi.formula;

/**
 * A class is used to produce an approximation of PI using Leibniz formula.
 * 
 * @author Truong Nguyen
 * */
public class PILeibnizFormula extends PIFormula{

	/**
	 * Constructor
	 * */
	public PILeibnizFormula( long startPoint, long endPoint ) {
		super( startPoint, endPoint );
	}

	/**
	 * Calculates the Pi value from startpoint to endpoint using Leibniz formula 
	 * 
	 * <p>Leibniz Formula: PI = 4 - 4/3 + 4/5 - 4/7...go on
	 * */
	protected double calculate() {
		double pi = 0;
		int sign;
		long denominator;
		
		sign = ( startPoint % 2 == 0 )? 1 : -1 ;
		
		for ( long n = startPoint; n <= endPoint; n++ ) {
			denominator = ( ( 2 * n ) + 1 ) * sign;
			pi = pi +  4.0 / denominator;

			sign = -sign;
		}
		return pi;
	}

}
