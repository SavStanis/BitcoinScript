package com.savstanis.btcs.syntaxanalyzer;

import com.savstanis.btcs.syntaxanalyzer.expressions.AbstractExpression;

import java.util.List;

public class SyntaxAnalyzerResult {
    private int tokensAnalyzed;
    private List<AbstractExpression> expressions;

    public SyntaxAnalyzerResult() {
    }

    public SyntaxAnalyzerResult(int tokensAnalyzed, List<AbstractExpression> expressions) {
        this.tokensAnalyzed = tokensAnalyzed;
        this.expressions = expressions;
    }

    public int getTokensAnalyzed() {
        return tokensAnalyzed;
    }

    public void setTokensAnalyzed(int tokensAnalyzed) {
        this.tokensAnalyzed = tokensAnalyzed;
    }

    public List<AbstractExpression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<AbstractExpression> expressions) {
        this.expressions = expressions;
    }
}
