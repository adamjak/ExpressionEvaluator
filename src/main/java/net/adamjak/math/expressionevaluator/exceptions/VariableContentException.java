/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.exceptions;

/**
 *
 * @author madamjak
 */
public class VariableContentException extends Exception {

    public VariableContentException() {
    }

    public VariableContentException(String message) {
        super(message);
    }

    public VariableContentException(String message, Throwable cause) {
        super(message, cause);
    }

}
