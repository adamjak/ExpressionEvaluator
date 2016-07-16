/*
 * Copyright (c) 2016, Marian Adamjak
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.adamjak.utils;

import net.adamjak.utils.validators.Validator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Marian Adamjak
 */
public class Utils {
    
    public static List<Character> stringToListChars(String input){
        List<Character> output = new ArrayList<>();
        if(input == null || input.isEmpty()){
            return output;
        }
        for(char c : input.toCharArray()){
            output.add(c);
        }
        return output;
    }
    
    public static int countCharInString(String input, char searchChar){
        if(input == null || input.isEmpty()){
            return 0;
        }
        int count = 0;
        for(char c: input.toCharArray()){
            if(c == searchChar){
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check if there is an intersection of two array
     * @param <T>  tyep of array
     * @param array1 
     * @param array2
     * @return true if arrays have one equal element
     */
    public static <T> boolean isIntersection(T[] array1, T[] array2){
        if(array1.length == 0 || array2.length == 0){
            return false;
        }
        for(T e1 : array1){
            for(T e2 : array2){
                if(e1 == null){
                    if(e2 == null){
                        return true;
                    }
                } else {
                    if(e1.equals(e2)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static <T> boolean isInArray(T[] array, T element){
        if(array == null || array.length == 0){
            return false;
        }
        for(T el : array){
            if(el == null && element == null){
                return true;
            }
            if(el != null && el.equals(element)){
                return true;
            }
        }
        return false;
    }
    
    public static<T> T lastElement(List<T> list){
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.get(list.size()-1);
    }
    
    public static <T> List<T> arrayToList(T[] array){
        if(array == null || array.length == 0){
            return Collections.emptyList();
        }
        List<T> output = new ArrayList<>(array.length);
        output.addAll(Arrays.asList(array));
        return output;
    }
    
    public static boolean isStringNullOrEmpty(CharSequence text){
        return text == null || text.length() == 0;
    }
    
    public static boolean isStringNotNullAndEmpty(CharSequence text){
        return text != null && text.length() != 0;
    }
    
    public static <T> boolean isCollectionNullOrEmpty(Collection<T> collection){
        return collection == null || collection.isEmpty();
    }
    
        public static <T> boolean isCollectionNotNullAndEmpty(Collection<T> collection){
        return collection != null && collection.isEmpty() == false;
    }
    
    public static <T> void validateInput(T input, Validator<T> validator, Throwable raiseIfInvalid) throws Throwable{
        if(validator == null){
            throw new NullPointerException("Validator is null");
        }
        if(raiseIfInvalid == null){
             throw new NullPointerException("raiseIfInvalid is null");
        }
        if(validator.isValid(input) == false){
            throw raiseIfInvalid;
        }
    }
    public static <T> void validateInput(T input, Validator<T> validator, Class<? extends Throwable> raiseIfInvalidClass) throws Throwable  {
        validateInput(input, validator, raiseIfInvalidClass,null);
    }

    public static <T,K extends Throwable> void validateInput(T input, Validator<T> validator, Class<? extends K> raiseIfInvalidClass, String errorMessage) throws K  {
        if(validator == null){
            throw new NullPointerException("Validator is null");
        }
        if(validator.isValid(input)){
            return;
        }
        if(raiseIfInvalidClass == null){
             throw new NullPointerException("raiseIfInvalidClass is null");
        }
        
        K raiseIfInvalid;
        
        try {
            Constructor<K> constructor;
            if(isStringNotNullAndEmpty(errorMessage)){
                constructor = (Constructor<K>) raiseIfInvalidClass.getConstructor(String.class);
                raiseIfInvalid = constructor.newInstance(errorMessage);
            } else {
                constructor = (Constructor<K>) raiseIfInvalidClass.getConstructor();
                raiseIfInvalid = constructor.newInstance();
            }
            throw raiseIfInvalid;
        } catch (NoSuchMethodException |InvocationTargetException |InstantiationException| IllegalAccessException ex) {
            throw new IllegalArgumentException("Can not create throwable instance", ex);
        }
    }
}
