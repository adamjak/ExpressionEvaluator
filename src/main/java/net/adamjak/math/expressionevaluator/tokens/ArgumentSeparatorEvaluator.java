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
