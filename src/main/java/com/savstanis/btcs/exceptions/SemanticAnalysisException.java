package com.savstanis.btcs.exceptions;

public class SemanticAnalysisException extends RuntimeException {
    public SemanticAnalysisException() {
    }

    public SemanticAnalysisException(String message) {
        super(message);
    }

    public SemanticAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
