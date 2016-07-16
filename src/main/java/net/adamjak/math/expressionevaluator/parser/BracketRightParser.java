/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.parser;

import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;


/**
 *
 * @author adamjak
 */
public class BracketRightParser extends TokenParser{

    
    private static final TokenType tokenType = TokenType.BRACKET_RIGHT;
    
    public BracketRightParser() {
        super(tokenType);
    }
    
    @Override
    public ParseOutput parse(int startPosition) {
        return findOneCharToken(startPosition, BRACKET_RIGHT_ALLOW_CHARS);
    }
    
    @Override
    public boolean canParseAfterToken(Token token) {
        if(token == null){
            return true;
        }
        switch(token.getType()){
            case UNARY_OPERATOR:
                return false;
            case BINARY_OPERATOR:
                return false;
            case ARGUMENT_SEPARATOR:
                return true;
            case TEXT:
                return true;
            case BRACKET_LEFT:
                return true;
            case BRACKET_RIGHT:
                return true;
            case SPACE:
                return true;
            case NUMBER:
                return true;
            case FUNCTION_NAME:
                return false;
            case VARIABLE_NAME:
                return true;
            case UNKOWN:
                return false;
            default:
                return false;            
        }
    }
}
