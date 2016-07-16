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
package net.adamjak.math.expressionevaluator.tokens;

import net.adamjak.math.expressionevaluator.parser.ArgumentSeparatorParser;
import net.adamjak.math.expressionevaluator.parser.BinaryOperatorParser;
import net.adamjak.math.expressionevaluator.parser.BracketLeftParser;
import net.adamjak.math.expressionevaluator.parser.BracketRightParser;
import net.adamjak.math.expressionevaluator.parser.FunctionNameParser;
import net.adamjak.math.expressionevaluator.parser.NumberParser;
import net.adamjak.math.expressionevaluator.parser.SpaceParser;
import net.adamjak.math.expressionevaluator.parser.TextParser;
import net.adamjak.math.expressionevaluator.parser.TokenParser;
import net.adamjak.math.expressionevaluator.parser.UnaryOperatorParser;
import net.adamjak.math.expressionevaluator.parser.VariableNameParser;


/**
 *
 * @author Marian Adamjak
 */
public enum TokenType {
    UNARY_OPERATOR(UnaryOperatorParser.class, true,false),
    BINARY_OPERATOR(BinaryOperatorParser.class, false, false),
    ARGUMENT_SEPARATOR(ArgumentSeparatorParser.class,false,false),
    TEXT(TextParser.class,true,true),
    BRACKET_LEFT(BracketLeftParser.class,true,false),
    BRACKET_RIGHT(BracketRightParser.class,false,true),
    SPACE(SpaceParser.class,true,true),
    NUMBER(NumberParser.class,true,true),
    FUNCTION_NAME(FunctionNameParser.class,true,false),
    VARIABLE_NAME(VariableNameParser.class,true,true),
    UNKOWN(null,false,false);
    
    private TokenParser tokenParser = null;
    private final Class<? extends TokenParser> tokenParserClass;
    private final boolean canBeFirst;
    private final boolean canBeLast;
    
    private TokenType(Class<? extends TokenParser> tokenParserClass, boolean canBeFirst, boolean canBeLast ){
        this.tokenParserClass = tokenParserClass;
        this.canBeFirst = canBeFirst;
        this.canBeLast =canBeLast;
    }

    public boolean canBeFirst() {
        return canBeFirst;
    }

    public boolean canBeLast() {
        return canBeLast;
    }

    
    public TokenParser getTokenParser() {
        if(this.tokenParserClass == null){
            return null;
        }
        if(this.tokenParser == null){
            try {
                this.tokenParser = this.tokenParserClass.newInstance();
            } catch (IllegalAccessException| InstantiationException ex) {
                return null;
            } 
        }
        return tokenParser;
    }

}
