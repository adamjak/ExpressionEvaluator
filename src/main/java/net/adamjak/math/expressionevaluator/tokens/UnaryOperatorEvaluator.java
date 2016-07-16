/*
 * Copyright (c) 2016, Marian Adamjak
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.

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
 */
package net.adamjak.math.expressionevaluator.tokens;

import java.util.ArrayList;
import java.util.List;
import net.adamjak.math.expressionevaluator.ExpressionSequence;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.math.expressionevaluator.results.Result;
import net.adamjak.math.expressionevaluator.results.ResultMultiple;
import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.results.ResultType;

/**
 *
 * @author Marian Adamjak
 */
public class UnaryOperatorEvaluator extends TokenEvaluator{
    
    private enum Operator{

        PLUS('+'), 
        MINUS('-');
        private Character charOp;
        
        private Operator(Character charOp){
            this.charOp = charOp;
        }
        
        Character getChar(){
            return this.charOp;
        }
        
        public Number aplyOperator(Number input){
            if(this == MINUS){
                return -1 * (Double) input;
            } else if(this == PLUS){
                return input;
            }
            throw new UnsupportedOperationException("Uniplemented unary operator "+this.name());
        }
        
        static Operator getOperator(String charOp){
            for(Operator op : Operator.values()){
                if(op.getChar() == charOp.charAt(0)){
                    return op;
                }
            }
            return null;
        }
    }
    
    private static UnaryOperatorEvaluator instance;
    
    public static UnaryOperatorEvaluator getInstance(){
        if(instance == null){
            instance = new UnaryOperatorEvaluator();
        }
        return instance;
    }
    
    public final static List<Character> UNARY_OPERATOR_ALLOW_CHARS;
    static {
        UNARY_OPERATOR_ALLOW_CHARS = new ArrayList<>();
        for(Operator op : Operator.values()){
            UNARY_OPERATOR_ALLOW_CHARS.add(op.getChar());
        }
    }
    
    private UnaryOperatorEvaluator(){
    }
    
    @Override
    public TokenEvaluateResult evaluate(List<ExpressionSequence> exprSeq, int tokenPosition) throws EvaluateException {
        if(tokenPosition >= (exprSeq.size()-1)){
            throw new EvaluateException("Can not evaluate - Bad unary token position");
        }
        Token token = getToken(exprSeq, tokenPosition, TokenType.UNARY_OPERATOR);
        Operator op = Operator.getOperator(token.getContent());
        if(op == null){
            throw new IllegalStateException("Token contains unknown operator");
        }
        if((exprSeq.get(tokenPosition + 1) instanceof Result) == false){
            throw new EvaluateException("Operator can not be evaluated with incompatible input - result is expected");
        }
        Result result = (Result)exprSeq.get(tokenPosition + 1);
        TokenEvaluateResult evaluateResult;
        if(result.getResultType() == ResultType.SIMPLE){
            ResultSimple rs = (ResultSimple) result;
            TokenEvaluateResultType resType;
            ResultSimple outputResult;
            if(rs.getResultSimpleType() == ResultSimple.ResultSimpleType.NUMBER_VALUE){
                outputResult = new ResultSimple(op.aplyOperator(rs.getNumberResult()),tokenPosition, 2);
                resType = TokenEvaluateResultType.EVALUATE_FULL;
            } else {
                outputResult = rs;
                resType = TokenEvaluateResultType.NO_EVALUATE;
            }
           
            evaluateResult = new TokenEvaluateResult(resType, outputResult , tokenPosition, tokenPosition +1);
        } else if (result.getResultType() == ResultType.MULTIPLE){
            ResultMultiple rm = (ResultMultiple) result;
            List<ResultSimple> outputList = new ArrayList<>();
            int countAply = 0;
            for(ResultSimple rs : rm.getResultsList()){
                if(rs.getResultSimpleType() == ResultSimple.ResultSimpleType.NUMBER_VALUE){
                    outputList.add(new ResultSimple(op.aplyOperator(rs.getNumberResult()),tokenPosition, 2));
                    countAply++;
                } else {
                    outputList.add(rs);
                }
            }
            ResultMultiple outputResult = ResultMultiple.createFromResultSimple(outputList);
            TokenEvaluateResultType resType;
            if(countAply == 0){
                resType = TokenEvaluateResultType.NO_EVALUATE;
            } else if(countAply < rm.getResultsList().size()){
                resType = TokenEvaluateResultType.EVALUATE_PARTIALY;
            } else {
                resType = TokenEvaluateResultType.EVALUATE_FULL;
            }
            evaluateResult = new TokenEvaluateResult(resType, outputResult , tokenPosition, tokenPosition +1);
        } else {
            throw new IllegalStateException("Unkown result type");
        }
        return evaluateResult;
    }
    
    
    
}
