package com.savstanis.btcs.translator.opcode;

import com.savstanis.btcs.exceptions.BitcoinCompilerRuntimeException;
import com.savstanis.btcs.exceptions.InvalidTransactionException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class OpcodeActionSupplier {
    private static final Map<String, OpcodeAction> mapOfActions;

    static {
        mapOfActions = new HashMap<>();

        mapOfActions.put("OP_0", OpcodeActionSupplier::opFalse);
        mapOfActions.put("OP_FALSE", OpcodeActionSupplier::opFalse);
        mapOfActions.put("OP_TRUE", OpcodeActionSupplier::opTrue);
        mapOfActions.put("OP_1", OpcodeActionSupplier::opTrue);
        mapOfActions.put("OP_1NEGATE", OpcodeActionSupplier::opTrue);
        mapOfActions.put("OP_2", OpcodeActionSupplier::op2);
        mapOfActions.put("OP_3", OpcodeActionSupplier::op3);
        mapOfActions.put("OP_4", OpcodeActionSupplier::op4);
        mapOfActions.put("OP_5", OpcodeActionSupplier::op5);
        mapOfActions.put("OP_6", OpcodeActionSupplier::op6);
        mapOfActions.put("OP_7", OpcodeActionSupplier::op7);
        mapOfActions.put("OP_8", OpcodeActionSupplier::op8);
        mapOfActions.put("OP_9", OpcodeActionSupplier::op9);
        mapOfActions.put("OP_10", OpcodeActionSupplier::op10);
        mapOfActions.put("OP_11", OpcodeActionSupplier::op11);
        mapOfActions.put("OP_12", OpcodeActionSupplier::op12);
        mapOfActions.put("OP_13", OpcodeActionSupplier::op13);
        mapOfActions.put("OP_14", OpcodeActionSupplier::op14);
        mapOfActions.put("OP_15", OpcodeActionSupplier::op15);
        mapOfActions.put("OP_16", OpcodeActionSupplier::op16);

        mapOfActions.put("OP_VERIFY", OpcodeActionSupplier::opVerify);
        mapOfActions.put("OP_RETURN", OpcodeActionSupplier::opReturn);

        mapOfActions.put("OP_NOP",OpcodeActionSupplier::opNop);
        mapOfActions.put("OP_ADD", OpcodeActionSupplier::opAdd);
        mapOfActions.put("OP_DEPTH", OpcodeActionSupplier::opDepth);
        mapOfActions.put("OP_DROP", OpcodeActionSupplier::opDrop);
        mapOfActions.put("OP_DUP", OpcodeActionSupplier::opDup);
        mapOfActions.put("OP_NIP", OpcodeActionSupplier::opNip);
        mapOfActions.put("OP_OVER", OpcodeActionSupplier::opOver);
        mapOfActions.put("OP_ROT", OpcodeActionSupplier::opRot);
        mapOfActions.put("OP_SWAP", OpcodeActionSupplier::opSwap);
        mapOfActions.put("OP_TUCK", OpcodeActionSupplier::opTuck);
        mapOfActions.put("OP_1ADD", OpcodeActionSupplier::op1Add);
        mapOfActions.put("OP_NEGATE", OpcodeActionSupplier::opNegate);
        mapOfActions.put("OP_ABS", OpcodeActionSupplier::opAbs);
        mapOfActions.put("OP_NOT", OpcodeActionSupplier::opNot);
        mapOfActions.put("OP_SHA256", OpcodeActionSupplier::opSha256);
        mapOfActions.put("OP_HASH256", OpcodeActionSupplier::opHash256);
        mapOfActions.put("OP_EQUAL", OpcodeActionSupplier::opEqual);
    }

    public static OpcodeAction getOpcodeAction(String actionName) {
        return mapOfActions.get(actionName);
    }

    private static void opEqual(Deque<byte[]> stack) {
        if (stack.size() < 2) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for 'tuck' operation");
        }
        var last = stack.pollLast();
        var secondToLast = stack.pollLast();

        if (Arrays.equals(last, secondToLast)) {
            addIntValueOnStack(stack, 1);
        } else {
            stack.addLast(new byte[]{});
        }
    }

    private static void opHash256(Deque<byte[]> stack) {
        opSha256(stack);
        opSha256(stack);
    }

    private static void opSha256(Deque<byte[]> stack) {

        var input = stack.pollLast();
        if (input == null || new BigInteger(input).equals(BigInteger.ZERO)) {
            input = new byte[]{};
        }

        try {
            var md = MessageDigest.getInstance("SHA-256");
            var output = md.digest(input);
            stack.addLast(output);
        } catch (NoSuchAlgorithmException e) {
            throw new BitcoinCompilerRuntimeException("Not supported hashing algorithm");
        }
    }

    private static void opNot(Deque<byte[]> stack) {
        var firstArg = stack.pollLast();
        if (firstArg == null || !new BigInteger(firstArg).equals(BigInteger.ZERO)) {
            addIntValueOnStack(stack, 1);
        } else {
            stack.addLast(new byte[]{});
        }
    }

    private static void opAbs(Deque<byte[]> stack) {
        var firstArg = stack.pollLast();
        if (firstArg != null) {
            var firstArgConverted = new BigInteger(firstArg);
            stack.addLast(firstArgConverted.abs().toByteArray());
        } else {
            stack.addLast(new byte[]{});
        }
    }

    private static void opNegate(Deque<byte[]> stack) {
        var firstArg = stack.pollLast();
        if (firstArg != null) {
            var firstArgConverted = new BigInteger(firstArg);
            stack.addLast(firstArgConverted.negate().toByteArray());
        } else {
            stack.addLast(new byte[]{});
        }
    }

    private static void op1Add(Deque<byte[]> stack) {
        var firstArg = stack.pollLast();
        BigInteger firstArgConverted;
        if (firstArg == null) {
            firstArgConverted = new BigInteger(String.valueOf(1));
        } else {
            firstArgConverted = new BigInteger(firstArg).add(BigInteger.ONE);
        }

        stack.addLast(firstArgConverted.toByteArray());
    }

    private static void opTuck(Deque<byte[]> stack) {
        if (stack.size() < 2) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for 'tuck' operation");
        }
        var last = stack.pollLast();
        var secondToLast = stack.pollLast();
        stack.addLast(
                (last == null) ? null : last.clone()
        );
        stack.addLast(secondToLast);
        stack.addLast(last);
    }

    private static void opSwap(Deque<byte[]> stack) {
        if (stack.size() < 2) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for 'swap' operation");
        }
        var last = stack.pollLast();
        var secondToLast = stack.pollLast();
        stack.addLast(last);
        stack.addLast(secondToLast);
    }

    private static void opRot(Deque<byte[]> stack) {
        if (stack.size() < 3) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for 'rot' operation");
        }
        var last = stack.pollLast();
        var secondToLast = stack.pollLast();
        var thirdToLast = stack.pollLast();

        stack.addLast(secondToLast);
        stack.addLast(last);
        stack.addLast(thirdToLast);
    }

    private static void opOver(Deque<byte[]> stack) {
        if (stack.size() < 2) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for 'over' operation");
        }
        var last = stack.pollLast();
        var secondToLast = stack.peekLast() == null ? null : stack.peekLast().clone();
        stack.addLast(last);
        stack.addLast(secondToLast);
    }

    private static void opNip(Deque<byte[]> stack) {
        if (stack.size() < 2) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for nip operation");
        }
        var last = stack.pollLast();
        stack.pollLast();
        stack.addLast(last);
    }

    private static void opDup(Deque<byte[]> stack) {
        if (stack.size() == 0) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for drop operation");
        }
        stack.addLast(
                (stack.peekLast() == null) ? null : stack.peekLast().clone()
        );
    }

    private static void opDrop(Deque<byte[]> stack) {
        stack.pollLast();
    }

    private static void opDepth(Deque<byte[]> stack) {
        addIntValueOnStack(stack, stack.size());
    }

    private static void opVerify(Deque<byte[]> stack) throws InvalidTransactionException {
        var last = stack.pollLast();
        if (last== null || last.length == 0 || new BigInteger(last).equals(BigInteger.ZERO)) {
            throw new InvalidTransactionException();
        }
    }

    private static void opReturn(Deque<byte[]> stack) throws InvalidTransactionException {
        throw new InvalidTransactionException();
    }

    private static void opNop(Deque<byte[]> stack) {

    }

    private static void opAdd(Deque<byte[]> stack) {
        var firstArg = stack.pollLast();
        var secondArg = stack.pollLast();
        if (firstArg == null || secondArg == null) {
            throw new BitcoinCompilerRuntimeException("Not enough arguments for add operation");
        }

        var firstArgConverted = new BigInteger(firstArg);
        var secondArgConverted = new BigInteger(secondArg);


        stack.addLast(firstArgConverted.add(secondArgConverted).toByteArray());
    }

    private static void opFalse(Deque<byte[]> stack) {
        stack.addLast(new byte[]{});
    }

    private static void opTrue(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 1);
    }

    private static void addIntValueOnStack(Deque<byte[]> stack, int value) {
        stack.addLast(ByteBuffer.allocate(4).putInt(value).array());
    }

    private static void op2(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 2);
    }

    private static void op3(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 3);
    }

    private static void op4(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 4);
    }

    private static void op5(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 5);
    }

    private static void op6(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 6);
    }
    private static void op7(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 7);
    }

    private static void op8(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 8);
    }

    private static void op9(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 9);
    }

    private static void op10(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 10);
    }

    private static void op11(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 11);
    }

    private static void op12(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 12);
    }

    private static void op13(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 13);
    }

    private static void op14(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 14);
    }

    private static void op15(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 15);
    }

    private static void op16(Deque<byte[]> stack) {
        addIntValueOnStack(stack, 16);
    }

    private static void opNegate1(Deque<byte[]> stack) {
        addIntValueOnStack(stack, -1);

    }
}
