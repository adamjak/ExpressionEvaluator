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
import java.util.List;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;

/**
 *
 * @author Marian Adamjak
 */
public class MathFunction implements Function{

    enum FunctionName {
        ABS, ACOS, ASIN, ATAN, ATAN2,SIN,SINH,COS,COSH,TAN,TANH,EXP,LOG,LOG10,LOGBASE,POWER; 
        
        public Double[] calculate(Double... arguments){
            Double[] results;
            switch (this) {
                case ABS:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("ABS function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.abs(arguments[0]);
                    }
                    return results;
                case ACOS:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("ACOS function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.acos(arguments[0]);
                    }
                    return results;
                case ASIN:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("ASIN function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.asin(arguments[0]);
                    }
                    return results;
                case ATAN:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("ATAN function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.atan(arguments[0]);
                    }
                    return results;
                case SIN:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("SIN function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.sin(arguments[0]);
                    }
                    return results;
                case SINH:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("COS function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.sinh(arguments[0]);
                    }
                    return results;
                case COS:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("COS function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.cos(arguments[0]);
                    }
                    return results;
                case COSH:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("COS function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.cosh(arguments[0]);
                    }
                    return results;
                case TAN:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("TAN function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.tan(arguments[0]);
                    }
                    return results;
                 case TANH:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("TAN function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.tanh(arguments[0]);
                    }
                    return results;   
                 case EXP:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("EXP function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.exp(arguments[0]);
                    }
                    return results;
                case ATAN2:
                    if(arguments == null || arguments.length != 2){
                        throw new IllegalArgumentException("ATAN2 function requires just two parameters");
                    }
                    results = new Double[1];
                    results[0] = Math.atan2(arguments[0], arguments[1]);
                    return results;
                case LOG:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("LOG function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.log(arguments[0]);
                    }
                    return results;
                case LOG10:
                    if(arguments == null || arguments.length < 1){
                        throw new IllegalArgumentException("LOG10 function requires one parameter, at least");
                    }
                    results = new Double[arguments.length];
                    for(int i = 0; i<results.length; i++){
                        results[i] = Math.log10(arguments[0]);
                    }
                    return results;
                case LOGBASE:
                    if(arguments == null || arguments.length != 2){
                        throw new IllegalArgumentException("LOGBASE function requires just two parameters");
                    }
                    results = new Double[1];
                    
                    results[0] = Math.log(arguments[1])/Math.log(arguments[0]);
                    return results;
                case POWER:
                    if(arguments == null || arguments.length != 2){
                        throw new IllegalArgumentException("POWER function requires just two parameters");
                    }
                    results = new Double[1];
                    results[0] = Math.pow(arguments[0], arguments[1]);
                    return results;
                default:
                    throw new UnsupportedOperationException("Unknown Math function: " + this.name());
            }
        }
   
    }
    
    private final FunctionName functionName;

    MathFunction(FunctionName functionName) {
        this.functionName = functionName;
    }
    
    @Override
    public final void init() {
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
                    throw new EvaluateException("Can not parse " + token.getContent() + " into number",token.startPoisition() ,nfe );
                }
            } else {
                throw new EvaluateException("Token is not NUMBER ", token.startPoisition());
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
        if(inputs.length == 0){
            Double[] outputs = this.functionName.calculate((Double[]) null);
            return getResultsFromArray(outputs);
        }
        List<Double> argumets = new ArrayList<>();
        for(ResultSimple input : inputs){
            switch (input.getResultSimpleType()){
                case NUMBER_VALUE:
                    argumets.add(input.getNumberResult().doubleValue());
                    break;
                case TEXT_VALUE:                    
                    try {
                        argumets.add(Double.parseDouble(input.getTextResult()));
                    } catch (NumberFormatException nfe) {
                        throw new EvaluateException("Can not parse " + input.getTextResult() + " into number",input.startPoisition() ,nfe );
                    }
                    break;
                case BOOLEAN_VALUE:
                    throw new EvaluateException(this.functionName.name() + " - bad argument - BOOLEAN", input.startPoisition());
                case EMPTY:
                    throw new EvaluateException(this.functionName.name() + " - bad argument - EMPTY", input.startPoisition());
                default:
                    throw new UnsupportedOperationException(this.functionName.name() + " - Unimplememnted input type " + input.getResultSimpleType().name());
            }
        }
        Double[] outputs = this.functionName.calculate(argumets.toArray(new Double[argumets.size()]));
        return getResultsFromArray(outputs);
    }
    
    private List<ResultSimple> getResultsFromArray(Double... results){
        List<ResultSimple> outputs = new ArrayList<>();
        if(results == null || results.length == 0){
            return outputs;
        }
        for(int i = 0; i<results.length; i++){
            outputs.add(new ResultSimple(results[i], 0, 1));
        }
        return outputs;
    }
}
