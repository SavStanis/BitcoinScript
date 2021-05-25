package com.savstanis.btcs.syntaxanalyzer.expressions;

public class OpCodeExpression extends AbstractExpression{
    private String type;

    public OpCodeExpression(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
