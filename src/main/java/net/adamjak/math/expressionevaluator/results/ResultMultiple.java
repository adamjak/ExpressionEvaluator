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

import java.util.ArrayList;
import java.util.List;
import net.adamjak.utils.Utils;

/**
 *
 * @author Marian Adamjak
 */
public class ResultMultiple extends Result {
    
    private final ResultSimple[] results;
    
    public static ResultMultiple createFromResultSimple(ResultSimple... results){
        if(results == null || results.length == 0){
            throw new IllegalArgumentException("There is no input result");
        }
        Result[] resultArray = new Result[results.length];
        for(int i = 0; i<results.length; i++){
            resultArray[i] = results[i];
        }
        return new ResultMultiple(resultArray);
    }
    
    public static ResultMultiple createFromResultSimple(List<ResultSimple> results){
        if(results == null || results.isEmpty()){
            throw new IllegalArgumentException("There is no input result");
        }
        return new ResultMultiple(results.toArray(new Result[results.size()]));
        
    }
    
    
    public ResultMultiple(Result... results) {
        super(ResultType.MULTIPLE);
        if(results == null || results.length == 0){
            throw new IllegalArgumentException("There is no input result");
        }
        this.results = createResultSimpleArray(results);
        setStartPositionAndLength();
    }
    
    public ResultMultiple(List<Result> results) {
        super(ResultType.MULTIPLE);
        if(results == null || results.isEmpty()){
            throw new IllegalArgumentException("There is no input result");
        }
        this.results = createResultSimpleArray(results.toArray(new Result[results.size()]));
        setStartPositionAndLength();
    }
    
    private ResultSimple[] createResultSimpleArray(Result... results){
        List<ResultSimple> resSimpleList = new ArrayList<>();
        for(Result result : results){
            if(result instanceof ResultSimple){
                resSimpleList.add((ResultSimple) result);
            } else if(result instanceof ResultMultiple){
                ResultMultiple rm = (ResultMultiple) result;
                resSimpleList.addAll(rm.getResultsList());
            } else {
                throw new IllegalArgumentException("Unknown Result subclass");
            }
        }
        return resSimpleList.toArray(new ResultSimple[resSimpleList.size()]);
    }
    
    private void setStartPositionAndLength(){
        int startPos = results[0].startPoisition();
        int endPos = results[0].startPoisition() + results[0].length() -1;
        for(ResultSimple rs : results){
            if(rs.startPoisition() < startPos){
                startPos = rs.startPoisition();
            }
            int endPosResult = rs.startPoisition() + rs.length() -1;
            if(endPosResult > endPos){
                endPos = endPosResult;
            }
        }
        setStartAndLength(startPos, endPos);
    }
    
    public ResultSimple[] getResults() {
        return results;
    }
    
    public List<ResultSimple> getResultsList(){
        return Utils.arrayToList(results);
    }

    @Override
    public String toString() {
        String output = "ResultMultiple {" + System.lineSeparator();
        for(ResultSimple rs : this.results){
            output = output + rs.toString() + System.lineSeparator();
        }        
        output = output  + '}';
        return output;
    }
    
    
    
}
