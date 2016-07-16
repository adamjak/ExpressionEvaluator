/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.functions;

import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import java.util.ArrayList;
import java.util.List;
import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;


/**
 *
 * @author Marian Adamjak
 */
@ExpressionFunction(name ="SUM")
public class Sum implements Function {

    @Override
    public void init() {
    }

    @Override
    public List<ResultSimple> getResults(Token... inputs) throws EvaluateException {
        List<ResultSimple> results = new ArrayList<>();
        if(inputs == null){
            return results;
        }

        ResultSimple[] resultsArray = new ResultSimple[inputs.length];
        for(int i = 0; i < inputs.length; i++){
            Token token = inputs[i];
            if(token.getType() == TokenType.NUMBER){
                try {
                    ResultSimple rs = new ResultSimple(Double.parseDouble(token.getContent()), token.startPoisition(), token.length());
                    resultsArray[i] = rs;
                } catch (NumberFormatException nfe) {
                    throw new EvaluateException("Can not parse " + token.getContent() + "into number", nfe );
                }
            } else {
                throw new EvaluateException("Token is not NUMBER near position: "+token.startPoisition());
            }
        }
        
        return getResults(resultsArray);
    }

    @Override
    public List<ResultSimple> getResults(ResultSimple... inputs) throws EvaluateException {
        List<ResultSimple> results = new ArrayList<>();
        if(inputs == null){
            return results;
        }
        Double sum = 0D;
        for(ResultSimple input : inputs){
            switch (input.getResultSimpleType()){
                case NUMBER_VALUE:
                    sum = sum + input.getNumberResult().doubleValue();
                    break;
                case TEXT_VALUE:
                    try {
                        sum = sum + Double.parseDouble(input.getTextResult());                    
                    } catch (NumberFormatException nfe) {
                        throw new EvaluateException("Can not parse " + input.getTextResult() + "into number", nfe );
                    }
                    break;
                case BOOLEAN_VALUE:
                    // ignore boolean value
                    break;
                default:
                    throw new AssertionError(input.getResultSimpleType().name());
            }
        }
        results.add(new ResultSimple(sum, inputs[0].startPoisition(), inputs[0].length()));
        return results;
    }
    
}
