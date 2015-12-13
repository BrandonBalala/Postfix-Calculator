package com.brandonbalala.logic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.exception.PostfixParsingException;
import com.brandonbalala.logic.Postfix;

@RunWith(Parameterized.class)
public class EvaluatorTestException {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private String[] infixArray;
	private Double expectedResult;
	private Postfix postfix;
	private Queue<String> infixQueue;

	@Before
	public void initialize() {
		postfix = new Postfix();
		infixQueue = new ArrayDeque<String>();
	}

	// Each parameter should be placed as an argument here
	// Every time runner triggers, it will pass the arguments
	// from parameters we defined in primeNumbers() method
	public EvaluatorTestException(String[] input, Double expected) {
		this.infixArray = input;
		this.expectedResult = expected;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> expressionAndExpectedResults() {
		return Arrays.asList(
			new Object[][] {
				{ new String[] { "0.2", "HELLO KEN", "0.2", "+", "1.01", "*", "6" }, new Double(6.1) },
				{ new String[] { "(", "44", "/", "/", ")", "*", "(", "53", "/", "2", ")" }, new Double(583) },
				{ new String[] { "100", "*", "4", "(", "-72", ")", "63" }, new Double(391) },
				{ new String[] { ")", "(", "86", "*", "1.63", ")", "/", "2", ")", "*", "54" }, new Double(3784.86) },
				{ new String[] { "(", "(", "(", "9", "+", "10", ")", ")", ")", "+", "2", "/" }, new Double(21) } 
			});
	}

	@Test(expected = PostfixParsingException.class )
	public void testSolvePostFixExpression() throws PostfixParsingException {
		
		String infixString = "";

		for (int cntr = 0; cntr < infixArray.length; cntr++) {
			infixQueue.offer(infixArray[cntr]);
			infixString+=infixArray[cntr];
		}
		log.info("Parameterized String is : " + infixString);

		try {
			postfix.parsePostfix(infixQueue);
		} catch (PostfixParsingException e) {
			log.info(e.getMessage());
			throw new PostfixParsingException();
		}

		Double result = Math.round(postfix.solvePostfixExpression() * 100.0) / 100.0;
		log.info("Expected Result : " + expectedResult);
		log.info("Actual Result : " + result);

		assertEquals(expectedResult, result);
	}
}
