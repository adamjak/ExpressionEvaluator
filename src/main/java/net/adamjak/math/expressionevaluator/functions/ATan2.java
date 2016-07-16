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
@ExpressionFunction(name = "ATN2")
public class ATan2 extends MathFunction {

    public ATan2() {
        super(FunctionName.ATAN2);
        init();
    }

}
