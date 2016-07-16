/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.run;

import net.adamjak.math.expressionevaluator.BaseExpression;
import net.adamjak.math.expressionevaluator.ExpressionSequence;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.math.expressionevaluator.exceptions.ParseException;
import net.adamjak.math.expressionevaluator.variables.VariableValueSet;

/**
 *
 * @author adamjak
 */
public class TestExpr {

    public static void main(String args[]) {

        String test1 = "1+1";
        String test2 = "1+-1";
        String test3 = "SIN(1;2;3;4)";
        String test4 = "(1+2;2)-(2*3;2-1)";
        String test5 = "-(3-4)*2";
        String test6 = " -SIN( 4)^ 2 + COS (4) ^2";
        String test7 = "TAN(PI()/2)";
        String test8 = "1+ATN2(x;1)+2";
        String test9 = "CONST('pi')";
        String test10 = "POWER(2;-8)";

        BaseExpression be;

        try {
            be = BaseExpression.create(test10);
        } catch (ParseException ex) {
            System.err.println(ex.toString());
            return;
        }
//        List<ExpressionSequence> parseTokens = be.getExprSeq();
//        for(ExpressionSequence es : parseTokens){
//            System.out.println(es.toString());
//        }
//        System.out.println("++++ variables ++++");
//        for(String varName : be.getVariableNames()){
//            System.out.println(varName);
//        }
        VariableValueSet varSet = new VariableValueSet();
        varSet.addNumberVariableValue("x", 2);
        try {
            be.evaluate(varSet);
            for (ExpressionSequence es : be.getExprSeq()) {
                System.out.println(es.toString());
            }
        } catch (EvaluateException ex) {
            System.err.println(ex.toString());
        }
        System.out.println("DONE");
    }
}
