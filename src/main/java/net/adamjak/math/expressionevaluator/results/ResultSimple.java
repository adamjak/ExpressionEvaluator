/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.results;

/**
 *
 * @author adamjak
 */
public class ResultSimple extends Result{
    
    public enum ResultSimpleType{
        NUMBER_VALUE, TEXT_VALUE, BOOLEAN_VALUE, EMPTY
    }
    
    private final ResultSimpleType resultType;
    private final Number numberResult;
    private final String textResult;
    private final Boolean booleanResult;
    
    
    public ResultSimple(Number numberResult, int sourceStartPosition, int sourceLenght){
        super(ResultType.SIMPLE);
        setStartPosition(sourceStartPosition);
        setLength(sourceLenght);
        this.resultType = ResultSimpleType.NUMBER_VALUE;
        this.numberResult = numberResult;
        this.textResult = null;
        this.booleanResult = null;

        
    }
    
    public ResultSimple(String textResult, int sourceStartPosition, int sourceLenght){
        super(ResultType.SIMPLE);
        setStartPosition(sourceStartPosition);
        setLength(sourceLenght);
        this.resultType = ResultSimpleType.TEXT_VALUE;
        this.numberResult = null;
        this.textResult = textResult;
        this.booleanResult = null;
        
    }
    
    public ResultSimple(Boolean booleanResult, int sourceStartPosition, int sourceLenght){
        super(ResultType.SIMPLE);
        setStartPosition(sourceStartPosition);
        setLength(sourceLenght);
        this.resultType = ResultSimpleType.BOOLEAN_VALUE;
        this.numberResult = null;
        this.textResult = null;
        this.booleanResult = booleanResult;
    }

    public ResultSimple(int sourceStartPosition, int sourceLenght){
        super(ResultType.SIMPLE);
        setStartPosition(sourceStartPosition);
        setLength(sourceLenght);
        this.resultType = ResultSimpleType.EMPTY;
        this.numberResult = null;
        this.textResult = null;
        this.booleanResult = null;
    }
    
    public ResultSimpleType getResultSimpleType() {
        return resultType;
    }

    public Number getNumberResult() {
        return numberResult;
    }

    public String getTextResult() {
        return textResult;
    }

    public Boolean getBooleanResult() {
        return booleanResult;
    }

    @Override
    public String toString() {
        String output;
        switch (resultType) {
            case NUMBER_VALUE:
                output = numberResult.toString();
                break;
            case TEXT_VALUE:
                output = textResult;                        
                break;
            case BOOLEAN_VALUE:
                output = booleanResult.toString();
                break;
            case EMPTY:
                output = "";
                break;
            default:
                throw new AssertionError(resultType.name());            
        }
        return "ResultType=" + resultType + " Value: " + output;
    }
    
}
