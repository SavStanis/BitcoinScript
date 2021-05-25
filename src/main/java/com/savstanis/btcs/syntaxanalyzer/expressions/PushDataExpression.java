package com.savstanis.btcs.syntaxanalyzer.expressions;

import com.savstanis.btcs.syntaxanalyzer.data.AbstractData;

import java.math.BigInteger;

public class PushDataExpression extends AbstractExpression {
    private BigInteger maxSize;
    private BigInteger minSize;
    private AbstractData size;
    private AbstractData data;

    public PushDataExpression() {
    }

    public PushDataExpression(BigInteger minSize, BigInteger maxSize, AbstractData size, AbstractData data) {
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.size = size;
        this.data = data;
    }

    public BigInteger getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(BigInteger maxSize) {
        this.maxSize = maxSize;
    }

    public BigInteger getMinSize() {
        return minSize;
    }

    public void setMinSize(BigInteger minSize) {
        this.minSize = minSize;
    }

    public AbstractData getSize() {
        return size;
    }

    public void setSize(AbstractData size) {
        this.size = size;
    }

    public AbstractData getData() {
        return data;
    }

    public void setData(AbstractData data) {
        this.data = data;
    }
}
