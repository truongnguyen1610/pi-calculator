package com.pi.calculator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.pi.formula.PIFormulaFactory;
import com.pi.formula.PIFormula;
import com.pi.formula.PIFormulaType;
import com.pi.model.PIData;

/**
 * This class implements {@link PICalculatorInterface} and is used to calculate an approximation of PI.
 * It has {@link #calculatePI(long)} method which is an entry point of calculation process. By invoking this method,
 * the calculation will be started.
 * 
 * <p> To cancel the on-going calculation, an {@link #isCancel} variable is used as one of the exit conditions
 * in {@link #executeCalculation(long)} method. When the {@link #cancelCalculation()} is invoked, <tt>isCancel</tt> is set
 * to true. So that the <tt>excuteCalculation</tt> can exit the loop calculation
 * 
 * <p> To execute the calculation, this class creates formula objects which are used to calculate the PI, based on the {@link PIFormulaType}
 * 
 * @author Truong Nguyen
 * */

public class PICalculator implements PICalculatorInterface {

	/**
	 * Stores the default range in which each {@link PIFormula} thread shall work
	 * */
	private static final int DEFAULT_RANGE = 100000;


	/**
	 * Stores a reference to the thread pool service which is used to control thread's operations
	 * */
	private ExecutorService executor = null;

	/**
	 * A number representing a number of threads are working simultaneously 
	 * */
	private int noOfThread;
	
	/**
	 * Stores the computed PIData
	 * */
	private PIData piData = null;

	/**
	 * An boolean value indicates if the calculation is canceled
	 * */
	private volatile boolean isCancel = false;

	/**
	 * A PIFormulaType used to determine which PIFormula will be created to calculate the PI  
	 * */
	private PIFormulaType formulaType = null;

	/**
	 * A builder helps to create instance of PIFormula
	 * */
	private PICalculatorBuilder builder = null;

	/**
	 * Constructor
	 * */
	public PICalculator( PIFormulaType formula ) {
		this.formulaType = formula;
		this.noOfThread = Runtime.getRuntime().availableProcessors();

		this.executor = Executors.newFixedThreadPool( noOfThread );
		this.builder = new PICalculatorBuilder();
	}

	/**
	 * Executes the calculation by creating {@link PIFormula} threads. This method is invoked by {@link #calculatePI(long)}.
	 * It controls the whole calculation process such as when a new thread should be added to the {@link #executor},
	 * when the calculation should stop and update the computed PI to {@link #piData}
	 * 
	 *  @param n the terminal point in formula used to calculate the PI
	 *  @throws Exception the
	 *                <tt>Exception</tt> may be thrown if any error is occurred during the calculation
	 * */
	private void executeCalculation( long n ) throws Exception {
		List< Future< Double > > piFutureList = new LinkedList<>();
		Double pi = 0.0;
		long startPoint = 0;
		long endPoint = -1; // set to -1 to allow the loop runs at least one time if n = 0

		// loop until the endPoint has not reached n yet and the calculation is not canceled
		while ( !this.isCancel && endPoint < n ) {
			endPoint = startPoint + DEFAULT_RANGE;

			// sets endPoint equal to n if it is large than n
			if (endPoint > n) {
				endPoint = n;
			}

			/*
			 * submits the tasks to thread pool. A queue is used to store
			 * all submitted threads
			 */
			piFutureList.add( executor.submit( builder.newFormulaInstance(
					startPoint, endPoint ) ) );
			startPoint = endPoint + 1;
			
			// allows new threads will be added to the pool if it  is not full
			if ( piFutureList.size() >= noOfThread ) {
				/* 
				 * when the pool is full, checks and gets the result if any thread is done.
				 * so that new thread can be added. this is a blocking method
				 */	
				pi = pi + this.getResultAny( piFutureList );
			}

		}

		pi = pi + this.getResultAll( piFutureList );
		piData = new PIData( pi, endPoint );
	}

	/**
	 * Gets result of a thread, which is done. This is a blocking method.
	 * It will wait until any thread is done. It is invoked by {@link #executeCalculation(long)}}
	 * when the thread pool is full
	 * 
	 * @param piFutureList the future list of threads submitted to the thread pool
	 * @return Double the result of a thread which is done
	 * @throws Exception the <tt>exception</tt> may be throw from {@link Future#get()}
	 * 
	 * */
	private Double getResultAny( List< Future< Double > > piFutureList )
			throws Exception {
		int size = piFutureList.size();
		Future< Double > future;
		Double pi = 0.0;
		int index = 0;

		// loop until any thread is done
		for ( ; ; ) {
			future = piFutureList.get( index );

			if ( future.isDone() ) {
				pi = future.get();
				piFutureList.remove( index );
				break;
			}
			index++;
			index = index % size;
		}
		return pi;
	}

	/**
	 * Gets results of all threads from piFutureList. The result of each thread is retrieved by calling {@link Future#get()}
	 * 
	 * @param piFutureList the future list of threads submitted to the thread pool
	 * @return Double the sum of all thread's result
	 * @throws Exception the <tt>exception</tt> may be thrown from {@link Future#get()}
	 * 
	 * */
	private Double getResultAll( List< Future< Double > > piFutureList)
			throws Exception {
		Double pi = 0.0;
		
		for ( Future< Double > piFuture : piFutureList ) {
			pi = pi + piFuture.get();
		}
		return pi;
	}

	/**
	 * Calculates and returns an approximation of PI with n as a terminal point
	 * 
	 * @param n the terminal point used in a PI formula
	 * @return PIData The PI calculation result
	 * @throws Exception
	 *                The
	 *                <tt>Exception</tt> may be thrown if any error is occurred during the calculation
	 * */
	@Override
	public PIData calculatePI( long n ) throws Exception {
		if ( n < 0 ) {
			throw new Exception(
					"Invalid value received, n value should be equal or larger than 0");
		}
		
		// sets to false when the calculation is starting 
		this.isCancel = false;
		
		// executes the Pi calculation by creating sub-threads
		this.executeCalculation( n );
		
		// shutdowns the thread pool after the calculation is completed
		executor.shutdown();

		return piData;
	}

	/**
	 * Cancels the remaining calculation. 
	 * <p>This method updates the cancel state to true. So that the {@link #calculatePI(long)} can stop
	 * and return the current computed PIData
	 * 
	 * */
	@Override
	public void cancelCalculation() {
		this.isCancel = true;
	}

	/**
	 * Gets the PI data which were already computed
	 * 
	 * @return the PIData were already computed. Null will be returned if the calculation does not start
	 * */
	public PIData getPIData() {
		return piData;
	}

	/**
	 * This is a helper class of {@link PICalculator}. It is used to increase the flexibility 
	 * while creating or getting objects. 
	 * <p>Currently, there is only one method {@link #newFormulaInstance(long, long)} helping
	 * create new formula instance. New methods may be added in the future.
	 * This builder class is also meant for unit testing. we can use this class to mock different objects
	 * */
	private class PICalculatorBuilder {

		/**
		 * Returns a new instance of {@link PIFormula} based on {@link PICalculator#formulaType}
		 * 
		 *  @param startPoint the start point at which the calculation shall begins
		 *  @param endPoint the end point at which the calculation shall ends
		 *  @return PIFormula an instance of {@link PIFormula}
		 * */
		private PIFormula newFormulaInstance( long startPoint, long endPoint ) {
			return new PIFormulaFactory().getPIFormula( formulaType, startPoint,
					endPoint );
		}
	}

}
