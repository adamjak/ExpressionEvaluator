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
 * @author madamjak
 */
public class BinaryOperatorEvaluator extends TokenEvaluator {
    
    public enum Operator{

        PLUS('+',1), 
        MINUS('-',1),
        MULTI('*',2),
        DIVIDE('/',2),
        POWER('^',3);
        
        private final Character charOp;
        private final int priority;
        
        private Operator(Character charOp, int priority){
            this.charOp = charOp;
            this.priority = priority;
        }
        
        Character getChar(){
            return this.charOp;
        }
        
        public int getPriority(){
            return this.priority;
        }
        
        Number aplyOperator(Number left, Number right) throws EvaluateException{
           switch(this){
                case PLUS:
                    return left.doubleValue() + right.doubleValue();
                case MINUS:
                    return left.doubleValue() - right.doubleValue();
                case MULTI:
                    return left.doubleValue() * right.doubleValue();
                case DIVIDE:
                    if(right.doubleValue() == 0){
                        throw new EvaluateException("Divide by zero");
                    }
                    return left.doubleValue() / right.doubleValue();
                case POWER:
                    return Math.pow(left.doubleValue(), right.doubleValue());
                default:
                    throw new UnsupportedOperationException("Unimplemented binary operator "+ this.name());
               
           }
        }
        
        public static Operator getOperator(String charOp){
            for(Operator op : Operator.values()){
                if(op.getChar() == charOp.charAt(0)){
                    return op;
                }
            }
            return null;
        }
    }

    private static BinaryOperatorEvaluator instance;
    
    public static BinaryOperatorEvaluator getInstance(){
        if(instance == null){
            instance = new BinaryOperatorEvaluator();
        }
        return instance;
    }
    
    
    public final static List<Character> BINARY_OPERATOR_ALLOW_CHARS;
    static {
        BINARY_OPERATOR_ALLOW_CHARS = new ArrayList<>();
        for(BinaryOperatorEvaluator.Operator op : BinaryOperatorEvaluator.Operator.values()){
            BINARY_OPERATOR_ALLOW_CHARS.add(op.getChar());
        }
    }
    
    private BinaryOperatorEvaluator(){
    }
    
    @Override
    public TokenEvaluateResult evaluate(List<ExpressionSequence> exprSeq, int tokenPosition) throws EvaluateException {
        if(tokenPosition == 0 || tokenPosition >= (exprSeq.size()-1)){
            throw new EvaluateException("Can not evaluate - Bad unary token position");
        }
        Token token = getToken(exprSeq, tokenPosition, TokenType.BINARY_OPERATOR);
        BinaryOperatorEvaluator.Operator op = BinaryOperatorEvaluator.Operator.getOperator(token.getContent());
        if(op == null){
            throw new IllegalStateException("Token contains unknown operator");
        }
        if((exprSeq.get(tokenPosition + 1) instanceof Result) == false){
            throw new EvaluateException("Operator can not be evaluated with incompatible right input - result is expected");
        }
        if((exprSeq.get(tokenPosition - 1) instanceof Result) == false){
            throw new EvaluateException("Operator can not be evaluated with incompatible left input - result is expected");
        }
        Result leftResult = (Result)exprSeq.get(tokenPosition - 1);
        Result rightResult = (Result)exprSeq.get(tokenPosition + 1);
        if(leftResult.getResultType() != rightResult.getResultType()){
            throw new EvaluateException("Operator can not be evaluated with incompatible left and right input - result types are different");
        }
        TokenEvaluateResult evaluateResult = null;
        if(leftResult.getResultType() == ResultType.SIMPLE){
            ResultSimple rsLeft = (ResultSimple) leftResult;
            ResultSimple rsRight = (ResultSimple) rightResult;
            ResultSimple outputResult;
            if(    rsLeft.getResultSimpleType() == ResultSimple.ResultSimpleType.NUMBER_VALUE 
                && rsRight.getResultSimpleType() == ResultSimple.ResultSimpleType.NUMBER_VALUE){
                
                outputResult = new ResultSimple(op.aplyOperator(rsLeft.getNumberResult(), rsRight.getNumberResult()),tokenPosition-1, 3);
                
            } else {
                throw new EvaluateException("Bad input for operator " + op.name() + "left: "+ rsLeft.toString()+ "right: " + rsRight.toString());
            }           
            evaluateResult = new TokenEvaluateResult(TokenEvaluateResultType.EVALUATE_FULL, outputResult , tokenPosition-1, tokenPosition +1);
        } else if (leftResult.getResultType() == ResultType.MULTIPLE){
            ResultMultiple rmLeft = (ResultMultiple) leftResult;
            ResultMultiple rmRight = (ResultMultiple) rightResult;
            if(rmLeft.getResultsList().size() != rmRight.getResultsList().size()){
                throw new EvaluateException("Bad multiple input for operator " + op.name() + " Different count of multiple inputs " + "left: "+ rmLeft.toString()+ "right: " + rmRight.toString());
            }
            for(ResultSimple rs : rmLeft.getResultsList()){
                if(rs.getResultSimpleType() != ResultSimple.ResultSimpleType.NUMBER_VALUE){
                    throw new EvaluateException("Bad multiple left input for operator " + op.name() + " one of input is not number");
                }
            }
            for(ResultSimple rs : rmRight.getResultsList()){
                if(rs.getResultSimpleType() != ResultSimple.ResultSimpleType.NUMBER_VALUE){
                    throw new EvaluateException("Bad multiple right input for operator " + op.name() + " one of input is not number");
                }
            }
            List<ResultSimple> resultList = new ArrayList<>();
            for(int i = 0; i < rmLeft.getResultsList().size(); i++){
                Number result = op.aplyOperator(rmLeft.getResultsList().get(i).getNumberResult(), rmRight.getResultsList().get(i).getNumberResult());
                resultList.add(new ResultSimple(result,tokenPosition-1,3));
            }
            ResultMultiple outputResult = ResultMultiple.createFromResultSimple(resultList);
            evaluateResult = new TokenEvaluateResult(TokenEvaluateResultType.EVALUATE_FULL, outputResult , tokenPosition-1, tokenPosition +1);
        } else {
            throw new UnsupportedOperationException("Unkown result type");
        }
        return evaluateResult;
    }
    
}
