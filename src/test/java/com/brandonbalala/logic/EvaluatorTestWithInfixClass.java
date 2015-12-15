package com.brandonbalala.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.exception.InfixParsingException;
import com.brandonbalala.exception.PostfixParsingException;
import com.brandonbalala.logic.Infix;
import com.brandonbalala.logic.Postfix;

@RunWith(Parameterized.class)
public class EvaluatorTestWithInfixClass {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private String infixString;
	private Double expectedResult;
	private String expectedPostfix;
	private Infix infix;
	private Postfix postfix;

	@Before
	public void initialize() {
		infix = new Infix();
		postfix = new Postfix();
	}

	// Each parameter should be placed as an argument here
	// Every time runner triggers, it will pass the arguments
	// from parameters we defined in primeNumbers() method
	public EvaluatorTestWithInfixClass(String input, Double expectedResult, String expectedPostfix) {
		this.infixString = input;
		this.expectedResult = expectedResult;
		this.expectedPostfix = expectedPostfix;
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> expressionAndExpectedResults() {
		return Arrays.asList(
				new Object[][] { { "(500*1.7/-5.3)+2-0.75/1.45", new Double(-158.89), "5001.7*-5.3/2+0.751.45/-" }, { "(-96/7)+(0.99*70)/(1.07*2)", new Double(18.67), "-967/0.9970*1.072*/+" },
						{ "68+(2+9-18)/1.2*5.3", new Double(37.08), "6829+18-1.2/5.3*+" }, { "-99+3+1-2/(-4+0.5)", new Double(-94.43), "-993+1+2-40.5+/-" },
						{ "(100/0.3*0.5+4)*0.75", new Double(128), "1000.3/0.5*4+0.75*" }, { "30/(50+0.5)*10/-5", new Double(-1.19), "30500.5+/10*-5/" }, { "(10-20)*2-3*2", new Double(-26), "1020-2*32*-" },
						{ "(4*2+3-2)/(6/8)", new Double(12), "42*3+2-68//" }, { "(1+8-5/2)*2+4", new Double(17), "18+52/-2*4+" }, { "18+2-0.75/(-2*0.5)", new Double(20.75), "182+0.75-20.5*/-" } });
	}

	@Test
	public void testSolvePostFixExpression() {
		log.info("Parameterized String is : " + infixString);
		try {
			infix.setInfixQueue(infixString);
			postfix.parsePostfix(infix);
		} catch (InfixParsingException e) {
			log.info(e.getMessage());
		} catch (PostfixParsingException e) {
			log.info(e.getMessage());
		} catch (Exception e){
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
		log.info("Parameterized String is : " + infixString);
		try {
			infix.setInfixQueue(infixString);
			postfix.parsePostfix(infix);
		} catch (InfixParsingException e) {
			log.info(e.getMessage());
		} catch (PostfixParsingException e) {
			log.info(e.getMessage());
		} catch (Exception e){
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
