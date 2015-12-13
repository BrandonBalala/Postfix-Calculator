package com.brandonbalala.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class EvaluatorUtility {
	private static final String OPENING_PARENTHESIS = "(";
	private static final String CLOSING_PARENTHESIS = ")";
	public static final String ADDITION = "+";
	public static final String SUBSTRACTION = "-";
	public static final String MULTIPLICATION = "*";
	public static final String DIVISION = "/";
	public static final String DECIMAL_POINT = ".";
	private static final Set<String> OPERATORS = new HashSet<String>(
			Arrays.asList(new String[] { ADDITION, SUBSTRACTION, MULTIPLICATION, DIVISION }));

	public static final String STARTING_ERR_MSG = "Invalid expression, can't start expression with an operator or a closing parenthesis";
	public static final String ENDING_ERR_MSG = "Invalid expression, can't end expression with an operator or an opening parenthesis";
	public static final String OPERATOR_ERR_MSG = "Invalid expression, an operator can't proceed another operator or an opening parenthesis";
	public static final String OPENING_PARENTHESIS_ERR_MSG = "Invalid expression, an opening parenthesis can't proceed a number or an closing parenthesis";
	public static final String CLOSING_PARENTHESIS_ERR_MSG = "Invalid expression, a closing parenthesis can't proceed an operator or an opening parenthesis";
	public static final String CHARACTER_ERR_MSG = "Invalid expression. Can only be composed of numbers, operators and parentheses";
	public static final String PARENTHESES_ERR_MSG = "Invalid expression, misuse of parentheses";
	public static final String NUMBER_ERR_MSG = "Invalid expression, a number can't proceed a closing parenthesis";
	public static final String NUMBER_FORMAT_ERR_MSG = "Invalid expression, invalid format for a number";
	public static final String EMPTY_ERR_MSG = "Invalid expression, passed a null or an empty string";
	
	/**
	 * Regular expression that checks whether given string is a numeric
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumeric(String string) {
		  try  
		  {  
		    double d = Double.parseDouble(string);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true; 
	}

	public static boolean isOperator(String string) {
		return OPERATORS.contains(string);
	}

	public static boolean isOpeningParenthesis(String string) {
		return string.equals(OPENING_PARENTHESIS);
	}

	public static boolean isClosingParenthesis(String string) {
		return string.equals(CLOSING_PARENTHESIS);
	}
	
	public static boolean isAddition(String string) {
		return string.equals(ADDITION);
	}
	
	public static boolean isSubstraction(String string) {
		return string.equals(SUBSTRACTION);
	}
	
	public static boolean isNegativeSign(String string) {
		return string.equals(SUBSTRACTION);
	}
	
	public static boolean isMultiplication(String string) {
		return string.equals(MULTIPLICATION);
	}
	
	public static boolean isDivision(String string) {
		return string.equals(DIVISION);
	}
	
	public static boolean isDecimalPoint(String string) {
		return string.equals(DECIMAL_POINT);
	}
	
	public static int getOperatorPrecedence(String operator) {
		if(isAddition(operator) || isSubstraction(operator)){
			return 1;
		}
		
		if(isMultiplication(operator) || isDivision(operator)){
			return 2;
		}
		
		return -1;
	}
}
