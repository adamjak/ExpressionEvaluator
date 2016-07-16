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
public class ObjectNotNull implements Validator<Object>{

    private static ObjectNotNull instance = null;
    
    public static ObjectNotNull getInstance(){
        if(instance == null){
            instance = new ObjectNotNull();
        }
        return instance;
    }
    
    private ObjectNotNull(){
    }
    
    @Override
    public boolean isValid(Object inputToValidate) {
        return inputToValidate != null;
    }
    
}
