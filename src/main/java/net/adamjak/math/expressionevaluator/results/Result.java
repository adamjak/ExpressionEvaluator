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
package net.adamjak.math.expressionevaluator.results;

import net.adamjak.math.expressionevaluator.ExpressionSequence;

/**
 *
 * @author Marian Adamjak
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
