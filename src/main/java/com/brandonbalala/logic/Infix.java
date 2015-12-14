package com.brandonbalala.logic;

import java.util.ArrayDeque;
import java.util.Queue;

import com.brandonbalala.exception.InfixParsingException;
import com.brandonbalala.exception.PostfixParsingException;
import com.brandonbalala.utility.EvaluatorUtility;

public class Infix {
	private Queue<String> infixQueue;
	private String lastOffer;


	/**
	 * Constructor
	 */
	public Infix() {
		// Creating a queue in Java 1.6
		infixQueue = new ArrayDeque<String>();
		lastOffer = "";
	}

	/**
	 * Get the infix queue
	 * @return infixQueue
	 */
	public Queue<String> getInfixQueue() {
		return infixQueue;
	}

	/**
	 * Sets the infix queue, by parsing the string into a queue. 
	 * @param expression
	 * @throws InfixParsingException, if string does not represent a proper mathematical equation
	 */
	public void setInfixQueue(String expression) throws InfixParsingException {
		parseInfix(expression);
	}

	/**
	 * Parses the string expression into an infix expression
	 * @param expression
	 * @throws InfixParsingException, if string does not represent a proper mathematical equation
	 */
	private void parseInfix(String expression) throws InfixParsingException {
		//Check for null or empty
		if (expression == null || expression.isEmpty()) {
			throwInfixParsingException(EvaluatorUtility.EMPTY_ERR_MSG);
		}

		//Initialize variables
		String tempNumber = "";
		int openingCntr = 0;
		int closingCntr = 0;

		//Split string into array of chars
		char[] characters = expression.trim().toCharArray();

		String firstChar = String.valueOf(characters[0]);
		String lastChar = String.valueOf(characters[characters.length - 1]);

		// Makes sure that the expression does not start with an ) or any of the
		// operators(+,-,%,*)
		if ((EvaluatorUtility.isOperator(firstChar) || EvaluatorUtility.isClosingParenthesis(firstChar)) && !EvaluatorUtility.isSubtraction(firstChar))
			throwInfixParsingException(EvaluatorUtility.STARTING_ERR_MSG);

		// Makes sure that the expression does not end with an ( or any of the
		// operators(+,-,%,*)
		if (EvaluatorUtility.isOperator(lastChar) || EvaluatorUtility.isOpeningParenthesis(lastChar))
			throwInfixParsingException(EvaluatorUtility.ENDING_ERR_MSG);

		//Looping through the array of chars, checks what type of character it is and deals with it
		for (int cntr = 0; cntr < characters.length; cntr++) {
			String theChar = String.valueOf(characters[cntr]);

			if (EvaluatorUtility.isOperator(theChar)) {
				tempNumber = addNumberToQueue(tempNumber);

				if (!EvaluatorUtility.isNegativeSign(theChar) && (EvaluatorUtility.isOperator(lastOffer) || EvaluatorUtility.isOpeningParenthesis(lastOffer)))
					throwInfixParsingException(EvaluatorUtility.OPERATOR_ERR_MSG);

				//Deals with the negative sign
				if (EvaluatorUtility.isNegativeSign(theChar)
						&& (lastOffer.isEmpty() || EvaluatorUtility.isOperator(lastOffer) || EvaluatorUtility.isOpeningParenthesis(lastOffer)))
					tempNumber += theChar;
				else
					addOperatorToQueue(theChar);
			} else if (EvaluatorUtility.isOpeningParenthesis(theChar)) {
				tempNumber = addNumberToQueue(tempNumber);

				if (EvaluatorUtility.isNumeric(lastOffer) || EvaluatorUtility.isClosingParenthesis(lastOffer))
					throwInfixParsingException(EvaluatorUtility.OPENING_PARENTHESIS_ERR_MSG);

				addOperatorToQueue(theChar);
				openingCntr++;
			} else if (EvaluatorUtility.isClosingParenthesis(theChar)) {
				tempNumber = addNumberToQueue(tempNumber);

				if (EvaluatorUtility.isOperator(lastOffer) || EvaluatorUtility.isOpeningParenthesis(lastOffer))
					throwInfixParsingException(EvaluatorUtility.CLOSING_PARENTHESIS_ERR_MSG);

				addOperatorToQueue(theChar);
				closingCntr++;
				
				//Checks that the opening parenthesis was used before the closing parenthesis
				if(closingCntr > openingCntr)
					throwInfixParsingException(EvaluatorUtility.PARENTHESES_ERR_MSG);
			} else if (EvaluatorUtility.isDecimalPoint(theChar)) {
				tempNumber += theChar;
			} else if (EvaluatorUtility.isNumeric(theChar)) {
				tempNumber += theChar;
			} else {
				throwInfixParsingException(EvaluatorUtility.CHARACTER_ERR_MSG);
			}
		}

		// If anything left in tempNumber
		addNumberToQueue(tempNumber);

		// Makes sure that all opening parentheses have their respective closing
		// parentheses
		if (openingCntr != closingCntr)
			throwInfixParsingException(EvaluatorUtility.PARENTHESES_ERR_MSG);

	}
	
	/**
	 * Deals with adding the given number, if it is actually a number, to the infix Queue 
	 * @param number
	 * @return
	 * @throws InfixParsingException
	 */
	private String addNumberToQueue(String number) throws InfixParsingException {

		//Check empty
		if (!number.isEmpty()) {
			if (EvaluatorUtility.isClosingParenthesis(lastOffer))
				throwInfixParsingException(EvaluatorUtility.NUMBER_ERR_MSG);

			//Check that it truly is a numeric
			if (!EvaluatorUtility.isNumeric(number))
				throwInfixParsingException(EvaluatorUtility.NUMBER_FORMAT_ERR_MSG);

			//Adding to the infix queue
			infixQueue.offer(number);
			//keep track of the last added element
			lastOffer = number;

			//will end up clearing up the tempNumber variable 
			return "";
		}
		
		//returns the empty number string
		return number;
	}

	/**
	 * Adds the given operator to the infix queue
	 * @param element
	 */
	private void addOperatorToQueue(String element) {
		infixQueue.offer(element);
		lastOffer = element;
	}

	/**
	 * Empties the infix queue
	 */
	private void emptyInfixQueue() {
		infixQueue = new ArrayDeque<String>();
		lastOffer = "";
	}

	/**
	 * 	Throws exception and empty the infix queue
	 * @param message
	 * @throws InfixParsingException
	 */
	private void throwInfixParsingException(String message) throws InfixParsingException {
		emptyInfixQueue();
		throw new InfixParsingException(message);
	}
}
