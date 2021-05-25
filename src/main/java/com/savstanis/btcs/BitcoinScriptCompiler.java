package com.savstanis.btcs;

import com.savstanis.btcs.exceptions.SyntaxException;
import com.savstanis.btcs.semanticanalyzer.SemanticAnalyzer;
import com.savstanis.btcs.syntaxanalyzer.SyntaxAnalyzer;
import com.savstanis.btcs.tokenizer.Tokenizer;
import com.savstanis.btcs.translator.BitcoinScriptTranslator;

public class BitcoinScriptCompiler {

    private final Tokenizer tokenizer;
    private final BitcoinScriptFileReader bitcoinScriptFileReader;
    private final SyntaxAnalyzer syntaxAnalyzer;
    private final SemanticAnalyzer semanticAnalyzer;
    private final BitcoinScriptTranslator bitcoinScriptTranslator;

    public BitcoinScriptCompiler() {
        tokenizer = new Tokenizer();
        bitcoinScriptFileReader = new BitcoinScriptFileReader();
        syntaxAnalyzer = new SyntaxAnalyzer();
        semanticAnalyzer = new SemanticAnalyzer();
        bitcoinScriptTranslator = new BitcoinScriptTranslator();
    }

    public boolean compile(String filePath) {
        var parsedScript = bitcoinScriptFileReader.readFile(filePath);
        var tokenizerResult = tokenizer.analyse(parsedScript);
        var syntaxAnalyzerResult = syntaxAnalyzer.analyze(tokenizerResult);

        if (syntaxAnalyzerResult.getTokensAnalyzed() != 0) {
            throw new SyntaxException();
        }

        semanticAnalyzer.analyze(syntaxAnalyzerResult.getExpressions());
        var translatorResult = bitcoinScriptTranslator.run(syntaxAnalyzerResult.getExpressions());
        printCompilationResult(translatorResult);

        return translatorResult;
    }

    private void printCompilationResult(boolean translatorResult) {
        if (translatorResult) {
            System.out.println("Transaction is valid!");
        } else {
            System.err.println("Transaction is not valid!");
        }
    }
}
