package com.brandonbalala.logic;

import java.util.ArrayDeque;
import java.util.Queue;

import com.brandonbalala.exception.InfixParsingException;
import com.brandonbalala.exception.PostfixParsingException;
import com.brandonbalala.utility.EvaluatorUtility;

public class Infix {
	private Queue<String> infixQueue;
	private String lastOffer;


	public Infix() {
		// Creating a queue in Java 1.6
		infixQueue = new ArrayDeque<String>();
		lastOffer = "";
	}

	public Queue<String> getInfixQueue() {
		return infixQueue;
	}

	public void setInfixQueue(String expression) throws InfixParsingException {
		parseInfix(expression);
	}

	private void parseInfix(String expression) throws InfixParsingException {
		if (expression.equals(null) || expression.isEmpty()) {
			throwInfixParsingException(EvaluatorUtility.EMPTY_ERR_MSG);
		}

		String tempNumber = "";
		int openingCntr = 0;
		int closingCntr = 0;

		char[] characters = expression.trim().toCharArray();

		String firstChar = String.valueOf(characters[0]);
		String lastChar = String.valueOf(characters[characters.length - 1]);

		// Makes sure that the expression does not start with an ) or any of the
		// operators(+,-,%,*)
		if ((EvaluatorUtility.isOperator(firstChar) || EvaluatorUtility.isClosingParenthesis(firstChar)) && !EvaluatorUtility.isSubstraction(firstChar))
			throwInfixParsingException(EvaluatorUtility.STARTING_ERR_MSG);

		// Makes sure that the expression does not end with an ( or any of the
		// operators(+,-,%,*)
		if (EvaluatorUtility.isOperator(lastChar) || EvaluatorUtility.isOpeningParenthesis(lastChar))
			throwInfixParsingException(EvaluatorUtility.ENDING_ERR_MSG);

		for (int cntr = 0; cntr < characters.length; cntr++) {
			String theChar = String.valueOf(characters[cntr]);

			if (EvaluatorUtility.isOperator(theChar)) {
				tempNumber = addNumberToQueue(tempNumber);

				if (!EvaluatorUtility.isNegativeSign(theChar) && (EvaluatorUtility.isOperator(lastOffer) || EvaluatorUtility.isOpeningParenthesis(lastOffer)))
					throwInfixParsingException(EvaluatorUtility.OPERATOR_ERR_MSG);

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

	private String addNumberToQueue(String number) throws InfixParsingException {

		if (!number.isEmpty()) {
			if (EvaluatorUtility.isClosingParenthesis(lastOffer))
				throwInfixParsingException(EvaluatorUtility.NUMBER_ERR_MSG);

			if (!EvaluatorUtility.isNumeric(number))
				throwInfixParsingException(EvaluatorUtility.NUMBER_FORMAT_ERR_MSG);

			infixQueue.offer(number);
			lastOffer = number;

			return "";
		}
		return number;
	}

	private void addOperatorToQueue(String element) {
		infixQueue.offer(element);
		lastOffer = element;
	}

	private void emptyInfixQueue() {
		infixQueue = new ArrayDeque<String>();
		lastOffer = "";
	}

	private void throwInfixParsingException(String message) throws InfixParsingException {
		emptyInfixQueue();
		throw new InfixParsingException(message);
	}
}
