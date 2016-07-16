/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator.parser;

import java.util.ArrayList;
import java.util.List;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.StringNotNullAndEmpty;

/**
 *
 * @author adamjak2726607
 */
public class InputParser {

    public enum ParseStatus {

        OK, ERROR, UNPARSED;
    }

    private static final StringNotNullAndEmpty stringNotNullEmptyValidator = StringNotNullAndEmpty.getInstance();

    private final String inputLine;
    private final List<Token> parsedToken;
    private ParseStatus parseStatus = null;
    private int lastParsePosition;

    public static InputParser createParser(String inputLine) {
        Utils.validateInput(inputLine, stringNotNullEmptyValidator, NullPointerException.class, "Parameter inputLine can not be null");
        InputParser parser = new InputParser(inputLine);
        parser.parse();
        return parser;
    }

    private InputParser(String inputLine) {
        this.inputLine = inputLine;
        this.parsedToken = new ArrayList<>();
    }

    public ParseStatus getParseStatus() {
        return this.parseStatus;
    }

    public List<Token> getParsedToken() {
        return parsedToken;
    }

    public String getInputLine() {
        return inputLine;
    }

    public int getLastParsePosition() {
        return lastParsePosition;
    }

    private void parse() {
        this.parseStatus = ParseStatus.UNPARSED;
        List<TokenParser> startLineTokenParsers = startLineTokenParsers();
        int parsePosition = 0;
        int newPosition = 0;
        for (TokenParser tokenParser : startLineTokenParsers) {
            tokenParser.setInputLine(inputLine);
            TokenParser.ParseOutput parseOutput = tokenParser.parse(parsePosition);
            if(parseOutput.getParseResultType() == TokenParser.ParseResultType.OK){
                newPosition = parseOutput.getNextParsePosition();
                this.parsedToken.add(parseOutput.getParsedToken());
                break;
            }
        }
        if (parsePosition == newPosition) {
            this.parseStatus = ParseStatus.ERROR;
            this.lastParsePosition = parsePosition;
            return;
        }
        parsePosition = newPosition;
        while (parsePosition < inputLine.length()) {
            Token beforeToken = this.parsedToken.get(this.parsedToken.size() - 1);
            List<TokenParser> afterTokenParsers = afterTokenParsers(beforeToken);
            for (TokenParser tokenParser : afterTokenParsers) {
                tokenParser.setInputLine(inputLine);
                TokenParser.ParseOutput parseOutput = tokenParser.parse(parsePosition);
                if(parseOutput.getParseResultType() == TokenParser.ParseResultType.OK){
                    newPosition = parseOutput.getNextParsePosition();
                    this.parsedToken.add(parseOutput.getParsedToken());
                    break;
                }
            }
            if (parsePosition == newPosition) {
                this.parseStatus = ParseStatus.ERROR;
                this.lastParsePosition = parsePosition;
                return;
            }
            parsePosition = newPosition;
        }
        this.parseStatus = ParseStatus.OK;
        this.lastParsePosition = parsePosition;
    }

    private List<TokenParser> startLineTokenParsers() {
        List<TokenParser> parsers = new ArrayList<>();
        for (TokenType tokenType : TokenType.values()) {
            if (tokenType.getTokenParser()!= null && tokenType.canBeFirst()) {
                parsers.add(tokenType.getTokenParser());
            }
        }
        return parsers;
    }

    private List<TokenParser> afterTokenParsers(Token token) {
        List<TokenParser> parsers = new ArrayList<>();
        if (token == null) {
            return parsers;
        }
        for (TokenType tokenType : TokenType.values()) {
            if (tokenType.getTokenParser()!= null &&  tokenType.getTokenParser().canParseAfterToken(token)) {
                parsers.add(tokenType.getTokenParser());
            }
        }
        return parsers;
    }
}
