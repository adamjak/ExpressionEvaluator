/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.functions;

import java.util.ArrayList;
import java.util.List;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.tokens.Token;

/**
 *
 * @author madamjak
 */
@ExpressionFunction(name ="E")
public class EValue implements Function {

    private static final List<ResultSimple> output = new ArrayList<>(1);
    
    static{
        ResultSimple rs = new ResultSimple(Math.E, 0, 0);
        output.add(rs);
    }
    
    
    @Override
    public void init() {
    }

    @Override
    public List<ResultSimple> getResults(Token... inputs) throws EvaluateException {
        return output;
    }

    @Override
    public List<ResultSimple> getResults(ResultSimple... inputs) throws EvaluateException {
        return output;
    }
    
}
