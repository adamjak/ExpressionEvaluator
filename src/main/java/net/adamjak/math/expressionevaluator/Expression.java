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
package net.adamjak.math.expressionevaluator;

import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.adamjak.math.expressionevaluator.functions.ExpressionFunction;
import net.adamjak.math.expressionevaluator.functions.Function;
import net.adamjak.math.expressionevaluator.variables.VariableValueSet;
import net.adamjak.utils.ClassFinder;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.StringNotNullAndEmpty;

/**
 *
 * @author Marian Adamjak
 */
public abstract class Expression {

    private static boolean isFunctionMapInitialized = false;
    private final String input;
    private static final Map<String, Function> FUNCTION_MAP = new HashMap<>();

    public static Map<String, Function> getFunctionMap() {
        if (isFunctionMapInitialized == false) {
            initFunctionMap();
            isFunctionMapInitialized = true;
        }
        return FUNCTION_MAP;
    }

    public static Function getFunctionByName(String name) {
        if (name == null) {
            return null;
        }
        if (getFunctionMap().containsKey(name) == false) {
            return null;
        }
        return getFunctionMap().get(name);
    }

    private static void initFunctionMap() {
        ClassFinder clsFinder = new ClassFinder();
        List<Class<?>> classes = clsFinder.findAnnotatedClass("net.adamjak.math.expressionevaluator.functions", true, ExpressionFunction.class);
        for (Class<?> cls : classes) {
            ExpressionFunction annotation = cls.getAnnotation(ExpressionFunction.class);
            if (annotation != null) {
                Function func;
                if(Function.class.isAssignableFrom(cls)){
                    try {
                        func = (Function) cls.newInstance();
                        func.init();
                        FUNCTION_MAP.put(annotation.name(), func);
                    } catch (IllegalAccessException | InstantiationException ex) {
                        // silent ignore
                    }
                }
            }
        }
    }

    public Expression(String input) {
        Utils.validateInput(input, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Input is null or empty");
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public abstract List<ExpressionSequence> evaluate(VariableValueSet variableSet) throws EvaluateException;

    public abstract List<String> getVariableNames();

}
