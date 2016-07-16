/*
 *
 * Copyright (c) 2016, Marian Adamjak
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
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
 * @author Marian Adamjak
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
