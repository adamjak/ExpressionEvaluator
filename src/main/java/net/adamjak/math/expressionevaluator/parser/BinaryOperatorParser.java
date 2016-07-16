/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.parser;

import net.adamjak.math.expressionevaluator.tokens.BinaryOperatorEvaluator;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;

/**
 *
 * @author adamjak
 */
public class BinaryOperatorParser extends TokenParser {

    private static final TokenType tokenType = TokenType.BINARY_OPERATOR;

    public BinaryOperatorParser() {
        super(tokenType);
    }

    @Override
    public ParseOutput parse(int startPosition) {
        ParseOutput output = findOneCharToken(startPosition, BINARY_OPERATOR_ALLOW_CHARS);
        if(output.getParsedToken() != null){
            int priority = BinaryOperatorEvaluator.Operator.getOperator(output.getParsedToken().getContent()).getPriority();
            output.getParsedToken().setPriority(priority);
        }
        return output;
    }

    @Override
    public boolean canParseAfterToken(Token token) {
        if (token == null) {
            return true;
        }
        switch (token.getType()) {
            case UNARY_OPERATOR:
                return false;
            case BINARY_OPERATOR:
                return false;
            case ARGUMENT_SEPARATOR:
                return false;
            case TEXT:
                return false;
            case BRACKET_LEFT:
                return false;
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
