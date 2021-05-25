package com.savstanis.btcs.translator;

import com.savstanis.btcs.exceptions.BitcoinCompilerRuntimeException;
import com.savstanis.btcs.exceptions.InvalidTransactionException;
import com.savstanis.btcs.syntaxanalyzer.expressions.AbstractExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.ConditionExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.OpCodeExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.PushDataExpression;
import com.savstanis.btcs.translator.opcode.OpcodeActionSupplier;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class BitcoinScriptTranslator {
    private final int MAX_BYTE_VECTOR_SIZE = 520;

    private final Deque<byte[]> mainStack;
    private boolean isTransactionValid;

    public BitcoinScriptTranslator() {
        mainStack = new ArrayDeque<>();
        isTransactionValid = true;
    }

    public boolean run(List<AbstractExpression> expressions) {
        for (var expression: expressions) {
            if (!isTransactionValid) {
                return false;
            }
            runExpression(expression);
        }

        return checkStackState();
    }

    private void runExpression(AbstractExpression expression) {
        if (expression instanceof PushDataExpression) {
            runPushData((PushDataExpression) expression);
            return;
        }

        if (expression instanceof OpCodeExpression) {
            runOpcode((OpCodeExpression) expression);
            return;
        }

        if (expression instanceof ConditionExpression) {
            runCondition((ConditionExpression) expression);
            return;
        }

        throw new BitcoinCompilerRuntimeException("Not implemented expression was found");
    }

    private void runOpcode(OpCodeExpression expression) {
        var opcodeAction = OpcodeActionSupplier.getOpcodeAction(expression.getType());
        try {
            opcodeAction.run(mainStack);
        } catch (InvalidTransactionException e) {
            isTransactionValid = false;
        }
    }

    private void runPushData(PushDataExpression expression) {
        var pushedData = expression.getData().getParsedData().toByteArray();
        if (pushedData.length > MAX_BYTE_VECTOR_SIZE) {
            throw new BitcoinCompilerRuntimeException("Pushed data has size over " + MAX_BYTE_VECTOR_SIZE + " bytes");
        }

        if (!expression.getSize().getParsedData().equals(new BigInteger(String.valueOf(pushedData.length)))) {
            throw new BitcoinCompilerRuntimeException("Pushed data size is not equal to the declared size");
        }

        mainStack.addLast(pushedData);
    }

    private void runCondition(ConditionExpression conditionExpression) {
        boolean conditionResult = false;

        var last = mainStack.pollLast();
        if (last != null && last.length > 0 && !new BigInteger(last).equals(BigInteger.ZERO)) {
            conditionResult = true;
        }

        if (conditionResult) {
            run(conditionExpression.getTrueExpression());
        } else if (conditionExpression.getFalseExpression() != null) {
            run(conditionExpression.getFalseExpression());
        }
    }

    private boolean checkStackState() {
        var last = mainStack.peekLast();

        if (last == null || last.length == 0 || new BigInteger(last).equals(BigInteger.ZERO)) {
            return false;
        }
        return true;
    }
}
