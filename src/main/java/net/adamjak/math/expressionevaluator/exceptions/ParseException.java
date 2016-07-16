/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.exceptions;

/**
 *
 * @author adamjak
 */
public class ParseException extends Exception {
    
    private final Integer position;
    
    public ParseException() {
        this.position = null;
    }

    public ParseException(int position){
        this.position = position;
    }
    
    public ParseException(String message) {
        super(message);
        this.position = null;
    }

    public ParseException(String message, int position) {
        super(message);
        this.position = position;
    }
    
    public ParseException(String message, Throwable cause) {
        super(message, cause);
        this.position = null;
    }
 
    public ParseException(String message, int position, Throwable cause) {
        super(message, cause);
        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "ParseException{ message=" + getMessage() + "; position=" + getPosition() + '}';
    }
    
}
