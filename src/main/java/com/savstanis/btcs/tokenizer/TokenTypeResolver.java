package com.savstanis.btcs.tokenizer;

import com.savstanis.btcs.exceptions.LexerException;

import java.util.HashSet;
import java.util.Set;

public class TokenTypeResolver {
    private static Set<String> opCodes;
    private static Set<String> pushdataOp;

    static {
        opCodes = new HashSet<>();
        opCodes.add("OP_0");
        opCodes.add("OP_FALSE");
        opCodes.add("OP_1NEGATE");
        opCodes.add("OP_1");
        opCodes.add("OP_TRUE");
        opCodes.add("OP_2");
        opCodes.add("OP_3");
        opCodes.add("OP_4");
        opCodes.add("OP_5");
        opCodes.add("OP_6");
        opCodes.add("OP_7");
        opCodes.add("OP_8");
        opCodes.add("OP_9");
        opCodes.add("OP_10");
        opCodes.add("OP_11");
        opCodes.add("OP_12");
        opCodes.add("OP_13");
        opCodes.add("OP_14");
        opCodes.add("OP_15");
        opCodes.add("OP_16");
        opCodes.add("OP_NOP");
        opCodes.add("OP_IF");
        opCodes.add("OP_ELSE");
        opCodes.add("OP_ENDIF");
        opCodes.add("OP_VERIFY");
        opCodes.add("OP_RETURN");
        opCodes.add("OP_DEPTH");
        opCodes.add("OP_DROP");
        opCodes.add("OP_DUP");
        opCodes.add("OP_NIP");
        opCodes.add("OP_OVER");
        opCodes.add("OP_ROT");
        opCodes.add("OP_SWAP");
        opCodes.add("OP_TUCK");
        opCodes.add("OP_1ADD");
        opCodes.add("OP_NEGATE");
        opCodes.add("OP_ABS");
        opCodes.add("OP_NOT");
        opCodes.add("OP_ADD");
        opCodes.add("OP_SHA256");
        opCodes.add("OP_HASH256");
        opCodes.add("OP_EQUAL");

        pushdataOp = new HashSet<>();
        pushdataOp.add("OP_PUSHDATA1");
        pushdataOp.add("OP_PUSHDATA2");
        pushdataOp.add("OP_PUSHDATA4");

    }

    public static TokenType getTokenType(String token) {
        if (pushdataOp.contains(token)) {
            return TokenType.PUSHDATA_OP;
        }

        if (opCodes.contains(token.toUpperCase())) {
            return TokenType.OP_CODE;
        }

        if (isDecimal(token)) {
            return TokenType.DEC;
        }

        if (isHexadecimal(token)) {
            return TokenType.HEX;
        }

        throw new LexerException();
    }

    private static boolean isDecimal(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private static boolean isHexadecimal(String s) {
        return s != null && s.matches("0[xX][0-9a-fA-F]+");
    }

}
