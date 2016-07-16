/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.adamjak.math.expressionevaluator;

import net.adamjak.math.expressionevaluator.results.ResultSimple;
import net.adamjak.math.expressionevaluator.results.ResultMultiple;
import net.adamjak.math.expressionevaluator.tokens.Token;
import net.adamjak.math.expressionevaluator.tokens.TokenType;
import net.adamjak.math.expressionevaluator.exceptions.EvaluateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.adamjak.math.expressionevaluator.functions.Function;
import net.adamjak.math.expressionevaluator.exceptions.ParseException;
import net.adamjak.math.expressionevaluator.functions.StringToNumber;
import net.adamjak.math.expressionevaluator.parser.InputParser;
import net.adamjak.math.expressionevaluator.results.Result;
import net.adamjak.math.expressionevaluator.tokens.ArgumentSeparatorEvaluator;
import net.adamjak.math.expressionevaluator.tokens.BinaryOperatorEvaluator;
import net.adamjak.math.expressionevaluator.tokens.TokenEvaluateResult;
import net.adamjak.math.expressionevaluator.tokens.UnaryOperatorEvaluator;
import net.adamjak.math.expressionevaluator.variables.VariableValue;
import net.adamjak.math.expressionevaluator.variables.VariableValueSet;
import net.adamjak.utils.Utils;
import net.adamjak.utils.validators.ObjectNotNull;

/**
 *
 * @author adamjak
 */
public class BaseExpression extends Expression {

    private final List<ExpressionSequence> exprSeq;
    private final StringToNumber stringToNumber = new StringToNumber();

    public static BaseExpression create(String input) throws ParseException {
        BaseExpression be = new BaseExpression(input);
        be.parseInput();
        return be;
    }

    private BaseExpression(String input) {
        super(input);
        exprSeq = new ArrayList<>();
    }

    private void parseInput() throws ParseException {
        InputParser inputParser = InputParser.createParser(getInput().trim());
        if (inputParser.getParseStatus() == InputParser.ParseStatus.ERROR) {
            throw new ParseException("Incorrect char in input", inputParser.getLastParsePosition() + 1);
        }
        int bracketCount = 0;
        for (Token token : inputParser.getParsedToken()) {
            if (token.getType() == TokenType.FUNCTION_NAME) {
                if (Expression.getFunctionMap().containsKey(token.getContent()) == false) {
                    throw new ParseException("Unknown function name", token.startPoisition() + 1);
                }
            }
            if (token.getType() == TokenType.BRACKET_LEFT) {
                bracketCount = bracketCount + 1;
                token.setPriority(bracketCount);
            }
            if (token.getType() == TokenType.BRACKET_RIGHT) {
                token.setPriority(bracketCount);
                bracketCount = bracketCount - 1;
                if (bracketCount < 0) {
                    throw new ParseException("Unexpected right bracket", token.startPoisition() + 1);
                }
            }
        }
        Token lastToken = Utils.lastElement(inputParser.getParsedToken());
        if (lastToken.getType().canBeLast() == false) {
            throw new ParseException("Unexpected last token", lastToken.startPoisition() + 1);
        }

        exprSeq.addAll(inputParser.getParsedToken());
        convertNumberTokens();
        convertTextTokens();
    }

    public List<ExpressionSequence> getExprSeq() {
        return exprSeq;
    }

