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

import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.exceptions.VariableContentException;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.ObjectNotNull;
import net.adamjak.utils.validators.StringNotNullAndEmpty;

/**
 *
 * @author Marian Adamjak
 */
public class VariableValue {
    
    private final String name;
    private final String textContent;
    private final Number numberContent;
    private final Boolean booleanContent;
    private final VariableValueType type;

    public VariableValue(String name, String textContent) {
        Utils.validateInput(name, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Name can not be null or empty");
        Utils.validateInput(textContent,ObjectNotNull.getInstance() , NullPointerException.class, "TextContent can not be null");
        this.name = name;
        this.textContent = textContent;
        this.numberContent = null;
        this.booleanContent = null;
        this.type = VariableValueType.TEXT;
    }
    
    public VariableValue(String name, Number numberContent) {
        Utils.validateInput(name, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Name can not be null or empty");
        Utils.validateInput(numberContent,ObjectNotNull.getInstance() , NullPointerException.class, "NumberContent can not be null");
        this.name = name;
        this.textContent = null;
        this.numberContent = numberContent;
        this.booleanContent = null;
        this.type = VariableValueType.NUMBER;
    }
    
    public VariableValue(String name, Boolean booleanContent) {
        Utils.validateInput(name, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Name can not be null or empty");
        Utils.validateInput(booleanContent,ObjectNotNull.getInstance() , NullPointerException.class, "BooleanContent can not be null");
        this.name = name;
        this.textContent = null;
        this.numberContent = null;
        this.booleanContent = booleanContent;
        this.type = VariableValueType.BOOLEAN;
    }

    public String getName() {
        return name;
    }

    public String getTextContent() throws VariableContentException {
        if(this.type != VariableValueType.TEXT){
            throw new VariableContentException("Content is not text");
        }
        return textContent;
    }

    public Number getNumberContent() throws VariableContentException {
        if(this.type != VariableValueType.NUMBER){
            throw new VariableContentException("Content is not number");
        }
        return numberContent;
    }

    public Boolean getBooleanContent() throws VariableContentException {
        if(this.type != VariableValueType.BOOLEAN){
            throw new VariableContentException("Content is not boolean");
        }
        return booleanContent;
    }
    
    public VariableValueType getType() {
        return type;
    }
    
    public ResultSimple getResult(int sourceStartPosition, int sourceLenght){
        switch (this.type){
            case NUMBER:
                return new ResultSimple(numberContent, sourceStartPosition, sourceLenght);
            case TEXT:
                return new ResultSimple(textContent, sourceStartPosition, sourceLenght);
            case BOOLEAN:
                return new ResultSimple(booleanContent, sourceStartPosition, sourceLenght);
            default:
                throw new IllegalStateException("Content is not implemented: " + this.type.name());            
        }            
    }
}
