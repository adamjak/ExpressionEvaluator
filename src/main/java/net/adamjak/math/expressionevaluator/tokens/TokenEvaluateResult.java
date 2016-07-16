/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.tokens;

import net.adamjak.math.expressionevaluator.results.Result;

/**
 *
 * @author adamjak
 */
public class TokenEvaluateResult {
    
    private final TokenEvaluateResultType evaluateResultType;
    private final Result evaluateResult;
    private final int replaceStartPosition;
    private final int replaceEndPosition;

    public TokenEvaluateResult(TokenEvaluateResultType evaluateResultType, Result evaluateResult, int replaceStartPosition, int replaceEndPosition) {
        this.evaluateResultType = evaluateResultType;
        this.evaluateResult = evaluateResult;
        this.replaceStartPosition = replaceStartPosition;
        this.replaceEndPosition = replaceEndPosition;
    }

    public TokenEvaluateResultType getEvaluateResultType() {
        return evaluateResultType;
    }

    public Result getEvaluateResult() {
        return evaluateResult;
    }

    public int getReplaceStartPosition() {
        return replaceStartPosition;
    }

    public int getReplaceEndPosition() {
        return replaceEndPosition;
    }
    
    
    
    
}
