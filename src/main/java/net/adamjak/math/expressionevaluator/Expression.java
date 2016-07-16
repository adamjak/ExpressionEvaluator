/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author adamjak
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
