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
package net.adamjak.math.expressionevaluator.parser;

import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;

/**
 *
 * @author Marian Adamjak
 */
public class ArgumentSeparatorParser extends TokenParser {

    private static final TokenType tokenType = TokenType.ARGUMENT_SEPARATOR;

    public ArgumentSeparatorParser() {
        super(tokenType);
    }

    @Override
    public ParseOutput parse(int startPosition) {
        return findOneCharToken(startPosition, ARGUMENT_SEPARATOR_ALLOW_CHARS);
    }

    @Override
    public boolean canParseAfterToken(Token token) {
        if (token == null) {
            return true;
        }
        switch (token.getType()) {
            case UNARY_OPERATOR:
                return false;
            case BINARY_OPERATOR:
                return false;
            case ARGUMENT_SEPARATOR:
                return true;
            case TEXT:
                return true;
            case BRACKET_LEFT:
                return true;
            case BRACKET_RIGHT:
                return true;
            case SPACE:
                return true;
            case NUMBER:
                return true;
            case FUNCTION_NAME:
                return false;
            case VARIABLE_NAME:
                return true;
            case UNKOWN:
                return false;
            default:
                return false;
        }
    }
}
