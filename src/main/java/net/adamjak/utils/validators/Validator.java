/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.utils.validators;

/**
 *
 * @author adamjak2726607
 */
public interface Validator<T> {
    boolean isValid(T inputToValidate);
}
