package com.brandonbalala.logic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import com.brandonbalala.exception.InfixParsingException;
import com.brandonbalala.exception.PostfixParsingException;
import com.brandonbalala.utility.EvaluatorUtility;

public class Postfix {
	private Queue<String> postfixQueue;
	private Deque<String> operatorStack;
	private Deque<String> operandStack;
	private Deque<String> expressionDeque;

	/**
	 * Constructor
	 */
	public Postfix() {
		postfixQueue = new ArrayDeque<String>();
		operatorStack = new ArrayDeque<String>();
		operandStack = new ArrayDeque<String>();
		expressionDeque = new ArrayDeque<String>();
	}

	/**
	 * Get the postfix queue
	 * 
	 * @return
	 */
	public Queue<String> getPostFixQueue() {
		return postfixQueue;
	}

	/**
	 * Parses an infix queue into a postfix queue
	 * 
	 * @param infixQueue
	 * @throws PostfixParsingException
	 */
	public void parsePostfix(Queue<String> infixQueue) throws PostfixParsingException {
		// Validates the infix queue before actually parsing
		validateInfixQueue(infixQueue);

		// Loop that deals with converting from infix queue to postfix queue
		while (!infixQueue.isEmpty()) {
			String element = infixQueue.poll();

			if (EvaluatorUtility.isNumeric(element)) {
				postfixQueue.offer(element);
			} else if (EvaluatorUtility.isOperator(element)) {
				String operator;

				do {
					operator = evaluateOperator(element);
				} while (!operator.isEmpty());
			} else if (EvaluatorUtility.isOpeningParenthesis(element)) {
				operatorStack.push(element);
			} else if (EvaluatorUtility.isClosingParenthesis(element)) {
				evaluateClosingParenthesis();
			}
		}

		// Add whatever is left in the operator stack to the postfix queue
		while (!operatorStack.isEmpty()) {
			postfixQueue.offer(operatorStack.pop());
		}
	}

	/**
	 * Validate whether the infix queue represents a correctly syntaxed
	 * mathematical equation
	 * 
	 * @param infixQueue
	 * @throws PostfixParsingException
	 */
	private void validateInfixQueue(Queue<String> infixQueue) throws PostfixParsingException {
		// Check for null or empty
		if (infixQueue.isEmpty() || infixQueue == null) {
			throwPostfixParsingException(EvaluatorUtility.EMPTY_ERR_MSG);
		}

		/// Initialize variables
		// Creating a copy of the infix queue
		Queue<String> infixQueueCopy = new LinkedList<String>(infixQueue);
		String lastElement = "";
		String element;
		int openingCntr = 0;
		int closingCntr = 0;

		String firstElement = infixQueueCopy.peek();
		// Check first element start with proper element
		if ((EvaluatorUtility.isOperator(firstElement) || EvaluatorUtility.isClosingParenthesis(firstElement)))
			throwPostfixParsingException(EvaluatorUtility.STARTING_ERR_MSG);

		// Looping through infix queue, looking for anomalies
		while (!infixQueueCopy.isEmpty()) {
			element = infixQueueCopy.poll();

			if (EvaluatorUtility.isOperator(element)) {
				if (EvaluatorUtility.isOperator(lastElement) || EvaluatorUtility.isOpeningParenthesis(lastElement))
					throwPostfixParsingException(EvaluatorUtility.OPERATOR_ERR_MSG);
			} else if (EvaluatorUtility.isOpeningParenthesis(element)) {
				if (EvaluatorUtility.isNumeric(lastElement) || EvaluatorUtility.isClosingParenthesis(lastElement))
					throwPostfixParsingException(EvaluatorUtility.OPENING_PARENTHESIS_ERR_MSG);

				openingCntr++;
			} else if (EvaluatorUtility.isClosingParenthesis(element)) {
				if (EvaluatorUtility.isOperator(lastElement) || EvaluatorUtility.isOpeningParenthesis(lastElement))
					throwPostfixParsingException(EvaluatorUtility.CLOSING_PARENTHESIS_ERR_MSG);

				closingCntr++;

				if (closingCntr > openingCntr)
					throwPostfixParsingException(EvaluatorUtility.PARENTHESES_ERR_MSG);
			} else if (EvaluatorUtility.isNumeric(element)) {
				if (EvaluatorUtility.isClosingParenthesis(lastElement)) {
					throwPostfixParsingException(EvaluatorUtility.NUMBER_ERR_MSG);
				}
			} else {
				throwPostfixParsingException(EvaluatorUtility.CHARACTER_ERR_MSG);
			}

			lastElement = element;
		}

		// Check that there are the same number of opening and closing
		// parenthesis
		if (openingCntr != closingCntr) {
			throwPostfixParsingException(EvaluatorUtility.PARENTHESES_ERR_MSG);
		}

		// Check that you are ending the equation with a a valid element
		if (EvaluatorUtility.isOperator(lastElement) || EvaluatorUtility.isOpeningParenthesis(lastElement))
			throwPostfixParsingException(EvaluatorUtility.ENDING_ERR_MSG);
	}

