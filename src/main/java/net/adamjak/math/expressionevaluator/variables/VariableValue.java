/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.variables;

import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.exceptions.VariableContentException;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.ObjectNotNull;
import net.adamjak.utils.validators.StringNotNullAndEmpty;

/**
 *
 * @author madamjak
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
