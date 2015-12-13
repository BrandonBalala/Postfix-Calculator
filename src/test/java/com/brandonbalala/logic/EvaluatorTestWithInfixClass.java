package com.brandonbalala.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

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
	public EvaluatorTestWithInfixClass(String input, Double expected) {
		this.infixString = input;
		this.expectedResult = expected;
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> expressionAndExpectedResults() {
		return Arrays.asList(
				new Object[][] { { "(500*1.7/-5.3)+2-0.75/1.45", new Double(-158.89) }, { "(-96/7)+(0.99*70)/(1.07*2)", new Double(18.67) },
						{ "68+(2+9-18)/1.2*5.3", new Double(37.08) }, { "-99+3+1-2/(-4+0.5)", new Double(-94.43) },
						{ "(100/0.3*0.5+4)*0.75", new Double(128) }, { "30/(50+0.5)*10/-5", new Double(-1.19) }, { "(10-20)*2-3*2", new Double(-26) },
						{ "(4*2+3-2)/(6/8)", new Double(12) }, { "(1+8-5/2)*2+4", new Double(17) }, { "18+2-0.75/(-2*0.5)", new Double(20.75) } });
	}

	@Test
	public void testSolvePostFixExpression() {
		log.info("Parameterized String is : " + infixString);
		try {
			infix.setInfixQueue(infixString);
			postfix.parsePostfix(infix);
		} catch (InfixParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PostfixParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}

		Double result = Math.round(postfix.solvePostfixExpression() * 100.0) / 100.0;
		log.info("Expected Result : " + expectedResult);
		log.info("Actual Result : " + result);

		assertEquals(expectedResult, result);
	}
}
