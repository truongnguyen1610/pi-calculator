package com.pi.formula;

/**
 * This factory class helps create a specific instance of {@link PIFormula}
 * based on PICalculatorType parameter
 * 
 * @author Truong Nguyen
 * */
public class PIFormulaFactory {

	/**
	 * Returns an instance of
	 * {@link PIFormula} which is corresponding with {@link PIFormulaType}
	 * 
	 * @param formulaType an enum represents a type of formula
	 * @param startPoint a value at which {@link PIFormula} begins the calculation
	 * @param endPoint a value at which {@link PIFormula} ends the calculation
	 * @return PIFormula an instance of {@link PIFormula}. Null will be returned if PICalculatorType is invalid
	 *  
	 * */
	public PIFormula getPIFormula( PIFormulaType formulaType, long startPoint, long endPoint ) {
		PIFormula piFormula = null;

		if ( PIFormulaType.LEIBNIZ == formulaType ) {
			piFormula = new PILeibnizFormula( startPoint, endPoint );
		}
		return piFormula;
	}

}
