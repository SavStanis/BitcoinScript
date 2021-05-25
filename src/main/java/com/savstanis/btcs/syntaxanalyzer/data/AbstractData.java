package com.savstanis.btcs.syntaxanalyzer.data;

import java.math.BigInteger;

public class AbstractData {
    private String rawData;
    private BigInteger parsedData;

    public AbstractData() {
    }

    public AbstractData(String rawData, BigInteger parsedData) {
        this.rawData = rawData;
        this.parsedData = parsedData;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public BigInteger getParsedData() {
        return parsedData;
    }

    public void setParsedData(BigInteger parsedData) {
        this.parsedData = parsedData;
    }
}
