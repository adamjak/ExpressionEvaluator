/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.utils.validators;

import java.util.Collection;
import net.adamjak.utils.Utils;

/**
 *
 * @author madamjak
 */
public class CollectionNotNullAndEmpty implements Validator<Collection>{

    private static CollectionNotNullAndEmpty instance = null;
    
    public static CollectionNotNullAndEmpty getInstance(){
        if(instance == null){
            instance = new CollectionNotNullAndEmpty();
        }
        return instance;
    }
    
    private CollectionNotNullAndEmpty(){
    }
    
    @Override
    public boolean isValid(Collection inputToValidate) {
        return Utils.isCollectionNotNullAndEmpty(inputToValidate);
    }
    
}
