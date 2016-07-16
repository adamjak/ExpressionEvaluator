/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;


/**
 *
 * @author adamjak
 */
public class NumberParser extends TokenParser{
    
    private static final TokenType tokenType = TokenType.NUMBER;
    private static final Pattern numberPattern = Pattern.compile(NUMBER_REGEX_PATTERN);
    
    
    public NumberParser() {
        super(tokenType);
    }
    
    @Override
    public ParseOutput parse(int startPosition) {
        Matcher matcher = numberPattern.matcher(getInputLine().subSequence(startPosition, getInputLine().length()));
        if(matcher.find()){
            if(matcher.start()==0){
                int position= startPosition+matcher.end();
                Token token = new Token(getInputLine().substring(startPosition, position), getParsedTokenType(), startPosition);
                return createSuccessParseOutput(position, token);
            }
        }
         return createErrorParseOutput(startPosition);
        //return findMultiCharToken(startPosition, NUMBER_ALLOW_CHARS);
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
