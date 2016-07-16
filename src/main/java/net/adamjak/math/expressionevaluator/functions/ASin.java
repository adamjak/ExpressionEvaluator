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
@ExpressionFunction(name ="ASIN")
public class ASin extends MathFunction {

    public ASin() {
        super(MathFunction.FunctionName.ASIN);
        init();
    }
    
}
