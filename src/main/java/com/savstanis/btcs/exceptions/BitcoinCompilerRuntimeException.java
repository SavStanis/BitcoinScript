package com.savstanis.btcs.exceptions;

public class BitcoinCompilerRuntimeException extends RuntimeException {
    public BitcoinCompilerRuntimeException() {
        super();
    }

    public BitcoinCompilerRuntimeException(String message) {
        super(message);
    }

    public BitcoinCompilerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
