/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.tokens;

import net.adamjak.math.expressionevaluator.ExpressionSequence;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.ObjectNotNull;
import net.adamjak.utils.validators.StringNotNullAndEmpty;

/**
 *
 * @author madamjak
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
