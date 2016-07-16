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
public class EvaluateException extends Exception {
    
    private final int position;
    
    public EvaluateException() {
        this.position = -1;
    }

    public EvaluateException(int position){
        this.position = position;
    }
    
    public EvaluateException(String message) {
        super(message);
        this.position = -1;
    }

    public EvaluateException(String message, int position) {
        super(message);
        this.position = position;
    }
    
    public EvaluateException(String message, Throwable cause) {
        super(message, cause);
        this.position = -1;
    }
    
    
    public EvaluateException(String message, int position, Throwable cause) {
        super(message, cause);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "EvaluateException{ message=" + getMessage() + " position=" + position + '}';
    }

    
    
}
