/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;

/**
 *
 * @author madamjak
 */
@ExpressionFunction(name ="CONST")
public class Constants implements Function {

    private static final Map<String,ResultSimple> CONSTANTS = new HashMap<>();
    
    static{
        CONSTANTS.put("PI", new ResultSimple(Math.PI, 0, 0));
        CONSTANTS.put("E", new ResultSimple(Math.E, 0, 0));
        CONSTANTS.put("FI", new ResultSimple((1.0 + Math.sqrt(5))/2, 0, 0));
    }
    
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
            if(token.getType() == TokenType.TEXT){
                 ResultSimple rs = new ResultSimple(token.getContent(), token.startPoisition(), token.length());
                    resultsArray[i] = rs;
            } else {
                throw new EvaluateException("Token is not TEXT - near position: "+token.startPoisition());
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
        for(ResultSimple input : inputs){
            ResultSimple rs;
            if(input.getResultSimpleType() == ResultSimple.ResultSimpleType.TEXT_VALUE){
                String constName = input.getTextResult().toUpperCase();                
                rs = CONSTANTS.get(constName);
                if(rs == null){
                    throw new EvaluateException("CONST - bad argument - Unknown constant name near position: " + input.startPoisition());
                }
            } else {
                throw new EvaluateException("CONST - bad argument - TEXT is expected near position" + input.startPoisition());
            }
            results.add(rs);
        }
        return results;
    }
    
}
