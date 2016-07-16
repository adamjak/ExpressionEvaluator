/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.tokens;

import java.util.List;
import net.adamjak.math.expressionevaluator.ExpressionSequence;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.math.expressionevaluator.results.Result;
import net.adamjak.math.expressionevaluator.results.ResultMultiple;
import net.adamjak.utils.Utils;

/**
 *
 * @author madamjak
 */
public class ArgumentSeparatorEvaluator extends TokenEvaluator {
    
    public final static List<Character> ARGUMENT_SEPARATOR_ALLOW_CHARS = Utils.stringToListChars(";");

    private static ArgumentSeparatorEvaluator instance;
    
    public static ArgumentSeparatorEvaluator getInstance(){
        if(instance == null){
            instance = new ArgumentSeparatorEvaluator();
        }
        return instance;
    }
    
    private ArgumentSeparatorEvaluator() {
    }

    
    
    @Override
    public TokenEvaluateResult evaluate(List<ExpressionSequence> exprSeq, int tokenPosition) throws EvaluateException {
        if(tokenPosition >= (exprSeq.size()-1)){
            throw new EvaluateException("Can not evaluate - Bad unary token position");
        }
        //check token validity
        getToken(exprSeq, tokenPosition, TokenType.ARGUMENT_SEPARATOR);
        if((exprSeq.get(tokenPosition + 1) instanceof Result) == false){
            throw new EvaluateException("Operator can not be evaluated with incompatible right input - result is expected");
        }
        if((exprSeq.get(tokenPosition - 1) instanceof Result) == false){
            throw new EvaluateException("Operator can not be evaluated with incompatible left input - result is expected");
        }
        Result leftResult = (Result) exprSeq.get(tokenPosition - 1);
        Result rightResult = (Result) exprSeq.get(tokenPosition + 1);
        Result output = new ResultMultiple(leftResult,rightResult);
        return new TokenEvaluateResult(TokenEvaluateResultType.EVALUATE_FULL, output, tokenPosition-1, tokenPosition+1);
    }
    
    
}
