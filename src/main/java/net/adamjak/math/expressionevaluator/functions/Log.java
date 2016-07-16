/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.functions;

/**
 *
 * @author madamjak
 */
@ExpressionFunction(name ="LOG")
public class Log extends MathFunction {

    public Log() {
        super(MathFunction.FunctionName.LOG);
        init();
    }
    
}
