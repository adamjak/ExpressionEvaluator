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
public class BracketLeftParser extends TokenParser{
    
    private static final TokenType tokenType = TokenType.BRACKET_LEFT;
    
    public BracketLeftParser() {
        super(tokenType);
    }
    
    @Override
    public ParseOutput parse(int startPosition) {
        return findOneCharToken(startPosition, BRACKET_LEFT_ALLOW_CHARS);
    }
        
    @Override
    public boolean canParseAfterToken(Token token) {
        if(token == null){
            return true;
        }
        switch(token.getType()){
            case UNARY_OPERATOR:
                return true;
            case BINARY_OPERATOR:
                return true;
            case ARGUMENT_SEPARATOR:
                return true;
            case TEXT:
                return false;
            case BRACKET_LEFT:
                return true;
            case BRACKET_RIGHT:
                return false;
            case SPACE:
                return true;
            case NUMBER:
                return false;
            case FUNCTION_NAME:
                return true;
            case VARIABLE_NAME:
                return false;
            case UNKOWN:
                return false;
            default:
                return false;            
        }
    }
}
