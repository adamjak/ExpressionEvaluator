/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.utils.validators;

import net.adamjak.utils.Utils;

/**
 *
 * @author adamjak
 */
public class StringNotNullAndEmpty implements Validator<String> {

    private static StringNotNullAndEmpty instance = null;
    
    public static StringNotNullAndEmpty getInstance(){
        if(instance == null){
            instance = new StringNotNullAndEmpty();
        }
        return instance;
    }
    
    private StringNotNullAndEmpty(){
    }
    
    @Override
    public boolean isValid(String inputToValidate) {
        return Utils.isStringNotNullAndEmpty(inputToValidate);
    }

}
