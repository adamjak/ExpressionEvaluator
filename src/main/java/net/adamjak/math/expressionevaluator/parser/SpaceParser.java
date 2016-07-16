/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.parser;

import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;

/**
 *
 * @author adamjak
 */
public class SpaceParser extends TokenParser{

    private static final TokenType tokenType = TokenType.SPACE;
    
    public SpaceParser() {
        super(tokenType);
    }

    @Override
    public ParseOutput parse(int startPosition) {
        return findMultiCharToken(startPosition, SPACE_ALLOW_CHARS);
    }
    
    @Override
    public boolean canParseAfterToken(Token token) {
        return true;
    }
}
