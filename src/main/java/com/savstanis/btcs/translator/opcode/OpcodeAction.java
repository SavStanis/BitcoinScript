package com.savstanis.btcs.translator.opcode;

import com.savstanis.btcs.exceptions.InvalidTransactionException;

import java.util.Deque;

@FunctionalInterface
public interface OpcodeAction {
    void run(Deque<byte[]> stack) throws InvalidTransactionException;
}
