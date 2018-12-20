package com.pi.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.pi.calculator.PICalculator;
import com.pi.calculator.PICalculatorInterface;
import com.pi.formula.PIFormulaType;
import com.pi.model.PIData;

/**
 * This is the main class of the application. Following are its main functions
 *  
 * <p>1. Be responsible for handing inputs, help check the validity of parameters
 * <p>2. Parse the parameters to the proper types used by the application
 * <p>3. Create an instance of PICalculator class in order to calculate the Pi value
 * <p>4. Listen to the ENTER key to stop the on-going calculation
 * <p>5. Output the Pi value along with the elapsed time duration
 * 
 * @author Truong Nguyen
 * */

public class PICalculatorMain {

	/**
	 * An constant string, which is used by cmdLineOptions to parse the formula type
	 * */
	private static final String TYPE_STR = "type";

	/**
	 * An constant string, which is used by cmdLineOptions to parse n value
	 * */
	private static final String N_STR = "n";

	/**
	 * The singleton instance of PICalculatorMain class
	 * */
	private static final PICalculatorMain thePiCalculatorMain = new PICalculatorMain();

	/**
	 * Stores the formula type parsed from cmdLineOptions
	 * */
	private PIFormulaType formulaType = null;

	/**
	 * Stores n value parsed from cmdLineOptions
	 * */
	private long n;

	/**
	 * An command line options used to parse the command line arguments
	 * */
	private Options cmdLineOptions = null;

	/**
	 * Stores an instance of PICalculatorInterface used to calculate PI
	 * */
	private PICalculatorInterface piCal = null;
	
	/**
	 * Stores the starting time when the calculation begins
	 * */
	private long startTime;
	
	/**
	 * Constructor, The modifier is private because this is an singleton class
	 * */
	private PICalculatorMain() {
		
		// sets default values for formulaType and n
		formulaType = PIFormulaType.LEIBNIZ;
		n = 100000000;

		cmdLineOptions = new Options();
		cmdLineOptions
				.addOption(
						TYPE_STR,
						true,
						"The formula is used to calculate an approximation of Pi. Default value is leibniz" );
		cmdLineOptions
				.addOption(
						N_STR,
						true,
						"The terminal point indicates when the program shall stop and return the Pi. Default value is 100,000,000" );
	}
	
	/**
	 * Outputs the calculation result to the screen. 
	 * 
	 * @param piData the {@link PIData} 
	 * 
	 * */
	private void outputResult( PIData piData ) {
		long timeTaken = System.nanoTime() - startTime;
		
		System.out.println( "\nPI = " + piData.getPi() + " with n->"
				+ piData.getN() );
		System.out.println( "Time took: " + timeTaken / 1e9 + " seconds" );
		
	}
	
	/**
	 * Prints the user guide if the arguments are not valid.
	 * 
	 * @param title the usage title used to output to the screen
	 * 
	 * */
	private void printUsage( String title) {
		if( formulaType == null ){
			System.out.println("The formula is not available or not valid. Please check pi calculator usage\n");
		}
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( title, cmdLineOptions );
	}
	
	/**
	 * Populates parameters used for calculating PI
	 * 
	 * @param cmdLine The CommandLine is used to populate the parameters
	 * @return true if all parameters are populated correctly; false otherwise
	 * 
	 * */
	private boolean populateParams( CommandLine cmdLine ) {
		boolean isParamsValid = true;

		if ( cmdLine.hasOption( TYPE_STR ) ) {
			formulaType = PIFormulaType.getPIFormulaType( cmdLine
					.getOptionValue( TYPE_STR ) );
			isParamsValid =  (null != formulaType );
			
		}

		if ( cmdLine.hasOption( N_STR ) ) {
			try {
				n = Long.parseLong( cmdLine.getOptionValue( N_STR ) );
				isParamsValid = ( isParamsValid && n >= 0 );
			} catch ( NumberFormatException e ) {
				isParamsValid = false;
			}

		}

		return isParamsValid;
	}
	
	/**
	 * Returns an singleton instance of PICalculatorMain class
	 * 
	 * @return PICalculatorMain
	 * 
	 * */
	public static PICalculatorMain getInstance() {
		return thePiCalculatorMain;
	}

	/**
	 * Starts the process of calculating the Pi value with the command line arguments
	 * 
	 * @param args the command line arguments
	 * 
	 * */
	public void run( String[] args ) {

		CommandLineParser parser = new BasicParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( cmdLineOptions, args );
			
			/* Starts calculating PI only if
			 * the parameters are populated successfully 
			 */
			if ( this.populateParams( line ) ) {

				Thread cancelThread = new Thread( new CancelRunable() );
				cancelThread.setDaemon( true );
				cancelThread.start();
				
				piCal = new PICalculator( this.formulaType );
				
				System.out
						.println("Please wait while the calculation is on going...\n"
								+ "Press ENTER if you want to stop the calculation and get the Pi value");
				
				startTime = System.nanoTime();
				
				this.outputResult( piCal.calculatePI( n ) );

			} else {
				this.printUsage( "pi calculator" );
			}

		} catch ( ParseException exp ) {
			this.printUsage( "pi calculator" );

		} catch ( Exception exp ) {
			exp.printStackTrace();
			System.err.println( "Error while calculating Pi.  Reason: "
					+ exp.getMessage() );
		}

	}

	/**
	 * This inner class is used by {@link PICalculatorMain} to observe if the ENTER key is pressed , 
	 * and then calls {@link PICalculator #cancelCalculation()} if the ENTER key is triggered
	 * */
	private class CancelRunable implements Runnable {

		/**
		 * Invoked when the thread is starting
		 * */
		@Override
		public void run() {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String line;
			try {
				do{
					line = in.readLine();
				}
				while ( line.length() != 0 );
				
				piCal.cancelCalculation();
				in.close();
				
			} catch ( Exception ex ) {
                System.err.println("Error occurred while reading the input stream; Reason " + ex.getMessage() );
                
			}
		}

	}

	/**
	 * Entry point of the application. This is where the program is starting
	 * 
	 * @param args the command line arguments
	 * 
	 * */

	public static void main(String[] args) {
		
		// runs the programs
		PICalculatorMain.getInstance().run( args );
	}
}