    @Override
    public List<ExpressionSequence> evaluate(VariableValueSet variableSet) throws EvaluateException {
        if (this.exprSeq.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        if (this.exprSeq.size() == 1) {
            List<ExpressionSequence> output = new ArrayList<>(1);
            output.add(this.exprSeq.get(0));
            return output;
        }
        if (containsVariable() && (variableSet == null || variableSet.getVariableNames().isEmpty())) {
            throw new EvaluateException("Expression contains variable(s), but no variableSet has given");
        }
        return evaluateExprSeq(variableSet);
    }

    @Override
    public List<String> getVariableNames() {
        List<String> variableNames = new ArrayList<>();
        for (ExpressionSequence eseq : this.exprSeq) {
            if (eseq instanceof Token) {
                Token token = (Token) eseq;
                if (token.getType() == TokenType.VARIABLE_NAME) {
                    variableNames.add(token.getContent());
                }
            }
        }
        return variableNames;
    }

    private boolean containsVariable() {
        for (ExpressionSequence eseq : this.exprSeq) {
            if (eseq instanceof Token) {
                Token token = (Token) eseq;
                if (token.getType() == TokenType.VARIABLE_NAME) {
                    return true;
                }
            }
        }
        return false;
    }

    private void convertNumberTokens() throws ParseException {
        for (int i = 0; i < exprSeq.size(); i++) {
            if (exprSeq.get(i) instanceof Token && ((Token) exprSeq.get(i)).getType() == TokenType.NUMBER) {
                try {
                    List<ResultSimple> results = stringToNumber.getResults((Token) exprSeq.get(i));
                    exprSeq.set(i, results.get(0));
                } catch (EvaluateException ex) {
                    throw new ParseException("Can not parse number. Error:" + ex.getMessage(), exprSeq.get(i).startPoisition());
                }
            }
        }
    }

    private void convertTextTokens() throws ParseException {
        for (int i = 0; i < exprSeq.size(); i++) {
            if (exprSeq.get(i) instanceof Token && ((Token) exprSeq.get(i)).getType() == TokenType.TEXT) {
                ResultSimple result = new ResultSimple(((Token) exprSeq.get(i)).getContent(), exprSeq.get(i).startPoisition(), exprSeq.get(i).length());
                exprSeq.set(i, result);
            }
        }
    }

    private List<ExpressionSequence> evaluateExprSeq(VariableValueSet variableSet) throws EvaluateException {
        replaceVariables(variableSet);
        evaluateExprSubSeq(new IntegerPair(0, this.exprSeq.size() - 1));
        return this.exprSeq;
    }

    private void evaluateExprSubSeq(IntegerPair evaluateRange) throws EvaluateException {
        Utils.validateInput(evaluateRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = evaluateRange.getInt01();
        int endSeq = evaluateRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        
        //remove spaces
        int sizeBefore = this.exprSeq.size();
        List<Integer> spacePositions = findAllPositionsSpace(new IntegerPair(startSeq, endSeq));
        for (int i = spacePositions.size() - 1; i >= 0; i--) {
            int positionToRemove = spacePositions.get(i);
            this.exprSeq.remove(positionToRemove);
        }
        if (sizeBefore > this.exprSeq.size()) {
            endSeq = endSeq - (sizeBefore - this.exprSeq.size());
        }
        
        //evaluating inside of brackets
        int bracketPriority = getMaxBracketPriority(new IntegerPair(startSeq, endSeq));
        while (bracketPriority > 0) {
            IntegerPair bracketPosition = findFirstStartEndPositionForBracketPriority(new IntegerPair(startSeq, endSeq), bracketPriority);
            if (bracketPosition == null) {
                break;
            }
            if ((bracketPosition.int02 - bracketPosition.int01) <= 0) {
                throw new EvaluateException("Can not interpret bracket at position: " + bracketPosition.int01);
            }
            if ((bracketPosition.int02 - bracketPosition.int01) == 1) {
                this.exprSeq.set(bracketPosition.int01, new ResultSimple(bracketPosition.int01, 2));
                this.exprSeq.remove(bracketPosition.int02);
                endSeq = endSeq - 1;
            } else if ((bracketPosition.int02 - bracketPosition.int01) == 2) {
                if (this.exprSeq.get(bracketPosition.int01 + 1) instanceof ResultSimple || this.exprSeq.get(bracketPosition.int01 + 1) instanceof ResultMultiple) {
                    this.exprSeq.remove(bracketPosition.int02);
                    this.exprSeq.remove(bracketPosition.int01);
                    endSeq = endSeq - 2;
                }
            } else {
                int lenBefore = this.exprSeq.size();
                evaluateExprSubSeq(new IntegerPair(bracketPosition.int01 + 1, bracketPosition.int02 - 1));
                endSeq = endSeq - (lenBefore - this.exprSeq.size());
            }
            bracketPriority = getMaxBracketPriority(new IntegerPair(startSeq, endSeq));
        }
        
        // evaluation functions
        int functionPosition = findFirstStartEndPositionFunction(new IntegerPair(startSeq, endSeq));
        while (functionPosition >= 0) {
            Token token = (Token) this.exprSeq.get(functionPosition);
            Function function = Expression.getFunctionByName(token.getContent());
            if (function == null) {
                throw new EvaluateException("Can not find function: " + token.getContent(),token.startPoisition());
            }
            List<ResultSimple> funcResult;
            int argumentLenght = 0;
            if (this.exprSeq.get(functionPosition + 1) instanceof ResultSimple) {
                ResultSimple rs = (ResultSimple) this.exprSeq.get(functionPosition + 1);
                argumentLenght = rs.length();
                funcResult = function.getResults(rs);
            } else if (this.exprSeq.get(functionPosition + 1) instanceof ResultMultiple) {
                ResultMultiple rm = (ResultMultiple) this.exprSeq.get(functionPosition + 1);
                argumentLenght = rm.length();
                funcResult = function.getResults(rm.getResults());
            } else {
                throw new EvaluateException("There is no suitable argument for function", token.startPoisition());
            }
            ExpressionSequence funcResSeq;
            if (funcResult.isEmpty()) {
                funcResSeq = new ResultSimple(functionPosition, argumentLenght);
            } else if (funcResult.size() == 1) {
                funcResSeq = funcResult.get(0);
            } else {
                funcResSeq = ResultMultiple.createFromResultSimple(funcResult);
            }
            this.exprSeq.set(functionPosition, funcResSeq);
            this.exprSeq.remove(functionPosition + 1);
            endSeq = endSeq - 1;
            functionPosition = findFirstStartEndPositionFunction(new IntegerPair(startSeq, endSeq));
        }
        
       // evaluate aritmetic operators + cumulate simple result around argument separator into multiple results
        List<Integer> argSeparatorPositions = findAllPositionsArgumentSeparator(new IntegerPair(startSeq, endSeq));
        while (argSeparatorPositions.isEmpty() == false) {
            if (argSeparatorPositions.size() == 1) {
                if (argSeparatorPositions.get(0) - startSeq == 1) {
                    if (endSeq - argSeparatorPositions.get(0) == 1) {
                        TokenEvaluateResult ter = ArgumentSeparatorEvaluator.getInstance().evaluate(exprSeq, argSeparatorPositions.get(0));
                        switch (ter.getEvaluateResultType()) {
                            case EVALUATE_FULL:
                                this.exprSeq.set(ter.getReplaceStartPosition(), ter.getEvaluateResult());
                                for (int removePosition = ter.getReplaceEndPosition(); removePosition > ter.getReplaceStartPosition(); removePosition--) {
                                    this.exprSeq.remove(removePosition);
                                }
                                endSeq = endSeq - (ter.getReplaceEndPosition() - ter.getReplaceStartPosition());
                                break;
                            case EVALUATE_PARTIALY:
                                throw new EvaluateException("Stop evaluation - Argument separator has been evaluated partialy.", tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                            case NO_EVALUATE:
                                throw new EvaluateException("Stop evaluation - Argument separator has not been evaluated." , tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                            default:
                                throw new UnsupportedOperationException("Unknown TokenResultType " + ter.getEvaluateResultType().name());

                        }
                    } else {
                        sizeBefore = this.exprSeq.size();
                        evaluateExprSubSeq(new IntegerPair(argSeparatorPositions.get(0) + 1, endSeq));
                        if (sizeBefore > this.exprSeq.size()) {
                            endSeq = endSeq - (sizeBefore - this.exprSeq.size());
                        } else {
                            throw new EvaluateException("Evaluating before argument separator fail.", tokenPositionToInputPosition(argSeparatorPositions.get(0)));
                        }
                    }
                } else {
                    sizeBefore = this.exprSeq.size();
                    evaluateExprSubSeq(new IntegerPair(startSeq, argSeparatorPositions.get(0) - 1));
                    if (sizeBefore > this.exprSeq.size()) {
                        endSeq = endSeq - (sizeBefore - this.exprSeq.size());
                    } else {
                        throw new EvaluateException("Evaluating before argument separator fail.", tokenPositionToInputPosition(argSeparatorPositions.get(0)));
                    }
                }
            } else if (argSeparatorPositions.get(0) - startSeq == 1) {
                if (argSeparatorPositions.get(1) - argSeparatorPositions.get(0) == 2) {
                    TokenEvaluateResult ter = ArgumentSeparatorEvaluator.getInstance().evaluate(exprSeq, argSeparatorPositions.get(0));
                    switch (ter.getEvaluateResultType()) {
                        case EVALUATE_FULL:
                            this.exprSeq.set(ter.getReplaceStartPosition(), ter.getEvaluateResult());
                            for (int removePosition = ter.getReplaceEndPosition(); removePosition > ter.getReplaceStartPosition(); removePosition--) {
                                this.exprSeq.remove(removePosition);
                            }
                            endSeq = endSeq - (ter.getReplaceEndPosition() - ter.getReplaceStartPosition());
                            break;
                        case EVALUATE_PARTIALY:
                            throw new EvaluateException("Stop evaluation - Argument separator has been evaluated partialy." , tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                        case NO_EVALUATE:
                            throw new EvaluateException("Stop evaluation - Argument separator has not been evaluated.", tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                        default:
                            throw new UnsupportedOperationException("Unknown TokenResultType " + ter.getEvaluateResultType().name());
                    }
                } else {
                    sizeBefore = this.exprSeq.size();
                    evaluateExprSubSeq(new IntegerPair(argSeparatorPositions.get(0) + 1, argSeparatorPositions.get(1) - 1));
                    if (sizeBefore > this.exprSeq.size()) {
                        endSeq = endSeq - (sizeBefore - this.exprSeq.size());
                    } else {
                        throw new EvaluateException("Evaluating before argument separator fail." + tokenPositionToInputPosition(argSeparatorPositions.get(0)));
                    }
                }
            } else {
                sizeBefore = this.exprSeq.size();
                evaluateExprSubSeq(new IntegerPair(startSeq, argSeparatorPositions.get(0) - 1));
                if (sizeBefore > this.exprSeq.size()) {
                    endSeq = endSeq - (sizeBefore - this.exprSeq.size());
                } else {
                    throw new EvaluateException("Evaluating before argument separator fail near position " + argSeparatorPositions.get(0));
                }
            }
            argSeparatorPositions = findAllPositionsArgumentSeparator(new IntegerPair(startSeq, endSeq));
        }
        evaluateArithmeticOperations(new IntegerPair(startSeq, endSeq));

    }

    private void evaluateArithmeticOperations(IntegerPair evaluateRange) throws EvaluateException {
        Utils.validateInput(evaluateRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = evaluateRange.getInt01();
        int endSeq = evaluateRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        if (startSeq == endSeq) {
            if (this.exprSeq.get(startSeq) instanceof Result) {
                return;
            } else {
                throw new EvaluateException("Evaluate AritmeticOperators - One token evaluating - result is excepted", this.exprSeq.get(startSeq).startPoisition());
            }
        }

        int unOpPosition = findFirstUnaryOperator(new IntegerPair(startSeq, endSeq));
        while (unOpPosition > -1) {
            TokenEvaluateResult ter = UnaryOperatorEvaluator.getInstance().evaluate(exprSeq, unOpPosition);
            switch (ter.getEvaluateResultType()) {
                case EVALUATE_FULL:
                    this.exprSeq.set(ter.getReplaceStartPosition(), ter.getEvaluateResult());
                    for (int removePosition = ter.getReplaceEndPosition(); removePosition > ter.getReplaceStartPosition(); removePosition--) {
                        this.exprSeq.remove(removePosition);
                    }
                    endSeq = endSeq - (ter.getReplaceEndPosition() - ter.getReplaceStartPosition());
                    break;
                case EVALUATE_PARTIALY:
                    throw new EvaluateException("Stop evaluation - Unary operator has been evaluated partialy.", tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                case NO_EVALUATE:
                    throw new EvaluateException("Stop evaluation - Unary operator has not been evaluated.", tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                default:
                    throw new UnsupportedOperationException("Unknown TokenResultType " + ter.getEvaluateResultType().name());

            }
            unOpPosition = findFirstUnaryOperator(new IntegerPair(startSeq, endSeq));
        }
        if (startSeq == endSeq) {
            if (this.exprSeq.get(startSeq) instanceof Result) {
                return;
            } else {
                throw new EvaluateException("Evaluate AritmeticOperators - No result after unary operators evaluation", this.exprSeq.get(startSeq).startPoisition());
            }
        }
        int maxOpPriority = getMaxBinaryOpPriority(new IntegerPair(startSeq, endSeq));
        if (maxOpPriority == -1) {
            throw new EvaluateException("Evaluate BinaryOperators - no operator there", this.exprSeq.get(startSeq).startPoisition());
        }
        while (maxOpPriority > 0) {
            int opPosition = findFirstBinaryOperatorByPriority(new IntegerPair(startSeq, endSeq), maxOpPriority);
            if (opPosition > 0) {
                TokenEvaluateResult ter = BinaryOperatorEvaluator.getInstance().evaluate(exprSeq, opPosition);
                switch (ter.getEvaluateResultType()) {
                    case EVALUATE_FULL:
                        this.exprSeq.set(ter.getReplaceStartPosition(), ter.getEvaluateResult());
                        for (int removePosition = ter.getReplaceEndPosition(); removePosition > ter.getReplaceStartPosition(); removePosition--) {
                            this.exprSeq.remove(removePosition);
                        }
                        endSeq = endSeq - (ter.getReplaceEndPosition() - ter.getReplaceStartPosition());
                        break;
                    case EVALUATE_PARTIALY:
                        throw new EvaluateException("Stop evaluation - Binary operator has been evaluated partialy. Postion near: " + tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                    case NO_EVALUATE:
                        throw new EvaluateException("Stop evaluation - Binary operator has not been evaluated. Postion near: " + tokenPositionToInputPosition(ter.getReplaceStartPosition()));
                    default:
                        throw new UnsupportedOperationException("Unknown TokenResultType " + ter.getEvaluateResultType().name());

                }
            }
            maxOpPriority = getMaxBinaryOpPriority(new IntegerPair(startSeq, endSeq));
        }
        if (startSeq == endSeq) {
            if (this.exprSeq.get(startSeq) instanceof Result) {
                return;
            } else {
                throw new EvaluateException("Evaluate BinaryOperators - Output is no result type",this.exprSeq.get(startSeq).startPoisition());
            }
        } else {
            throw new EvaluateException("Evaluate BinaryOperators - more then one token is result",this.exprSeq.get(startSeq).startPoisition());
        }
    }

    private int findFirstUnaryOperator(IntegerPair searchRange) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.UNARY_OPERATOR) {
                    return pos;
                }
            }
        }
        return -1;
    }

    private int findFirstBinaryOperatorByPriority(IntegerPair searchRange, int priority) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.BINARY_OPERATOR) {
                    if (token.getPriority() == priority) {
                        return pos;
                    }
                }
            }
        }
        return -1;
    }

    private List<Integer> findAllPositionsSpace(IntegerPair searchRange) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        List<Integer> output = new ArrayList<>();
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.SPACE) {
                    output.add(pos);
                }
            }
        }
        return output;
    }

    private List<Integer> findAllPositionsArgumentSeparator(IntegerPair searchRange) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        List<Integer> output = new ArrayList<>();
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.ARGUMENT_SEPARATOR) {
                    output.add(pos);
                }
            }
        }
        return output;
    }

    private int findFirstStartEndPositionFunction(IntegerPair searchRange) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.FUNCTION_NAME) {
                    return pos;
                }
            }
        }
        return -1;
    }

    private int getMaxBinaryOpPriority(IntegerPair searchRange) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        int maxPriority = -1;
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.BINARY_OPERATOR) {
                    if (token.getPriority() > maxPriority) {
                        maxPriority = token.getPriority();
                    }
                }
            }
        }
        return maxPriority;
    }

    private int getMaxBracketPriority(IntegerPair searchRange) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        int maxPriority = -1;
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.BRACKET_LEFT) {
                    if (token.getPriority() > maxPriority) {
                        maxPriority = token.getPriority();
                    }
                }
            }
        }
        return maxPriority;
    }

    private IntegerPair findFirstStartEndPositionForBracketPriority(IntegerPair searchRange, int priority) {
        Utils.validateInput(searchRange, ObjectNotNull.getInstance(), NullPointerException.class, "IntPair can not be null");
        int startSeq = searchRange.getInt01();
        int endSeq = searchRange.getInt02();
        checkSubSeqIndex(startSeq, endSeq);
        boolean leftBracketFound = false;
        for (int pos = startSeq; pos < endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.BRACKET_LEFT && token.getPriority() == priority) {
                    startSeq = pos;
                    leftBracketFound = true;
                    break;
                }
            }
        }
        if (leftBracketFound == false) {
            return null;
        }
        boolean rightBracketFound = false;
        for (int pos = startSeq + 1; pos <= endSeq; pos++) {
            if (this.exprSeq.get(pos) instanceof Token) {
                Token token = (Token) this.exprSeq.get(pos);
                if (token.getType() == TokenType.BRACKET_RIGHT && token.getPriority() == priority) {
                    endSeq = pos;
                    rightBracketFound = true;
                    break;
                }
            }
        }
        if (rightBracketFound == false) {
            return null;
        }
        return new IntegerPair(startSeq, endSeq);
    }

    private void replaceVariables(VariableValueSet variableSet) {
        for (int i = 0; i < exprSeq.size(); i++) {
            if (exprSeq.get(i) instanceof Token && ((Token) exprSeq.get(i)).getType() == TokenType.VARIABLE_NAME) {
                Token token = (Token) exprSeq.get(i);
                VariableValue varVal = variableSet.getVariableValueByName(token.getContent());
                //TODO: check variable type against neigbor tokens
                if (varVal != null) {
                    exprSeq.set(i, varVal.getResult(exprSeq.get(i).startPoisition(), exprSeq.get(i).length()));
                }
            }
        }
    }

    private void checkSubSeqIndex(int startSeq, int endSeq) {
        if (startSeq < 0 || startSeq >= this.exprSeq.size()) {
            throw new IndexOutOfBoundsException("StartSeq is out of index range");
        }
        if (endSeq < 0 || endSeq >= this.exprSeq.size()) {
            throw new IndexOutOfBoundsException("EndSeq is out of index range");
        }
        if (startSeq > endSeq) {
            throw new IllegalArgumentException("StartSeq is great then EndSeq");
        }
    }

    private int tokenPositionToInputPosition(int tokenPosition){
        if(tokenPosition < 0 || tokenPosition > this.exprSeq.size() -1){
            return -1;
        }
        return this.exprSeq.get(tokenPosition).startPoisition();
    }
    
    private class IntegerPair {

        private final int int01;
        private final int int02;

        public IntegerPair(int int01, int int02) {
            this.int01 = int01;
            this.int02 = int02;
        }

        public int getInt01() {
            return int01;
        }

        public int getInt02() {
            return int02;
        }

    }
}
