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
@ExpressionFunction(name ="STRNUM")
public class StringToNumber implements Function{

    @Override
    public List<ResultSimple> getResults(Token... inputs)throws EvaluateException {
        List<ResultSimple> results = new ArrayList<>();
        if(inputs == null){
            return results;
        }
        for(Token token : inputs){
            if(token.getType() == TokenType.NUMBER){
                try {
                    results.add(new ResultSimple(Double.parseDouble(token.getContent()),token.startPoisition(), token.length()));
                } catch (NumberFormatException nfe) {
                    throw new EvaluateException("Can not parse " + token.getContent() + "into number", nfe );
                }
            }
        }
        return results;
    }

    @Override
    public List<ResultSimple> getResults(ResultSimple... inputs) throws EvaluateException {
        List<ResultSimple> results = new ArrayList<>();
        if(inputs == null){
            return results;
        }
        for(ResultSimple input : inputs){
            switch (input.getResultSimpleType()){
                case NUMBER_VALUE:
                    results.add(new ResultSimple(input.getNumberResult(),input.startPoisition(), input.length()));
                    break;
                case TEXT_VALUE:
                    results.add(new ResultSimple(input.getTextResult(),input.startPoisition(), input.length()));
                    break;
                case BOOLEAN_VALUE:
                    results.add(new ResultSimple(input.getBooleanResult(),input.startPoisition(), input.length()));
                    break;
                default:
                    throw new AssertionError(input.getResultSimpleType().name());
            }
        }
        return results;
    }

    @Override
    public void init() {
    }
    
}
