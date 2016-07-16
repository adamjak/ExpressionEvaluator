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
