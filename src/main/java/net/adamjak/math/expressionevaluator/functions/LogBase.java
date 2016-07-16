/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.functions;

/**
 * First parameter is base of logarithm, second parameter is argument to logarithm on base
 * @author madamjak
 */
@ExpressionFunction(name ="LOGBASE")
public class LogBase extends MathFunction {

    public LogBase() {
        super(MathFunction.FunctionName.LOGBASE);
        init();
    }
    
}
