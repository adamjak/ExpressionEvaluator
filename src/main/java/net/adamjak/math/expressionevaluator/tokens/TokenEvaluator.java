/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.tokens;

import java.util.List;
import net.adamjak.math.expressionevaluator.ExpressionSequence;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.CollectionNotNullAndEmpty;

/**
 *
 * @author madamjak
 */
public abstract class TokenEvaluator {
    
    public abstract TokenEvaluateResult evaluate(List<ExpressionSequence> exprSeq, int tokenPosition) throws EvaluateException;
    
    Token getToken(List<ExpressionSequence> exprSeq, int tokenPosition, TokenType requestedType){
        Utils.validateInput(exprSeq, CollectionNotNullAndEmpty.getInstance(), NullPointerException.class, "exprSeq can not be null or empty");
        if(tokenPosition < 0 || tokenPosition >= exprSeq.size()){
            throw new IndexOutOfBoundsException("TokenPosition is lower then zero or great then size of exprSeq");
        }
        if((exprSeq.get(tokenPosition) instanceof Token) == false){
            throw new IllegalArgumentException("ExpressionSequencs on tokenPosition is not token");
        }
        Token token = (Token) exprSeq.get(tokenPosition);
        if(token.getType() != requestedType){
            throw new IllegalArgumentException("ExpressionSequencs on tokenPosition is not " + requestedType.name());
        }
        return token;
    }
    
}
