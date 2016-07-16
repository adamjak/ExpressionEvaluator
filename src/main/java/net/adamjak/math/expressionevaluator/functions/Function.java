/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.functions;

import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import java.util.List;
import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.tokens.Token;

/**
 *
 * @author adamjak
 */
public interface Function {

    public void init();
    public List<ResultSimple> getResults(Token... inputs) throws EvaluateException;
    public List<ResultSimple> getResults(ResultSimple... inputs)throws EvaluateException;
    
}
