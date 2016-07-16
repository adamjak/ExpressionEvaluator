/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.results;

import net.adamjak.math.expressionevaluator.ExpressionSequence;

/**
 *
 * @author madamjak
 */
public abstract class Result implements ExpressionSequence {
    private final ResultType resultType;
    private int sourceStartPosition;
    private int sourceLenght;
    
    public Result(ResultType resultType) {
        this.resultType = resultType;
        this.sourceLenght = 0;
        this.sourceStartPosition = 0;
    }

    public ResultType getResultType() {
        return resultType;
    }

    final void setStartPosition(int startPosition){
        this.sourceStartPosition = startPosition;
    }
    
    final void setLength(int length){
        this.sourceLenght = length;
    }
    
    final void setStartAndLength(int startPosition, int endPosition){
        this.sourceStartPosition = startPosition;
        this.sourceLenght = endPosition - startPosition +1;
    }
    
    @Override
    public int startPoisition() {
        return this.sourceStartPosition;
    }

    @Override
    public int length() {
        return this.sourceLenght;
    }
    
}
