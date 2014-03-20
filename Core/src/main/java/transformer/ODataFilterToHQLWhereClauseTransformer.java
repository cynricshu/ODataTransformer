package transformer;

import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ExpressionParser;
import transformer.expressionvisitor.HQLWhereClauseExpressionVisitor;

public class ODataFilterToHQLWhereClauseTransformer implements
        DataTransformer {

    @Override
    public String transform(String inputData, Object[] params) {
        CommonExpression expression = ExpressionParser.parse(inputData);
        HQLWhereClauseExpressionVisitor visitor = new HQLWhereClauseExpressionVisitor();
        expression.visit(visitor);

        String hqlWhereClause = visitor.generateHQLWhereClause();
        System.out.println(hqlWhereClause);
        return hqlWhereClause;
    }

    @Override
    public String getSourceTypeName() {
        return "ODataFilter";
    }

    @Override
    public String getDistTypeName() {
        return "HQLWhereClause";
    }


}
