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
@ExpressionFunction(name ="EXP")
public class Exp extends MathFunction {

    public Exp() {
        super(MathFunction.FunctionName.EXP);
        init();
    }
    
}
