/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.tokens;

import net.adamjak.math.expressionevaluator.parser.ArgumentSeparatorParser;
import net.adamjak.math.expressionevaluator.parser.BinaryOperatorParser;
import net.adamjak.math.expressionevaluator.parser.BracketLeftParser;
import net.adamjak.math.expressionevaluator.parser.BracketRightParser;
import net.adamjak.math.expressionevaluator.parser.FunctionNameParser;
import net.adamjak.math.expressionevaluator.parser.NumberParser;
import net.adamjak.math.expressionevaluator.parser.SpaceParser;
import net.adamjak.math.expressionevaluator.parser.TextParser;
import net.adamjak.math.expressionevaluator.parser.TokenParser;
import net.adamjak.math.expressionevaluator.parser.UnaryOperatorParser;
import net.adamjak.math.expressionevaluator.parser.VariableNameParser;


/**
 *
 * @author adamjak
 */
public enum TokenType {
    UNARY_OPERATOR(UnaryOperatorParser.class, true,false),
    BINARY_OPERATOR(BinaryOperatorParser.class, false, false),
    ARGUMENT_SEPARATOR(ArgumentSeparatorParser.class,false,false),
    TEXT(TextParser.class,true,true),
    BRACKET_LEFT(BracketLeftParser.class,true,false),
    BRACKET_RIGHT(BracketRightParser.class,false,true),
    SPACE(SpaceParser.class,true,true),
    NUMBER(NumberParser.class,true,true),
    FUNCTION_NAME(FunctionNameParser.class,true,false),
    VARIABLE_NAME(VariableNameParser.class,true,true),
    UNKOWN(null,false,false);
    
    private TokenParser tokenParser = null;
    private final Class<? extends TokenParser> tokenParserClass;
    private final boolean canBeFirst;
    private final boolean canBeLast;
    
    private TokenType(Class<? extends TokenParser> tokenParserClass, boolean canBeFirst, boolean canBeLast ){
        this.tokenParserClass = tokenParserClass;
        this.canBeFirst = canBeFirst;
        this.canBeLast =canBeLast;
    }

    public boolean canBeFirst() {
        return canBeFirst;
    }

    public boolean canBeLast() {
        return canBeLast;
    }

    
    public TokenParser getTokenParser() {
        if(this.tokenParserClass == null){
            return null;
        }
        if(this.tokenParser == null){
            try {
                this.tokenParser = this.tokenParserClass.newInstance();
            } catch (IllegalAccessException| InstantiationException ex) {
                return null;
            } 
        }
        return tokenParser;
    }

}
