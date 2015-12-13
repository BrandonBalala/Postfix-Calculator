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
	 * 
	 * @param infix
	 */
	public Postfix() {
		postfixQueue = new ArrayDeque<String>();
		operatorStack = new ArrayDeque<String>();
		operandStack = new ArrayDeque<String>();
		expressionDeque = new ArrayDeque<String>();
	}

	public void setPostFixQueue(Queue<String> infixQueue) throws PostfixParsingException {
		postfixQueue = parsePostfix(infixQueue);
	}

	public Queue<String> getPostFixQueue() {
		return postfixQueue;
	}

	public Queue<String> parsePostfix(Queue<String> infixQueue) throws PostfixParsingException {
		validateInfixQueue(infixQueue);

		// operatorStack = new ArrayDeque<String>();

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

		while (!operatorStack.isEmpty()) {
			postfixQueue.offer(operatorStack.pop());
		}

		return postfixQueue;
	}

	private void validateInfixQueue(Queue<String> infixQueue) throws PostfixParsingException {
		Queue<String> infixQueueCopy = new LinkedList<String>(infixQueue);
		String lastElement = "";
		String element;
		int openingCntr = 0;
		int closingCntr = 0;

		String firstElement = infixQueueCopy.peek();
		if ((EvaluatorUtility.isOperator(firstElement) || EvaluatorUtility.isClosingParenthesis(firstElement)))
			throwPostfixParsingException(EvaluatorUtility.STARTING_ERR_MSG);

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

		if (openingCntr != closingCntr) {
			throwPostfixParsingException(EvaluatorUtility.PARENTHESES_ERR_MSG);
		}

		if (EvaluatorUtility.isOperator(lastElement) || EvaluatorUtility.isOpeningParenthesis(lastElement))
			throwPostfixParsingException(EvaluatorUtility.ENDING_ERR_MSG);
	}
	
	public void parsePostfix(Infix infix) throws PostfixParsingException{
		parsePostfix(infix.getInfixQueue());
	}

	private String evaluateOperator(String operator) {
		if (operatorStack.isEmpty()) {
			operatorStack.push(operator);
		} else {
			int precedence = EvaluatorUtility.getOperatorPrecedence(operator);

			String prevOperator = operatorStack.peek();
			int prevPrecedence = EvaluatorUtility.getOperatorPrecedence(prevOperator);

			if (precedence > prevPrecedence) {
				operatorStack.push(operator);
			} else {
				postfixQueue.offer(operatorStack.pop());
				return operator;
			}
		}

		return "";
	}

	private void evaluateClosingParenthesis() {
		while (!operatorStack.isEmpty()) {
			String tempOperator = operatorStack.pop();
			if (EvaluatorUtility.isOpeningParenthesis(tempOperator)) {
				break;
			}

			postfixQueue.offer(tempOperator);
		}
	}

	// TODO
	public Double solvePostfixExpression() {
		operatorStack = new ArrayDeque<String>();
		int cntr = 0;
		while (!postfixQueue.isEmpty()) {
			String element = postfixQueue.poll();

			if (EvaluatorUtility.isNumeric(element)) {
				operandStack.push(element);
			} else if (EvaluatorUtility.isOperator(element)) {
				expressionDeque.offer(element);
				expressionDeque.offerLast(operandStack.pop());
				expressionDeque.offerFirst(operandStack.pop());

				operandStack.push(evaluateExpression());
			}
		}

		double result = Double.parseDouble(operandStack.pop());

		return result;
	}

	public String evaluateExpression() {
		Double firstOperand = Double.parseDouble(expressionDeque.pollFirst());
		Double lastOperand = Double.parseDouble(expressionDeque.pollLast());
		String operator = expressionDeque.poll();
		Double result = null;

		switch (operator) {
		case EvaluatorUtility.ADDITION:
			result = firstOperand + lastOperand;
			break;
		case EvaluatorUtility.SUBSTRACTION:
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

	private void throwPostfixParsingException(String message) throws PostfixParsingException {
		emptyAll();
		throw new PostfixParsingException(message);
	}

	private void emptyAll() {
		postfixQueue = new ArrayDeque<String>();
		operatorStack = new ArrayDeque<String>();
		operandStack = new ArrayDeque<String>();
		expressionDeque = new ArrayDeque<String>();
	}
}