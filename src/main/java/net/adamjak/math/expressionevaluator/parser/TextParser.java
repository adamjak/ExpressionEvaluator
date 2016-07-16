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
public class TextParser extends TokenParser{

    private static final TokenType tokenType = TokenType.TEXT;
    
    public TextParser() {
        super(tokenType);
    }

    @Override
    public ParseOutput parse(int startPosition) {
        if(getInputLine() == null){
            throw new IllegalStateException("InputLine is not set");
        }
        if(startPosition <0 || startPosition > getInputLine().length()-1){
            throw new IllegalArgumentException("Bad position to parse");
        }
        char startDelimiter = getInputLine().charAt(startPosition);
        if(TEXT_DELIMITER_CHARS.contains(startDelimiter)){
            int endPosition = getInputLine().indexOf(startDelimiter, startPosition+1);
            if(endPosition >=0){
                String text = getInputLine().substring(startPosition+1, endPosition);
                Token token = new Token(text, tokenType, startPosition);
                return createSuccessParseOutput(endPosition +1, token);  
            }
        }
        return createErrorParseOutput(startPosition);
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
                return false;
            case VARIABLE_NAME:
                return false;
            case UNKOWN:
                return false;
            default:
                return false;            
        }
    }
    
}
