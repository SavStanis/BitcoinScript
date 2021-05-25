package com.savstanis.btcs.syntaxanalyzer;

import com.savstanis.btcs.exceptions.SyntaxException;
import com.savstanis.btcs.syntaxanalyzer.data.AbstractData;
import com.savstanis.btcs.syntaxanalyzer.expressions.AbstractExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.ConditionExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.OpCodeExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.PushDataExpression;
import com.savstanis.btcs.tokenizer.Token;
import com.savstanis.btcs.tokenizer.TokenType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {
    public SyntaxAnalyzerResult analyze(List<Token> tokenList) {
        List<AbstractExpression> expressionList = new ArrayList<>();

        for (int i = 0, tokenListSize = tokenList.size(); i < tokenListSize; i++) {
            Token token = tokenList.get(i);

            if (TokenType.PUSHDATA_OP.equals(token.getType())) {
                var nextTokenType = tokenList.get(i + 1).getType();
                if (!TokenType.DEC.equals(nextTokenType) && !TokenType.HEX.equals(nextTokenType)) {
                    throw new SyntaxException();
                }
                i++;
                nextTokenType = tokenList.get(i + 1).getType();
                if (!TokenType.DEC.equals(nextTokenType) && !TokenType.HEX.equals(nextTokenType)) {
                    throw new SyntaxException();
                }
                expressionList.add(resolvePushDataExpression(tokenList.get(i).getValue(), tokenList.get(i + 1).getValue(), token.getValue()));

                i++;
                continue;
            }

            if (TokenType.DEC.equals(token.getType()) || TokenType.HEX.equals(token.getType())) {
                var nextTokenType = tokenList.get(i + 1).getType();
                nextTokenType = tokenList.get(i + 1).getType();
                if (!TokenType.DEC.equals(nextTokenType) && !TokenType.HEX.equals(nextTokenType)) {
                    throw new SyntaxException();
                }
                expressionList.add(resolvePushDataExpression(tokenList.get(i).getValue(), tokenList.get(i + 1).getValue(), null));

                i++;
                continue;
            }

            if (TokenType.OP_CODE.equals(token.getType())) {
                if ("OP_ELSE".equals(token.getValue()) || "OP_ENDIF".equals(token.getValue())) {
                    return new SyntaxAnalyzerResult(i + 1, expressionList);
                }

                if ("OP_IF".equals(token.getValue())) {
                    var conditionExpression = new ConditionExpression();

                    var ifResult = analyze(tokenList.subList(i + 1, tokenList.size()));

                    if (ifResult.getTokensAnalyzed() == tokenListSize - i) {
                        throw new SyntaxException();
                    }
                    conditionExpression.setTrueExpression(ifResult.getExpressions());
                    i += ifResult.getTokensAnalyzed();

                    if ("OP_ELSE".equals(tokenList.get(i).getValue())) {
                        var elseResult = analyze(tokenList.subList(i + 1, tokenList.size()));
                        if (elseResult.getTokensAnalyzed() == tokenListSize - i) {
                            throw new SyntaxException();
                        }
                        conditionExpression.setFalseExpression(elseResult.getExpressions());
                        i += elseResult.getTokensAnalyzed();
                    }

                    if (!"OP_ENDIF".equals(tokenList.get(i).getValue())) {
                        throw new SyntaxException();
                    }

                    expressionList.add(conditionExpression);
                } else {
                    expressionList.add(new OpCodeExpression(token.getValue()));
                }
            }

        }
        return new SyntaxAnalyzerResult(0, expressionList);
    }

    private PushDataExpression resolvePushDataExpression(String size, String data, String code) {
        var parsedSize = (isDecimal(size)) ? new BigInteger(size): new BigInteger(size.substring(2), 16);
        var parsedData = (isDecimal(data)) ? new BigInteger(data): new BigInteger(data.substring(2), 16);

        BigInteger minSize = null;
        BigInteger maxSize = null;

        if (code != null) {
            switch (code) {
                case "OP_PUSHDATA1":
                    maxSize = BigInteger.valueOf(255);
                    minSize = BigInteger.valueOf(1);
                    break;
                case "OP_PUSHDATA2":
                    maxSize = BigInteger.valueOf(256);
                    minSize = BigInteger.valueOf(65_535);
                    break;
                case "OP_PUSHDATA4":
                    maxSize = BigInteger.valueOf(65_536);
                    minSize = BigInteger.valueOf(4_294_967_295L);
                    break;
                default:
                    throw new SyntaxException("Invalid PushData OP");
            }
        }

        return new PushDataExpression(minSize, maxSize, new AbstractData(size, parsedSize), new AbstractData(data, parsedData));
    }

    private static boolean isDecimal(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private static boolean isHexadecimal(String s) {
        return s != null && s.matches("0[xX][0-9a-fA-F]+");
    }
}
