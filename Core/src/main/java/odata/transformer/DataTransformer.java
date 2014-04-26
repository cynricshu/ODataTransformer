package odata.transformer;

import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ExpressionParser;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:45
 */
public abstract class DataTransformer {
    public abstract <T> T transform(String inputData, Object[] params);

    CommonExpression parse(String inputData) {
        return ExpressionParser.parse(inputData);
    }

    abstract public String getDistTypeName();
}
