/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.variables;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.StringNotNullAndEmpty;

/**
 *
 * @author madamjak
 */
public class VariableValueSet {

    private final Map<String, VariableValue> variables;

    public VariableValueSet() {
        this.variables = new HashMap<>();
    }

    public VariableValueSet addTexVariableValue(String name, String textContent){
        Utils.validateInput(name, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Name can not be null or empty");
        VariableValue var = new VariableValue(name, textContent);
        this.variables.put(name, var);
        return this;
    }
    
    public VariableValueSet addNumberVariableValue(String name, Number numberContent){
        Utils.validateInput(name, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Name can not be null or empty");
        VariableValue var = new VariableValue(name, numberContent);
        this.variables.put(name, var);
        return this;
    }
    
    public VariableValueSet addBooleanVariableValue(String name, Boolean booleanContent){
        Utils.validateInput(name, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Name can not be null or empty");
        VariableValue var = new VariableValue(name, booleanContent);
        this.variables.put(name, var);
        return this;
    }
    
    public VariableValueSet removeVariable(String name){
        Utils.validateInput(name, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Name can not be null or empty");
        this.variables.remove(name);
        return this;
    }
    
    public Set<String> getVariableNames(){
        return Collections.unmodifiableSet(this.variables.keySet());
    }
    
    public boolean existVariableValue(String name){
        if(name == null){
            return false;
        }
        return this.variables.containsKey(name);
    }
    
    public VariableValue getVariableValueByName(String name){
        if(existVariableValue(name)){
            return this.variables.get(name);
        }
        return null;
    } 
    
}
