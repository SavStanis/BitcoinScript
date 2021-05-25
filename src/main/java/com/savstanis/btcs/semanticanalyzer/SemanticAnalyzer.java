package com.savstanis.btcs.semanticanalyzer;

import com.savstanis.btcs.exceptions.SemanticAnalysisException;
import com.savstanis.btcs.syntaxanalyzer.expressions.AbstractExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.ConditionExpression;
import com.savstanis.btcs.syntaxanalyzer.expressions.PushDataExpression;

import java.util.List;

public class SemanticAnalyzer {

    public void analyze(List<AbstractExpression> expressionList) {
        for (var expression : expressionList) {
            if (expression instanceof PushDataExpression) {
                var pushDataExpression = (PushDataExpression) expression;
                var dataSize = pushDataExpression.getSize().getParsedData();

                var minSize = pushDataExpression.getMinSize();
                var maxSize = pushDataExpression.getMaxSize();
                var isConstraintPresent = (minSize != null && maxSize != null);

                if (isConstraintPresent && (dataSize.compareTo(pushDataExpression.getMinSize()) < 0 || dataSize.compareTo(pushDataExpression.getMaxSize()) > 0)) {
                    throw new SemanticAnalysisException("Data size is out of the bounds");
                }

//                if (dataSize.compareTo(BigInteger.valueOf(pushDataExpression.getData().getParsedData().toByteArray().length)) != 0) {
//                    throw new SemanticAnalysisException("Data size is not equal to the determined size");
//                }
            }

            if (expression instanceof ConditionExpression) {
                var conditionExpression = (ConditionExpression) expression;
                if (conditionExpression.getTrueExpression() != null) {
                    this.analyze(conditionExpression.getTrueExpression());
                }

                if (conditionExpression.getFalseExpression() != null) {
                    this.analyze(conditionExpression.getFalseExpression());
                }
            }
        }
    }
}
