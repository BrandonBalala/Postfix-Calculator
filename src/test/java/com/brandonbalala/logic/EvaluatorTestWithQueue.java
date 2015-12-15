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
public class EvaluatorTestWithQueue {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private String[] infixArray;
	private Double expectedResult;
	private String expectedPostfix;
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
	public EvaluatorTestWithQueue(String[] input, Double expectedResult, String expectedPostix) {
		this.infixArray = input;
		this.expectedResult = expectedResult;
		this.expectedPostfix = expectedPostix;
	}

	// "",
	@Parameterized.Parameters
	public static Collection<Object[]> expressionAndExpectedResults() {
		return Arrays.asList(new Object[][] {
				{ new String[] { "1", "+", "255", "/", "3", "+", "(", "1", "*", "2", ")" }, new Double(88), "12553/+12*+" },
				{ new String[] { "(", "360", "-", "2.50", ")", "/", "(", "2", "*", "(", "3", "-", "2", ")", "+", "62",
						")" }, new Double(5.59), "3602.50-232-*62+/" },
				{ new String[] { "(", "2", "*", "3", "/", "1", ")", "+", "2" }, new Double(8), "23*1/2+" },
				{ new String[] { "1", "+", "(", "2.5", "-", "0.75", ")", "/", "70" }, new Double(1.02), "12.50.75-70/+" },
				{ new String[] { "(", "-22", "+", "4", ")", "/", "9" }, new Double(-2), "-224+9/" },
				{ new String[] { "(","(","64","-","7",")","+","(","44","/","4",")",")","/","2" },
						new Double(34), "647-444/+2/" },
				{ new String[] { "3.6", "+", "-0.2", "-", "4", "*", "9" },
						new Double(-32.6), "3.6-0.2+49*-" },
				{ new String[] { "34", "*", "4", "+", "(", "(", "9", "*", "10", ")", "/", "2", ")" }, new Double(181), "344*910*2/+" },
				{ new String[] { "(","-0.4","+","-1.5","+","4",")","*","5" }, new Double(10.5), "-0.4-1.5+4+5*" },
				{ new String[] { "(", "(", "74", "+", "52", ")", "/", "2", ")", "*", "(", "(", "9", "*", "8", ")", "/",
						"4", ")" }, new Double(1134), "7452+2/98*4/*" },

				{ new String[] { "0.2", "*", "0.2", "+", "1.01", "*", "6" }, new Double(6.1), "0.20.2*1.016*+" },
				{ new String[] { "(", "44", "/", "2", ")", "*", "(", "53", "/", "2", ")" }, new Double(583), "442/532/*" },
				{ new String[] { "100", "*", "4", "+", "-72", "+", "63" }, new Double(391), "1004*-72+63+" },
				{ new String[] { "(", "(", "86", "*", "1.63", ")", "/", "2", ")", "*", "54" }, new Double(3784.86), "861.63*2/54*" },
				{ new String[] { "(", "(", "(", "9", "+", "10", ")", ")", ")", "+", "2" }, new Double(21), "910+2+" } });
	}

	@Test
	public void testSolvePostFixExpression() {
		
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
		} catch (Exception e) {
			log.info(e.getMessage());
		}

		Double result = 0.0;
		try {
			result = Math.round(postfix.solvePostfixExpression() * 100.0) / 100.0;
		} catch (PostfixParsingException e) {
			log.info(e.getMessage());
		}
		
		log.info("Expected Result : " + expectedResult);
		log.info("Actual Result : " + result);

		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testCorrectPostfixExpression() {
		
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
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		
		Queue<String> postfixQueue = postfix.getPostFixQueue();
		StringBuilder sb = new StringBuilder();
		while(!postfixQueue.isEmpty()){
			sb.append(postfixQueue.poll());
		}
		
		log.info("Expected Postfix : " + expectedPostfix);
		log.info("Actual Postfix : " + sb.toString());

		assertEquals(expectedPostfix, sb.toString());
	}
}
