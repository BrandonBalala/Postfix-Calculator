package com.brandonbalala.exception;

public class InfixParsingException extends Exception{
    //Parameterless Constructor
    public InfixParsingException() {
    	super("Invalid expression");
    }

    //Constructor that accepts a message
    public InfixParsingException(String message)
    {
       super(message);
    }
}
