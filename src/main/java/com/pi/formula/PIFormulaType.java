package com.pi.formula;

/**
 * To increase the flexibility, This enum class is defined to represent formulas 
 * which are used to calculate an approximation of PI. Currently, there is only one formula {@link #LEIBNIZ}} defined.
 * But more formulas may be added in the future
 * 
 * @author Truong Nguyen
 * */
public enum PIFormulaType {
	LEIBNIZ;

	/**
	 * Returns an {@link PIFormulaType} enum is corresponding with formula string
	 * 
	 * @param formulaType a string represents an enum type
	 * @return PIFormulaType an PIFormulaType enum . 
	 * Null will be returned if no enum type matchs with formula string
	 * */
	public static PIFormulaType getPIFormulaType( String formulaType ) {
		for ( PIFormulaType type : PIFormulaType.values() ) {
			if ( type.toString().equalsIgnoreCase( formulaType ) ) {
				return type;
			}
		}
		return null;
	}
}
