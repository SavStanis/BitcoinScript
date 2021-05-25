package com.savstanis.btcs.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {
    public List<Token> analyse(String code) {
        List<String> splitString = Arrays.stream(code.split("\\W+"))
                .filter(value -> value != null && value.length() > 0)
                .collect(Collectors.toList());

        List<Token> tokenList = new ArrayList<>();

        for (String item: splitString) {
            TokenType tokenType = TokenTypeResolver.getTokenType(item);
            Token token = new Token(tokenType, item.toUpperCase());

            tokenList.add(token);
        }

        return tokenList;
    }
}
