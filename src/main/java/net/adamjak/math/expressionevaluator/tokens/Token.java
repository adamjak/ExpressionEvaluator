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

import net.adamjak.math.expressionevaluator.ExpressionSequence;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.ObjectNotNull;
import net.adamjak.utils.validators.StringNotNullAndEmpty;

/**
 *
 * @author Marian Adamjak
 */
public class Token implements ExpressionSequence{
    
    
    private final String content;
    private final TokenType type;
    private final int startPosition;
    private int priority;

    public Token(String content, TokenType type, int startPosition) {
        this(content,type,startPosition,-1);
    }
    
    public Token(String content, TokenType type, int startPosition, int priority) {
        Utils.validateInput(content, StringNotNullAndEmpty.getInstance(), NullPointerException.class, "Content can not be null or empty");
        Utils.validateInput(type, ObjectNotNull.getInstance(), NullPointerException.class, "Type can not be null or empty");
        this.content = content;
        this.type = type;
        this.startPosition = startPosition;
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int value){
        this.priority = value;
    }
    
    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Token{" + "Content=" + content + ", type=" + type + ", priority=" + priority +'}';
    }

    @Override
    public int startPoisition() {
        return startPosition;
    }

    @Override
    public int length() {
        return this.content.length();
    }
    
}
