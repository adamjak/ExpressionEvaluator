/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.results;

import java.util.ArrayList;
import java.util.List;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.CollectionNotNullAndEmpty;

/**
 *
 * @author madamjak
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