	/**
	 * Parses an infix queue by receiving an Infix object.
	 * 
	 * @param infix
	 * @throws PostfixParsingException
	 */
	public void parsePostfix(Infix infix) throws PostfixParsingException {
		// Call to overloaded method passing in the infixqueue taken from infix
		// object
		parsePostfix(infix.getInfixQueue());
	}

	/**
	 * Deals with operators when parsing the infix queue into postfix queue
	 * 
	 * @param operator
	 * @return
	 */
	private String evaluateOperator(String operator) {
		// Push operator into operatorStack if empty
		if (operatorStack.isEmpty()) {
			operatorStack.push(operator);
		} else {
			int precedence = EvaluatorUtility.getOperatorPrecedence(operator);

			String prevOperator = operatorStack.peek();
			int prevPrecedence = EvaluatorUtility.getOperatorPrecedence(prevOperator);

			// Compare precedence of the operator and operator on top of the
			// stack
			if (precedence > prevPrecedence) {
				operatorStack.push(operator);
			} else {
				postfixQueue.offer(operatorStack.pop());
				return operator;
			}
		}

		return "";
	}

	/**
	 * Deals with operators when parsing the infix queue into postfix queue
	 */
	private void evaluateClosingParenthesis() {
		while (!operatorStack.isEmpty()) {
			String tempOperator = operatorStack.pop();
			if (EvaluatorUtility.isOpeningParenthesis(tempOperator)) {
				break;
			}

			postfixQueue.offer(tempOperator);
		}
	}

	/**
	 * Called when actually trying to solve the equation
	 * 
	 * @return
	 * @throws PostfixParsingException
	 */
	public Double solvePostfixExpression() throws PostfixParsingException {
		// Check if null or empty
		if (postfixQueue.isEmpty() || postfixQueue == null)
			throwPostfixParsingException(EvaluatorUtility.SOLVE_ERROR_MSG);

		operatorStack = new ArrayDeque<String>();

		// Loop through postfix queue
		while (!postfixQueue.isEmpty()) {
			String element = postfixQueue.poll();

			if (EvaluatorUtility.isNumeric(element)) {
				operandStack.push(element);
			} else if (EvaluatorUtility.isOperator(element)) {
				// Adding the elements to the expression deque in correct order
				expressionDeque.offer(element);
				expressionDeque.offerLast(operandStack.pop());
				expressionDeque.offerFirst(operandStack.pop());

				// Figuring out solution to the expression deque and putting it
				// back into operand stack
				operandStack.push(evaluateExpression());
			}
		}

		// Choosing not to round right in here, letting the user do as he
		// pleases with the result
		double result = Double.parseDouble(operandStack.pop());

		return result;
	}

	/**
	 * Evaluates the expression deque. There are basically 4 options, either
	 * it's an addition, subtraction, multiplication, division. It performs the
	 * operation and returns the result as a string
	 * 
	 * @return
	 */
	public String evaluateExpression() {
		Double firstOperand = Double.parseDouble(expressionDeque.pollFirst());
		Double lastOperand = Double.parseDouble(expressionDeque.pollLast());
		String operator = expressionDeque.poll();
		Double result = null;

		switch (operator) {
		case EvaluatorUtility.ADDITION:
			result = firstOperand + lastOperand;
			break;
		case EvaluatorUtility.SUBTRACTION:
			result = firstOperand - lastOperand;
			break;
		case EvaluatorUtility.MULTIPLICATION:
			result = firstOperand * lastOperand;
			break;
		case EvaluatorUtility.DIVISION:
			result = firstOperand / lastOperand;
			break;
		}
		return String.valueOf(result);
	}

	/**
	 * Throws exception while also emptying all the fields
	 * @param message
	 * @throws PostfixParsingException
	 */
	private void throwPostfixParsingException(String message) throws PostfixParsingException {
		emptyAll();
		throw new PostfixParsingException(message);
	}

	/**
	 * Clear all the fields
	 */
	private void emptyAll() {
		postfixQueue = new ArrayDeque<String>();
		operatorStack = new ArrayDeque<String>();
		operandStack = new ArrayDeque<String>();
		expressionDeque = new ArrayDeque<String>();
	}
}