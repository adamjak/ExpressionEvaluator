/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.parser;

import java.util.List;
import net.adamjak.math.expressionevaluator.tokens.ArgumentSeparatorEvaluator;
import net.adamjak.math.expressionevaluator.tokens.BinaryOperatorEvaluator;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;
import net.adamjak.math.expressionevaluator.tokens.UnaryOperatorEvaluator;
import net.adamjak.utils.Utils;

/**
 *
 * @author adamjak
 */
public abstract class TokenParser {
    
    public enum ParseResultType{
        OK, ERROR
    }
    final static String NUMBER_REGEX_PATTERN = "[0-9]{0,}\\.{0,1}[0-9]{1,}([e|E][+|-][0-9]{1,}){0,}";
    final static String FUNCTION_NAME_REGEX_PATERN="[A-Z]{1,}[A-Z0-9_]{0,}";
    final static String VARIABLE_NAME_REGEX_PATERN="[a-z]{1,}[a-z0-9_]{0,}";
    final static List<Character> SPACE_ALLOW_CHARS = Utils.stringToListChars(" ");
    final static List<Character> VARIABLE_NAME_ALLOW_CHARS = Utils.stringToListChars("abcdefghijklmnopqrstuvwxyz");
    final static List<Character> UNARY_OPERATOR_ALLOW_CHARS = UnaryOperatorEvaluator.UNARY_OPERATOR_ALLOW_CHARS;
    final static List<Character> BINARY_OPERATOR_ALLOW_CHARS = BinaryOperatorEvaluator.BINARY_OPERATOR_ALLOW_CHARS;
    final static List<Character> ARGUMENT_SEPARATOR_ALLOW_CHARS = ArgumentSeparatorEvaluator.ARGUMENT_SEPARATOR_ALLOW_CHARS;
    final static List<Character> BRACKET_LEFT_ALLOW_CHARS = Utils.stringToListChars("([{");
    final static List<Character> BRACKET_RIGHT_ALLOW_CHARS = Utils.stringToListChars(")]}");
    final static List<Character> TEXT_DELIMITER_CHARS = Utils.stringToListChars("'\"");
    
    
    private String inputLine;
    private final TokenType parsedTokenType;

    TokenParser(TokenType tokenType) {
        this.inputLine = null;
        this.parsedTokenType = tokenType;
    }

    public String getInputLine() {
        return inputLine;
    }

    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    public TokenType getParsedTokenType() {
        return parsedTokenType;
    }

    /**
     * Parse input string. If string can be parsed, then new parsed token must add into parsedTokens list and return new startPosition.
     * If string can not be parsed, then return new startedPosion without changes.
     * @param startPosition - position to start parse
     * @return new start position
     */
    public abstract ParseOutput parse(int startPosition);
    
    public abstract boolean canParseAfterToken(Token token);
    
    ParseOutput createSuccessParseOutput(int nextParsePosition, Token parsedToken){
        return new ParseOutput(nextParsePosition, parsedToken,ParseResultType.OK);
    }
    
    ParseOutput createErrorParseOutput(int nextParsePosition){
        return new ParseOutput(nextParsePosition, null,ParseResultType.ERROR);
    }
    
    ParseOutput findMultiCharToken(int startPosition, List<Character> allowCharacters){
        if(getInputLine() == null){
            throw new IllegalStateException("InputLine is not set");
        }
        if(startPosition <0 || startPosition > getInputLine().length()-1){
            throw new IllegalArgumentException("Bad position to parse");
        }
        if(allowCharacters == null || allowCharacters.isEmpty()){
            throw new IllegalArgumentException("AllowCharacters is null or empty");
        }
        int position = startPosition;
        while(position < getInputLine().length()){
            if(allowCharacters.contains(getInputLine().charAt(position)) == false ){
                if(position == startPosition){
                    return createErrorParseOutput(startPosition);
                }
                Token token = new Token(getInputLine().substring(startPosition, position), getParsedTokenType(), startPosition);
                return createSuccessParseOutput(position, token);
            } else if(position + 1 == getInputLine().length()){
                Token token = new Token(getInputLine().substring(startPosition, position+1), getParsedTokenType(), startPosition);
                return createSuccessParseOutput(position+1, token);
            }
            position = position +1;
        }
        return createErrorParseOutput(startPosition);
    }
    
    ParseOutput findOneCharToken(int startPosition, List<Character> allowCharacters){
        if(getInputLine() == null){
            throw new IllegalStateException("InputLine is not set");
        }
        if (startPosition < 0 || startPosition > getInputLine().length() - 1) {
            throw new IllegalArgumentException("Bad position to parse");
        }
        if(allowCharacters == null || allowCharacters.isEmpty()){
            throw new IllegalArgumentException("AllowCharacters is null or empty");
        }
        if(allowCharacters.contains(getInputLine().charAt(startPosition))){
                Token token = new Token(getInputLine().substring(startPosition, startPosition+1), getParsedTokenType(), startPosition);                                
                return createSuccessParseOutput(startPosition+1, token);  
        }

        return createErrorParseOutput(startPosition);
    }
    
    public class ParseOutput{
        
        private final ParseResultType parseResultType;
        private final int nextParsePosition;
        private final Token parsedToken;

        public ParseOutput(int nextParsePosition, Token parsedToken, ParseResultType parseResultType) {
            this.nextParsePosition = nextParsePosition;
            this.parsedToken = parsedToken;
            this.parseResultType = parseResultType;
        }

        public int getNextParsePosition() {
            return nextParsePosition;
        }

        public Token getParsedToken() {
            return parsedToken;
        }

        public ParseResultType getParseResultType() {
            return parseResultType;
        }
         
    }
}
