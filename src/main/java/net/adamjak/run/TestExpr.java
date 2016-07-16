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
package net.adamjak.run;

import net.adamjak.math.expressionevaluator.BaseExpression;
import net.adamjak.math.expressionevaluator.ExpressionSequence;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.math.expressionevaluator.exceptions.ParseException;
import net.adamjak.math.expressionevaluator.variables.VariableValueSet;

/**
 *
 * @author Marian Adamjak
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
