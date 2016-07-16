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
@ExpressionFunction(name ="LOG10")
public class Log10 extends MathFunction {

    public Log10() {
        super(MathFunction.FunctionName.LOG10);
        init();
    }
    
}
