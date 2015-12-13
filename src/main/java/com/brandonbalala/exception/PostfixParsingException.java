package com.brandonbalala.exception;

public class PostfixParsingException extends Exception{
    //Parameterless Constructor
    public PostfixParsingException() {
    	super("Invalid expression");
    }

    //Constructor that accepts a message
    public PostfixParsingException(String message)
    {
       super(message);
    }
}
