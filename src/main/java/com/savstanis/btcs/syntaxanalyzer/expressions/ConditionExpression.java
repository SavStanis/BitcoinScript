package com.savstanis.btcs.syntaxanalyzer.expressions;

import java.util.List;

public class ConditionExpression extends AbstractExpression{
    private List<AbstractExpression> trueExpression;

    private List<AbstractExpression> falseExpression;

    public ConditionExpression() {
    }

    public ConditionExpression(List<AbstractExpression> trueExpression, List<AbstractExpression> falseExpression) {
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    public List<AbstractExpression> getTrueExpression() {
        return trueExpression;
    }

    public void setTrueExpression(List<AbstractExpression> trueExpression) {
        this.trueExpression = trueExpression;
    }

    public List<AbstractExpression> getFalseExpression() {
        return falseExpression;
    }

    public void setFalseExpression(List<AbstractExpression> falseExpression) {
        this.falseExpression = falseExpression;
    }

}
